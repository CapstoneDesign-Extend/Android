package com.example.schoolproject.nav.gp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ItemGpBinding;
import com.example.schoolproject.databinding.ItemTableBinding;
import com.example.schoolproject.databinding.ItemTlaabsTableBinding;
import com.example.schoolproject.model.ui.DataGP;
import com.example.schoolproject.model.ui.DataHomeBoard;
import com.example.schoolproject.model.ui.DataHomeDynamicMorning;
import com.example.schoolproject.model.ui.DataTimeTable;
import com.example.schoolproject.nav.home.HomeRecyclerViewAdapter;
import com.example.schoolproject.room.AppDatabase;
import com.example.schoolproject.room.entity.TimetableEntity;
import com.example.schoolproject.timetableview.TimetableView;
import com.github.tlaabs.timetableview.Schedule;

import java.util.ArrayList;
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
    // ============================================= 뷰홀더 클래스 START =======================================================
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
    // ********    시간표 뷰홀더 클래스     *********
    public class TimetableViewHolder extends RecyclerView.ViewHolder implements ScheduleCallback{
        private final ItemTlaabsTableBinding binding;


        public TimetableViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTlaabsTableBinding.bind(itemView);

            binding.timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
                @Override
                public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                    Toast.makeText(context, idx+"클릭되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            // 시간표 추가 버튼 클릭 시 동작
            binding.btnEditTable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 강의를 추가하는 대화상자(프래그먼트) 호출
                    FragBottomSheetDialog bottomSheet = new FragBottomSheetDialog();
                    bottomSheet.setCallback(TimetableViewHolder.this);
                    bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
                }
            });
        }
        public void bindData(DataTimeTable data){
            // sharedPrefs에서 시간표 json 가져와서 시간표 데이터 로드
            SharedPreferences sPrefs = context.getSharedPreferences("timetable_prefs", Context.MODE_PRIVATE);
            String json = sPrefs.getString("json", null);

            if (json != null) {
                binding.timetable.load(json);
            }
        }

        @Override
        public void onScheduleAdded(ArrayList<Schedule> schedules) {
            // 강의추가 화면에서 저장을 누르면 이게 호출됨. (인터페이스를 참조하는 방식)
            binding.timetable.add(schedules);
            String json = binding.timetable.createSaveData();

            // SharedPrefs 사용
            SharedPreferences sPrefs = context.getSharedPreferences("timetable_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sPrefs.edit();
            editor.putString("json", json);
            editor.apply();


            // ==========================    로컬 DB 로직 시작  =============================
//            // 데이터베이스 초기화
//            AppDatabase db = Room.databaseBuilder(
//                    context,
//                    AppDatabase.class, "database-name"
//            ).build();
//
//            // 데이터 저장
//            final TimetableEntity timetableEntity = new TimetableEntity();
//            timetableEntity.jsonData = json;
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    db.timetableDao().insertTimetable(timetableEntity);
//                }
//            }).start();
//
//            // 데이터 검색
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    TimetableEntity retrievedData = db.timetableDao().getTimetableById(1);
//                }
//            }).start();
            // ==========================    로컬 DB 로직 끝  =============================
        }
    }

    // ============================================= 뷰홀더 클래스 END =======================================================


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_TABLE:
                ItemTlaabsTableBinding itemTableBinding = ItemTlaabsTableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new TimetableViewHolder(itemTableBinding.getRoot());
            case VIEW_TYPE_GP:
                ItemGpBinding itemGpBinding = ItemGpBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new GPViewHolder(itemGpBinding.getRoot());
            default:
                throw new IllegalArgumentException("Invalid view type: "+viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = dataList.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_GP:
                GPViewHolder gpViewHolder = (GPViewHolder) holder;
                DataGP dataGP = (DataGP) item;
                gpViewHolder.bindData(dataGP);
                break;
            case VIEW_TYPE_TABLE:
                TimetableViewHolder timetableViewHolder = (TimetableViewHolder) holder;
                DataTimeTable dataTimeTable = (DataTimeTable) item;
                timetableViewHolder.bindData(dataTimeTable);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
