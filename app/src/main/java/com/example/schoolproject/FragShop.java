package com.example.schoolproject;

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

import com.google.android.material.snackbar.Snackbar;

public class FragShop extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

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
        recyclerView = view.findViewById(R.id.recycler_view_shop);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ShopRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);


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


        //notificationItem.setVisible(true);
        //searchItem.setVisible(true);
        //myPageItem.setVisible(false);
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
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
