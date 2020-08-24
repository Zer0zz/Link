package com.leconssoft.common.baseUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5
 * @author liu
 */
public class MD5
{
    
    /**
     * 使用MD5对原文进行加�?
     * @param value 原文
     * @return MD5加密�?
     */
    public static String digest(String value)
    {
        StringBuilder sb = null;
        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] result = digest.digest(value.getBytes());
            sb = new StringBuilder();
            for (byte b : result)
            {
                String hexString = Integer.toHexString(b & 0xFF);
                if (hexString.length() == 1)
                {
                    sb.append("0" + hexString);// 0~F
                }
                else
                {
                    sb.append(hexString);
                }
            }
        } catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * Encodes a string
     *
     * @param str String to encode
     * @return Encoded String
     * @throws Exception
     */
    public static String crypt(String str)
    {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        StringBuffer hexString = new StringBuffer();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] hash = md.digest();

            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                }
                else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hexString.toString().toUpperCase();
    }






    private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F' };


    protected static MessageDigest messagedigest = null;
    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            nsaex.printStackTrace();
        }
    }


    /**
     * 判断文件的md5校验码是否与一个已知的md5码相匹配
     *
     * @param key 要校验的文件
     * @param md5key 已知的md5校验码
     * @return 是否匹配
     */
    public static boolean isMatch(File key, String md5key) {
        try {
            String s = md5sum(key);
            return s.equals(md5key);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 生成字符串的md5校验值
     *
     * @param str 要校验的字符串
     * @return md5校验值
     */
    public static String md5sum(String str) {
        return getMD5String(str.getBytes());
    }

    /**
     * 生成文件的md5校验值
     *
     * @param file 要校验的文件
     * @return md5校验值
     * @throws IOException
     */
    public static String md5sum(File file) throws IOException {
        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        fis.close();
        return bufferToHex(messagedigest.digest());
    }

    private static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
