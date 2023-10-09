package com.example.schoolproject.model.retrofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;
import com.example.schoolproject.model.Comment;
import com.example.schoolproject.model.FileEntity;
import com.example.schoolproject.model.ui.DataHomeBoard;
import com.example.schoolproject.nav.home.HomeRecyclerViewAdapter;
import com.example.schoolproject.nav.shop.ShopRecyclerViewAdapter;
import com.example.schoolproject.post.PostActivity;
import com.example.schoolproject.post.PostPreviewRecyclerViewAdapter;
import com.example.schoolproject.post.PostRecyclerViewAdapter;
import com.example.schoolproject.post.PostWriteActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
// BoardCallBack(Create/Read/Update)
public class BoardCallback implements Callback<Board> {
    private Long postId;
    private Activity activity;
    private Context context;
    private RecyclerView.Adapter adapter;
    private List<Uri> imageUriList = new ArrayList<>();  // 게시글 작성 시의 이미지Uri  받아오기

    public BoardCallback(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public BoardCallback(Activity activity, Context context, List<Uri> imageUriList) {
        this.activity = activity;
        this.context = context;
        this.imageUriList = imageUriList;
    }

    public BoardCallback(Activity activity, Context context, RecyclerView.Adapter adapter) {
        this.activity = activity;
        this.context = context;
        this.adapter = adapter;
    }
    public BoardCallback(Long postId, Activity activity, Context context, RecyclerView.Adapter adapter){
        this.postId = postId;
        this.activity = activity;
        this.context = context;
        this.adapter = adapter;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    private static void showShortToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    private void finishActivity(Activity activity){
        activity.finish();
    }

    @Override
    public void onResponse(Call<Board> call, Response<Board> response) {
        if (response.isSuccessful()) {
            Board board = response.body();  // board 에는 imageURL 목록도 있음. 없다면 빈 객체가 저장됨
            String requestUrl = call.request().url().toString();  // 이 값을 구별해 적절한 로직 처리
            if (call.request().method().equals("GET")){
                PostRecyclerViewAdapter postAdapter = (PostRecyclerViewAdapter) adapter;
                postAdapter.setData(board);  // UPDATE Data: Post  -->  이때, 이미지도 함께 바인딩됨
                PostActivity postActivity = (PostActivity) activity;
                postActivity.setPostAuthorId(board.getMemberId());  // set postAuthorId to delete post or comments
                postActivity.setPostTitle(board.getTitle());
                postActivity.setPostContent(board.getContent());
                postActivity.setCallbackCompleted(true);  // 콜백 완료 알림
                postActivity.invalidateOptionsMenu();  // 메뉴 표시
                // 두 번째 콜백 호출(1: Post 2: Comment <--)
                CommentApiService commentApiService = new CommentApiService();
                Call<List<Comment>> call2 = commentApiService.getCommentsByBoardId(postId);
                call2.enqueue(new CommentCallback.CommentListCallBack(context, adapter));


            }else if (call.request().method().equals("POST")){
                if (imageUriList.isEmpty()){
                    // 일반 게시글 작성 시
                    showShortToast(context, "게시글 작성이 완료되었습니다.");
                    finishActivity(activity);
                }else{
                    // 이미지 업로드 시
                    PostWriteActivity postWriteActivity = (PostWriteActivity) activity;
                    postWriteActivity.setPostId(board.getId());

                    // 이미지 업로드하는 동안 사용자 입력 막기
                    ProgressDialog progressDialog = new ProgressDialog(activity);
                    progressDialog.setMessage("이미지 업로드 중입니다.");
                    progressDialog.setCancelable(false);  // 사용자가 취소할 수 없도록 설정
                    progressDialog.show();

                    postWriteActivity.uploadImageList(imageUriList, progressDialog);  // 이 안에서 액티비티 종료

                }


            } else if (call.request().method().equals("PUT")){
                FileApiService fileApiService = new FileApiService();

                // 일단 서버의 이미지를 모두 삭제하는 호출 실행 (업데이트 시에도 삭제후 업로드 하기 때문)
                Call<ResponseBody> deleteCall = fileApiService.deleteFilesByBoardId(board.getId());
                deleteCall.enqueue(new FileCallback<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody result) {
                        // 이미지 삭제 완료
                        if (!imageUriList.isEmpty()){
                            // 이미지 업로드
                            PostWriteActivity postWriteActivity = (PostWriteActivity) activity;
                            postWriteActivity.setPostId(board.getId());

                            ProgressDialog progressDialog = new ProgressDialog(activity);
                            progressDialog.setMessage("이미지 업로드 중입니다.");
                            progressDialog.setCancelable(false);  // 사용자가 취소할 수 없도록 설정
                            progressDialog.show();

                            postWriteActivity.uploadImageList(imageUriList, progressDialog);  // 이 함수 안에서 액티비티 종료

                        } else {
                            showShortToast(context, "게시글 수정이 완료되었습니다.");
                            finishActivity(activity);
                        }
                    }
                });



            } // delete 는 리턴이 Void 이기 때문에, 별도의 클래스로 작성함

        } else {
            showShortToast(context, "서버로부터 응답을 받을 수 없습니다.");

        }
    }

    @Override
    public void onFailure(Call<Board> call, Throwable t) {
        showShortToast(context, "인터넷 연결을 확인해주세요.");
    }



    // public inner class : BoardListCallBack(리스트 형태인 경우)

    public static class BoardListCallBack implements Callback<List<Board>>{
        private Context context;
        private RecyclerView.Adapter adapter;
        private BoardKind boardKind;
        private List<DataHomeBoard> dataHomeBoardList;

        public BoardListCallBack(Context context, RecyclerView.Adapter adapter){
            this.context = context;
            this.adapter = adapter;
        }
        public BoardListCallBack(Context context, RecyclerView.Adapter adapter, BoardKind boardKind, List<DataHomeBoard> dataHomeBoardList) {
            this.context = context;
            this.adapter = adapter;
            this.boardKind = boardKind;
            this.dataHomeBoardList = dataHomeBoardList;

        }

        @Override
        public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
            if (response.isSuccessful()) {
                List<Board> boardList = response.body();
                // distinguishing by instance
                if (adapter instanceof PostPreviewRecyclerViewAdapter) {    // PreviewPost
                    PostPreviewRecyclerViewAdapter postPrevAdapter = (PostPreviewRecyclerViewAdapter) adapter;
                    postPrevAdapter.convertAndSetData(boardList);  // 레거시 코드::서버 모델을 클라이언트 자체 모델로 변환

                } else if (adapter instanceof HomeRecyclerViewAdapter){  // Home
                    HomeRecyclerViewAdapter homeAdapter = (HomeRecyclerViewAdapter) adapter;
                    homeAdapter.convertData(boardKind, boardList, dataHomeBoardList);

                } else if (adapter instanceof ShopRecyclerViewAdapter){  // PreviewShop
                    ShopRecyclerViewAdapter shopAdapter = (ShopRecyclerViewAdapter) adapter;
                    shopAdapter.setData(boardList);
                }

                adapter.notifyDataSetChanged();
                // *주의*: request url에 해당 키워드가 실제로 포함되는지 확인 후 구현하기
                if (call.request().url().toString().contains("byBoardKind")) {

                } else if (call.request().url().toString().contains("byBoardKindAmount")){

                } else if (call.request().url().toString().contains("byTitle")) {

                } else if (call.request().url().toString().contains("byKeyword")) {

                }  else if (call.request().url().toString().contains("byKeywordKind")) {

                } else if (call.request().url().toString().contains("boards")) {
                    // 모든 url에 boards가 포함되므로 가장 마지막에 두기

                }
            } else {
//                if (call.request().url().toString().contains("byBoardKindAmount")){
//                    if (adapter instanceof HomeRecyclerViewAdapter){
//                        HomeRecyclerViewAdapter homeAdapter = (HomeRecyclerViewAdapter) adapter;
//                        homeAdapter.setData(boardKind, new ArrayList<>());
//                    }
//                } else {
                    showShortToast(context, "서버로부터 응답을 받을 수 없습니다.");
//                }
            }
        }
        @Override
        public void onFailure(Call<List<Board>> call, Throwable t) {
            showShortToast(context, "인터넷 연결을 확인해주세요.");
        }
    }

    // public inner class : Delete
    public static class DeleteBoardCallBack implements Callback<Void>{
        private Activity activity;
        private Context context;

        public DeleteBoardCallBack(Activity activity, Context context) {
            this.activity = activity;
            this.context = context;
        }

        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            showShortToast(context, "게시글이 삭제되었습니다.");
            activity.finish();
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            showShortToast(context, "Delete_onFailure");
        }
    }

}

