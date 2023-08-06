package com.example.schoolproject.restapi;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberCallback implements Callback<Member> {

    @Override
    public void onResponse(Call<Member> call, Response<Member> response) {
        if (response.isSuccessful()){
            Member member = response.body();
            // 서버로부터 받아온 회원 정보(member)를 처리하는 로직을 작성합니다.
            // 예를 들어, 회원 정보를 화면에 표시하거나 필요한 처리를 수행할 수 있습니다.
        }else {
            // 서버로부터 응답이 실패한 경우
            // 에러 처리를 해주는 로직을 작성합니다.
        }
    }

    @Override
    public void onFailure(Call<Member> call, Throwable t) {
        // 통신 중 네트워크 오류 등이 발생한 경우
        // 에러 처리를 해주는 로직을 작성합니다.
    }
}
