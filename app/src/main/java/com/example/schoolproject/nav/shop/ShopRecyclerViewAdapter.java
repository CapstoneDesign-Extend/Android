package com.example.schoolproject.nav.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ItemShopPreviewBinding;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;
import com.example.schoolproject.model.BoardKindUtils;
import com.example.schoolproject.model.DateConvertUtils;
import com.example.schoolproject.post.PostActivity;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        private BoardKind boardKind;
        private List<String> imageURLs;
        private Integer price;


        public postPreviewHolder(@NonNull ItemShopPreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


            // ================  게시글 클릭시 해당 게시글로 이동  ===================
            binding.shopWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("postId", postId);
                    intent.putExtra("imageURLs", (ArrayList<String>)imageURLs);
                    intent.putExtra("boardKind", "MARKET");
                    intent.putExtra("price", price);
                    context.startActivity(intent);
                }
            });
            //  ====================================================
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
            // *******************  여기서 값을 저장 후 리스너에서 인텐트로 전달  ***********************
            this.postId = board.getId();
            this.boardKind = board.getBoardKind();
            this.imageURLs = (ArrayList<String>) board.getImageURLs();
            this.price = board.getPrice();

            // *************  서버에서 정의된 이미지 확인 후 수정 예정 *******************
            // 이미지 url 리스트가 비어있는지 확인 후 이미지 노출 결정
            if (board.getImageURLs() == null || board.getImageURLs().isEmpty()){
                binding.wrapperShopImg.setVisibility(View.GONE);
            }else {
                binding.wrapperShopImg.setVisibility(View.VISIBLE);
                String imageUrl = board.getImageURLs().get(0);

                Glide.with(binding.ivShopImg.getContext())
                        .load(imageUrl)
                        .centerCrop()
                        .into(binding.ivShopImg);
            }

            binding.tvShopTitle.setText(board.getTitle());
            // Market preview는 Content를 미리 보여주지 않음(제목만 보여줌)
            binding.tvHeartCount.setText(Integer.toString(board.getLikeCnt()));
            binding.tvChatCount.setText(Integer.toString(board.getChatCnt()));
            binding.tvShopTime.setText(DateConvertUtils.convertDate(board.getFinalDate(), "date"));
            binding.tvShopAuthor.setText(board.getAuthor());
            // 가격 설정
            if (board.getPrice() != null){
                int price = board.getPrice();
                NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
                String formattedPrice = numberFormat.format(price) + "원";
                binding.tvShopPrice.setText(formattedPrice);
            } else {
                binding.tvShopPrice.setText("가격 정보가 없습니다.");
            }
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
