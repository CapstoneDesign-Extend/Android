package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class BoardApiService {
    private static final String BASE_URL = "http://www.extends.online:5438";
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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())  // add RxJava3 adapter
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

    public Call<Board> getBoardById(Long id) { return boardApi.getBoardById(id); }
    public Call<List<Board>> getBoardsByBoardKind(BoardKind boardKind){
        return boardApi.getBoardsByBoardKind(boardKind);
    }
    public Single<List<Board>> getBoardsByBoardKindAmountRx(BoardKind boardKind, int amount){
        return boardApi.getBoardsByBoardKindAmountRx(boardKind, amount);
    }

    public Call<List<Board>> getBoardsByTitle(String title) {
        return boardApi.getBoardsByTitle(title);
    }

    public Call<List<Board>> getBoardsByKeyword(String keyword) {
        return boardApi.getBoardsByKeyword(keyword);
    }
    public Call<List<Board>> getBoardsByKeywordKind(String keyword, BoardKind boardKind){
        return boardApi.getBoardsByKeywordKind(keyword, boardKind);
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

        @GET("/api/boards/{id}")
        Call<Board> getBoardById(@Path("id") Long id);  // 특정 id의 게시글 가져오기
        @GET("/api/boards/search/byBoardKind")  // 게시판 종류에 해당하는 게시글 가져오기
        Call<List<Board>> getBoardsByBoardKind(@Query("boardKind") BoardKind boardKind);
        @GET("/api/boards/search/byBoardKindAmount")  // 게시판 종류에 해당하는 게시글을 필요한 만큼만 가져오기
        Single<List<Board>> getBoardsByBoardKindAmountRx(@Query("boardKind") BoardKind boardKind, @Query("amount") int amount);
        @GET("/api/boards/search/byTitle")  // 제목만 검색
        Call<List<Board>> getBoardsByTitle(@Query("title") String title);
        @GET("/api/boards/search/byKeyword")  // 키워드로 제목 본문 함께 검색
        Call<List<Board>> getBoardsByKeyword(@Query("keyword") String keyword);

        @GET("/api/boards/search/byKeywordKind") // 특정 게시판에서 키워드로 검색
        Call<List<Board>> getBoardsByKeywordKind(@Query("keyword") String keyword, @Query("boardKind") BoardKind boardKind);

        @GET("/api/boards")
        Call<List<Board>> getAllBoards();


    }
}