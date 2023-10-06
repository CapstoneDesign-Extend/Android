package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.File;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    public Call<Board> uploadImageFiles(List<MultipartBody.Part> images, Long boardId){
        return fileApi.uploadImageFiles(images, boardId);
    }

    public Call<Board> uploadAttachFile(MultipartBody.Part attachFile, Long boardId) {
        return fileApi.uploadAttachFile(attachFile, boardId);
    }
    public Call<File> saveFile(File file) {
        return fileApi.saveFile(file);
    }

    public Call<File> getFileById(Long id) {
        return fileApi.getFileById(id);
    }

    public Call<Void> deleteFile(Long id) {
        return fileApi.deleteFile(id);
    }

    interface FileApi {
        @Multipart
        @POST("api/files/uploadImage")
        Call<Board> uploadImageFiles(@Part List<MultipartBody.Part> images, @Part("boardId") Long boardId);

        @Multipart
        @POST("api/files/uploadAttach")
        Call<Board> uploadAttachFile(@Part MultipartBody.Part attachFile, @Part("boardId") Long boardId);


        @POST("/api/files")
        Call<File> saveFile(@Body File file);

        @GET("/api/files/{id}")
        Call<File> getFileById(@Path("id") Long id);

        @DELETE("/api/files/{id}")
        Call<Void> deleteFile(@Path("id") Long id);
    }
}