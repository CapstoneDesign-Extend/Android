package com.example.schoolproject.nav.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ItemShopPreviewBinding;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.DateConvertUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ShopRecyclerViewAdapter extends RecyclerView.Adapter<ShopRecyclerViewAdapter.postPreviewHolder>{
    private Context context;
    private List<Object> dataList;

    public ShopRecyclerViewAdapter(Context context, List<Object> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    public void setData(List<Board> boardList) {
        if (dataList == null){
            dataList = new ArrayList<>();
        }else {
            dataList.clear();
        }
        this.dataList.addAll(boardList);
        notifyDataSetChanged();
    }

    public class postPreviewHolder extends RecyclerView.ViewHolder{
        private ItemShopPreviewBinding binding;
        private Long postId;


        public postPreviewHolder(@NonNull ItemShopPreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            // set OnClickListener for postWrapper
            binding.shopWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "move to post", 100).show();
                }
            });
        }
        public void bindData(Board board){
            // toggle counter visibility
            if (board.getLikeCnt() == 0){
                binding.wrapperHeartCount.setVisibility(View.GONE);
            }else {
                binding.wrapperHeartCount.setVisibility(View.VISIBLE);
            }
            if (board.getChatCnt() == 0){
                binding.wrapperChatCount.setVisibility(View.GONE);
            }else {
                binding.wrapperChatCount.setVisibility(View.VISIBLE);
            }
            // save postId+boardName and send to PostActivity in onClickListener
            this.postId = board.getId();

            // *************  서버에서 정의된 이미지 확인 후 수정 예정 *******************
//            if (data.getImageResourceId() == 0 ){
//                imageView.setVisibility(View.GONE);
//            }else {
//                imageView.setVisibility(View.VISIBLE);
//                imageView.setImageResource(data.getImageResourceId());
//            }
            binding.tvShopTitle.setText(board.getTitle());
            // Market preview는 Content를 미리 보여주지 않음(제목만 보여줌)
            binding.tvHeartCount.setText(Integer.toString(board.getLikeCnt()));
            binding.tvChatCount.setText(Integer.toString(board.getChatCnt()));
            binding.tvShopTime.setText(DateConvertUtils.convertDate(board.getFinalDate(), "date"));
            binding.tvShopAuthor.setText(board.getAuthor());
            // 가격 정보 가져오는 getter가 아직 없음
            //binding.tvShopPrice.setText(board.getPrice().toString() + "원");
        }
    }

    @NonNull
    @Override
    public postPreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemShopPreviewBinding binding = ItemShopPreviewBinding.inflate(inflater, parent, false);
        return new postPreviewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull postPreviewHolder holder, int position) {
        holder.bindData((Board)dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
