package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.Board;

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
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class BoardApiService {
    private static final String BASE_URL = "http://www.codeshare.live:5438";
    private BoardApi boardApi;

    public BoardApiService() {
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

        boardApi = retrofit.create(BoardApi.class);
    }

    public Call<Board> createBoard(Board board) {
        return boardApi.createBoard(board);
    }

    public Call<Board> updateBoard(Long id, Board updatedBoard) {
        return boardApi.updateBoard(id, updatedBoard);
    }

    public Call<Void> deleteBoard(Long id) {
        return boardApi.deleteBoard(id);
    }

    public Call<List<Board>> searchBoards(String title) {
        return boardApi.searchBoards(title);
    }

    public Call<List<Board>> getAllBoards() {
        return boardApi.getAllBoards();
    }

    interface BoardApi {
        @POST("/api/boards")
        Call<Board> createBoard(@Body Board board);

        @PUT("/api/boards/{id}")
        Call<Board> updateBoard(@Path("id") Long id, @Body Board updatedBoard);

        @DELETE("/api/boards/{id}")
        Call<Void> deleteBoard(@Path("id") Long id);

        @GET("/api/boards/search")
        Call<List<Board>> searchBoards(@retrofit2.http.Query("title") String title);

        @GET("/api/boards")
        Call<List<Board>> getAllBoards();
    }
}