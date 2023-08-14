package com.example.schoolproject.model.retrofit;

import com.example.schoolproject.model.Timetable;

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

public class TimetableApiService {
    private static final String BASE_URL = "http://www.codeshare.live:5438";
    private TimetableApi timetableApi;

    public TimetableApiService() {
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

        timetableApi = retrofit.create(TimetableApi.class);
    }

    public Call<Timetable> createTimetable(Timetable timetable) {
        return timetableApi.createTimetable(timetable);
    }

    public Call<Timetable> updateTimetable(int year, int semester, Timetable updatedTimetable) {
        return timetableApi.updateTimetable(year, semester, updatedTimetable);
    }

    public Call<Void> deleteTimetable(int year, int semester) {
        return timetableApi.deleteTimetable(year, semester);
    }

    public Call<List<Timetable>> getAllTimetables() {
        return timetableApi.getAllTimetables();
    }

    interface TimetableApi {
        @POST("/api/timetables")
        Call<Timetable> createTimetable(@Body Timetable timetable);

        @PUT("/api/timetables/{year}/{semester}")
        Call<Timetable> updateTimetable(@Path("year") int year,
                                        @Path("semester") int semester,
                                        @Body Timetable updatedTimetable);

        @DELETE("/api/timetables/{year}/{semester}")
        Call<Void> deleteTimetable(@Path("year") int year, @Path("semester") int semester);

        @GET("/api/timetables")
        Call<List<Timetable>> getAllTimetables();
    }
}