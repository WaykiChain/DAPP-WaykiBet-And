package com.wayki.wallet.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.wayki.wallet.BuildConfig;
import com.wayki.wallet.R;
import com.wayki.wallet.activity.CommonActivity;
import com.wayki.wallet.activity.MainActivity;
import com.wayki.wallet.activity.WebComonActivity;
import com.wayki.wallet.application.ActivityManager;
import com.wayki.wallet.application.App;
import com.wayki.wallet.dialog.CreateImportDialog;
import com.wayki.wallet.dialog.CreateWalletTipDialog;
import com.wayki.wallet.dialog.WalletDailog;
import com.wayki.wallet.utils.ApiConstants;
import com.wayki.wallet.utils.PermissionUtils;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.Stringconstant;
import com.wayki.wallet.utils.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_KEY;
import static com.wayki.wallet.utils.Stringconstant.REGISTE_FRAGMENT_KEY;

/**
 * Sonic javaScript Interface (Android API Level >= 17)
 */

public class JavaScriptInterface {

    private Context mContext;
    private final int REQUEST_CODE_STORAGE = 1;

    public JavaScriptInterface(Context mContext) {
        this.mContext = mContext;
    }

    @JavascriptInterface
    public String getAddress() {
        String address = SPUtils.get(mContext, SPConstants.WALLET_ADDRESS, "").toString();
        Toast.makeText(mContext, address, Toast.LENGTH_SHORT).show();
        return address;
    }

    @JavascriptInterface
    public String getMd5Hash() {
        String address = SPUtils.get(mContext, SPConstants.WALLET_MD5, "").toString();
        Toast.makeText(mContext, address, Toast.LENGTH_SHORT).show();
        return address;
    }

    @JavascriptInterface
    public String getLang() {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();

        String lang="en";
        if("zh".equals(language)){
            String lang_type=locale.getCountry();
            if ("CN".equals(lang_type)){
                lang= "zh-CHS";
            }else {
                lang= "zh-CHT";
            }
        }else{
            lang= "en";
        }
        return lang;
    }

    @JavascriptInterface
    public String getAppVersion() {
        String address = UIUtils.getVersionName(mContext);
        return address;
    }

    @JavascriptInterface
    public String getChannel() {
        String address = UIUtils.getChannelData(mContext);
        return address;
    }

    @JavascriptInterface
    public void jumpActivity(String url) {
        Intent intent = new Intent(mContext, WebComonActivity.class);
        intent.putExtra(Stringconstant.WEB_URL_VALUE, BuildConfig.API_HOST+url);
        intent.putExtra(Stringconstant.WEB_HAD_LOGIN, "true");
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void externalActivity(String url) {
        Intent intent = new Intent(mContext, WebComonActivity.class);
        intent.putExtra(Stringconstant.WEB_URL_VALUE, url);
        intent.putExtra(Stringconstant.WEB_HAD_LOGIN, "true");
        intent.putExtra(Stringconstant.EXTERNAL_WEB, "true");
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void loginOut() {
        ActivityManager.create().finishAllActivity();

        Intent intent = new Intent(mContext, WebComonActivity.class);
        intent.putExtra(Stringconstant.WEB_URL_VALUE, ApiConstants.web_login);
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void jumpMainActivity() {//
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }

    @JavascriptInterface
    public String getToken() {
        String token= SPUtils.get(mContext,SPConstants.APPTOKEN,"").toString();
        return  token;
    }

    @JavascriptInterface
    public String getCountryCode() {
        String code=  SPUtils.get(mContext, SPConstants.COUNTRYCODE, "").toString();
        return  code;
    }

    @JavascriptInterface
    public void setBindInfo(String phone,String token,String code) {
        App.getApplication().setToken(token);
        SPUtils.put(mContext,SPConstants.APPTOKEN,token);
        SPUtils.put(mContext, SPConstants.COUNTRYCODE, code);
        if(!TextUtils.isEmpty(phone)) {
            String phonee = SPUtils.get(mContext, SPConstants.WALLET_PHONE, "").toString();
            if (!phonee.equals(phone)) {
                SPUtils.put(mContext, SPConstants.WALLET_PHONE, phone);

                SPUtils.put(mContext, SPConstants.WALLET_ADDRESS, "");
                SPUtils.put(mContext, SPConstants.WALLET_PRIVATE_KEY, "");
                SPUtils.put(mContext, SPConstants.WALLET_WORDS, "");
                SPUtils.put(mContext, SPConstants.WALLET_MD5, "");
                SPUtils.put(mContext, SPConstants.REGID, "");
            }
        }
    }

    @JavascriptInterface
    public String getPhone() {
        String phone=  SPUtils.get(mContext,SPConstants.WALLET_PHONE,"").toString();
        return  phone;
    }

    @JavascriptInterface
    public String getPhoneModel() {
        String model=android.os.Build.BRAND.toLowerCase();
        return  model;
    }

    @JavascriptInterface
    public void finishActivity() {
        ((Activity) mContext).finish();
    }

    @JavascriptInterface
    public void savePicture(final String bitmapstr) {
        PermissionUtils.checkAndRequestPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                REQUEST_CODE_STORAGE,
                new PermissionUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        //将字符串转换成Bitmap类型
                        Bitmap bitmap = null;
                        try {
                            byte[] bitmapArray;
                            bitmapArray = Base64.decode(bitmapstr, Base64.DEFAULT);
                            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // 首先保存图片
                        File appDir = new File(Environment.getExternalStorageDirectory(),
                                "DCIM");
                        if (!appDir.exists()) {
                            appDir.mkdir();
                        }
                        String fileName = System.currentTimeMillis() + ".jpg";
                        File file = new File(appDir, fileName);
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                        } catch (FileNotFoundException e) {
                            UIUtils.showToast(UIUtils.getString(R.string.save_faile));
                            e.printStackTrace();
                        } catch (IOException e) {
                            UIUtils.showToast(UIUtils.getString(R.string.save_faile));
                            e.printStackTrace();
                        }

                        // 其次把文件插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                                    file.getAbsolutePath(), fileName, null);
                            UIUtils.showToast(UIUtils.getString(R.string.save_success));
                        } catch (FileNotFoundException e) {
                            UIUtils.showToast(UIUtils.getString(R.string.save_faile));
                            e.printStackTrace();
                        }
                        // 最后通知图库更新
                        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(new File(file.getPath()))));
                    }
                });
    }

    @JavascriptInterface
    public String getDevicesID() {
        String uuid_c= SPUtils.get(mContext, SPConstants.UUID,"").toString();
        return uuid_c;
    }
    boolean status=false;
    @JavascriptInterface
    public boolean getAccStatus(String address){
        String addr= SPUtils.get(mContext, SPConstants.WALLET_ADDRESS,"").toString();
        if(address==null||(""+address).equals("null")){
            showTip();
            status=false;
        }else{
            status=true;
        }
        return status;
    }


    /*
     * 注册提示导入钱包
     * */
    @JavascriptInterface
    public void showCreateWalletTip() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(REGISTE_FRAGMENT_KEY,"1");
        mContext.startActivity(intent);
        ((Activity)mContext).finish();
    }

    /*
     * 活动弹窗是否显示
     * */
    @JavascriptInterface
    public boolean activityDialog() {
        boolean isfirst=(Boolean) SPUtils.get(mContext,SPConstants.SHOWGUIDE,true);
        SPUtils.put(mContext,SPConstants.SHOWGUIDE,false);
        return isfirst;
    }


    private void showTip(){
        CreateWalletTipDialog walletDailog = new CreateWalletTipDialog(mContext,
                R.style.DialogStyle, R.layout.dialog_createwallet_tip);
        walletDailog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
            @Override
            public void onConfirm(WalletDailog dialog) {
                Intent intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(FRAGMENT_KEY, 4);
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });

        walletDailog.addOnCancelListener(new WalletDailog.onCancelListener() {
            @Override
            public void onCancel(WalletDailog dialog) {
                Intent intent = new Intent(mContext, CommonActivity.class);
                intent.putExtra(FRAGMENT_KEY, 5);
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });

        walletDailog.show();
        walletDailog.setCreateWalletTip();
    }


    @JavascriptInterface
    public void showwicc2Wusd(){
        ActivityManager.create().finishActivity(WebComonActivity.class);
        Intent intent=new Intent();
        intent.setAction(Stringconstant.EXCHANGE_PAGE);
        mContext.sendBroadcast(intent);
    }

}
