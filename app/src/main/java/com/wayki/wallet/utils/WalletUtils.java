package com.wayki.wallet.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.orhanobut.logger.Logger;
import com.wayki.wallet.R;
import com.wayki.wallet.application.App;
import com.wayki.wallet.bean.CreateWalletBean;
import com.wayki.wallet.callback.Wallet_Callback;
import com.wayki.wallet.utils.encryption.AESUtils2;
import com.wayki.wallet.utils.encryption.SHAUtil;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import core.CoinApi;
import core.JBtWallet;
import core.JNetParams;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WalletUtils {

    private static WalletUtils walletUtils;
    public static CoinApi coinApi;
    private static JNetParams jNetParams;
    private List<String> words;

    public static WalletUtils getInstance() {
        if (walletUtils == null) {
            walletUtils = new WalletUtils();
        }
        return walletUtils;
    }

    /**
     * 创建和导入钱包
     */
    public void createWallet(final Context mContext, final String password, final ArrayList<String> input_words,
                             final Wallet_Callback callback) {
        coinApi = App.getApplication().getCoinApi();
        jNetParams = App.getApplication().getjNetParams();
        words = (input_words == null ? coinApi.createAllCoinMnemonicCode() : input_words);
        StringBuilder sb = new StringBuilder();
        int size = words.size();
        int j = 0;
        for (String word : words) {
            sb.append(word);
            if (j != size - 1) {
                sb.append(" ");
            }
            j++;
        }
        final String wordsListString = sb.toString();

        boolean validate = coinApi.checkMnemonicCode(words);
        if (!validate) {
            callback.Failure(UIUtils.getString(R.string.invalid_mnemonic));
            return;
        }

        Observable.create(new ObservableOnSubscribe<JBtWallet>() {
            @Override
            public void subscribe(ObservableEmitter<JBtWallet> e) {
                JBtWallet jBtWallet = coinApi.createWallet(wordsListString, password, jNetParams);
                //App.getApplication().setjBtWallet(null);
                e.onNext(jBtWallet);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JBtWallet>() {
                    @Override
                    public void accept(JBtWallet jBtWallet) {

                        if (jBtWallet != null) {
                            CreateWalletBean bean=new CreateWalletBean();
                            String md5_pwd = MD5Util.getMD5Str(password);
                            String en_wordsListString = AESUtils2.encrypt(md5_pwd, wordsListString);
                            //SPUtils.put(mContext, SPConstants.WALLET_WORDS, en_wordsListString);
                            bean.setWords(en_wordsListString);

                            String mHash = SHAUtil.sha512Encrypt(wordsListString);
                           // SPUtils.put(mContext, SPConstants.WALLET_MD5, mHash);
                            bean.setHash(mHash);

                            //获取私钥
                            String strPri = coinApi.getPriKeyFromBtSeed(jBtWallet.getBtSeed(),
                                    password, jNetParams);
                            String en_pri_key = AESUtils2.encrypt(md5_pwd, strPri);
                            //SPUtils.put(mContext, SPConstants.WALLET_PRIVATE_KEY, en_pri_key);
                            bean.setPrivatekey(en_pri_key);

                            //获取钱包地址
                            String walletAddress = jBtWallet.getAddress();
                           // SPUtils.put(mContext, SPConstants.WALLET_ADDRESS, walletAddress);
                            bean.setAddress(walletAddress);

                            bean.setTip(input_words == null ? UIUtils.getString(R.string.create_success) : UIUtils.getString(R.string.import_success));


                            bean.setjBtWallet(jBtWallet);
                            bean.setCreata_import(input_words == null ? Stringconstant.CREATE:
                                    Stringconstant.IMPORT);
                            callback.Success(bean);
                        } else {
                            callback.Failure(UIUtils.getString(R.string.create_fail));
                        }

                    }
                });
    }

    /**
     * 获得助记词
     */
    public void getWords(Context mContext, String pwd, Wallet_Callback callback) {
        String md5_pwd = MD5Util.getMD5Str(pwd);
        String dwords = SPUtils.get(mContext, SPConstants.WALLET_WORDS, "").toString();
        String words = AESUtils2.decrypt(md5_pwd, dwords);
        if (words != null) {
            callback.Success(words);
        } else {
            callback.Failure(UIUtils.getString(R.string.pwd_error));
        }
    }

    /**
     * 修改密码
     */
    public void changePassword(Context mContext, String oldpwd, String newPwd, Wallet_Callback callback) {
        String md5_pwd = MD5Util.getMD5Str(oldpwd);
        String dwords = SPUtils.get(mContext, SPConstants.WALLET_WORDS, "").toString();
        String words = AESUtils2.decrypt(md5_pwd, dwords);
        if (words != null) {
            String new_md5_pwd = MD5Util.getMD5Str(newPwd);
            String new_en_words = AESUtils2.encrypt(new_md5_pwd, words);
            SPUtils.put(mContext, SPConstants.WALLET_WORDS, new_en_words);

            //String md5_new_pwd = MD5Util.getMD5Str(newPwd);
            String pre_en_pri_key = SPUtils.get(mContext,
                    SPConstants.WALLET_PRIVATE_KEY, "").toString();
            String pre_de_pri_key = AESUtils2.decrypt(md5_pwd, pre_en_pri_key);
            String new_en_pri_key = AESUtils2.encrypt(new_md5_pwd, pre_de_pri_key);
            SPUtils.put(mContext, SPConstants.WALLET_PRIVATE_KEY, new_en_pri_key);

            callback.Success(mContext.getString(R.string.pwd_change_success));

        } else {
            callback.Failure(mContext.getString(R.string.old_pwd_error));
        }
    }

    /**
     * 导出私钥
     */

    public void getPrivateKey(Context mContext, String oldpwd, Wallet_Callback callback) {
        String privatekey = SPUtils.get(mContext, SPConstants.WALLET_PRIVATE_KEY, "").toString();
        String md5_new_pwd = MD5Util.getMD5Str(oldpwd);
        String pre_de_pri_key = AESUtils2.decrypt(md5_new_pwd, privatekey);

        if (privatekey != null) {
            callback.Success(pre_de_pri_key);
        } else {
            callback.Failure(UIUtils.getString(R.string.pwd_error));
        }
    }

    /**
     * 获得地址
     */
    public String getAddress(Context mContext) {
        String address = SPUtils.get(mContext, SPConstants.WALLET_ADDRESS, "").toString();
        return address;
    }

    /**
     * 助记词备份hash
     */
    public String getHash(Context mContext) {
        String address = SPUtils.get(mContext, SPConstants.WALLET_MD5, "").toString();
        return address;
    }

    /**
     * 获得图片
     */

    public void getBitmap(final Context mContext, Wallet_Callback callback) {

        String address = SPUtils.get(mContext, SPConstants.WALLET_ADDRESS, "").toString();
        if (!"".equals(address)) {
            int QR_WIDTH = 800;
            int QR_HEIGHT = 800;
            try {
                // 需要引入core包
                QRCodeWriter writer = new QRCodeWriter();
                Logger.i("sss", "生成的文本：" + address);
                if (address == null || "".equals(address) || address.length() < 1) {
                    return;
                }
                // 把输入的文本转为二维码
                BitMatrix martix = writer.encode(address, BarcodeFormat.QR_CODE,
                        QR_WIDTH, QR_HEIGHT);

                System.out.println("w:" + martix.getWidth() + "h:"
                        + martix.getHeight());

                Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                hints.put(EncodeHintType.MARGIN, 1);
                BitMatrix bitMatrix = new QRCodeWriter().encode(address,
                        BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
                int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
                for (int y = 0; y < QR_HEIGHT; y++) {
                    for (int x = 0; x < QR_WIDTH; x++) {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * QR_WIDTH + x] = 0xff000000;
                        } else {
                            pixels[y * QR_WIDTH + x] = 0xffF5F5F5;
                        }
                    }
                }
                Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                        Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
                callback.Success(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
                //  Resources res = context.getResources();
                // Bitmap qrBitmap = BitmapFactory.decodeResource(res, R.drawable.qr_code_failed);
                // iv_qr.setImageBitmap(qrBitmap);
                callback.Failure(null);
            }

        } else {
            callback.Failure(null);
        }
    }
}