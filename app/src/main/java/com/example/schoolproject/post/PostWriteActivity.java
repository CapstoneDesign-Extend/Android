package com.example.schoolproject.post;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.schoolproject.R;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;
import com.example.schoolproject.model.FileEntity;
import com.example.schoolproject.model.Member;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.BoardCallback;
import com.example.schoolproject.model.retrofit.FileApiService;
import com.example.schoolproject.model.retrofit.FileCallback;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class PostWriteActivity extends AppCompatActivity {
    private ArrayList imageURLs;
    private String boardKind;
    private String loginId;
    private Toolbar toolbar;
    private TextView btn_done;
    private ImageView iv_camera;
    private EditText et_post_title, et_post_content, et_price;
    private CheckBox cb_isAnon;
    private String author;
    private Member member;
    private boolean isUpdate = false;  // 이 값에 따라 Create or Update 결정
    private boolean isShop = false;
    private Long postId;  // update 위한 postId
    private static final int PICK_IMAGE_REQUEST = 1;
    // 권한 요청을 위한 고유한 요청 코드
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout imageWrapper;
    private List<Uri> selectedImageUriList = new ArrayList<>();



    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);
        // get current boardName + saved ID + Member, set author ...etc from Intent
        boardKind = getIntent().getStringExtra("boardKind");
        postId = getIntent().getLongExtra("postId", -1);
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        SharedPreferences sharedPrefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        loginId = sharedPrefs.getString("loginId","error:ID is NULL");
        String memberJson = sharedPrefs.getString("memberJson", "error:Member is NULL");
        if (!memberJson.isEmpty()){
            Gson gson = new Gson();
            member = gson.fromJson(memberJson, Member.class);
            author = member.getName();
        }

        // connect resources
        btn_done = findViewById(R.id.tv_write_done);
        et_post_title = findViewById(R.id.et_post_title);
        et_post_content = findViewById(R.id.et_post_content);
        cb_isAnon = findViewById(R.id.cb_isAnon);
        iv_camera = findViewById(R.id.iv_post_camera);
        imageWrapper = findViewById(R.id.write_image_container);
        horizontalScrollView = findViewById(R.id.hsv_write_image_scroll);
        et_price = findViewById(R.id.et_post_price);

        // 장터게시판일 경우, 뷰 구조 변경
        if (boardKind.equals("MARKET")){
            et_price.setVisibility(View.VISIBLE);
            isShop = true;
            // 가격 입력 필드 콤마 + "원" 처리
            et_price.addTextChangedListener(new TextWatcher() {
               private String current = "";
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals(current)) {
                        et_price.removeTextChangedListener(this);

                        // 입력값을 숫자로 파싱
                        String cleanString = s.toString().replaceAll("[^\\d.]", "");
                        double parsed;
                        try {
                            parsed = Double.parseDouble(cleanString);
                        } catch (NumberFormatException e) {
                            parsed = 0.0;
                        }

                        // 콤마가 있는 포맷으로 변환
                        String formatted = NumberFormat.getInstance().format(parsed) + " 원";

                        current = formatted;
                        et_price.setText(formatted);
                        et_price.setSelection(formatted.length() - 2);

                        et_price.addTextChangedListener(this);
                    }
                }
            });



        }



        // 게시글 수정 버튼을 통해 진입하면, 작성된 글 불러와서 표시
        if (isUpdate){
            if (isShop){
                Integer price = getIntent().getIntExtra("price", -1);
                Log.e(TAG, "price: ===================================" + price);
                et_price.setText(String.valueOf(price));
            }
            et_post_title.setText(getIntent().getStringExtra("postTitle"));
            et_post_content.setText(getIntent().getStringExtra("postContent"));
            imageURLs = getIntent().getStringArrayListExtra("imageURLs");
            Log.e(TAG, "onCreate: imageURLs ************************************************ " + imageURLs);
            // 기존에 저장된 이미지 url을 미리보기에 로드
            for (Object imageUrl : imageURLs){
                Log.e(TAG, "onClick: " + (String) imageUrl );
                addImageToScrollViewOnEdit((String) imageUrl);
            }
        }

        // setting listener
        // 이미지 선택 버튼 클릭시 동작 : 권한 요청 및 이미지 선택기 열기
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //Toast.makeText(getApplicationContext(), "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show();
                        launchImagePicker();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(getApplicationContext(), "권한이 거부되었습니다.\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }

                };
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    TedPermission.create()
                            .setPermissionListener(permissionlistener)
                            .setDeniedMessage("사진을 업로드하기 위해 권한이 필요합니다.\n\n[설정] > [권한] 에서 권한을 허용해주세요.")
                            .setPermissions(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO)
                            .check();
                } else {
                    TedPermission.create()
                            .setPermissionListener(permissionlistener)
                            .setDeniedMessage("사진을 업로드하기 위해 권한이 필요합니다.\n\n[설정] > [권한] 에서 권한을 허용해주세요.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .check();
                }


            }
        });

        // ====================================  게시글 작성 버튼 클릭시 동작  ===================================================
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // 게시글 작성 버튼 로직

                if (et_post_title.getText().toString().equals("") || et_post_content.getText().toString().equals("")) {
                    Toast.makeText(PostWriteActivity.this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isShop){
                    String cleanString = et_price.getText().toString().replaceAll("[^\\d]", "");
                    if (cleanString.equals("")) {
                        Toast.makeText(PostWriteActivity.this, "가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                BoardApiService apiService = new BoardApiService();
                Board board = createBoardFromInput();

                if (isUpdate) {
                    // 게시글 수정 동작
                    Call<Board> call = apiService.updateBoard(postId, board);
                    BoardCallback callback = new BoardCallback(PostWriteActivity.this, getApplicationContext(), selectedImageUriList);
                    call.enqueue(callback);
                } else {
                    // 새로운 게시글 작성 동작
                    Call<Board> call = apiService.createBoard(board);
                    BoardCallback callback = new BoardCallback(PostWriteActivity.this, getApplicationContext(), selectedImageUriList);
                    call.enqueue(callback);
                }
            }
        });


        // checkbox:: set Initial State
        if (cb_isAnon.isChecked()){
            author = "익명";
        } else {
            author = loginId;
        }
        // get Anon state from checkbox
        cb_isAnon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    author = "익명";
                }else {
                    author = loginId;
                }
            }
        });

        // setting toolbar
        toolbar = findViewById(R.id.toolbar_write);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // show Keyboard
        et_post_title.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_post_title, InputMethodManager.SHOW_IMPLICIT);
    }
    private Board createBoardFromInput() {
        Board board = new Board();

        if (isShop) {
            String cleanString = et_price.getText().toString().replaceAll("[^\\d]", "");
            board.setPrice(Integer.parseInt(cleanString));
        }

        board.setBoardKind(BoardKind.valueOf(boardKind));
        board.setTitle(et_post_title.getText().toString());
        board.setContent(et_post_content.getText().toString());
        board.setLikeCnt(0);
        board.setChatCnt(0);
        board.setFinalDate(getCurrentTime("default"));

        if (cb_isAnon.isChecked()) {
            board.setAuthor("익명");
        } else {
            board.setAuthor(loginId);
        }

        if (!isUpdate) {
            board.setMember(member);
        }

        return board;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    // 시간 형식 변환 메소드
    private String getCurrentTime(String type){
        Date currentTime = new Date();
        SimpleDateFormat sdf_fullDate = new SimpleDateFormat("yy/MM/dd");
        SimpleDateFormat sdf_date = new SimpleDateFormat("MM/dd");
        SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf_default = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        switch (type){
            case "fullDate":
                return sdf_fullDate.format(currentTime);
            case "date":
                return sdf_date.format(currentTime);
            case "time":
                return sdf_time.format(currentTime);
            case "default":
                return sdf_default.format(currentTime);
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }




    //  ========================   파일 업로드 메소드 정의   =========================

    // 갤러리에서 선택한 이미지를 글 작성 화면에 미리 보여주기
    public void addImageToScrollView(Uri imageUri) {
        // 리스트에 이미지uri 추가
        selectedImageUriList.add(imageUri);
        
        horizontalScrollView.setVisibility(View.VISIBLE);
        ImageView imageView = new ImageView(PostWriteActivity.this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(350, 350)); // or other dimensions you desire

        // Glide를 사용하여 이미지 로딩
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(32));

        Glide.with(PostWriteActivity.this)
                .load(imageUri)
                .apply(requestOptions)
                .into(imageView);

        imageView.setPadding(10,10,10,10); // padding if needed

        // ImageView에 리스너 추가
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 이미지 제거 확인 대화상자 표시
                AlertDialog dialog = new AlertDialog.Builder(PostWriteActivity.this, R.style.RoundedDialog)
                        .setTitle("이미지 삭제")
                        .setMessage("이 이미지를 삭제할까요?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imageWrapper.removeView(imageView); // 이미지뷰 제거
                                selectedImageUriList.remove(imageUri);  // 이미지 URI를 리스트에서 제거
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .show();
                // 긍정적인 버튼 (예)의 텍스트 색상 변경
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));

                // 부정적인 버튼 (아니오)의 텍스트 색상 변경
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));


                return true; // 이벤트가 여기서 처리되었음을 나타냅니다.
            }
        });

        imageWrapper.addView(imageView);
    }
    public void addImageToScrollViewOnEdit(String imageUrl) {  // *** 서버에서 가져온 URL을 Uri로 변환 후 리스트에 추가  ***

        horizontalScrollView.setVisibility(View.VISIBLE);
        ImageView imageView = new ImageView(PostWriteActivity.this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(350, 350)); // or other dimensions you desire

        // Glide를 사용하여 이미지 로딩
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(32));

        Glide.with(PostWriteActivity.this)
                .load(imageUrl)
                .apply(requestOptions)
                .into(imageView);

        imageView.setPadding(10,10,10,10); // padding if needed

        // 캐시에서 URI 가져오기
        Glide.with(PostWriteActivity.this)
                .asFile()
                .load(imageUrl)
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        Uri imageUriFromCache = Uri.fromFile(resource);
                        imageView.setTag(imageUriFromCache);  // imageView에 태그로 URI 설정
                        selectedImageUriList.add(imageUriFromCache);
                    }
                });

        // ImageView에 리스너 추가
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 이미지 제거 확인 대화상자 표시
                AlertDialog dialog = new AlertDialog.Builder(PostWriteActivity.this, R.style.RoundedDialog)
                        .setTitle("이미지 삭제")
                        .setMessage("이 이미지를 삭제할까요?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                imageWrapper.removeView(imageView); // 이미지뷰 제거
                                Uri cachedUri = (Uri) imageView.getTag();  //  imageView의 태그에서 URI 가져오기
                                selectedImageUriList.remove(cachedUri);  // 이미지 URI를 리스트에서 제거
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .show();
                // 긍정적인 버튼 (예)의 텍스트 색상 변경
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));

                // 부정적인 버튼 (아니오)의 텍스트 색상 변경
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));


                return true; // 이벤트가 여기서 처리되었음을 나타냅니다.
            }
        });


        imageWrapper.addView(imageView);
    }


    // 이미지 선택 액티비티 실행
    private void launchImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // 다중 선택 허용
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // 이미지 선택 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {  // 다중 이미지가 선택된 경우
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    addImageToScrollView(imageUri);
                }
            } else if (data.getData() != null) {  // 단일 이미지가 선택된 경우
                Uri imageUri = data.getData();
                addImageToScrollView(imageUri);
            }
        } else {
            Log.e(TAG, "onActivityResult: 이미지를 선택하지 않았거나 오류가 발생했습니다.");
        }
    }
    // 이미지 업로드 함수
    public void uploadImageList(List<Uri> imageUriList, ProgressDialog dialog) {
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        try {
            for (Uri imageUri : imageUriList){
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                byte[] imageBytes = IOUtils.toByteArray(inputStream);  // Apache Commons IO 라이브러리를 사용합니다.
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageBytes);
                MultipartBody.Part body = MultipartBody.Part.createFormData("files", "image.jpg", requestFile);  // 파일 이름을 직접 제공
                imageParts.add(body);
            }

            FileApiService apiService = new FileApiService();
            if (postId != null && postId != -1){
                Call<List<FileEntity>> call = apiService.uploadFiles(imageParts, postId);
                call.enqueue(new FileCallback<List<FileEntity>>() {
                    @Override
                    public void onSuccess(List<FileEntity> result) {

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "이미지 업로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        finish();

                    }
                });
            }else {
                Toast.makeText(getApplicationContext(), "Error: postId is NULL.", Toast.LENGTH_SHORT).show();
            }
            // 이 콜백이 끝나면 addImageToScrollView() 호출됨  --> 로직 변경으로 다시 제거함
            //call.enqueue(new FileCallback.ImageCallBack(PostWriteActivity.this, getApplicationContext(), imageUri));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}