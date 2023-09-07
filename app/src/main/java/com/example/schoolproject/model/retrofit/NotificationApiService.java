package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.Notification;

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

public class NotificationApiService {
    private static final String BASE_URL = "http://www.extends.online:5438";
    private NotificationApi notificationApi;

    public NotificationApiService() {
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

        notificationApi = retrofit.create(NotificationApi.class);
    }

    public Call<List<Notification>> getNotificationsByMemberId(Long memberId) {
        return notificationApi.getNotificationsByMemberId(memberId);
    }

    public Call<Notification> createNotification(Long memberId, String content) {
        return notificationApi.createNotification(memberId, content);
    }

    public Call<Void> deleteNotification(Long notificationId) {
        return notificationApi.deleteNotification(notificationId);
    }

    interface NotificationApi {
        @GET("/api/notifications/{memberId}")
        Call<List<Notification>> getNotificationsByMemberId(@Path("memberId") Long memberId);

        @POST("/api/notifications/{memberId}")
        Call<Notification> createNotification(@Path("memberId") Long memberId, @Body String content);

        @DELETE("/api/notifications/{notificationId}")
        Call<Void> deleteNotification(@Path("notificationId") Long notificationId);
    }
}