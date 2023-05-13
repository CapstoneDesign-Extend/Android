package com.example.schoolproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView){
            super(itemView);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view and ViewHoldeer
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        // 이 리사이클러뷰를 이용해 표시되어야 하는 데이터의 전체 개수 리턴
        return 5;
    }

}
