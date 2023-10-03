package com.example.schoolproject.post;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.schoolproject.R;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;
import com.example.schoolproject.model.Member;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.BoardCallback;
import com.example.schoolproject.model.retrofit.FileApiService;
import com.example.schoolproject.model.retrofit.FileCallback;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class PostWriteActivity extends AppCompatActivity {
    private String boardKind;
    private String loginId;
    private Toolbar toolbar;
    private TextView btn_done;
    private ImageView iv_camera;
    private EditText et_post_title;
    private EditText et_post_content;
    private CheckBox cb_isAnon;
    private String author;
    private Member member;
    private boolean isUpdate = false;  // 이 값에 따라 Create or Update 결정
    private Long postId;  // update 위한 postId
    private static final int PICK_IMAGE_REQUEST = 1;
    // 권한 요청을 위한 고유한 요청 코드
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout imageWrapper;


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

        // 게시글 수정 버튼을 통해 진입하면 해당 로직 실행
        if (isUpdate){
            et_post_title.setText(getIntent().getStringExtra("postTitle"));
            et_post_content.setText(getIntent().getStringExtra("postContent"));
        }

        // setting listener
        // 이미지 선택 버튼 클릭시 동작
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 사용자에게 권한 허가 요청
                if (ContextCompat.checkSelfPermission(PostWriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 이미 허용되어 있으면 사진 선택 액티비티를 바로 실행합니다.
                    launchImagePicker();
                } else {
                    // 권한을 요청합니다.
                    requestStoragePermission();
                }

            }
        });


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // 게시글 작성 버튼 로직

                if (et_post_title.getText().toString().equals("") || et_post_content.getText().toString().equals("")){
                    Toast.makeText(PostWriteActivity.this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    if (!isUpdate){
                        // 새로운 게시글 작성 동작
                        BoardApiService apiService = new BoardApiService();
                        Board board = new Board();
                        board.setBoardKind(BoardKind.valueOf(boardKind));
                        board.setTitle(et_post_title.getText().toString());
                        board.setContent(et_post_content.getText().toString());
                        board.setMember(member);
                        board.setLikeCnt(0);
                        board.setChatCnt(0);
                        board.setFinalDate(getCurrentTime("default"));
                        if (cb_isAnon.isChecked()) {author = "익명";}
                        board.setAuthor(author);

                        Call<Board> call = apiService.createBoard(board);
                        BoardCallback callback = new BoardCallback(PostWriteActivity.this, getApplicationContext());
                        call.enqueue(callback);
                    }else {
                        // 게시글 수정 동작
                        BoardApiService apiService = new BoardApiService();
                        Board board = new Board();
                        board.setTitle(et_post_title.getText().toString());
                        board.setContent(et_post_content.getText().toString());
                        if (cb_isAnon.isChecked()) {author = "익명";}
                        else {author = loginId;}
                        board.setAuthor(author);
                        Toast.makeText(getApplicationContext(), author, Toast.LENGTH_SHORT).show();

                        Call<Board> call = apiService.updateBoard(postId, board);
                        BoardCallback callback = new BoardCallback(PostWriteActivity.this, getApplicationContext());
                        call.enqueue(callback);
                    }



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

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




    // 이미지 업로드 함수
    private void uploadImage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = IOUtils.toByteArray(inputStream);  // Apache Commons IO 라이브러리를 사용합니다.
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageBytes);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);  // 파일 이름을 직접 제공합니다.

            FileApiService apiService = new FileApiService();
            Call<ResponseBody> call = apiService.uploadImage(body);
            // 이 콜백이 끝나면 addImageToScrollView() 호출됨
            call.enqueue(new FileCallback.ImageCallBack(PostWriteActivity.this, getApplicationContext(), imageUri));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addImageToScrollView(Uri imageUri) {
        Log.e(TAG, "addImageToScrollView: URI :"+ imageUri );
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

    // URI에서 실제 파일 경로 가져오기  *** 안드로이드 10 이상에서는 호환 안됨 ***
    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri == null){
            return null;
        }

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor != null && cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        } else {
            Log.e(TAG, "getRealPathFromURI: 경로를 가져올 수 없습니다." );
            return null;  // 경로를 가져올 수 없는 경우
        }

    }

    // 권한을 요청하는 메서드
    private void requestStoragePermission() {
        // 권한을 이미 허용한 경우, 권한 요청 대화상자를 표시하지 않고 바로 실행할 수 있습니다.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 권한이 이미 허용되어 있음
            // 이곳에서 파일 읽기 작업을 수행할 수 있습니다.
        } else {
            // 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용되었을 때의 처리: 여기서 이미지 선택 액티비티를 실행합니다.
                launchImagePicker();
            } else {
                // 권한이 거부되었을 때의 처리
                Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // 이미지 선택 액티비티를 실행하는 메서드
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
                    uploadImage(imageUri);  // 각 이미지를 업로드합니다.
                }
            } else if (data.getData() != null) {  // 단일 이미지가 선택된 경우
                Uri imageUri = data.getData();
                uploadImage(imageUri);
            }
        } else {
            Log.e(TAG, "onActivityResult: 이미지를 선택하지 않았거나 오류가 발생했습니다.");
        }
    }
}