package com.wayki.wallet.utils.encryption;

/**
 * Created by Administrator on 2018/5/11.
 */

public class AESUtil {


    public static AES mAes = null;
    public static String enString = null;
    public static String deString = null;
    public static byte[] mBytes = null;


 /*   public static String encrypt(String pub, String key) {
        if (key == null) {
            return null;
        }
        String md5_key = MD5Util.getMD5Str(key);
        Log.i("AES", "decrypt: md5_key:" + md5_key);
        if (mAes == null) {
            mAes = new AES(md5_key);
        }

        try {
            mBytes = pub.getBytes("utf-8");
        } catch (Exception e) {

        }
        enString = mAes.encrypt(mBytes);
        return enString;
    }*/

    public static String encrypt(String pub) {


        if (mAes == null) {
            mAes = new AES();
        }

        try {
            mBytes = pub.getBytes("utf-8");
        } catch (Exception e) {

        }
        enString = mAes.encrypt(mBytes);
        return enString;
    }

    public static String decrypt(String en_string) {

        if (mAes == null) {
            mAes = new AES();
        }
        deString = mAes.decrypt(en_string);
        return deString;
    }


}
