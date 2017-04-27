/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: Administrator
 * Created: 2017/04/20
 * Description:
 *
 */

package com.inter3i.base;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

public class Base64Util {

    /**
     * @param bytes
     * @return
     */
    public static byte[] decode(final byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }

    /**
     * @param encryptData
     * @param charset
     * @return
     */
    public static String decode(final String encryptData, String charset) {
        byte[] datas = Base64.decodeBase64(encryptData.getBytes(Charset.forName(charset)));
        return new String(datas, Charset.forName(charset));
    }

    public static String decode(final String encryptData) {
        return decode(encryptData, HttpUtils.CHARSET_UTF8);
    }

    /**
     * @param bytes
     * @return
     */
    public static String encode(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * @param dataStr
     * @param charset
     * @return
     */
    public static String encode(final String dataStr, String charset) {
        return new String(Base64.encodeBase64(dataStr.getBytes(Charset.forName(charset))), Charset.forName(charset));
    }

    public static String encode(final String dataStr) {
        return encode(dataStr, HttpUtils.CHARSET_UTF8);
    }

}
