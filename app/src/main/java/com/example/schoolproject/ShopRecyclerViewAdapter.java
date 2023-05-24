package com.example.schoolproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class ShopRecyclerViewAdapter extends RecyclerView.Adapter<ShopRecyclerViewAdapter.myViewHolder>{

    public class myViewHolder extends RecyclerView.ViewHolder{
        protected LinearLayout shopWrapper;
        protected TextView tv_title;
        protected TextView tv_price;
        protected TextView tv_time;
        protected TextView tv_isANON;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            this.shopWrapper = itemView.findViewById(R.id.shop_wrapper);
            this.tv_title = itemView.findViewById(R.id.tv_shop_title);
            this.tv_price = itemView.findViewById(R.id.tv_shop_price);
            this.tv_time = itemView.findViewById(R.id.tv_shop_time);
            this.tv_isANON = itemView.findViewById(R.id.tv_shop_isANON);

            // set OnClickListener for boardWrapper
            this.shopWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "move to post", 100).show();
                }
            });
        }
        public void bindData(){

        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop,parent,false);
        myViewHolder viewHolder = new myViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

}
