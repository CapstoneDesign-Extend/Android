package com.example.schoolproject.model.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class FileCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            onError(response.code(), response.message());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(t);
    }

    // 성공 시 호출될 메서드
    public abstract void onSuccess(T result);

    // API 응답이 성공이지만 예상된 형태가 아닌 경우 호출될 메서드
    public void onError(int errorCode, String errorMessage) {
        // 기본적으로 에러 메시지를 출력
        // 필요에 따라서 확장 가능
        System.err.println("Error Code: " + errorCode);
        System.err.println("Error Message: " + errorMessage);
    }

    // 통신 실패나, 예외 상황 등에서 호출될 메서드
    public void onFailure(Throwable t) {
        // 기본적으로 예외 메시지를 출력
        // 필요에 따라서 확장 가능
        t.printStackTrace();
    }
}
