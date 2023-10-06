package com.example.schoolproject.post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ItemImageSliderBinding;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder> {
    private Context context;
//    private List<String> imageURLs;  // 이미지 url 리스트
    List<Integer> imageResources;  // 이미지 리소스 id 리스트

//    public ImageSliderAdapter(Context context, List<String> imageURLs) {
//        this.context = context;
//        this.imageURLs = imageURLs;
//    }
    public ImageSliderAdapter(Context context, List<Integer> imageResources) {
        this.context = context;
        this.imageResources = imageResources;
    }

    @NonNull
    @Override
    public ImageSliderAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderAdapter.ImageViewHolder holder, int position) {
//        String imageURL = imageURLs.get(position);
        int resId = imageResources.get(position);
        Glide.with(context)
                        .load(resId)
                                .into(holder.binding.ivImageSlider);
    }

    @Override
    public int getItemCount() {
        return imageResources.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        private ItemImageSliderBinding binding;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemImageSliderBinding.bind(itemView);
        }
    }
}
