package com.wayki.wallet.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.wayki.wallet.R;
import com.wayki.wallet.application.ActivityManager;
import com.wayki.wallet.callback.FragmentKeyDown;
import com.wayki.wallet.dialog.TipDialog;
import com.wayki.wallet.dialog.WalletDailog;
import com.wayki.wallet.fragment.WebFragment;
import com.wayki.wallet.utils.NetworkUtil;
import com.wayki.wallet.utils.Stringconstant;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static com.wayki.wallet.utils.Stringconstant.URL_KEY;

public class WebComonActivity extends AppCompatActivity {

    @Bind(R.id.fl_launch_main)
    FrameLayout flLaunchMain;
    private FragmentManager mFragmentManager;
    private String hadlogin;
    private NetworkConnectChangedReceiver receiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.create().addActivity(this);
        setContentView(R.layout.activity_common);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFragmentManager = this.getSupportFragmentManager();

        String URL = getIntent().getStringExtra(Stringconstant.WEB_URL_VALUE);
        hadlogin=getIntent().getStringExtra(Stringconstant.WEB_HAD_LOGIN);
        if (URL != null) {
            openFragment(1, URL);
        }

        receiver=new NetworkConnectChangedReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        //filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(receiver,filter);

    }


    private WebFragment webFragment;

    private void openFragment(int key, String value) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;

        switch (key) {
            case 1:
                ft.add(R.id.fl_launch_main, webFragment = WebFragment.getInstance(mBundle = new Bundle()), WebFragment.class.getName());
                mBundle.putString(URL_KEY, value);
                break;
            default:
                break;

        }
        ft.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
//		mAgentWebFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        WebFragment mAgentWebFragment = this.webFragment;
        if (mAgentWebFragment != null) {
            FragmentKeyDown mFragmentKeyDown = mAgentWebFragment;
            if (mFragmentKeyDown.onFragmentKeyDown(keyCode, event)) {
                return true;
            } else {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(hadlogin!=null){
                       finish();
                    }else{
                        TipDialog tipDialog = new TipDialog(this, R.style.DialogStyle
                                , R.layout.dialog_double_tip);
                        tipDialog.show();
                        tipDialog.addOnCancelListener(getString(R.string.cancel), new WalletDailog.onCancelListener() {
                            @Override
                            public void onCancel(WalletDailog dialog) {
                                dialog.dismiss();
                            }
                        });
                        tipDialog.addOnConfirmListener(getString(R.string.confirm), new WalletDailog.onComfirmListener() {
                            @Override
                            public void onConfirm(WalletDailog dialog) {
                                finish();
                            }
                        });
                        tipDialog.setMessage(getString(R.string.exit_tip));
                    }
                }
                return super.onKeyDown(keyCode, event);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    class NetworkConnectChangedReceiver  extends BroadcastReceiver {

        public int getConnectivityStatus(Context context) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = manager.getActiveNetworkInfo();
            if (null != info) {
                if (info.getType() == TYPE_WIFI)
                    return TYPE_WIFI;

                if (info.getType() == TYPE_MOBILE)
                    return TYPE_MOBILE;
            }
            return -1;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = getConnectivityStatus(context);

            String text = "No Network Connection ..";
            if (status == TYPE_WIFI) {
                text = "Wifi Connected ..";
            } else if (status == TYPE_MOBILE) {
                text = "Mobile Data Connected ..";
            }
            if(NetworkUtil.isConnected(context)){
                 //Logger.e("网络连接");
               webFragment.refresh();
            }else{
                //Logger.e("网络断开");
                //networkStatus(false);
            }
        }
    }




    @Override
    protected void onDestroy() {
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}
