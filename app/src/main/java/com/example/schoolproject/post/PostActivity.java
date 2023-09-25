package com.example.schoolproject.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ActivityPostBinding;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKindUtils;
import com.example.schoolproject.model.Comment;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.BoardCallback;
import com.example.schoolproject.model.retrofit.CommentApiService;
import com.example.schoolproject.model.retrofit.cnet.CommentCallback;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class PostActivity extends AppCompatActivity {
    private Long postId;
    private Long memberId;
    private String loginId;
    private String boardKind;
    private String author;
    private TextView tv_boardName;
    private List<Object> dataList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private boolean isNotificationEnabled = false;
    private Menu menu;


    @Override
    protected void onResume() {
        super.onResume();
        // get post matching postId
        loadData();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPostBinding binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sPref = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        postId = getIntent().getLongExtra("postId",-1);
        memberId = sPref.getLong("id", -1);
        loginId = sPref.getString("loginId", null);
        boardKind = getIntent().getStringExtra("boardKind");
        if (postId == -1){
            Toast.makeText(getApplicationContext(), "Fatal Error: postId is null", Toast.LENGTH_SHORT).show();
        }


        // set boardName
        tv_boardName = findViewById(R.id.tv_post_board_name);
        tv_boardName.setText(BoardKindUtils.getBoardTitleByString(boardKind));
        // setting Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_post);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setting RecyclerView
        recyclerView = findViewById(R.id.recyclerview_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        dataList = new ArrayList<>(); // initialize empty data
        adapter = new PostRecyclerViewAdapter(dataList);
        recyclerView.setAdapter(adapter);

        // setting listeners
        binding.ivCommentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.tvPostComment.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    if (binding.cbCommentIsAnon.isChecked()){ author = "익명"; }
                    else { author = loginId; }
                    CommentApiService apiService = new CommentApiService();
                    Call<Comment> call = apiService.createComment(postId, memberId, binding.tvPostComment.getText().toString(), author);
                    call.enqueue(new CommentCallback(postId, PostActivity.this, getApplicationContext(), adapter));
                    // 댓글 작성 후 입력폼 초기화 및 키보드 숨기기
                    binding.tvPostComment.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_post, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.notification:
                isNotificationEnabled = !isNotificationEnabled; // toggle notificationFlag
                if (isNotificationEnabled){
                    Toast.makeText(this, "알림이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                    menu.findItem(R.id.notification).setIcon(R.drawable.icon_notification_on_gicon);
                    Drawable drawable = menu.findItem(R.id.notification).getIcon();
                    if (drawable != null){
                        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.black));
                    }
                } else {
                    Toast.makeText(this, "알림이 해제되었습니다.", Toast.LENGTH_SHORT).show();
                    menu.findItem(R.id.notification).setIcon(R.drawable.icon_notification_off_gicon);
                    Drawable drawable = menu.findItem(R.id.notification).getIcon();
                    if (drawable != null){
                        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.colorBasicGray));
                    }
                }
                return true;
            case R.id.sendMessage:
                Toast.makeText(this, "메시지 전송", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.report:
                Toast.makeText(this, "해당 사용자를 신고합니다.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.block:
                Toast.makeText(this, "해당 사용자를 차단합니다.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                Toast.makeText(this, "게시글 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                return true;

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void loadData(){
        BoardApiService boardApiService = new BoardApiService();
        Call<Board> call1 = boardApiService.getBoardById(postId);
        call1.enqueue(new BoardCallback(postId, PostActivity.this, getApplicationContext(), adapter));
    }
}