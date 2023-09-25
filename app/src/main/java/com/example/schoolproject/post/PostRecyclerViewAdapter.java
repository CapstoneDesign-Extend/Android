package com.example.schoolproject.post;

import static android.content.ContentValues.TAG;
import static com.example.schoolproject.model.DateConvertUtils.convertDate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ItemCommentBinding;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.Comment;
import com.example.schoolproject.model.DateConvertUtils;

import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_COMMENT = 1;
    private static final int VIEW_TYPE_REPLY = 2;

    private Context context;
    private List<Object> dataList;

    public PostRecyclerViewAdapter(List<Object> dataList){
        this.dataList = dataList;
    }
    public void clearData(){
        dataList.clear();
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
        private ImageView iv_profile;
        private TextView tv_author, tv_date, tv_time, tv_title, tv_content, tv_heart_count, tv_chat_count;
        private TextView btn_like, btn_scrap;

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
                    Log.d(TAG, "onClick: =========Like버튼 클릭========");

                }
            });
            this.btn_scrap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: =========Scrap버튼 클릭========");
                }
            });
        }
        public void bindData(Board data){
            //iv_profile.setImageResource(data.getImageResourceId());  // (using default image)
            tv_author.setText(data.getAuthor());
            tv_date.setText(convertDate(data.getFinalDate(),"date"));
            tv_time.setText(convertDate(data.getFinalDate(),"time"));
            tv_title.setText(data.getTitle());
            tv_content.setText(data.getContent());
            tv_heart_count.setText(String.valueOf(data.getLikeCnt()));
            tv_chat_count.setText("0");
        }

    }
    // viewHolder 2: Comments
    public class CommentViewHolder extends RecyclerView.ViewHolder{
        private ItemCommentBinding binding;
        public CommentViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bindData(Comment data){
            //binding.ivCommentLike.setImageResource(data.getImageResourceId());
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
                View view1 = inflater.inflate(R.layout.item_post, parent, false);
                return new PostViewHolder(view1);
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
