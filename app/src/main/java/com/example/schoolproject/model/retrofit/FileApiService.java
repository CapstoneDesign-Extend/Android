package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.File;

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

public class FileApiService {
    private static final String BASE_URL = "http://www.codeshare.live:5438";
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
        @POST("/api/files")
        Call<File> saveFile(@Body File file);

        @GET("/api/files/{id}")
        Call<File> getFileById(@Path("id") Long id);

        @DELETE("/api/files/{id}")
        Call<Void> deleteFile(@Path("id") Long id);
    }
}