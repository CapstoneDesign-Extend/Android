package com.example.schoolproject.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.schoolproject.nav.board.FragBoard;
import com.example.schoolproject.R;

public class NotificationActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TextView tv_notification;
    private TextView tv_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        // connect res
        viewPager2 = findViewById(R.id.notification_viewpager);
        tv_notification = findViewById(R.id.tv_notification_notice);
        tv_message = findViewById(R.id.tv_notification_message);

        // setting ViewPager
        NotificationViewPagerAdapter adapter = new NotificationViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new FragNotificationNotice());
        adapter.addFragment(new FragNotificationMessage());

        viewPager2.setSaveEnabled(false);  // ? prevent crashing when reload
        viewPager2.setAdapter(adapter);

        // set onPageChangedListener
        TextView[] textViews = {
                tv_notification,
                tv_message
        };
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                    for (int i = 0; i < textViews.length; i++) {
                        if (i == position){
                            FragBoard.setTextViewStyle(textViews[i]);
                        }else {
                            FragBoard.setTextViewDefaultStyle(textViews[i]);
                        }
                    }
                }
        });
        // setting listeners on textView
        tv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(0);
            }
        });
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(1);
            }
        });
    }  // End of the onCreate()

    // style changing methods
    //private void setTextViewStyle()

}
