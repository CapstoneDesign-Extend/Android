package com.example.schoolproject.restapi;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class MemberApiService{
    //private static final String BASE_URL = "http://221.167.224.209/";
    private static final String BASE_URL = "http://www.codeshare.live/";
    //private static final String BASE_URL = "http://www.naver.com/";
    //private static final String BASE_URL = "http://www.asdf.com/";
    private MemberApi memberApi;

    public MemberApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        memberApi = retrofit.create(MemberApi.class);
    }

    public Call<Member> getMemberById(long id){
        return memberApi.getMemberById(id);
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
}

// declaring API interface
interface MemberApi {
    // 특정 ID의 회원 정보를 조회하는 API 엔드포인트
    @GET("/api/members/{id}")
    Call<Member> getMemberById(@Path("id") Long id);

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