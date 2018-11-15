package org.waykichain.testwicc;

import java.math.BigInteger;

public class Utils {
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

    /**
     * 10进制转16进制
     */
    public static String toHexString(String vals) {
        BigInteger v1 = new BigInteger(vals, 10);
        String amount = v1.toString(16);
        return amount;
    }
}
