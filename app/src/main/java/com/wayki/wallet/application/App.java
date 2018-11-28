package com.wayki.wallet.application;

import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.wayki.wallet.BuildConfig;
import com.wayki.wallet.utils.SPConstants;
import com.wayki.wallet.utils.SPUtils;
import com.wayki.wallet.utils.Stringconstant;
import com.wayki.wallet.utils.UIUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import core.CoinApi;
import core.Constants;
import core.JBtWallet;
import core.JKeyPath;
import core.JNetParams;
import okhttp3.OkHttpClient;

public class App extends Application {
    private static Context application;
    public  CoinApi coinApi;
    private boolean isinit;
    private JNetParams jNetParams;
    public static String Token="";
    public JBtWallet jBtWallet=null;
    //程序启动时加载jni库
    static {
        System.loadLibrary("coinapi");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        coinApi = new CoinApi();
        isinit=coinApi.init();
        setJNetParams();
        application = getApplicationContext();
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        initOkGo();
    }

    public static App getApplication() {
        return (App) application;
    }

    private void initOkGo() {
        String uuid_c= SPUtils.get(this, SPConstants.UUID,"").toString();
        if("".equals(uuid_c)) {
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            SPUtils.put(this, SPConstants.UUID,uuid);
        }

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put(Stringconstant.DEVICES_UUID, UIUtils.getUUID());
        headers.put(Stringconstant.LANG, UIUtils.getLang());
        headers.put(Stringconstant.USER_TIMEZONE, UIUtils.getCurrentTimeZone());//header不支持中文，不允许有特殊字符
        HttpParams params = new HttpParams();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(BuildConfig.DEBUG?HttpLoggingInterceptor.Level.BODY:
                HttpLoggingInterceptor.Level.NONE);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
    }

    public void setJNetParams() {
        jNetParams = new JNetParams();//
        jNetParams.setCoinType(Constants.COIN_TYPE_WICC);
        jNetParams.setSymbol(Constants.COIN_SYMBOL_WICC);
        jNetParams.setVersion(1);
        if(BuildConfig.DEBUG){
        jNetParams.setNettype(Constants.NETWORK_TYPE_TEST);
        }else {
        jNetParams.setNettype(Constants.NETWORK_TYPE_MAIN);
        }
        JKeyPath jKeyPath = new JKeyPath();
        jKeyPath.setPath1(44);
        jKeyPath.setHd1(true);
        jKeyPath.setPath2(99999);
        jKeyPath.setHd2(true);
        jKeyPath.setPath3(0);
        jKeyPath.setHd3(true);
        jKeyPath.setPath4(0);
        jKeyPath.setHd4(false);
        jKeyPath.setPath5(0);
        jKeyPath.setHd5(false);
        jKeyPath.setSymbol(Constants.COIN_SYMBOL_WICC);

       if(BuildConfig.DEBUG){
           jNetParams.setHDprivate(0x7d5c5a26);
           jNetParams.setHDpublic(0x7d573a2c);
           jNetParams.setP2KH(135);
           jNetParams.setP2SH(88);
           jNetParams.setKeyprefixes(210);
       }else {
           jNetParams.setHDprivate(0x4c233f4b);
           jNetParams.setHDpublic(0x4c1d3d5f);
           jNetParams.setP2KH(73);
           jNetParams.setP2SH(51);
           jNetParams.setKeyprefixes(153);
       }

        jNetParams.setN(32768);
        jNetParams.setR(8);
        jNetParams.setP(1);

        jNetParams.setKeyPath(jKeyPath);

    }

    public JNetParams getjNetParams() {
        return jNetParams;
    }

    public  CoinApi getCoinApi() {
        return coinApi;
    }

    public boolean isIsinit() {
        return isinit;
    }

    public  String getToken() {
        return Token;
    }

    public  void setToken(String token) {
        Token = token;
    }

    public JBtWallet getjBtWallet() {
        return jBtWallet;
    }

    public void setjBtWallet(JBtWallet jBtWallet) {
        this.jBtWallet = jBtWallet;
    }

}
