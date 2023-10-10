package com.example.schoolproject;

import static androidx.camera.core.CameraXThreads.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolproject.databinding.ActivityCameraBinding;
import com.example.schoolproject.model.Access;
import com.example.schoolproject.model.Member;
import com.example.schoolproject.model.retrofit.MemberApiService;
import com.example.schoolproject.model.retrofit.MemberCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivity extends AppCompatActivity {
    ActivityCameraBinding binding;

    private Preview preview;
    private ImageCapture imageCapture;
    private ImageAnalysis imageAnalysis;
    private CameraSelector cameraSelector;
    private ProcessCameraProvider cameraProvider;

    private boolean isCameraResultVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //   =============  다음 버튼 클릭시 동작  =============
        binding.btnCameraNext.setOnClickListener(view -> {
            // 저장된 사용자의 정보 가져오기
            SharedPreferences sPrefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
            String json = sPrefs.getString("memberJson", null);
            Gson gson = new Gson();
            Member member = gson.fromJson(json, Member.class);
            // 회원정보와 학생증 정보가 같으면, 학과정보를 추가해서 회원정보 수정 후, 권한 부여
            if (
                    binding.tvCameraSchoolname.getText().toString().equals(member.getSchoolName())
                    && binding.tvCameraName.getText().toString().equals(member.getName())
                    && binding.tvCameraSid.getText().toString().equals(String.valueOf(member.getStudentId()))
            ){
                // member에 학과정보 추가 및 권한 부여
                member.setDepartment(binding.tvCameraDepartment.getText().toString());
                member.setAccess(Access.STUDENT);



                MemberApiService apiService = new MemberApiService();
                Call<Member> call = apiService.updateMember(member.getId(), member);
                call.enqueue(new Callback<Member>() {
                    @Override
                    public void onResponse(Call<Member> call, Response<Member> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "학생증 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            // 앱 내부에도 변경사항 저장
                            SharedPreferences.Editor editor = sPrefs.edit();
                            editor.putString("memberJson", gson.toJson(member));
                            editor.putBoolean("isCertified", true);
                            editor.apply();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "서버로부터 응답을 받을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Member> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                // 회원정보와 학생증 정보가 다를 때 처리
                Toast.makeText(getApplicationContext(), "회원정보와 학생증 정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //   =============  다시 스캔하기 버튼 클릭 시 동작  =============
        binding.tvCameraRetry.setOnClickListener(view -> {
            if (isCameraResultVisible){
                binding.wrapperCameraResult.setVisibility(View.GONE);
                isCameraResultVisible = false;
            }
            // 카메라 다시 시작
            startCamera();
        });

        PreviewView previewView = findViewById(R.id.previewView);

        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        preview = new Preview.Builder().build();

        imageCapture = new ImageCapture.Builder().build();

        imageAnalysis = new ImageAnalysis.Builder()
                .setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                // 이미지 처리 및 OCR 실행
                processImage(image);
            }
        });

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                // 기존에 바인딩된 카메라를 언바인드합니다.
                cameraProvider.unbindAll();

                // CameraSelector, Preview, ImageCapture 및 ImageAnalysis를 사용하여 카메라를 바인드합니다.
                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);

                // 카메라 미리보기를 설정합니다.
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

            } catch (ExecutionException | InterruptedException e) {
                // 에러 처리를 해주는 부분입니다.
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));

    }
    // ======================================  OnCreate 끝  ========================================
    @OptIn(markerClass = ExperimentalGetImage.class)
    private void processImage(ImageProxy image){
        InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());
        TextRecognizer recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());

        recognizer.process(inputImage)
                .addOnSuccessListener(text -> {
                    // 추출된 텍스트를 사용자에게 표시
                    String extractedText = text.getText();
                    //Toast.makeText(getApplicationContext(), extractedText, Toast.LENGTH_SHORT).show();
                    if (extractedText.contains("대학교")){

                        Pattern pattern = Pattern.compile("\\d{5,}");
                        Matcher matcher = pattern.matcher(extractedText);

                        if (matcher.find() || extractedText.contains("학과")){
                            // 인식한 텍스트가 학생증인 것 같다면 배열에 추가하고, 이 값을 넘겨주기
                            //extractedList.add(extractedText);
                            // 카메라 인식을 중단하기
                            cameraProvider.unbindAll();
                            // 가져온 값을 뷰에 설정하는 함수 호츌
                            setData(extractedText);
                            // 인식결과 보이게 설정
                            binding.wrapperCameraResult.setVisibility(View.VISIBLE);
                            isCameraResultVisible = true;
                            Log.e("TAG", "text: =============" +extractedText);


                            image.close();
                        }
                    }
                    image.close();
                })
                .addOnFailureListener(e -> {
                    // 에러 발생 시 처리
                    e.printStackTrace();
                });

    }
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // 기존에 바인딩된 카메라를 언바인드합니다.
                cameraProvider.unbindAll();

                // CameraSelector, Preview, ImageCapture 및 ImageAnalysis를 사용하여 카메라를 바인드합니다.
                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);

                // 카메라 미리보기를 설정합니다.
                preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

            } catch (ExecutionException | InterruptedException e) {
                // 에러 처리를 해주는 부분입니다.
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }
    private void setData(String data) {

        Pattern universityPattern = Pattern.compile("\\b\\w*대학교\\w*\\b");
        Matcher universityMatcher = universityPattern.matcher(data);
        // "대학교"가 포함된 문자열 블록 추출
        if (universityMatcher.find()) {
            String universityBlock = universityMatcher.group(0);
            binding.tvCameraSchoolname.setText(universityBlock);
        }

        // 숫자로 이루어진 학번 추출
        Pattern studentIDPattern = Pattern.compile("\\d{7,}");
        Matcher studentIDMatcher = studentIDPattern.matcher(data);

        if (studentIDMatcher.find()) {
            String studentID = studentIDMatcher.group(0);
            binding.tvCameraSid.setText(studentID);
        }

        // "학과"로 끝나는 문자열 추출
        Pattern departmentPattern = Pattern.compile(".*학과");
        Matcher departmentMatcher = departmentPattern.matcher(data);

        if (departmentMatcher.find()) {
            String departmentBlock = departmentMatcher.group(0);
            binding.tvCameraDepartment.setText(departmentBlock);
        }

        // 이름 추출:: "학생" 과 "대학" 을 포함하지 않는 2~4자리 한글 단어
        Pattern namePattern = Pattern.compile("\\b(?!학생|대학)[가-힣]{2,4}\\b");
        Matcher nameMatcher = namePattern.matcher(data);
        if (nameMatcher.find()) {
            String nameBlock = nameMatcher.group(0);
            binding.tvCameraName.setText(nameBlock);
        }

    }

}