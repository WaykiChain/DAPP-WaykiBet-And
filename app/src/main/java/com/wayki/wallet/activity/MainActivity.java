package com.wayki.wallet.activity;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.wayki.wallet.R;
import com.wayki.wallet.bean.entity.BottomBarEntity;
import com.wayki.wallet.dialog.CreateImportDialog;
import com.wayki.wallet.dialog.TipDialog;
import com.wayki.wallet.dialog.WalletDailog;
import com.wayki.wallet.fragment.WalletTabFragment;
import com.wayki.wallet.fragment.WebFragment;
import com.wayki.wallet.mvp.mainpresenter.MainPresenter;
import com.wayki.wallet.mvp.mainpresenter.MainView;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.Stringconstant;
import com.wayki.wallet.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_KEY;
import static com.wayki.wallet.utils.Stringconstant.REGISTE_FRAGMENT_KEY;
import static com.wayki.wallet.utils.Stringconstant.URL_KEY;


public class MainActivity extends BaseMvpActivity<MainView, MainPresenter>
        implements MainView {
    @Bind(R.id.fl_main)
    FrameLayout flMain;
    @Bind(R.id.ll_main_bottom)
    RadioGroup llMainBottom;
    private FragmentManager fragmentManager;
    private List<Fragment> fragments = new ArrayList<>();
    private int currentIndex = 0;
    private Fragment currentFragment = new Fragment();
    private FragmentTransaction ft;
    //当前显示的fragment
    // private NavigationTabBar navigationTabBar;
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    private mainReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI(savedInstanceState);
        receiver = new mainReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Stringconstant.EXCHANGE_PAGE);
        registerReceiver(receiver, filter);
      String jumpFromReg=getIntent().getStringExtra(REGISTE_FRAGMENT_KEY);
      if("1".equals(jumpFromReg)){
          CreateImportDialog dialog_full_qrcode = new CreateImportDialog(this);
          dialog_full_qrcode.setCancelable(false);
          dialog_full_qrcode.show();
      }
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        presenter = new MainPresenter(this, this);
        presenter.bindInfo();//获取绑定信息
        presenter.bottomBar();
        return presenter;
    }

    @Override
    public String getEmptyText() {
        return "";
    }

    /**
     * 加载失败，点击重试
     */
    @Override
    public void onClickLoadFailText() {

    }

    @Override
    public void getDataSuccess(Object o) {

    }

    @Override
    public void getDataFail(String fail) {

    }

    @SuppressLint("ResourceType")
    @Override
    public void genBottomItem(BottomBarEntity infoEntity) {
        String bottomItem = SPUtils.get(this, SPConstants.BOTTOMCONFIG, "").toString();
        if ("".equals(bottomItem)) {
            if (llMainBottom != null) {
                llMainBottom.removeAllViews();
            }
            for (int i = 0; i < infoEntity.getData().size(); i++) {

                if (infoEntity.getData().get(i).getOnShelf() == 1) {

                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                            , ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    RadioButton radioButton = new RadioButton(MainActivity.this);
                    radioButton.setLayoutParams(params);
                    Bitmap a = null;
                    radioButton.setButtonDrawable(new BitmapDrawable(a));
                    radioButton.setGravity(Gravity.CENTER);
                    radioButton.setBackground(getResources().getDrawable(R.drawable.trans));
                    radioButton.setPadding(0, 10, 0, 0);
                    radioButton.setText(infoEntity.getData().get(i).getTitle());
                    radioButton.setTextColor(getResources().getColorStateList(R.drawable.rb_textcolor));
                    llMainBottom.addView(radioButton);

                    String type = infoEntity.getData().get(i).getType();
                    if ("100".equals(type)) {
                        String webUrl = infoEntity.getData().get(i).getRedirectUrl();
                        Bundle bundle = new Bundle();
                        bundle.putString(URL_KEY, webUrl);
                        WebFragment sonicFragment = WebFragment.getInstance(bundle);
                        fragments.add(sonicFragment);
                    } else if ("200".equals(type)) {
                        WalletTabFragment walletFragment = new WalletTabFragment();
                        fragments.add(walletFragment);
                    }

                    UIUtils.getDrawable(this,
                            infoEntity.getData().get(i).getImageUrlSecond(),
                            infoEntity.getData().get(i).getSelectImageUrlSecond(),
                            radioButton);
                    if (i == 0) {
                        radioButton.setChecked(true);
                    }
                }
            }
            showFragment();
            llMainBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int count = group.getChildCount();
                    for (int i = 0; i < count; i++) {
                        RadioButton button = (RadioButton) group.getChildAt(i);
                        if (button.isChecked()) {
                            currentIndex = i;
                            showFragment();
                        }
                    }
                }
            });
        } else {
            if (infoEntity != null && infoEntity.getData() != null) {
                String jsonConfig = new Gson().toJson(infoEntity);
                SPUtils.put(this, SPConstants.BOTTOMCONFIG, jsonConfig);
            }
        }

    }

    private void initUI(Bundle savedInstanceState) {
        ft = getSupportFragmentManager().beginTransaction();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) { // “内存重启”时调用
            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);
            int size = fragments.size();
            //注意，添加顺序要跟下面添加的顺序一样!
            fragments.removeAll(fragments);

            for (int i = 0; i < size; i++) {
                fragments.add(fragmentManager.findFragmentByTag(i + ""));
            }
            //恢复fragment页面
            restoreFragment();
        } else {      //正常启动时调用

            String bottomItem = SPUtils.get(this, SPConstants.BOTTOMCONFIG, "").toString();
            if (!"".equals(bottomItem)) {
                BottomBarEntity infoEntity = new Gson().fromJson(bottomItem, BottomBarEntity.class);
                if (infoEntity != null && infoEntity.getData() != null) {
                    genBottomItem(infoEntity);
                }
            }

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //“内存重启”时保存当前的fragment名字
        outState.putInt(CURRENT_FRAGMENT, currentIndex);
        super.onSaveInstanceState(outState);
    }

    boolean isFirst=false;
    @Override
    protected void onResume() {
        super.onResume();
        String adde=SPUtils.get(this,SPConstants.WALLET_ADDRESS,"").toString();
        if("".equals(adde)){

                if (llMainBottom != null && fragments != null) {
                    for (int i = 0; i < fragments.size(); i++) {
                        if (fragments.get(i) instanceof WebFragment) {
                            RadioButton button = (RadioButton) llMainBottom.getChildAt(i);
                            button.setChecked(true);
                            break;
                        }
                    }
                }
        }

        if(isFirst){
        if (currentFragment!=null&&currentFragment instanceof WebFragment) {
            ((WebFragment) currentFragment).refreshPage();
        }
        }
        isFirst=true;

    }

    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    FragmentTransaction transaction;

    private void showFragment() {
        transaction = fragmentManager.beginTransaction();
        //如果之前没有添加过
        if (!fragments.get(currentIndex).isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.fl_main, fragments.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag
        } else {
            // Logger.e("transaction2");
            transaction
                    .hide(currentFragment)
                    .show(fragments.get(currentIndex));
        }
        currentFragment = fragments.get(currentIndex);
        transaction.commit();

        transaction.hide(currentFragment);
        transaction.show(currentFragment);
        if (currentFragment instanceof WebFragment) {
            ((WebFragment) currentFragment).refreshPage();
        }else if(currentFragment instanceof WalletTabFragment){
            String words=SPUtils.get(this,SPConstants.WALLET_ADDRESS,"").toString();
            if("".equals(words)){
                Intent intent6= new Intent(this, CommonActivity.class);
                intent6.putExtra(FRAGMENT_KEY, 4);
                startActivity(intent6);
            }
        }

    }
    /**
     * 恢复fragment
     */
    private void restoreFragment() {
        FragmentTransaction mBeginTreansaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {

            if (i == currentIndex) {
                mBeginTreansaction.show(fragments.get(i));
            } else {
                mBeginTreansaction.hide(fragments.get(i));
            }
        }
        mBeginTreansaction.commit();
        //把当前显示的fragment记录下来
        currentFragment = fragments.get(currentIndex);
    }


    //重写onKeyDown方法,对按键(不一定是返回按键)监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TipDialog tipDialog = new TipDialog(this, R.style.DialogStyle
                    , R.layout.dialog_double_tip,0.35);
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
        return false;
    }


    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView(true);
        }

        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    class mainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Stringconstant.EXCHANGE_PAGE.equals(action)) {
                if (llMainBottom != null && fragments != null) {
                    for (int i = 0; i < fragments.size(); i++) {
                        if (fragments.get(i) instanceof WalletTabFragment) {
                            RadioButton button = (RadioButton) llMainBottom.getChildAt(i);
                            button.setChecked(true);
                        }
                    }
                }
            }
        }
    }

}
