package com.example.schoolproject.model.retrofit;

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

public class MemberApiService{
    //private static final String BASE_URL = "http://localhost:8080";
    private static final String BASE_URL = "http://www.extends.online:5438";
    private MemberApi memberApi;

    public MemberApiService(){
        // add logging interceptor & set loggingLevel
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // add loggingInterceptor to OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        // use OkHttpClient when Retrofit building
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        memberApi = retrofit.create(MemberApi.class);
    }

    public Call<Member> getMemberById(long id){
        return memberApi.getMemberById(id);
    }
    public Call<List<Member>> getMemberByStudentId(int studentId){
        return memberApi.getMemberByStudentId(studentId);
    }
    public Call<Member> getMemberByLoginId(String loginId){
        return memberApi.getMemberByLoginId(loginId);
    }

    public Call<Member> getMemberByEmail(String email){
        return memberApi.getMemberByEmail(email);
    }

    public Call<List<Member>> getAllMembers(){
        return memberApi.getAllMembers();
    }

    public Call<Member> createMember(Member member){
        return memberApi.createMember(member);
    }

    public Call<Void> deleteMember(long id){
        return memberApi.deleteMemberById(id);
    }

    // declaring API interface
    interface MemberApi {
        // 특정 ID의 회원 정보를 조회하는 API 엔드포인트
        @GET("/api/members/{id}")
        Call<Member> getMemberById(@Path("id") Long id);

        // 학번으로 회원 조회
        @GET("/api/members/byStudentId/{studentId}")
        Call<List<Member>> getMemberByStudentId(@Path("studentId") int studentId);

        // 로그인 ID로 회원 조회
        @GET("/api/members/byLoginId/{loginId}")
        Call<Member> getMemberByLoginId(@Path("loginId") String loginId);

        // 이메일로 회원 조회
        @GET("/api/members/byEmail/{email}")
        Call<Member> getMemberByEmail(@Path("email") String email);

        // 모든 회원 정보를 조회하는 API 엔드포인트
        @GET("/api/members")
        Call<List<Member>> getAllMembers();

        // 회원을 생성하는 API 엔드포인트
        @POST("/api/members")
        Call<Member> createMember(@Body Member member);

        // 특정 ID의 회원 정보를 삭제하는 API 엔드포인트
        @DELETE("/api/members/{id}")
        Call<Void> deleteMemberById(@Path("id") Long id);
    }
}

