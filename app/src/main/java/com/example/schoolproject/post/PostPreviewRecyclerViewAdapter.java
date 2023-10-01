package com.example.schoolproject.post;

import static com.example.schoolproject.model.DateConvertUtils.convertDate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.ui.DataPost;

import java.util.ArrayList;
import java.util.List;

public class PostPreviewRecyclerViewAdapter extends RecyclerView.Adapter<PostPreviewRecyclerViewAdapter.postPreviewHolder>{
    private Context context;
    private List<Object> dataList;

    // db 모델을 ui 모델로 변환 (*초기 코드* 이후 다른 어댑터는 db모델을 그대로 사용)
    public void convertAndSetData(List<Board> boardList){
        if (dataList == null){
            dataList = new ArrayList<>();
        } else {
            dataList.clear();  // Clear exiting data before adding new data
        }
        for(Board b : boardList){
            DataPost p = new DataPost();
            //p.setImageResourceId();
            p.setPostId(b.getId());
            if (!existingSamePost(p.getPostId())) {
                p.setBoardType(String.valueOf(b.getBoardKind()));
                //p.setUserId();
                p.setTitle(b.getTitle());
                p.setContent(b.getContent());
                p.setAuthor(b.getAuthor());
                p.setDate(convertDate(b.getFinalDate(),"date"));
                p.setTime(convertDate(b.getFinalDate(),"time"));
                p.setHeart_count(String.valueOf(b.getLikeCnt()));
                p.setChat_count(String.valueOf(b.getChatCnt()));

                dataList.add(p);
            }
        }
        notifyDataSetChanged();
    }
    private boolean existingSamePost(Long postId){
        if (dataList == null) {return false;}
        for (Object item : dataList){
            DataPost dataPost = (DataPost) item;
            if (dataPost.getPostId() != null && dataPost.getPostId().equals(postId)){
                return true;
            }
        }
        return false;
    }


    public PostPreviewRecyclerViewAdapter(Context context, List<Object> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    public class postPreviewHolder extends RecyclerView.ViewHolder{
        private Long postId;
        private String boardKind;
        protected LinearLayout boardWrapper;
        protected LinearLayout heartWrapper;
        protected LinearLayout chatWrapper;
        protected ImageView imageView;
        protected TextView tv_title;
        protected TextView tv_data;
        protected TextView tv_heartCount;
        protected TextView tv_chatCount;
        protected TextView tv_time;
        protected TextView tv_auth;

        public postPreviewHolder(@NonNull View itemView) {
            super(itemView);
            this.boardWrapper = itemView.findViewById(R.id.board_notice_wrapper);
            this.imageView = itemView.findViewById(R.id.iv_board_notice_preview);
            this.tv_title = itemView.findViewById(R.id.tv_board_notice_title);
            this.tv_data = itemView.findViewById(R.id.tv_board_notice_data);
            this.tv_heartCount = itemView.findViewById(R.id.tv_heart_count);
            this.tv_chatCount = itemView.findViewById(R.id.tv_chat_count);
            this.tv_time = itemView.findViewById(R.id.tv_board_notice_time);
            this.tv_auth = itemView.findViewById(R.id.tv_board_notice_auth);
            this.chatWrapper = itemView.findViewById(R.id.wrapper_chatCount);
            this.heartWrapper = itemView.findViewById(R.id.wrapper_heartCount);

            // set OnClickListener for boardWrapper
            this.boardWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("postId", postId);
                    //Toast.makeText(context, postId.toString(), Toast.LENGTH_SHORT).show();

                    intent.putExtra("boardKind", boardKind);
                    context.startActivity(intent);
                }
            });
        }
        public void bindData(DataPost data){
            // toggle counter visibility
            if (data.getHeart_count().equals("0")){
                heartWrapper.setVisibility(View.GONE);
            }else {
                heartWrapper.setVisibility(View.VISIBLE);
            }
            if (data.getChat_count().equals("0")){
                chatWrapper.setVisibility(View.GONE);
            }else {
                chatWrapper.setVisibility(View.VISIBLE);
            }
            // save postId+boardName and send to PostActivity in onClickListener
            this.postId = data.getPostId();
            this.boardKind = String.valueOf(data.getBoardType());

            if (data.getImageResourceId() == 0 ){
                imageView.setVisibility(View.GONE);
            }else {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(data.getImageResourceId());
            }
            tv_title.setText(data.getTitle());
            tv_data.setText(data.getContent());
            tv_heartCount.setText(data.getHeart_count());
            tv_chatCount.setText(data.getChat_count());
            tv_time.setText(data.getTime());
            tv_auth.setText(data.getAuthor());
        }
    }

    @NonNull
    @Override
    public postPreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_preview,parent,false);
        postPreviewHolder viewHolder = new postPreviewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull postPreviewHolder holder, int position) {
        postPreviewHolder postPreviewHolder = holder;
        DataPost dataPost = (DataPost) dataList.get(position);
        postPreviewHolder.bindData(dataPost);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
