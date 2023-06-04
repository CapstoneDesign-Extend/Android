package com.example.schoolproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_COMMENT = 1;
    private static final int VIEW_TYPE_REPLY = 2;
    private List<Object> dataList;

    public PostRecyclerViewAdapter(List<Object> dataList){
        this.dataList = dataList;
    }
    public int getItemViewType(int position){
        if (dataList.get(position) instanceof DataPost){
            return VIEW_TYPE_POST;
        }
        return -1;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_profile;
        private TextView tv_author, tv_date, tv_time, tv_title, tv_content, tv_heart_count, tv_chat_count;
        private Button btn_like, btn_scrap;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_post_profile);
            this.tv_author = itemView.findViewById(R.id.tv_post_author);
            this.tv_date = itemView.findViewById(R.id.tv_post_date);
            this.tv_time = itemView.findViewById(R.id.tv_post_time);
            this.tv_title = itemView.findViewById(R.id.tv_post_title);
            this.tv_content = itemView.findViewById(R.id.tv_post_content);
            this.tv_heart_count = itemView.findViewById(R.id.tv_heart_count);
            this.tv_chat_count = itemView.findViewById(R.id.tv_chat_count);
            this.btn_like = itemView.findViewById(R.id.btn_like);
            this.btn_scrap = itemView.findViewById(R.id.btn_scrap);

            // set listeners
            this.btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            this.btn_scrap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        public void bindData(DataPost data){
            //iv_profile.setImageResource(data.getImageResourceId());  // (using default image)
            tv_author.setText(data.getAuthor());
            tv_date.setText(data.getDate());
            tv_time.setText(data.getTime());
            tv_title.setText(data.getTitle());
            tv_content.setText(data.getContent());
            tv_heart_count.setText(data.getHeart_count());
            tv_chat_count.setText(data.getChat_count());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case VIEW_TYPE_POST:
                View view1 = inflater.inflate(R.layout.item_post, parent, false);
                return new PostViewHolder(view1);
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
                DataPost dataPost = (DataPost) itemData;
                postViewHolder.bindData(dataPost);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
