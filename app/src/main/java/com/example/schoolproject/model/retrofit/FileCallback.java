package com.example.schoolproject.model.retrofit;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolproject.model.File;
import com.example.schoolproject.post.PostWriteActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileCallback implements Callback<File> {
    private AppCompatActivity activity;
    private Context context;

    public FileCallback(AppCompatActivity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    private static void showShortToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Call<File> call, Response<File> response) {
        if (response.isSuccessful()) {
            File file = response.body();
            if (file == null || file.getId() == null) {
                // 파일 업로드 실패 또는 오류 처리
                showShortToast(context, "파일 업로드를 실패했습니다.");
            } else {
                // 파일 업로드 성공
                showShortToast(context, "파일 업로드를 완료했습니다.");
                // 서버 응답 처리
            }
        } else {
            // 파일 업로드 실패
            showShortToast(context, "서버로부터 응답을 받을 수 없습니다.");
        }
    }

    @Override
    public void onFailure(Call<File> call, Throwable t) {
        // 네트워크 오류 또는 예외 처리
        showShortToast(context, "인터넷 연결을 확인해주세요.");
    }

    // inner Class:: Callback<ResponseBody>

    public static class ImageCallBack implements Callback<ResponseBody> {
        Activity activity;
        Context context;
        Uri imageUri;

        public ImageCallBack(Activity activity, Context context, Uri imageUri) {
            this.activity = activity;
            this.context = context;
            this.imageUri = imageUri;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                // 이미지 업로드 성공
                // 서버 응답 처리
                ((PostWriteActivity) activity).addImageToScrollView(imageUri);

            } else {
                // 이미지 업로드 실패
                // 오류 처리
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            // 네트워크 오류 또는 예외 처리
        }
    }

}