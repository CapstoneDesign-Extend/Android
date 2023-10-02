package com.example.schoolproject.post;

import static android.content.ContentValues.TAG;
import static com.example.schoolproject.model.DateConvertUtils.convertDate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ItemCommentBinding;
import com.example.schoolproject.databinding.ItemPostBinding;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.Comment;
import com.example.schoolproject.model.DateConvertUtils;
import com.example.schoolproject.model.Like;
import com.example.schoolproject.model.LikeStatus;
import com.example.schoolproject.model.retrofit.CommentApiService;
import com.example.schoolproject.model.retrofit.CommentCallback;
import com.example.schoolproject.model.retrofit.LikeApiService;
import com.example.schoolproject.model.retrofit.LikeCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_COMMENT = 1;
    private static final int VIEW_TYPE_REPLY = 2;
    private Activity activity;
    private Context context;
    private List<Object> dataList;
    private Long postId;  // postViewHolder에서 초기화
    private Long currentUserId;  // sharedPerf에서 가져오기
    private SharedPreferences sharedPrefs;
    private LikeStatus serverLikeStatus;  // PostActivity에서 setter로 설정됨
    // 로컬 likeStatus(recyclerView dataBind()에서 사용), post는 "post", comment는 "{commentId}" 로 구별
    private Map<String, Boolean> localLikeStatus = new HashMap<>();


    public PostRecyclerViewAdapter(Activity activity, Context context, List<Object> dataList) {
        this.activity = activity;
        this.context = context;
        this.dataList = dataList;
        sharedPrefs = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        this.currentUserId = sharedPrefs.getLong("id", -1);
    }

    public void clearData(){
        dataList.clear();
    }

    public void setServerLikeStatus(LikeStatus serverLikeStatus) {
        this.serverLikeStatus = serverLikeStatus;
    }

    public void setData(Object data){
        dataList.add(data);
        notifyDataSetChanged();
    }
    public void setCommentList(List<Comment> list){
        dataList.addAll(list);
        notifyDataSetChanged();
    }
    public int getItemViewType(int position){
        if (dataList.get(position) instanceof Board){
            return VIEW_TYPE_POST;
        } else if (dataList.get(position) instanceof Comment){
            return VIEW_TYPE_COMMENT;
        }
        return -1;
    }

    // ViewHolder 1: Post
    public class PostViewHolder extends RecyclerView.ViewHolder{
        private ItemPostBinding binding;

        private boolean isPostLiked = false;  // 바인딩할때 업데이트됨
        private int currentPostLikeCnt = -1; // 기본값 -1 설정

        public PostViewHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // set listeners
            binding.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {  // Post의 "좋아요" 로직
                    LikeApiService apiService = new LikeApiService();
                    Call<Like> call = apiService.addLikeToBoard(postId, currentUserId);
                    call.enqueue(new LikeCallback(activity, context));
                    if (!isPostLiked){
                        // 텍스트를 Like -> Liked! 로 바꾸기
                        binding.btnLike.setText("Liked!");
                        // 현재 좋아요 개수 가져와서 ++ 하는 로직(빠른 피드백을 위한 View 단독 업데이트, 실제 트랜잭션은 비동기 처리)
                        String currentLikeCntStr = binding.tvHeartCount.getText().toString();
                        try {
                            currentPostLikeCnt = Integer.parseInt(currentLikeCntStr);
                        }catch (NumberFormatException e){
                            // 부적절한 값일 경우 예외 처리
                        }
                        currentPostLikeCnt++;
                        binding.tvHeartCount.setText(String.valueOf(currentPostLikeCnt));
                        // isLiked 플래그 상태 업데이트
                        isPostLiked = true;
                        localLikeStatus.put("post", true);  // post 아이템 스크롤out 후 재결합될때 활용
                    } else {
                        // 텍스트를 다시 원래대로 바꾸기
                        binding.btnLike.setText("Like");
                        // 현재 좋아요 개수 가져와서 -- 하는 로직(빠른 피드백을 위한 View 단독 업데이트, 실제 트랜잭션은 비동기 처리)
                        String currentLikeCntStr = binding.tvHeartCount.getText().toString();
                        try {
                            currentPostLikeCnt = Integer.parseInt(currentLikeCntStr);
                        }catch (NumberFormatException e){
                            // 부적절한 값일 경우 예외 처리
                        }
                        currentPostLikeCnt--;
                        binding.tvHeartCount.setText(String.valueOf(currentPostLikeCnt));
                        // isLiked 플래그 상태 업데이트
                        isPostLiked = false;
                        localLikeStatus.put("post", false);  // post 아이템 스크롤out 후 재결합될때 활용
                    }
                }
            });
            binding.btnScrap.setOnClickListener(new View.OnClickListener() {  // Post의 Scrap 버튼 클릭 시 동작
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: =========Scrap버튼 클릭========");
                }
            });
        }
        public void bindData(Board data){

            // "좋아요" 클릭 상태 Setting 로직
            if (localLikeStatus.get("post") != null){        // 로컬 상태가 존재한다면, 로컬의 값을 기준으로 UI 변경
                if (localLikeStatus.get("post")){
                    // 텍스트를 Like -> Liked! 로 바꾸기
                    binding.btnLike.setText("Liked!");
                    isPostLiked = true;
                } else {
                    // 텍스트를 기본값으로 변경
                    binding.btnLike.setText("Like");
                    isPostLiked = false;
                }
                binding.tvHeartCount.setText(String.valueOf(currentPostLikeCnt));  // 로컬의 cnt값 표시
            } else {                                // 로컬 상태가 존재하지 않는다면, 서버에서 가져온 LikedState에 따라 UI 설정
                if (serverLikeStatus.isLikedBoard()){
                    // 텍스트를 Like -> Liked! 로 바꾸기
                    binding.btnLike.setText("Liked!");
                    isPostLiked = true;
                }else {
                    // 텍스트를 기본값으로 변경
                    binding.btnLike.setText("Like");
                    isPostLiked = false;
                }
                binding.tvHeartCount.setText(String.valueOf(data.getLikeCnt()));  // 서버의 cnt 값 표시
            }
            // 나머지 값 설정
            // iv_profile.setImageResource(data.getImageResourceId());  // (using default image)
            binding.tvPostAuthor.setText(data.getAuthor());
            binding.tvPostDate.setText(convertDate(data.getFinalDate(),"date"));
            binding.tvPostTime.setText(convertDate(data.getFinalDate(),"time"));
            binding.tvPostTitle.setText(data.getTitle());
            binding.tvPostContent.setText(data.getContent());
            binding.tvChatCount.setText(String.valueOf(data.getChatCnt()));
            postId = data.getId();  // 상위 클래스로 넘긴 후 CommentViewHolder클래스에서 사용(댓글 삭제 후 갱신 로직)

            // 이미지 처리
//            if (data.hasImages()){
//
//            }
        }

    }
    // viewHolder 2: Comments
    public class CommentViewHolder extends RecyclerView.ViewHolder{
        private ItemCommentBinding binding;
        private Long memberId; // 댓글을 작성한 사람의 id (DB SEQ)
        private Long commentId; // 댓글 id
        private SharedPreferences sharedPrefs;
        private boolean isCommentLiked = false;
        private int currentCommentLikeCnt = -1;  // 초기화 값 지정
        public CommentViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.ivCommentLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {  // Comment의 "좋아요" 로직
                    LikeApiService apiService = new LikeApiService();
                    Call<Like> call = apiService.addLikeToComment(commentId, currentUserId);
                    call.enqueue(new LikeCallback(activity, context));
                    //Log.e(TAG, "isCommentLiked: "+isCommentLiked +  "likeCnt_TEXTVIEW: " + binding.tvCommentHeartCount.getText().toString());
                    if (!isCommentLiked){
                        // 현재 좋아요 개수 가져와서 ++ 하는 로직(빠른 피드백을 위한 View 단독 업데이트, 실제 트랜잭션은 비동기 처리)
                        String currentLikeCntStr = binding.tvCommentHeartCount.getText().toString();
                        try {
                            currentCommentLikeCnt = Integer.parseInt(currentLikeCntStr);
                        }catch (NumberFormatException e){
                            // 부적절한 값일 경우 예외 처리
                        }
                        currentCommentLikeCnt++;
                        // 로컬 변경값으로 UI 업데이트
                        binding.tvCommentHeartCount.setText(String.valueOf(currentCommentLikeCnt));
                        // 아이콘 색 변경
                        binding.ivCommentLike.setImageResource(R.drawable.icon_heart_gicon_colored);
                        // 플래그 상태 업데이트
                        isCommentLiked = true;
                        localLikeStatus.put(commentId.toString(), true); // comment 아이템 스크롤out 후 재결합될때 활용
                        // "좋아요"를 눌러 0에서 1로 변했을때
                        if (currentCommentLikeCnt == 1){
                            binding.wrapperHeartCount.setVisibility(View.VISIBLE);
                        }
                    }else {
                        // 현재 좋아요 개수 가져와서 ++ 하는 로직(빠른 피드백을 위한 View 단독 업데이트, 실제 트랜잭션은 비동기 처리)
                        String currentLikeCntStr = binding.tvCommentHeartCount.getText().toString();
                        try {
                            currentCommentLikeCnt = Integer.parseInt(currentLikeCntStr);
                        }catch (NumberFormatException e){
                            // 부적절한 값일 경우 예외 처리
                        }
                        currentCommentLikeCnt--;
                        binding.tvCommentHeartCount.setText(String.valueOf(currentCommentLikeCnt));
                        // 아이콘 색 변경
                        binding.ivCommentLike.setImageResource(R.drawable.icon_heart_gicon);
                        // 플래그 상태 업데이트
                        isCommentLiked = false;
                        localLikeStatus.put(commentId.toString(), false); // comment 아이템 스크롤out 후 재결합될때 활용
                        // "좋아요"를 눌러 1에서 0으로 변했을때
                        if (currentCommentLikeCnt == 0){
                            binding.wrapperHeartCount.setVisibility(View.GONE);
                        }
                    }
                }
            });
            binding.ivCommentMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View anchorView = binding.ivCommentMore;  // 이 뷰 주변에 팝업 메뉴 표시
                    PopupMenu popupMenu = new PopupMenu(context, anchorView);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    // 현재 접속자가 작성한 댓글에만 삭제 메뉴 표시
                    if (currentUserId == memberId){
                        inflater.inflate(R.menu.comment_menu_author, popupMenu.getMenu());
                    }else{
                        inflater.inflate(R.menu.comment_menu, popupMenu.getMenu());
                    }
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.item_message:  // 메시지 전송
                                    Toast.makeText(context, "메시지 전송", Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.item_report:  // 댓글 신고
                                    Toast.makeText(context, "해당 댓글을 신고합니다.", Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.item_delete:  // 댓글 삭제 로직
                                    CommentApiService apiService = new CommentApiService();
                                    Call<Void> call = apiService.deleteCommentById(commentId);
                                    call.enqueue(new CommentCallback.DeleteCommentCallBack(activity, context, PostRecyclerViewAdapter.this, postId));
                                    return true;
                                default: return false;
                            }

                        }
                    });

                }
            });
        }
        public void bindData(Comment data){
            //binding.ivCommentLike.setImageResource(data.getImageResourceId());
            memberId = data.getMemberId();
            commentId = data.getId();

            // "좋아요" 클릭 상태 setting 로직
            if (localLikeStatus.get(commentId.toString()) != null){     // 로컬 Like상태가 존재하면, 로컬의 것을 사용
                if (localLikeStatus.get(commentId.toString())) {  // 로컬에서 like을 눌렀을때
                    // 아이콘 색 변경
                    binding.ivCommentLike.setImageResource(R.drawable.icon_heart_gicon_colored);
                    isCommentLiked = true;
                } else {                                          // 로컬에서 like를 취소했을때
                    // 아이콘 원래대로
                    binding.ivCommentLike.setImageResource(R.drawable.icon_heart_gicon);
                    isCommentLiked = false;
                }
            } else {                                                    // 로컬값이 없으면 서버의 Like상태 사용
                if (serverLikeStatus.getLikedCommentIds().contains(commentId)){
                    // 아이콘 색 변경
                    binding.ivCommentLike.setImageResource(R.drawable.icon_heart_gicon_colored);
                    isCommentLiked = true;
                } else {
                    // 아이콘 원래대로
                    binding.ivCommentLike.setImageResource(R.drawable.icon_heart_gicon);
                    isCommentLiked = false;
                }
            }
            // 아이콘 가시성 로직 적용
            if (currentCommentLikeCnt != -1) {  // 값이 초기값에서 변경된 이력이 있다면 로컬에서 조작한것.
                if (currentCommentLikeCnt == 0){
                    binding.wrapperHeartCount.setVisibility(View.GONE);
                } else {
                    binding.wrapperHeartCount.setVisibility(View.VISIBLE);
                }
            } else {                            // 로컬에서 조작하지 않았다면 서버의 것을 사용
                if (data.getLikeCount() == 0){
                    binding.wrapperHeartCount.setVisibility(View.GONE);
                } else {
                    binding.wrapperHeartCount.setVisibility(View.VISIBLE);
                }
            }
            // 나머지 값 설정
            binding.tvCommentAuthor.setText(data.getAuthor());
            binding.tvCommentContents.setText(data.getContent());
            binding.tvCommentDate.setText(DateConvertUtils.convertDate(data.getFinalDate().toString(), "date"));
            binding.tvCommentTime.setText(DateConvertUtils.convertDate(data.getFinalDate().toString(), "time"));
            binding.tvCommentHeartCount.setText(String.valueOf(data.getLikeCount()));
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case VIEW_TYPE_POST:
                ItemPostBinding postBinding = ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
                return new PostViewHolder(postBinding);

            case VIEW_TYPE_COMMENT:
                ItemCommentBinding commentBinding = ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
                return new CommentViewHolder(commentBinding);

            default:
                throw new IllegalArgumentException("Invalid view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object itemData = dataList.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_POST:
                PostViewHolder postViewHolder = (PostViewHolder) holder;
                Board board = (Board) itemData;
                postViewHolder.bindData(board);
                break;
            case VIEW_TYPE_COMMENT:
                CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
                Comment comment = (Comment) itemData;
                commentViewHolder.bindData(comment);
                break;
            //case VIEW_TYPE_REPLY: break;

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
