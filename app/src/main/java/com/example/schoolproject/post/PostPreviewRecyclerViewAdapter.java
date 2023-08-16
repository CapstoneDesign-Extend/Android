package com.example.schoolproject.post;

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
import com.example.schoolproject.model.ui.DataPost;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PostPreviewRecyclerViewAdapter extends RecyclerView.Adapter<PostPreviewRecyclerViewAdapter.postPreviewHolder>{
    private Context context;
    private List<Object> dataList;
    public PostPreviewRecyclerViewAdapter(Context context, List<Object> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    public class postPreviewHolder extends RecyclerView.ViewHolder{
        private int postId;
        private String boardName;
        protected LinearLayout boardWrapper;
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

            // set OnClickListener for boardWrapper
            this.boardWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "move to post", 100).show();
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("postId", postId);
                    intent.putExtra("boardName", boardName);
                    context.startActivity(intent);
                }
            });
        }
        public void bindData(DataPost data){
            // save postId+boardName and send to PostActivity in onClickListener
            this.postId = data.getPostId();
            this.boardName = data.getBoardType();

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
