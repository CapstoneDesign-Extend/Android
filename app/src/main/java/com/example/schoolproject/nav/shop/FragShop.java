package com.example.schoolproject.nav.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproject.model.Board;
import com.example.schoolproject.model.BoardKind;
import com.example.schoolproject.model.BoardKindUtils;
import com.example.schoolproject.model.retrofit.BoardApiService;
import com.example.schoolproject.model.retrofit.BoardCallback;
import com.example.schoolproject.post.PostWriteActivity;
import com.example.schoolproject.R;
import com.example.schoolproject.notification.NotificationActivity;
import com.example.schoolproject.search.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class FragShop extends Fragment {
    private View view;
    private FloatingActionButton fab;
    private List<Object> dataList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        // Load Data
//        BoardApiService apiService = new BoardApiService();
//        Call<List<Board>> call = apiService.getBoardsByBoardKind(BoardKind.MARKET);
//        call.enqueue(new BoardCallback.BoardListCallBack(getContext(), adapter));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_shop, container,false);

        setHasOptionsMenu(true);

        // setting ToolBar
        Toolbar toolbar = view.findViewById(R.id.toolbar_shop);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        // setting RecyclerView
//        recyclerView = view.findViewById(R.id.recycler_view_shop);
//        layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//
//        dataList = new ArrayList<>();  // init with empty data
//        adapter = new ShopRecyclerViewAdapter(getContext(), dataList);
//        recyclerView.setAdapter(adapter);

        // setting fab
        fab = view.findViewById(R.id.fab_write);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(view.getContext(), PostWriteActivity.class);
//                intent.putExtra("boardKind","MARKET");
//                startActivity(intent);




                // 테스트 게시글 작성:


            }
        });



        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_home,menu);
        MenuItem notificationItem = menu.findItem(R.id.notification);
        MenuItem searchItem = menu.findItem(R.id.search);

        // position change : (notification,search) -> (search, notification)
        menu.removeItem(R.id.notification);
        menu.removeItem(R.id.search);
        menu.removeItem(R.id.myPage);
        menu.add(Menu.NONE, R.id.search, Menu.NONE, "search")
                        .setIcon(searchItem.getIcon())
                                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(Menu.NONE, R.id.notification, Menu.NONE, "notification")
                        .setIcon(notificationItem.getIcon())
                                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);



        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                // "notification" 아이템이 클릭되었을 때 수행할 코드 작성
                Snackbar.make(view, "notification", 100).show();
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
                return true;
            case R.id.search:
                // "search" 아이템이 클릭되었을 때 수행할 코드 작성
                Snackbar.make(view, "search", 100).show();
                Intent intent1 = new Intent(getContext(), SearchActivity.class);
                intent1.putExtra("searchType", "market");
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
