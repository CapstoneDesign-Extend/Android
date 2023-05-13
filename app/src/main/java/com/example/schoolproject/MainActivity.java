package com.example.schoolproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FragHome fragHome;
    private FragBoard fragBoard;
    private FragShop fragShop;
    private FragTable fragTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home: setFrag(fragHome); return true;
                    case R.id.menu_board: setFrag(fragBoard); return true;
                    case R.id.menu_shop: setFrag(fragShop); return true;
                    case R.id.menu_table: setFrag(fragTable); return true;
                    default: return false;
                }
            }
        });

        fragHome = new FragHome();
        fragBoard = new FragBoard();
        fragShop = new FragShop();
        fragTable = new FragTable();

        setFrag(fragHome);
    }

    private void setFrag(Fragment frag){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameWrapper, frag);
        fragmentTransaction.commit();
    }

}