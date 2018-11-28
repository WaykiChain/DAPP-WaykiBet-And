package com.wayki.wallet.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.wayki.wallet.R;
import com.wayki.wallet.application.ActivityManager;
import com.wayki.wallet.callback.LaunchCallback;
import com.wayki.wallet.fragment.LaunchFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LaunchActivity extends AppCompatActivity implements LaunchCallback{

    @Bind(R.id.fl_launch_main)
    FrameLayout flLaunchMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isTaskRoot()) {
            /* If this is not the root activity */
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }

        ActivityManager.create ().addActivity (this);
        setContentView(R.layout.activity_common);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LaunchFragment launchFragment=new LaunchFragment();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transition=manager.beginTransaction();
        transition.replace(R.id.fl_launch_main,launchFragment);
        transition.commit();
    }

    @Override
    public void loadWebFragment(String url) {
       
    }
}
