package com.example.schoolproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GPRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_TABLE = 0;
    private static final int VIEW_TYPE_GP = 1;
    private Context context;
    private List<Object> dataList;

    public GPRecyclerViewAdapter(Context context, List<Object> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.get(position) instanceof DataTimeTable)
            return VIEW_TYPE_TABLE;
        else if (dataList.get(position) instanceof DataGP)
            return VIEW_TYPE_GP;
        return -1;
    }
    // ViewHolders
    public class TableViewHolder extends RecyclerView.ViewHolder{
        // declare views
        ImageButton btn_editTable;
        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            // connecting resources
            btn_editTable = itemView.findViewById(R.id.btn_edit_table);
            btn_editTable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(itemView.getContext(), PostWriteActivity.class);
                    //itemView.getContext().startActivity(intent);
                }
            });
        }
        public void bindData(DataTimeTable data){
            // binding data
        }

    }
    public class GPViewHolder extends RecyclerView.ViewHolder{
        ImageButton btn_editGP;
        TextView tv_currentGPA, tv_targetGPA;

        public GPViewHolder(@NonNull View itemView) {
            super(itemView);
            // connect resources
            tv_currentGPA = itemView.findViewById(R.id.tv_current_gpa);
            tv_targetGPA = itemView.findViewById(R.id.tv_target_gpa);
            btn_editGP = itemView.findViewById(R.id.btn_edit_gp);
            btn_editGP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // inflate dialog layout
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.dialog_edit_gp, null);
                    // generate custom dialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext(), R.style.RoundedDialog);
                    builder.setView(dialogView);
                    builder.setTitle("학점 입력");
                    // get dialog's view
                    EditText et_currentGPA = dialogView.findViewById(R.id.et_current_gpa);
                    EditText et_targetGPA = dialogView.findViewById(R.id.et_target_gpa);
                    // set Confirm button
                    builder.setPositiveButton("저장", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // save user's input data
                            tv_currentGPA.setText(et_currentGPA.getText());
                            tv_targetGPA.setText(et_targetGPA.getText());
                        }
                    });
                    // set Cancel button
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    // show dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
        public void bindData(DataGP data){
            // bind data
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case VIEW_TYPE_TABLE:
                View v1 = inflater.inflate(R.layout.item_table, parent, false);
                return new TableViewHolder(v1);
            case VIEW_TYPE_GP:
                View v2 = inflater.inflate(R.layout.item_gp, parent, false);
                return new GPViewHolder(v2);
            default:
                throw new IllegalArgumentException("Invalid view type: "+viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
