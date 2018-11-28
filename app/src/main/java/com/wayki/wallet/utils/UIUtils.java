package com.wayki.wallet.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wayki.wallet.R;
import com.wayki.wallet.activity.CommonActivity;
import com.wayki.wallet.activity.MainActivity;
import com.wayki.wallet.activity.WebComonActivity;
import com.wayki.wallet.application.ActivityManager;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.BottomButtonBean;
import com.wayki.wallet.bean.CommonBean;
import com.wayki.wallet.dialog.CreateWalletTipDialog;
import com.wayki.wallet.dialog.TipDialog;
import com.wayki.wallet.dialog.WalletDailog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.wayki.wallet.utils.Stringconstant.FRAGMENT_KEY;

/**
 * 和Ui相关的工具类
 * Created by zst on 2016/3/9.
 */
public class UIUtils {

    /**
     * 10进制转16进制
     */
    public static String toHexString(String vals) {
        BigInteger v1 = new BigInteger(vals, 10);
        String amount = v1.toString(16);
        return amount;
    }

    /**
     * 转换为为网络序调用锁仓的方法
     */
    public static String valueSort(String vals) {
        int length = vals.length();
        ArrayList<String> lists = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (length % 2 == 0) {
            for (int i = 0; i < vals.length(); i++) {
                if (i % 2 == 1) {
                    if (sb.toString().length() > 0) {
                        sb.delete(0, sb.toString().length());
                    }
                    sb.append(vals.charAt(i - 1));
                    sb.append(vals.charAt(i));
                    lists.add(sb.toString());
                }
            }
        } else {
            vals = "0" + vals;
            for (int i = 0; i < vals.length(); i++) {
                if (i % 2 == 1) {
                    if (sb.toString().length() > 0) {
                        sb.delete(0, sb.toString().length());
                    }
                    sb.append(vals.charAt(i - 1));
                    sb.append(vals.charAt(i));
                    lists.add(sb.toString());
                }
            }
        }

        Collections.reverse(lists);
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < lists.size(); i++) {
            sb1.append(lists.get(i));
        }

        String fornat = "0000000000000000";
        int len = fornat.length() - sb1.toString().length();
        for (int i = 0; i < len; i++) {
            sb1.append("0");
        }

        return sb1.toString();
    }

    /**
     * 16进制转二进制
     */
    public static byte[] hexString2binaryString(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /* 资源类 */
    public static Resources getResource() {
        return App.getApplication().getResources();
    }

    /* 得到上下文 */
    public static Context getContext() {
        return App.getApplication();
    }

    /**
     * 获取到string字符数组
     *
     * @param tabNames 字符数组id
     * @return
     */
    public static String getString(int tabNames) {
        return getResource().getString(tabNames);
    }

    /**
     * 获取到string字符数组
     *
     * @param tabNames 字符数组id
     * @return
     */
    public static String[] getStringArray(int tabNames) {
        return getResource().getStringArray(tabNames);
    }

    /**
     * dip转换px
     *
     * @param dip
     * @return
     */
    public static int dip2px(int dip) {
        final float scale = getResource().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * px转换dip
     *
     * @param px
     * @return
     */
    public static int px2dip(int px) {
        final float scale = getResource().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 绑定布局文件
     * <p/>
     * Fragment的initView()可使用
     *
     * @param id layout布局文件
     * @return
     */
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    /**
     * 得到图片 - use Resource by id
     *
     * @param id
     * @return
     */
    public static Drawable getDrawalbe(int id) {
        return getResource().getDrawable(id);
    }

    /**
     * 得到颜色 - use Resource by id
     *
     * @param id
     * @return
     */
    public static int getColor(int id) {
        return getResource().getColor(id);
    }

    /**
     * 获取dimens中的值 - use Resource by id
     *
     * @param homePictureHeight id
     * @return
     */
    public static int getDimens(int homePictureHeight) {
        return (int) getResource().getDimension(homePictureHeight);
    }

    /**
     * 验证手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9])|(16[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 显示Toast
     *
     * @param message
     */
    public static void showToast(String message) {
        // Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText(message);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setPadding(20, 20, 20, 20);
        textView.setBackground(getContext().getResources().getDrawable(R.drawable.toast_bg));
        Toast toast = new Toast(getContext());
        toast.setView(textView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 得到屏幕宽度
     *
     * @return px
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);// 取得窗口属性

        return dm.widthPixels;
    }

    /**
     * 得到屏幕高度
     *
     * @return px
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);// 取得窗口属性

        return dm.heightPixels;
    }

    /**
     * 图片高度(宽度全屏,得到图片的显示高度)
     * <p/>
     * 通过比例公式:screenWidth/x = 2/1 (微绘图片规定尺寸)
     *
     * @return
     */
    public static int getPicShowHeight() {
        int h = Math.round((float) (getScreenWidth() * 1) / (float) 2);
        return h;
    }

    /**
     * 图片高度(宽度输入,得到图片的显示高度)
     * <p/>
     * 通过比例公式:screenWidth/x = 750/560 (微绘图片规定尺寸)
     *
     * @return
     */
    public static int getPicShowHeight(int width) {
        int h = Math.round((float) (width * 560) / (float) 750);
        return h;
    }


    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 得到状态栏高度
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResource().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResource().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 判断是否有网络
     */

    /**
     * 隐藏软键盘
     */
    public static void hideKeyboard(Activity c) {
        try {
            InputMethodManager imm = (InputMethodManager) c
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(c.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
        }
    }

    /**
     * 判断网址是否有效
     */
    public static boolean isLinkAvailable(String link) {
        Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序渠道信息]
     *
     * @return 当前应用的版本渠道
     */

    public static String getChannelData(Context mContext) {
        if (mContext == null || TextUtils.isEmpty("UMENG_CHANNEL")) {
            return null;
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = mContext.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
    }


    public static void toClipboardManager(String message) {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, message);
        clipboard.setPrimaryClip(clipData);
        showToast(getString(R.string.aleady_copied));
    }


    /**
     * 字母加数字组合 8-12位
     */
    public static boolean matcherPassword(String value) {
        String regex = "(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,12}";
        return testRegex(regex, value);
    }

    public static boolean testRegex(String regex, String inputValue) {
        return Pattern.compile(regex).matcher(inputValue).matches();
    }


    public static String getErrorCode(String code, final Context context) {
        Locale locale = UIUtils.getContext().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        ArrayList<String> langs = new ArrayList<String>(Arrays.asList("en", "zh", "ko"));
        if (!langs.contains(language)) {
            language = "en";
        }

        int fileName = R.raw.config_en;
        switch (language) {
            case "en":
                fileName = R.raw.config_en;
                break;
            case "zh":
                fileName = R.raw.config_zh;
                break;
            case "ko":
                fileName = R.raw.config_en;
                break;
        }
        Map<String, String> newList = new HashMap<>();
        try {
            InputStream is = getContext().getResources().openRawResource(fileName);
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                String[] strs = str.split("=");
                newList.put(strs[0] + "", strs[1] + "");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String value = "";
        for (String str : newList.keySet()) {
            if ((code + "").equals(str)) {
                value = newList.get(code);
            }
        }

        if ("2011".equals(code)) {//token过期
            TipDialog tipDialog = new TipDialog(context, R.style.DialogStyle
                    , R.layout.dialog_signal_tip);
            tipDialog.setCancelable(false);
            tipDialog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
                @Override
                public void onConfirm(WalletDailog dialog) {
                    ActivityManager.create().finishAllActivity();
                    Intent intent = new Intent(context, WebComonActivity.class);
                    intent.putExtra(Stringconstant.WEB_URL_VALUE, ApiConstants.web_login);
                    context.startActivity(intent);
                }
            });
            tipDialog.show();
            tipDialog.setMessage(getString(R.string.invalid_token));

        }


        return value;
    }

    /**
     * 获得设备号
     */

    public static String getUUID() {
        String uuid_c = SPUtils.get(getContext(), SPConstants.UUID, "").toString();
        return uuid_c;
    }

    /*
    * 获得时区
    * */
    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
       // String strTz = tz.getDisplayName(false,TimeZone.SHORT);
        String strTz=(tz.getRawOffset()/3600000)+"";
        return strTz;
    }

    public static String toBigDecimal2toBigInteger(double v) {
        BigDecimal bd = new BigDecimal(v);
        String s = bd.toBigInteger().toString();
        return s;
    }

    public static String formatTime(String time) {
        if (time == null || TextUtils.isEmpty(time)) {
            return "";
        }
        Long time_l = Long.parseLong(time);
        SimpleDateFormat aDate = new SimpleDateFormat("yyyy-mm-dd  HH:mm");
        return aDate.format(time_l);
    }


    /**
    * 保留四位小数，不四舍五入
    * */
    public static String getMoney4(String text) {
        if(TextUtils.isEmpty(text)||"null".equals(text)) return "";
        Double f = Double.parseDouble(text);
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        return decimalFormat.format(f);
    }

    public static String getMoney2(String text) {
        if(TextUtils.isEmpty(text)||"null".equals(text)) return "";
        Double f = Double.parseDouble(text);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        return decimalFormat.format(f);
    }

    public static String getMoney8(String text) {
        if(TextUtils.isEmpty(text)||"null".equals(text)) return "";
        Double f = Double.parseDouble(text);
        DecimalFormat decimalFormat = new DecimalFormat("0.00000000");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        return decimalFormat.format(f);
    }

    /**
     * 保留四位小数，四舍五入
     * */
    public static String setTextDecimal4(String text) {
        if(TextUtils.isEmpty(text)||"null".equals(text)) return "";
        Double f = Double.parseDouble(text);
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        return decimalFormat.format(f);
    }

    public static String setTextDecimal2(String text) {
        if(TextUtils.isEmpty(text)||"null".equals(text)) return "";
        Double f = Double.parseDouble(text);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(f);
    }

    public static String setTextDecimal8(String text) {
        if(TextUtils.isEmpty(text)||"null".equals(text)) return "";
        Double f = Double.parseDouble(text);
        DecimalFormat decimalFormat = new DecimalFormat("0.00000000");
        return decimalFormat.format(f);
    }

    /**
     * 检查钱包是否存在
     * */
    public static boolean checkWallet(final Context mContext) {
        String words = SPUtils.get(getContext(), SPConstants.WALLET_WORDS, "").toString();
        if ("".equals(words)) {
            //UIUtils.showToast(getString(R.string.pls_import_create));

            CreateWalletTipDialog walletDailog = new CreateWalletTipDialog(mContext,
                    R.style.DialogStyle, R.layout.dialog_createwallet_tip);
            walletDailog.addOnConfirmListener(new WalletDailog.onComfirmListener() {
                @Override
                public void onConfirm(WalletDailog dialog) {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, CommonActivity.class);
                    intent.putExtra(FRAGMENT_KEY, 4);
                    mContext.startActivity(intent);
                }
            });

            walletDailog.addOnCancelListener(new WalletDailog.onCancelListener() {
                @Override
                public void onCancel(WalletDailog dialog) {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, CommonActivity.class);
                    intent.putExtra(FRAGMENT_KEY, 5);
                    mContext.startActivity(intent);
                }
            });

            walletDailog.show();
            walletDailog.setCreateWalletTip();

            return false;
        }
        return true;
    }

    /**
    * 检查钱包是否备份
    * */

    public static boolean checkBackup(int id) {
        String words = SPUtils.get(getContext(), SPConstants.WALLET_WORDS, "").toString();

        if (id == R.id.iv_wallet_detail) {

            return false;
        } else if ("".equals(words)) {
            return false;
        } else {
            boolean needbackup = (boolean) SPUtils.get(getContext(), SPConstants.NEEDBACKUP, true);
            return needbackup;
        }
    }

    public  static  void getDrawable(final Context mContext, final String imageUrl1,
                                     final String imageUrl2, final RadioButton imageView) {
       final StateListDrawable drawable = new StateListDrawable();
        Observable.create(new ObservableOnSubscribe<CommonBean>() {
            @Override
            public void subscribe(ObservableEmitter<CommonBean> e) throws Exception {
                List<CommonBean> bitmaps = new ArrayList<CommonBean>();
                bitmaps.add(new CommonBean(imageUrl1,0));
                bitmaps.add(new CommonBean(imageUrl2,1));
                for (CommonBean s : bitmaps) {
                    e.onNext(s);//发送多个事件
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<CommonBean, ObservableSource<BottomButtonBean>>() {
                    @Override
                    public ObservableSource<BottomButtonBean> apply(CommonBean bitmap) throws Exception {
                        List<BottomButtonBean> lists = new ArrayList<>();
                        Bitmap myBitmap1 = Glide.with(mContext)
                                .load(bitmap.getName())
                                .asBitmap()
                                .centerCrop()
                                .placeholder(R.drawable.account)
                                .error(R.drawable.account)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(250, 250)
                                .get();
                      // Bitmap bitmap1= BitmapFactory.decodeResource(mContext.getResources(),R.drawable.account );

                        lists.add(new BottomButtonBean(getbitmap(myBitmap1,(MainActivity)mContext),bitmap.getId()));
                        return Observable.fromIterable(lists);//.delay(10, TimeUnit.NANOSECONDS);//将这些事件合并成一个Observable
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BottomButtonBean>() {
                    @Override
                    public void accept(BottomButtonBean bitmap) throws Exception {
                        if(bitmap.getId()==1) {
                            drawable.addState(new int[]{android.R.attr.state_checked},new BitmapDrawable(bitmap.getBitmap()));
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            imageView.setCompoundDrawables(null, drawable, null, null);
                        }else if(bitmap.getId()==0){
                            drawable.addState(new int[]{-android.R.attr.state_checked},new BitmapDrawable(bitmap.getBitmap()));
                        }
                    }
                });
    }


    public static Bitmap getbitmap(Bitmap bitmap,Activity mContext){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float screenWidth  = mContext.getWindow().getWindowManager().getDefaultDisplay().getWidth();		// 屏幕宽（像素，如：480px）
        float screenHeight = mContext.getWindow().getWindowManager().getDefaultDisplay().getHeight();		// 屏幕高（像素，如：800p）
        float scaleWidth = screenWidth/5/width;
        float scaleHeight = screenHeight*0.1f/height;

        // 取得想要缩放的matrix參數
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的圖片
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,true);
        return newbm;
    }


    /**
     * 获得语言
    * */
    public static String getLang() {
        Locale locale = getContext().getResources().getConfiguration().locale;
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

}
