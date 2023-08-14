package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.Comment;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class CommentApiService {
    private static final String BASE_URL = "http://www.codeshare.live:5438";
    private CommentApi commentApi;

    public CommentApiService() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        commentApi = retrofit.create(CommentApi.class);
    }

    public Call<Comment> createComment(Long boardId, Long memberId, String content) {
        return commentApi.createComment(boardId, memberId, content);
    }

    public Call<List<Comment>> getCommentsByBoardId(Long boardId) {
        return commentApi.getCommentsByBoardId(boardId);
    }

    public Call<List<Comment>> getCommentsByMemberId(Long memberId) {
        return commentApi.getCommentsByMemberId(memberId);
    }

    public Call<List<Comment>> searchComments(String content) {
        return commentApi.searchComments(content);
    }

    public Call<Void> deleteCommentById(Long commentId) {
        return commentApi.deleteCommentById(commentId);
    }

    public Call<Void> deleteCommentsByMemberId(Long memberId) {
        return commentApi.deleteCommentsByMemberId(memberId);
    }

    interface CommentApi {
        @POST("/api/comments")
        Call<Comment> createComment(@Query("boardId") Long boardId,
                                    @Query("memberId") Long memberId,
                                    @Query("content") String content);

        @GET("/api/comments/{boardId}")
        Call<List<Comment>> getCommentsByBoardId(@Path("boardId") Long boardId);

        @GET("/api/comments/member/{memberId}")
        Call<List<Comment>> getCommentsByMemberId(@Path("memberId") Long memberId);

        @GET("/api/comments/search")
        Call<List<Comment>> searchComments(@Query("content") String content);

        @DELETE("/api/comments/{commentId}")
        Call<Void> deleteCommentById(@Path("commentId") Long commentId);

        @DELETE("/api/comments/member/{memberId}")
        Call<Void> deleteCommentsByMemberId(@Path("memberId") Long memberId);
    }
}