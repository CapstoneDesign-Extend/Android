package com.example.schoolproject.model.retrofit.cnet;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class CnetApiService {
    private final String API_KEY = "08ad7f206b74aa62db2eefecb8717170";
    private static final String BASE_URL = "http://www.career.go.kr/cnet/openapi/";
    private CnetSchoolApi schoolApi;

    public CnetApiService(){
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

        schoolApi = retrofit.create(CnetSchoolApi.class);
    }

    public Call<CnetSchoolResponse> getSchoolList(String svcType, String svcCode, String contentType, String gubun, String thisPage, String perPage){
        return schoolApi.getSchoolList(API_KEY, svcType, svcCode, contentType, gubun, thisPage, perPage);
    }


}

// https://www.career.go.kr/cnet/front/openapi/openApiSchoolCenter.do
interface CnetSchoolApi {
    @GET("http://www.career.go.kr/cnet/openapi/getOpenApi")
    Call<CnetSchoolResponse> getSchoolList(
            @Query("apiKey") String apiKey,
            @Query("svcType") String svcType,
            @Query("svcCode") String svcCode,
            @Query("contentType") String contentType,
            @Query("gubun") String gubun,
            @Query("thisPage") String thisPage,
            @Query("perPage") String perPage
    );}