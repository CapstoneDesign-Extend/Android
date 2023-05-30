package com.example.schoolproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class BoardReportRecyclerViewAdapter extends RecyclerView.Adapter<BoardReportRecyclerViewAdapter.myViewHolder>{

    public class myViewHolder extends RecyclerView.ViewHolder{
        protected LinearLayout boardWrapper;
        protected TextView tv_title;
        protected TextView tv_data;
        protected TextView tv_time;
        protected TextView tv_isANON;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            this.boardWrapper = itemView.findViewById(R.id.board_notice_wrapper);
            this.tv_title = itemView.findViewById(R.id.tv_board_notice_title);
            this.tv_data = itemView.findViewById(R.id.tv_board_notice_data);
            this.tv_time = itemView.findViewById(R.id.tv_board_notice_time);
            this.tv_isANON = itemView.findViewById(R.id.tv_board_notice_auth);

            // set OnClickListener for boardWrapper
            this.boardWrapper.setOnClickListener(new View.OnClickListener() {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_notice,parent,false);
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
