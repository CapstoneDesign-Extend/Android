package com.example.schoolproject.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolproject.R;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;
import com.example.schoolproject.model.Member;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.BoardCallback;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;

public class PostWriteActivity extends AppCompatActivity {
    private String boardKind;
    private String loginId;
    private Toolbar toolbar;
    private TextView btn_done;
    private EditText et_post_title;
    private EditText et_post_content;
    private CheckBox cb_isAnon;
    private String author;
    private Member member;
    private boolean isUpdate = false;  // 이 값에 따라 Create or Update 결정
    private Long postId;  // update 위한 postId


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

        // 게시글 수정 버튼을 통해 진입하면 해당 로직 실행
        if (isUpdate){
            et_post_title.setText(getIntent().getStringExtra("postTitle"));
            et_post_content.setText(getIntent().getStringExtra("postContent"));
        }

        // setting listener
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
//        et_post_title.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(et_post_title, InputMethodManager.SHOW_IMPLICIT);
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
}