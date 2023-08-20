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
import com.example.schoolproject.test.DataBaseHelper;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostWriteActivity extends AppCompatActivity {
    private DataBaseHelper dbHelper;
    private String boardKind;
    private String loginId;
    private Toolbar toolbar;
    private TextView btn_done;
    private EditText et_post_title;
    private EditText et_post_content;
    private CheckBox cb_isAnon;
    private String author;
    private Member member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);
        // get current boardName + saved ID + Member
        boardKind = getIntent().getStringExtra("boardKind");
        SharedPreferences sharedPrefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        loginId = sharedPrefs.getString("loginId","error:ID is NULL");
        String memberJson = sharedPrefs.getString("memberJson", "error:Member is NULL");
        if (!memberJson.isEmpty()){
            Gson gson = new Gson();
            member = gson.fromJson(memberJson, Member.class);
        }
        // connect resources
        btn_done = findViewById(R.id.tv_write_done);
        et_post_title = findViewById(R.id.et_post_title);
        et_post_content = findViewById(R.id.et_post_content);
        cb_isAnon = findViewById(R.id.cb_isAnon);


        // setting listener
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_post_title.getText().toString().equals("") || et_post_content.getText().toString().equals("")){
                    Toast.makeText(PostWriteActivity.this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    BoardApiService apiService = new BoardApiService();
                    Board board = new Board();
                    board.setBoardKind(BoardKind.valueOf(boardKind));
                    board.setTitle(et_post_title.getText().toString());
                    board.setContent(et_post_content.getText().toString());
                    board.setMember(member);
                    board.setFinalDate(getCurrentTime("default"));

                    Call<Board> call = apiService.createBoard(board);
                    call.enqueue(new Callback<Board>() {
                        @Override
                        public void onResponse(Call<Board> call, Response<Board> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(v.getContext(), "게시글 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(v.getContext(), "서버로부터 응답을 받을 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Board> call, Throwable t) {
                            Toast.makeText(v.getContext(), "네트워크 오류입니다. 인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });

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