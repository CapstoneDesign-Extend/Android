package com.example.schoolproject.model.retrofit;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.schoolproject.model.Like;
import com.example.schoolproject.model.Member;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikeCallback implements Callback<Like> {
    private Activity activity;
    private Context context;

    public LikeCallback(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    private static void showShortToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void finishActivity(Activity activity){
        activity.finish();
    }

    @Override
    public void onResponse(Call<Like> call, Response<Like> response) {
        if (response.isSuccessful()) {
            Like like = response.body();
            if (like == null || like.getLikeId() == null) {
                showShortToast(context, "좋아요를 취소합니다.");
            } else {
                showShortToast(context, "좋아요를 눌렀습니다.");
            }
        } else {
            showShortToast(context, "서버로부터 응답을 받을 수 없습니다.");
        }
    }

    @Override
    public void onFailure(Call<Like> call, Throwable t) {
        showShortToast(context, "인터넷 연결을 확인해주세요.");
    }
}
