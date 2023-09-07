package com.example.schoolproject.search;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Switch;
import android.widget.TextView;

import com.example.schoolproject.R;
import com.example.schoolproject.databinding.ActivitySearchBinding;
import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.BoardCallback;
import com.example.schoolproject.post.PostPreviewRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class SearchActivity extends AppCompatActivity {
    // **게시판에서 왔는지 or 장터에서 왔는지 구별 필요: 인텐트로 받는 게 최선인지?
    private ActivitySearchBinding binding;
    private String searchType;
    private List<Object> dataList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        searchType = getIntent().getStringExtra("searchType");

        // setting recyclerView
        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new PostPreviewRecyclerViewAdapter(this, dataList);
        binding.recyclerViewSearch.setAdapter(adapter);

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (searchType){
                    case "board": searchBoard(); break;
                    case "market" : searchMarket(); break;
                }

            }
        });
        binding.etSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                    switch (searchType){
                        case "board": searchBoard(); break;
                        case "shop": searchMarket(); break;
                    }
                    return true;
                }
                return false;
            }
        });

    }
    private void searchBoard(){
        BoardApiService apiService = new BoardApiService();
        Call<List<Board>> call = apiService.getBoardsByKeyword(binding.etSearchInput.getText().toString());
        call.enqueue(new BoardCallback.BoardListCallBack(getApplicationContext(), adapter));
    }
    private void searchMarket(){
        BoardApiService apiService = new BoardApiService();
        Call<List<Board>> call = apiService.getBoardsByKeywordKind(binding.etSearchInput.getText().toString(), BoardKind.MARKET);
        call.enqueue(new BoardCallback.BoardListCallBack(getApplicationContext(), adapter));
    }


}