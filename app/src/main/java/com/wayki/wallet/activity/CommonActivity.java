package com.wayki.wallet.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.wayki.wallet.R;
import com.wayki.wallet.application.ActivityManager;
import com.wayki.wallet.fragment.ChangePasswordFragment;
import com.wayki.wallet.fragment.CreateWalletFragment;
import com.wayki.wallet.fragment.ExchangeHistoryFragment;
import com.wayki.wallet.fragment.TransferFragment;
import com.wayki.wallet.fragment.WalletManagerFragment;
import com.wayki.wallet.fragment.WiccHistoryFragment;

import butterknife.ButterKnife;

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_HISTORY_KEY;
import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_KEY;
import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_VALUE;
import static com.wayki.wallet.utils.Stringconstant.REGISTE_FRAGMENT_KEY;
import static com.wayki.wallet.utils.Stringconstant.TRANSFER_ADDRESS;

public class CommonActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;
    private String registeKey="";
    private String transfer_address="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.create ().addActivity (this);
        setContentView(R.layout.activity_common);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int key = getIntent().getIntExtra(FRAGMENT_KEY, -1);
        int fragment_value=getIntent().getIntExtra(FRAGMENT_HISTORY_KEY,0);
        transfer_address=getIntent().getStringExtra(TRANSFER_ADDRESS);
        registeKey=getIntent().getStringExtra(REGISTE_FRAGMENT_KEY);
        mFragmentManager = this.getSupportFragmentManager();
        openFragment(key, fragment_value);
    }

    private void openFragment(int key,int fragment_value) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;

        switch (key){
            case 0:
                ExchangeHistoryFragment recordFragment=new ExchangeHistoryFragment();
                 mBundle = new Bundle();
                mBundle.putString(FRAGMENT_VALUE,"");
                recordFragment.setArguments(mBundle);
                ft.replace(R.id.fl_launch_main, recordFragment);
                break;
            case 1:
                WiccHistoryFragment wiccRecordFragment=new WiccHistoryFragment();
                mBundle = new Bundle();
                mBundle.putString(FRAGMENT_VALUE,fragment_value+"");
                wiccRecordFragment.setArguments(mBundle);
                ft.replace(R.id.fl_launch_main, wiccRecordFragment);
                break;

            case 2:
                TransferFragment transferFragment=new TransferFragment();
                mBundle = new Bundle();
                mBundle.putString(FRAGMENT_VALUE,transfer_address);
                transferFragment.setArguments(mBundle);
                ft.replace(R.id.fl_launch_main, transferFragment);
                break;
            case 3:
                WalletManagerFragment managerFragment=new WalletManagerFragment();
                mBundle = new Bundle();
                mBundle.putString(FRAGMENT_VALUE,"");
                managerFragment.setArguments(mBundle);
                ft.replace(R.id.fl_launch_main, managerFragment);
                break;
            case 4:
                CreateWalletFragment createWalletFragment=new CreateWalletFragment();
                mBundle = new Bundle();
                mBundle.putString(FRAGMENT_VALUE,"1");
                mBundle.putString(REGISTE_FRAGMENT_KEY,registeKey);
                createWalletFragment.setArguments(mBundle);
                ft.replace(R.id.fl_launch_main, createWalletFragment);
                break;
            case 5:
                CreateWalletFragment importWallet=new CreateWalletFragment();
                mBundle = new Bundle();
                mBundle.putString(FRAGMENT_VALUE,"2");
                mBundle.putString(REGISTE_FRAGMENT_KEY,registeKey);
                importWallet.setArguments(mBundle);
                ft.replace(R.id.fl_launch_main, importWallet);
                break;

            case 6:
                ChangePasswordFragment passwordFragment=new ChangePasswordFragment();
                ft.replace(R.id.fl_launch_main, passwordFragment);
                break;
        }

        ft.commit();
    }
}
