package com.wayki.wallet.fragment;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wayki.wallet.BuildConfig;
import com.wayki.wallet.R;
import com.wayki.wallet.activity.WebComonActivity;
import com.wayki.wallet.bean.entity.UpGradeEntity;
import com.wayki.wallet.dialog.TipDialog;
import com.wayki.wallet.dialog.WalletDailog;
import com.wayki.wallet.mvp.launchpresenter.LaunchPresenter;
import com.wayki.wallet.mvp.launchpresenter.LaunchView;
import com.wayki.wallet.utils.ApiConstants;
import com.wayki.wallet.utils.PermissionUtils;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.Stringconstant;
import com.wayki.wallet.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LaunchFragment extends BaseFragment<LaunchView, LaunchPresenter> implements LaunchView {
    @Bind(R.id.tv_isdebug)
    TextView tvIsdebug;
    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int REQUEST_CODE_STORAGE = 1;
    @Bind(R.id.vp_launch)
    ViewPager vpLaunch;
    @Bind(R.id.tv_guide)
    TextView tvGuide;
    @Bind(R.id.btn_enter)
    Button btnEnter;
    private UpGradeEntity entity;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_launch;
    }

    @Override
    public String getEmptyText() {
        return null;
    }

    @Override
    public void initUI() {
        if (BuildConfig.DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("版本：" + BuildConfig.VERSION_NAME + "\n");
            sb.append("测试版");
            tvIsdebug.setText(sb.toString());
        }

    }

    @Override
    public LaunchPresenter createPresenter() {
        presenter = new LaunchPresenter(LaunchFragment.this, getActivity());

        PermissionUtils.checkAndRequestPermission(getContext(), LaunchFragment.this, WRITE_EXTERNAL_STORAGE,
                REQUEST_CODE_STORAGE,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        // 权限已被授予
                        presenter.checkUpdate();
                    }
                });
        return presenter;
    }

    @Override
    public void onClickLoadFailText() {

    }

    @Override
    public void showNoNetError() {

    }

    @Override
    public void loadingDialog(String tip) {
        super.loadingDialog(tip);
    }

    @Override
    public void dismissDialog() {
        super.dismissDialog();
    }

    @Override
    public void showTip(String str) {
        //super.showTip(str);
        TipDialog tipDialog = new TipDialog(getActivity(), R.style.DialogStyle
                , R.layout.dialog_signal_tip);
        tipDialog.show();
        tipDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                if (presenter != null) {
                    presenter.checkUpdate();
                }
                dialog.dismiss();
            }
        });
        tipDialog.setMessage(str);
    }

    @Override
    public void getDataSuccess(final Object o) {
        entity = (UpGradeEntity) o;
        Boolean showGuide = (Boolean) SPUtils.get(getContext(), SPConstants.GUIDEPAGE, true);

        if (showGuide) {
            tvGuide.setVisibility(View.VISIBLE);
            final MyViewPager myViewPager = new MyViewPager();//相当于适配器
            vpLaunch.setAdapter(myViewPager);//这个容器与适配器绑定
            vpLaunch.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    tvGuide.setText(getString(text[position]));
                    if (position == 2) {
                        btnEnter.setVisibility(View.VISIBLE);
                        btnEnter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                 SPUtils.put(getContext(), SPConstants.GUIDEPAGE, false);
                                showUpdate();
                            }
                        });
                    } else {
                        btnEnter.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            showUpdate();
        }

    }

    private void showUpdate() {
        if (entity.getCode() == 0 && entity.getData() != null) {
            UpGradeEntity.DataBean dataBean = entity.getData();
            if (dataBean.getForceUpgrade() == -1) {//不升级
                Intent intent = new Intent(getActivity(), WebComonActivity.class);
                intent.putExtra(Stringconstant.WEB_URL_VALUE, ApiConstants.web_login);
                getActivity().startActivity(intent);
                getActivity().finish();
            } else if (dataBean.getForceUpgrade() == 1) {//强制升级
                TipDialog tipDialog = new TipDialog(getActivity(), R.style.DialogStyle
                        , R.layout.dialog_signal_tip, 0.35);
                tipDialog.setCancelable(false);
                tipDialog.show();
                tipDialog.addOnConfirmListener(getString(R.string.download), new WalletDailog.onComfirmListener() {
                    @Override
                    public void onConfirm(WalletDailog dialog) {
                        if (entity.getData().getReleasePackageUrl() != null) {
                            presenter.downLoad(entity.getData().getReleasePackageUrl());
                        }
                    }
                });
                tipDialog.setMessage(getString(R.string.found_new_version)+"\n"+entity.getData().getDescription());

            } else if (dataBean.getForceUpgrade() == 0) {//选择升级
                TipDialog tipDialog = new TipDialog(getActivity(), R.style.DialogStyle
                        , R.layout.dialog_double_tip, 0.35);
                tipDialog.setCancelable(false);
                tipDialog.show();
                tipDialog.addOnCancelListener(getString(R.string.cancel), new WalletDailog.onCancelListener() {
                    @Override
                    public void onCancel(WalletDailog dialog) {
                        Intent intent = new Intent(getActivity(), WebComonActivity.class);
                        intent.putExtra(Stringconstant.WEB_URL_VALUE, ApiConstants.web_login);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                });
                tipDialog.addOnConfirmListener(getString(R.string.download), new WalletDailog.onComfirmListener() {
                    @Override
                    public void onConfirm(WalletDailog dialog) {
                        if (entity.getData().getReleasePackageUrl() != null) {
                            presenter.downLoad(entity.getData().getReleasePackageUrl());
                        }
                    }
                });
                tipDialog.setMessage(getString(R.string.found_new_version)+"\n"+entity.getData().getDescription());
            }
        } else {
            if (entity.getCode() != 0) {
                UIUtils.showToast(UIUtils.getErrorCode(entity.getCode() + "", getContext()));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
                    // 权限申请成功
                    if (presenter != null) {
                        presenter.checkUpdate();
                    }
                } else {
                    UIUtils.showToast("请打开SD卡存储权限");
                }

                break;
        }
    }

    @Override
    public void getDataFail(String fail) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (presenter != null) {
            presenter.detachView(true);
        }
    }

    //三张图片（引导页）
    private int[] img = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
    private int[] text = {R.string.easy_kz, R.string.more_rate, R.string.data_in_blockchain};

    private class MyViewPager extends PagerAdapter {
        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imge = new ImageView(getActivity());
            imge.setScaleType(ImageView.ScaleType.FIT_XY);
            imge.setImageResource(img[position]);

           /* if (position == 2) {
                imge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SPUtils.put(getContext(), SPConstants.GUIDEPAGE, false);
                        showUpdate();
                    }
                });
            }*/
            container.addView(imge);
            return imge;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
