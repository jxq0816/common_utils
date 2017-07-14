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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.nio.charset.Charset;

public class HttpUtils {
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_HEAD = "HEAD";
    public static final String HTTP_METHOD_OPTIONS = "OPTIONS";
    public static final String HTTP_METHOD_PUT = "PUT";
    public static final String HTTP_METHOD_DELETE = "DELETE";
    public static final String HTTP_METHOD_TRACE = "TRACE";

    public static final String CHARSET_UTF8 = "utf8";


    public static final int TCP_PARAM_TIME_OUT_DEFAULT = 5 * 60;

    // 默认类型:表单
   /* public static final String CONTENT_TYPE_Form_URLENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_TEXT_XML_UTF8 = "text/xml;charset=utf8";
    public static final String CONTENT_TYPE_JSON_UTF8 = "application/json;charset=utf8";*/

    public static final String CONTENT_TYPE_TEXT_XML = "text/xml";
    public static final String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";

    public static final String HTTP_PROTOCAL_PREFIX = "http://";

    static {
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
            @Override
            public URLStreamHandler createURLStreamHandler(String protocol) {
                return new sun.net.www.protocol.http.Handler();
            }
        });
    }

    public static String executeGet(String charset, String requstUrl, int timeout) {
        if (timeout <= 0) {
            timeout = TCP_PARAM_TIME_OUT_DEFAULT;
        }
        OutputStream outputStream = null;
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(null, requstUrl, new sun.net.www.protocol.http.Handler());
            urlConnection = (HttpURLConnection) url.openConnection();
            // com.sun.net.ssl.internal.www.protocol.https.Handler()
            urlConnection.setReadTimeout(timeout);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(HTTP_METHOD_GET);

            urlConnection.setRequestProperty("Content-type", CONTENT_TYPE_TEXT_XML + ";charset = " + charset);
            urlConnection.addRequestProperty("http.keepAlive", "false");
            // urlConnection.addRequestProperty("Content-Length",
            // String.valueOf(requestData.getBytes(charset).length));
            urlConnection.addRequestProperty("Connection", "Close");
            urlConnection.connect();

            int respHttpCode = urlConnection.getResponseCode();

            if (respHttpCode != HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getErrorStream();
                String errorMsg = null;
                if (null != inputStream) {
                    byte[] respData = Inter3iIOUtils.copyToByteArray(inputStream);
                    errorMsg = new String(respData, charset);
                }
                throw new RuntimeException("Illegal httpStatusCode '" + respHttpCode + "' for url '" + requstUrl
                        + "' ErrorMsg:[" + errorMsg + "].");
            } else {
                inputStream = urlConnection.getInputStream();
                byte[] respData = Inter3iIOUtils.copyToByteArray(inputStream);
                if (charset.toLowerCase().equals("utf8") || charset.toLowerCase().equals("utf-8")) {
                    return Inter3iIOUtils.removeHeader4UTF8(respData, charset);
                }
                return new String(respData, Charset.forName(charset));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("HttpRequestSender excute Http post request exception, cause:[" + e.getMessage()
                    + "].", e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
    }

    public static String executePost(String requestData, String charset, String requstUrl, int timeout,
                                     String contentType) {
        if (timeout <= 0) {
            timeout = TCP_PARAM_TIME_OUT_DEFAULT;
        }

        OutputStream outputStream = null;
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(null, requstUrl, new sun.net.www.protocol.http.Handler());
            urlConnection = (HttpURLConnection) url.openConnection();
            // com.sun.net.ssl.internal.www.protocol.https.Handler()

            urlConnection.setReadTimeout(timeout);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(HTTP_METHOD_POST);

            if (null != contentType && contentType.length() > 0) {
                urlConnection.setRequestProperty("Content-type", contentType);
            } else {
                //默认
                urlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            }
            urlConnection.addRequestProperty("http.keepAlive", "false");

            if (null != requestData && requestData.length() > 0) {
                urlConnection
                        .addRequestProperty("Content-Length", String.valueOf(requestData.getBytes(charset).length));
            }
            urlConnection.addRequestProperty("Connection", "Close");

            urlConnection.connect();
            outputStream = urlConnection.getOutputStream();

            if (null != requestData && requestData.length() > 0) {
                outputStream.write(requestData.getBytes(charset));
            }
            outputStream.flush();

            int respHttpCode = urlConnection.getResponseCode();

            if (respHttpCode != HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getErrorStream();

                String nullInputMsg = "";
                if (inputStream == null) {
                    inputStream = urlConnection.getInputStream();
                    nullInputMsg = "getErrorStream is null";
                }

                byte[] respData = null;
                if (inputStream != null) {
                    respData = Inter3iIOUtils.copyToByteArray(inputStream);
                } else {
                    nullInputMsg += "getInputStream is null.";
                    respData = nullInputMsg.getBytes();
                }
                String errorMsg = new String(respData, charset);
                throw new RuntimeException("Illegal httpStatusCode '" + respHttpCode + "' for url '" + requstUrl
                        + "' ErrorMsg:[" + errorMsg + "]. Data: " + requestData);
            } else {
                inputStream = urlConnection.getInputStream();
                byte[] respData = Inter3iIOUtils.copyToByteArray(inputStream);
                if (charset.toLowerCase().equals("utf8") || charset.toLowerCase().equals("utf-8")) {
                    return Inter3iIOUtils.removeHeader4UTF8(respData, charset);
                }
                return new String(respData, Charset.forName(charset));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("HttpRequestSender excute Http post request exception, cause:[" + e.getMessage()
                    + "].", e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
    }
}
