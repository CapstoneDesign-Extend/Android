package com.example.schoolproject.model.retrofit;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.auth.login.LoginActivity;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.nav.board.FragBoardIssue;
import com.example.schoolproject.post.PostPreviewRecyclerViewAdapter;
import com.example.schoolproject.post.PostRecyclerViewAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardCallback implements Callback<Board> {
    private Activity activity;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    private void showShortToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    private void finishActivity(Activity activity){
        activity.finish();
    }

    @Override
    public void onResponse(Call<Board> call, Response<Board> response) {
        if (response.isSuccessful()){
            Board board = response.body();

            if (call.request().method().equals("POST")){
                showShortToast(context, "게시글 작성이 완료되었습니다.");
                finishActivity(activity);
            } else if (call.request().method().equals("UPDATE")){
                showShortToast(context, "게시글 수정이 완료되었습니다.");

            } else if (call.request().method().equals("DELETE")){
                // warning:: deleteBoard's return type is "Void"
                showShortToast(context, "게시글이 정상적으로 삭제되었습니다.");
            }

        } else {
            Toast.makeText(context, "서버로부터 응답을 받을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<Board> call, Throwable t) {
        Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
    }



    // public inner class : BoardListCallBack


    public static class BoardListCallBack implements Callback<List<Board>>{
        private Context context;
        private RecyclerView.Adapter adapter;
        public BoardListCallBack(Context context, RecyclerView.Adapter adapter){
            this.context = context;
            this.adapter = adapter;
        }


        @Override
        public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
            if (response.isSuccessful()) {
                List<Board> boardList = response.body();
                // distinguishing by instance
                if (adapter instanceof PostPreviewRecyclerViewAdapter) {
                    PostPreviewRecyclerViewAdapter postPrevAdapter = (PostPreviewRecyclerViewAdapter) adapter;
                    postPrevAdapter.convertAndSetData(boardList);
                } else if (adapter instanceof PostRecyclerViewAdapter) {
                    PostRecyclerViewAdapter postAdapter = (PostRecyclerViewAdapter) adapter;
                    //postAdapter.convertAndSetData(boardList);
                }

                adapter.notifyDataSetChanged();

                if (call.request().url().toString().contains("getBoardsByKind")) {


                } else if (call.request().url().toString().contains("getBoardsByTitle")) {

                } else if (call.request().url().toString().contains("getBoardsByKeyWord")) {

                } else if (call.request().url().toString().contains("getAllBoards")) {

                }

            } else {
                Toast.makeText(context, "서버로부터 응답을 받을 수 없습니다.", Toast.LENGTH_SHORT).show();

            }
        }
        @Override
        public void onFailure(Call<List<Board>> call, Throwable t) {
            Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

}

