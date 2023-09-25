package com.example.schoolproject.model.retrofit.cnet;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.Comment;
import com.example.schoolproject.nav.home.HomeRecyclerViewAdapter;
import com.example.schoolproject.post.PostPreviewRecyclerViewAdapter;
import com.example.schoolproject.post.PostRecyclerViewAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentCallback implements Callback<Comment> {
    private Context context;
    private RecyclerView.Adapter adapter;

    public CommentCallback(Context context, RecyclerView.Adapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onResponse(Call<Comment> call, Response<Comment> response) {
        if (response.isSuccessful()) {  // response 200
            Comment comment = response.body();
            if (call.request().method().equals("GET")){
                PostRecyclerViewAdapter postAdapter = (PostRecyclerViewAdapter) adapter;
                postAdapter.setData(comment);
                adapter.notifyDataSetChanged();

            }else if (call.request().method().equals("POST")){
                Toast.makeText(context, "댓글 작성이 완료되었습니다.", Toast.LENGTH_SHORT).show();

            } else if (call.request().method().equals("UPDATE")){
                //Toast.makeText(context, "댓글 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();

            } else if (call.request().method().equals("DELETE")){
                // warning:: deleteComment's return type is "Void"
                Toast.makeText(context, "댓글이 정상적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }

        } else {  // response를 아예 못받았을떄
            Toast.makeText(context, "서버로부터 응답을 받을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<Comment> call, Throwable t) {
        Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        t.printStackTrace();

    }

    public static class CommentListCallBack implements Callback<List<Comment>>{
        private Context context;
        private RecyclerView.Adapter adapter;

        public CommentListCallBack(Context context, RecyclerView.Adapter adapter) {
            this.context = context;
            this.adapter = adapter;
        }

        @Override
        public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
            if (response.isSuccessful()) {
                List<Comment> commentList = response.body();
                Log.d(TAG, "onResponse: 서버로부터 데이터를 가져왔습니다!\n어댑터의 setCommentList를 실행합니다...");
                // distinguishing by instance
                // 어댑터는 postRecyclerViewAdapter 하나만 들어오기때문에 구별할 필요없음
                PostRecyclerViewAdapter postRecyclerViewAdapter = (PostRecyclerViewAdapter) adapter;
                postRecyclerViewAdapter.setCommentList(commentList);

                // *주의*: request url에 해당 키워드가 실제로 포함되는지 확인 후 구현하기
                if (call.request().url().toString().contains("member")) {
                    // memberId:Long 으로 댓글 리스트 가져오기

                } else if (call.request().url().toString().contains("search")){
                    // 내용으로 댓글 검색

                } else if (call.request().url().toString().contains("comments")) {
                    // 모든 url에 comments가 포함되므로 가장 마지막에 두기
                    // boardId로 댓글 리스트 가져오기

                }
            } else {
                Toast.makeText(context, "서버로부터 응답을 받을 수 없습니다.",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<List<Comment>> call, Throwable t) {
            Log.d(TAG, "onFailure: 서버로부터 데이터를 가져오지 못했습니다.");
        }
    }
}
