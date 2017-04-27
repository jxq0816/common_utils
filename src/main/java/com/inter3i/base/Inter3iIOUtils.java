/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: wangchaochao
 * Created: 2017/04/10
 * Description:
 *
 */

package com.inter3i.base;

import java.io.*;
import java.nio.charset.Charset;

public class Inter3iIOUtils {
    private static int BUFFER_SIZE = 1024;

    /**
     * 从输入流中读取字节数组
     *
     * @param in 输入流
     * @return 字节数组
     * @throws IOException IOException
     */
    public static byte[] copyToByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy(in, out);
        return out.toByteArray();
    }

    /**
     * 将输入流中的数据读取并写入输出流，在次不关闭该流，应该在创建的地方进行关闭
     *
     * @param in
     * @param out
     * @return
     * @throws IOException
     */
    private static int copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

    /**
     * 将字节数组反序列化成对象
     *
     * @param bytes 字节数组
     * @return 反序列化后的对象
     */
    public static Serializable unmarshal(byte[] bytes) {
        try {
            ObjectInputStream ois = null;
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bis);
                return (Serializable) ois.readObject();
            } finally {
                if (ois != null) {
                    ois.close();
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException("Unmarshal byte array to object error.", e);
        }
    }

    /**
     * 将对象序列化成字节数组
     *
     * @param serializable 对象
     * @return 字节数组
     */
    public static byte[] marshal(Serializable serializable) {
        try {
            ObjectOutputStream oos = null;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(bos);
                oos.writeObject(serializable);
                oos.flush();
                return bos.toByteArray();
            } finally {
                if (oos != null) {
                    oos.close();
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException("Marshal object '" + serializable.getClass().getName()
                    + "' to byte array error.", e);
        }
    }

    public static String removeHeader4UTF8(byte[] respBytes, String charset) {
        byte[] respNew = new byte[respBytes.length - 3];
        if (respBytes[0] == 0xef && respBytes[1] == 0xbb && respBytes[2] == 0xbf) {
            System.arraycopy(respBytes, 3, respNew, 0, respBytes.length - 3);
            respBytes = respNew;
        }
        String respData = new String(respBytes, Charset.forName(charset));
        if (respData.equals("")) {
            throw new RuntimeException("removeHeader4UTF8 error,response is null!");
        }
        // 去除window中 utf8的php有bom
        if (((int) respData.charAt(0)) == 65279) {
            respData = respData.substring(1);
        } else if (((int) respData.charAt(0)) == 38168) {
            respData = respData.substring(1);
        } else {
            int ttt = ((int) respData.charAt(0));
        }
        return respData;
    }
}
