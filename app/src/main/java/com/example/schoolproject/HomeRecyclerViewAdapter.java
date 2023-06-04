package com.example.schoolproject;

import android.content.Context;
import android.content.Intent;
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

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_BOARD = 0;
    private static final int VIEW_TYPE_DYNAMIC_MORNING = 1;
    private Context context;
    private List<Object> dataList;

    public HomeRecyclerViewAdapter(Context context, List<Object> dataList){
        this.context = context;
        this.dataList = dataList;
    }
    public int getItemViewType(int position){
        if(dataList.get(position) instanceof DataHomeBoard){
            return VIEW_TYPE_BOARD;
        } else if (dataList.get(position) instanceof DataHomeDynamicMorning) {
            return VIEW_TYPE_DYNAMIC_MORNING;
        }
        return -1;
    }

    public class homeDynamicMorningViewHolder extends RecyclerView.ViewHolder{
        protected TextView tv_title;
        protected TextView tv_lecture1;
        protected TextView tv_lecture2;

        public homeDynamicMorningViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.tv_home_dynamic_morning_title);
            this.tv_lecture1 = itemView.findViewById(R.id.tv_home_dynamic_morning_lecture1);
            this.tv_lecture2 = itemView.findViewById(R.id.tv_home_dynamic_morning_lecture2);
        }
        public void bindData(DataHomeDynamicMorning data){
            tv_title.setText(data.getTitle());
            tv_lecture1.setText(data.getLecture1());
            tv_lecture2.setText(data.getLecture2());
        }
    }

    public class homeBoardViewHolder extends RecyclerView.ViewHolder{
        protected List<LinearLayout> rowWrappers;
        protected LinearLayout boardMore;
        protected TextView tv_title;
        private List<TextView> nameViews;
        private List<TextView> dataViews;

        public homeBoardViewHolder(View itemView){
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
                String boardName = "";
                @Override
                public void onClick(View v) {
                    boardName = ((TextView)v.findViewById(R.id.tv_home_board_title)).getText().toString();
                    Snackbar.make(v, boardName, 100).show();
                    Intent intent = new Intent(context, BoardActivity.class);
                    intent.putExtra("boardName", boardName);
                    context.startActivity(intent);
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
                                //Intent intent = new Intent(context, )
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
            tv_title.setText(data.getBoard_name());
            List<String> names = data.getPost_titles();
            List<String> datas = data.getPost_contents();
            int size = names.size();
            if (size==0){  // show "작성된 게시글이 없습니다"
                rowWrappers.get(0).setVisibility(View.GONE);
                rowWrappers.get(1).setVisibility(View.GONE);
                rowWrappers.get(2).setVisibility(View.VISIBLE);
            }
            if (size==1){
                rowWrappers.get(0).setVisibility(View.VISIBLE);
                rowWrappers.get(1).setVisibility(View.GONE);
                rowWrappers.get(2).setVisibility(View.GONE);

            }
            if (size==2){
                rowWrappers.get(0).setVisibility(View.VISIBLE);
                rowWrappers.get(1).setVisibility(View.VISIBLE);
                rowWrappers.get(2).setVisibility(View.GONE);

            }
            for (int i=0; i<size; i++){
                nameViews.get(i).setText(names.get(i));
                dataViews.get(i).setText(datas.get(i));
                rowWrappers.get(2).setVisibility(View.GONE);

            }
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create view and ViewHolder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case VIEW_TYPE_BOARD:
                View view1 = inflater.inflate(R.layout.item_home,parent,false);
                return new homeBoardViewHolder(view1);
            case VIEW_TYPE_DYNAMIC_MORNING:
                View view2 = inflater.inflate(R.layout.item_home_dynamic_morning,parent,false);
                return new homeDynamicMorningViewHolder(view2);
            default:
                throw new IllegalArgumentException("Invalid view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = dataList.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_BOARD:
                homeBoardViewHolder homeBoardViewHolder = (HomeRecyclerViewAdapter.homeBoardViewHolder) holder;  // casting ViewHolder
                DataHomeBoard dataHomeBoard = (DataHomeBoard) item;
                homeBoardViewHolder.bindData(dataHomeBoard);
                break;
            case VIEW_TYPE_DYNAMIC_MORNING:
                homeDynamicMorningViewHolder homeDynamicMorningViewHolder = (HomeRecyclerViewAdapter.homeDynamicMorningViewHolder) holder;
                DataHomeDynamicMorning dataHomeDynamicMorning = (DataHomeDynamicMorning) item;
                homeDynamicMorningViewHolder.bindData(dataHomeDynamicMorning);
                break;
        }
    }


    @Override
    public int getItemCount() {
        // return total items to display
        return dataList.size();
    }

}
