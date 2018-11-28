package com.wayki.wallet.utils.encryption;

import android.util.Log;

import com.wayki.wallet.utils.MD5Util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2018/5/11.
 */

public class AES {

    private final String KEY_GENERATION_ALG = "PBEWITHSHAANDTWOFISH-CBC";
    // private final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";
    private final int HASH_ITERATIONS = 10000;
    private final int KEY_LENGTH = 128;
    private char[] humanPassphrase = {'P', 'e', 'r', ' ', 'v', 'a', 'l', 'l',
            'u', 'm', ' ', 'd', 'u', 'c', 'e', 's', ' ', 'L', 'a', 'b', 'a',
            'n', 't'};// per vallum duces labant
    private byte[] salt = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD,
            0xE, 0xF}; // must save this for next time we want the key

    private PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salt,
            HASH_ITERATIONS, KEY_LENGTH);
    private final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";// AES/CBC/PKCS7Padding

    private SecretKeyFactory keyfactory = null;
    private SecretKey sk = null;
    private SecretKeySpec skforAES = null;
    private static final String base_kD = "123456789012";
    private static String base_kE_ = "1873";// key必须为16位，可更改为自己的key
    private static String base_kD_ = "9513";
    public static final String base_kE = "852346791537";
    private static String ivParameter = base_kD + base_kD_;// 密钥默认偏移，可更改
    private byte[] iv = ivParameter.getBytes();
    private IvParameterSpec IV;

    public static String base_kA = "7561DE";
    public static String base = base_kA + Base64Decoder.base_kD + Base64Encoder.base_kE;//
    // key必须为16位，可更改为自己的key
    private static String sKey_ = "85234679RBDFdP2a"/*MD5Util.getMD5Str(base)*/;//
    // key必须为16位，可更改为自己的key
    private static String sKey = base_kE + base_kE_;// key必须为16位，可更改为自己的key

    public AES() {

        sKey_ = MD5Util.getMD5Str(base);
        sKey = sKey_.substring(0, 16);
        ivParameter = sKey_.substring(16, 32);
        try {
            keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
            sk = keyfactory.generateSecret(myKeyspec);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("AESdemo", "no key factory support for PBEWITHSHAANDTWOFISH-CBC");
        } catch (InvalidKeySpecException ikse) {
            Log.e("AESdemo", "invalid key spec for PBEWITHSHAANDTWOFISH-CBC");
        }

        // This is our secret key. We could just save this to a file instead of
        // regenerating it
        // each time it is needed. But that file cannot be on the device (too
        // insecure). It could
        // be secure if we kept it on a server accessible through https.

        // byte[] skAsByteArray = sk.getEncoded();
        byte[] skAsByteArray;
        try {
            skAsByteArray = sKey.getBytes("ASCII");
            skforAES = new SecretKeySpec(skAsByteArray, "AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        IV = new IvParameterSpec(iv);
    }

    public String encrypt(byte[] plaintext) {
        byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintext);
        String base64_ciphertext = Base64Encoder.encode(ciphertext);
        return base64_ciphertext;
    }

    public String decrypt(String ciphertext_base64) {
        byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
        byte[] bytes = decrypt(CIPHERMODEPADDING, skforAES, IV,
                s);
        String decrypted = null;
        if (bytes != null) {
            decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV,
                    s));
            return decrypted;
        }

        return decrypted;
    }

    // Use this method if you want to add the padding manually
    // AES deals with messages in blocks of 16 bytes.
    // This method looks at the length of the message, and adds bytes at the end
    // so that the entire message is a multiple of 16 bytes.
    // the padding is a series of bytes, each set to the total bytes added (a
    // number in range 1..16).
    private byte[] addPadding(byte[] plain) {
        byte plainpad[] = null;
        int shortage = 16 - (plain.length % 16);
        // if already an exact multiple of 16, need to add another block of 16
        // bytes
        if (shortage == 0)
            shortage = 16;
        // reallocate array bigger to be exact multiple, adding shortage bits.
        plainpad = new byte[plain.length + shortage];
        for (int i = 0; i < plain.length; i++) {
            plainpad[i] = plain[i];
        }
        for (int i = plain.length; i < plain.length + shortage; i++) {
            plainpad[i] = (byte) shortage;
        }
        return plainpad;
    }

    // Use this method if you want to remove the padding manually
    // This method removes the padding bytes
    private byte[] dropPadding(byte[] plainpad) {
        byte plain[] = null;
        int drop = plainpad[plainpad.length - 1]; // last byte gives number of
        // bytes to drop

        // reallocate array smaller, dropping the pad bytes.
        plain = new byte[plainpad.length - drop];
        for (int i = 0; i < plain.length; i++) {
            plain[i] = plainpad[i];
            plainpad[i] = 0; // don't keep a copy of the decrypt
        }
        return plain;
    }

    private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                           byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.ENCRYPT_MODE, sk, IV);
            return c.doFinal(msg);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e("AESdemo", "no cipher getinstance support for " + cmp);
        } catch (NoSuchPaddingException nspe) {
            Log.e("AESdemo", "no cipher getinstance support for padding " + cmp);
        } catch (InvalidKeyException e) {
            Log.e("AESdemo", "invalid key exception");
        } catch (InvalidAlgorithmParameterException e) {
            Log.e("AESdemo", "invalid algorithm parameter exception");
        } catch (IllegalBlockSizeException e) {
            Log.e("AESdemo", "illegal block size exception");
        } catch (BadPaddingException e) {
            Log.e("AESdemo", "bad padding exception");
        }
        return null;
    }

    private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                           byte[] ciphertext) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.DECRYPT_MODE, sk, IV);
            return c.doFinal(ciphertext);
        } catch (NoSuchAlgorithmException nsae) {
            Log.i("AES", "no cipher getinstance support for " + cmp);
        } catch (NoSuchPaddingException nspe) {
            Log.i("AES", "no cipher getinstance support for padding " + cmp);
        } catch (InvalidKeyException e) {
            Log.i("AES", "invalid key exception");
        } catch (InvalidAlgorithmParameterException e) {
            Log.i("AES", "invalid algorithm parameter exception");
        } catch (IllegalBlockSizeException e) {
            Log.i("AES", "illegal block size exception");
        } catch (BadPaddingException e) {
            Log.i("AES", "bad padding exception");
            e.printStackTrace();
        }
        return null;
    }

    public String get16BitKey() {
        sKey = sKey_.substring(0, 15);
        return sKey;
    }
}
