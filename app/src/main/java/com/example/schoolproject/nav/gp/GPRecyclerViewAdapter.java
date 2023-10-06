package com.example.schoolproject.nav.gp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ItemGpBinding;
import com.example.schoolproject.databinding.ItemTableBinding;
import com.example.schoolproject.model.ui.DataGP;
import com.example.schoolproject.model.ui.DataTimeTable;

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
    // 학점입력 뷰홀더
    public class GPViewHolder extends RecyclerView.ViewHolder{
        private final ItemGpBinding binding;

        public GPViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemGpBinding.bind(itemView);
            // connect resources

            binding.btnEditGp.setOnClickListener(new View.OnClickListener() {
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
                            binding.tvCurrentGpa.setText(et_currentGPA.getText());
                            binding.tvTargetGpa.setText(et_targetGPA.getText());
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
                    // 긍정적인 버튼 (저장)의 텍스트 색상 변경
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setTextColor(context.getResources().getColor(R.color.colorAccent));

                    // 부정적인 버튼 (예를 들면 "취소")의 텍스트 색상도 동일한 방식으로 변경 가능합니다.
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negativeButton.setTextColor(context.getResources().getColor(R.color.colorAccent));                }
            });
        }
        public void bindData(DataGP data){
            // bind data
        }
    }
    // 시간표 뷰홀더 클래스
    public class TimetableViewHolder extends RecyclerView.ViewHolder{
        private final ItemTableBinding binding;
        public TimetableViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTableBinding.bind(itemView);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case VIEW_TYPE_TABLE:
                ItemTableBinding itemTableBinding = ItemTableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new TableViewHolder(itemTableBinding.getRoot());
            case VIEW_TYPE_GP:
                ItemGpBinding itemGpBinding = ItemGpBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new TableViewHolder(itemGpBinding.getRoot());
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
