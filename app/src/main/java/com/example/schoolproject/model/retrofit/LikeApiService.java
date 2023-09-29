package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.Like;
import com.example.schoolproject.model.Member;

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

    // Declaring API interface
    interface LikeApi {
        @POST("/api/like/board/{boardId}/member/{memberId}")
        Call<Like> addLikeToBoard(@Path("boardId") Long boardId, @Path("memberId") Long memberId);

        @POST("/api/like/comment/{commentId}/member/{memberId}")
        Call<Like> addLikeToComment(@Path("commentId") Long commentId, @Path("memberId") Long memberId);
    }
}
