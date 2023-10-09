package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.FileEntity;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class FileApiService {
    private static final String BASE_URL = "http://www.extends.online:5438";
    private FileApi fileApi;

    public FileApiService() {
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

        fileApi = retrofit.create(FileApi.class);
    }

    public Call<List<FileEntity>> uploadFiles(List<MultipartBody.Part> files, Long boardId){
        return fileApi.uploadFiles(files, boardId);
    }

    public Call<ResponseBody> downloadFile(Long id) {
        return fileApi.downloadFile(id);
    }

    public Call<List<String>> getFileUrlsByBoardId(Long boardId) {
        return fileApi.getFileUrlsByBoardId(boardId);
    }



    interface FileApi {
        @Multipart
        @POST("/api/file/upload")
        Call<List<FileEntity>> uploadFiles(
                @Part List<MultipartBody.Part> files,
                @Query("boardId") Long boardId
        );

        @GET("/api/file/download/{id}")
        Call<ResponseBody> downloadFile(@Path("id") Long id);

        @GET("/api/file/urls/{boardId}")
        Call<List<String>> getFileUrlsByBoardId(@Path("boardId") Long boardId);

    }
}