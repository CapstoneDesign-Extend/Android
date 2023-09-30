package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.Like;
import com.example.schoolproject.model.LikeStatus;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class LikeApiService {
    private static final String BASE_URL = "http://www.extends.online:5438";
    private LikeApi likeApi;

    public LikeApiService() {
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

        likeApi = retrofit.create(LikeApi.class);
    }

    public Call<Like> addLikeToBoard(Long boardId, Long memberId) {
        return likeApi.addLikeToBoard(boardId, memberId);
    }

    public Call<Like> addLikeToComment(Long commentId, Long memberId) {
        return likeApi.addLikeToComment(commentId, memberId);
    }
    // postActivity 최초 로딩 시 체크할 좋아요 상태 가져오기
    public Call<LikeStatus> getLikedBoardAndComments(Long boardId, Long memberId) {
        return likeApi.getLikedBoardAndComments(boardId, memberId);
    }
    // Check if a member has liked a specific board or comment
    public Call<Boolean> isBoardLiked(Long boardId, Long memberId) {
        return likeApi.isBoardLiked(boardId, memberId);
    }

    public Call<Boolean> isCommentLiked(Long commentId, Long memberId) {
        return likeApi.isCommentLiked(commentId, memberId);
    }

    // Declaring API interface
    interface LikeApi {
        @POST("/api/like/board/{boardId}/member/{memberId}")
        Call<Like> addLikeToBoard(@Path("boardId") Long boardId, @Path("memberId") Long memberId);

        @POST("/api/like/comment/{commentId}/member/{memberId}")
        Call<Like> addLikeToComment(@Path("commentId") Long commentId, @Path("memberId") Long memberId);

        @GET("api/like/liked/board-and-comments/{boardId}/member/{memberId}")
        Call<LikeStatus> getLikedBoardAndComments(@Path("boardId") Long boardId, @Path("memberId") Long memberId);

        @GET("/api/like/board/{boardId}/member/{memberId}/exists")
        Call<Boolean> isBoardLiked(@Path("boardId") Long boardId, @Path("memberId") Long memberId);

        @GET("/api/like/comment/{commentId}/member/{memberId}/exists")
        Call<Boolean> isCommentLiked(@Path("commentId") Long commentId, @Path("memberId") Long memberId);
    }

}
