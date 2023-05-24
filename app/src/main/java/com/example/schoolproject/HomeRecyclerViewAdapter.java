package com.example.schoolproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.myViewHolder> {
    private List<DataHomeBoard> dataHomeBoards;

    public HomeRecyclerViewAdapter(List<DataHomeBoard> dataHomeBoards){
        this.dataHomeBoards = dataHomeBoards;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        protected List<LinearLayout> rowWrappers;
        protected LinearLayout boardMore;
        protected TextView tv_title;
        private List<TextView> nameViews;
        private List<TextView> dataViews;

        public myViewHolder(View itemView){
            super(itemView);
            this.boardMore = itemView.findViewById(R.id.home_board_more);
            this.tv_title = itemView.findViewById(R.id.tv_home_board_title);
            this.nameViews = new ArrayList<>();
            this.dataViews = new ArrayList<>();
            this.rowWrappers = new ArrayList<>();
            // connect multiple view res with getResources()
            for(int i=1;i<=5;i++){
                TextView nameView = itemView.findViewById(itemView.getContext().getResources()
                        .getIdentifier("tv_home_board_name" + i, "id", itemView.getContext().getPackageName()));
                TextView dataView = itemView.findViewById(itemView.getContext().getResources()
                        .getIdentifier("tv_home_board_data" + i, "id", itemView.getContext().getPackageName()));
                LinearLayout rowWrapper = itemView.findViewById(itemView.getContext().getResources()
                        .getIdentifier("home_board_wrapper" + i, "id", itemView.getContext().getPackageName()));
                nameViews.add(nameView);
                dataViews.add(dataView);
                rowWrappers.add(rowWrapper);
            }
            // set OnClickListener for "more" wrapper
            this.boardMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "more", 100).show();
                }
            });
            // set OnClickListener for rowWrappers
            for(LinearLayout rowWrapper: rowWrappers){
                rowWrapper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = v.getId();
                        String text = "";

                        switch (id){
                            case R.id.home_board_wrapper1:
                                text = ((TextView)v.findViewById(R.id.tv_home_board_name1)).getText().toString();
                                break;
                            case R.id.home_board_wrapper2:
                                text = ((TextView)v.findViewById(R.id.tv_home_board_name2)).getText().toString();
                                break;
                            case R.id.home_board_wrapper3:
                                text = ((TextView)v.findViewById(R.id.tv_home_board_name3)).getText().toString();
                                break;
                            case R.id.home_board_wrapper4:
                                text = ((TextView)v.findViewById(R.id.tv_home_board_name4)).getText().toString();
                                break;
                            case R.id.home_board_wrapper5:
                                text = ((TextView)v.findViewById(R.id.tv_home_board_name5)).getText().toString();
                                break;
                        }
                        Snackbar.make(v,text,100).show();
                    }
                });
            }


        }
        public void bindData(DataHomeBoard data){
            tv_title.setText(data.getTitle());
            List<String> names = data.getNames();
            List<String> datas = data.getDatas();
            int size = names.size();
            for (int i=0; i<size; i++){
                nameViews.get(i).setText(names.get(i));
                dataViews.get(i).setText(datas.get(i));
            }
        }
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view and ViewHoldeer
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);
        myViewHolder viewHolder = new myViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        // when list value added
        DataHomeBoard data = dataHomeBoards.get(position);
        holder.bindData(data);
    }

    @Override
    public int getItemCount() {
        // return total items to display
        return dataHomeBoards.size();
    }

}
