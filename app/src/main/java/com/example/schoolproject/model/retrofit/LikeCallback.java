package com.example.schoolproject.model.retrofit;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.schoolproject.model.Like;
import com.example.schoolproject.model.LikeStatus;
import com.example.schoolproject.test.MyUtils;

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
//                showShortToast(context, "좋아요를 취소합니다.");
            } else {
//                showShortToast(context, "좋아요를 눌렀습니다.");
            }
        } else {
            showShortToast(context, "서버로부터 응답을 받을 수 없습니다.");
        }
    }

    @Override
    public void onFailure(Call<Like> call, Throwable t) {
        showShortToast(context, "인터넷 연결을 확인해주세요.");
    }

    // Static Inner Class:: IsLikedCallBack
    public static class IsLikedCallback implements Callback<Boolean> {
        private Context context;

        public IsLikedCallback(Context context) {
            this.context = context;
        }

        @Override
        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
            if (response.isSuccessful()) {
                Boolean result = response.body();
                if (result != null && result) {
//                    showShortToast(context, "좋아요가 존재합니다.");
                } else {
//                    showShortToast(context, "좋아요가 존재하지 않습니다.");
                }
            } else {
                showShortToast(context, "서버로부터 응답을 받을 수 없습니다.");
            }
        }

        @Override
        public void onFailure(Call<Boolean> call, Throwable t) {
            showShortToast(context, "인터넷 연결을 확인해주세요.");
        }
    }

    // Static Inner Class:: GetLikeStatusCallback
    public static class GetLikeStatusCallback implements Callback<LikeStatus> {
        private Context context;
        private MutableLiveData<LikeStatus> liveData;

        public GetLikeStatusCallback(Context context, MutableLiveData<LikeStatus> liveData) {
            this.context = context;
            this.liveData = liveData;
        }

        @Override
        public void onResponse(Call<LikeStatus> call, Response<LikeStatus> response) {
            if (response.isSuccessful()) {
                LikeStatus likeStatus = response.body();

                liveData.setValue(likeStatus);  // liveData 업데이트
                if (likeStatus != null) {
//                    showShortToast(context, "좋아요 상태를 가져왔습니다.");
                } else {
//                    showShortToast(context, "좋아요 상태 정보가 존재하지 않습니다.");
                }
            } else {
                showShortToast(context, "서버로부터 응답을 받을 수 없습니다.");
            }
        }

        @Override
        public void onFailure(Call<LikeStatus> call, Throwable t) {
            showShortToast(context, "인터넷 연결을 확인해주세요.");
        }

        private static void showShortToast(Context context, String message){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
