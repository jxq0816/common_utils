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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 平台专用的事件格式化函数(提升性能)
 */
public class SystemDateFormat {

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    private static Calendar calendar = Calendar.getInstance();
    private static Lock lock = new ReentrantLock();

    /**
     * 时间格式化(数据库记录)
     *
     * @param
     * @return 1 yyyy MM dd  HH mm ss SSS
     */
    public static int[] formatDate2IntArray(Date date) {
        int year, mon, day, hour, min, sec, milli, wednesday;
        int[] result = new int[8];
        try {
            lock.lock();
            calendar.setTime(date);

            year = calendar.get(Calendar.YEAR);
            mon = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);
            sec = calendar.get(Calendar.SECOND);
            milli = calendar.get(Calendar.MILLISECOND);
            wednesday = calendar.get(Calendar.WEDNESDAY);

            result[0] = year;
            result[1] = mon;
            result[2] = day;
            result[3] = hour;
            result[4] = min;
            result[5] = sec;
            result[6] = milli;
            result[7] = wednesday;
        } finally {
            lock.unlock();
        }
        return result;
    }

    public static int[] formatDate2IntArray(final long date) {
        return formatDate2IntArray(new Date(date));
    }

    public static String formatDateTime2String(long timeStamp) {
        return formatDateTime2String(new Date(timeStamp));
    }

    public static String formatDateTime2String(Date date) {
        return formatDateTime2String(date, '-', ' ', ':', '.');
    }

    public static String formatDateTime2String(Date date, char dataSep, char timeSep) {
        return formatDateTime2String(date, dataSep, ' ', timeSep);
    }

    public static String formatDateTime2String(Date date, char dataSep, char dataTimeSep, char timeSep) {
        return formatDateTime2String(date, dataSep, dataTimeSep, timeSep, '.');
    }

    /**
     * 只获取date部分
     *
     * @param date
     * @param dataSep
     * @return
     */
    public static String formatDate2String(Date date, char dataSep) {
        int[] intResult = formatDate2IntArray(date);
        String result = geneDataStr(intResult, dataSep);
        return result;
    }

    private static String geneDataStr(final int[] intResult, final char dataSep) {
        String result = intResult[0] < 10 ? "0" + Integer.toString(intResult[0]) : Integer.toString(intResult[0]);
        result += dataSep + (intResult[1] < 10 ? "0" + Integer.toString(intResult[1]) : Integer.toString(intResult[1]));
        result += dataSep + (intResult[2] < 10 ? "0" + Integer.toString(intResult[2]) : Integer.toString(intResult[2]));
        return result;
    }

    public static String formatDateTime2String(Date date, char dataSep, char dataTimeSep, char timeSep, char milliSep) {
        int[] intResult = formatDate2IntArray(date);

        // 年月日
        /*String result = intResult[0] < 10 ? "0" + Integer.toString(intResult[0]) : Integer.toString(intResult[0]);
        result += dataSep + (intResult[1] < 10 ? "0" + Integer.toString(intResult[1]) : Integer.toString(intResult[1]));
        result += dataSep + (intResult[2] < 10 ? "0" + Integer.toString(intResult[2]) : Integer.toString(intResult[2]));*/
        String result = geneDataStr(intResult, dataSep);

        result += dataTimeSep;

        // 时分秒毫秒
        result += (intResult[3] < 10 ? "0" + Integer.toString(intResult[3]) : Integer.toString(intResult[3]));
        result += timeSep + (intResult[4] < 10 ? "0" + Integer.toString(intResult[4]) : Integer.toString(intResult[4]));
        result += timeSep + (intResult[5] < 10 ? "0" + Integer.toString(intResult[5]) : Integer.toString(intResult[5]));

        result += milliSep;
        if (intResult[6] < 10) {
            result += "00" + Integer.toString(intResult[6]);
        } else if (intResult[6] < 100) {
            result += "0" + Integer.toString(intResult[6]);
        }
        return result;
    }

    /**
     * 获取当前时间，用于设置事件开始、结束时间
     *
     * @return
     */
    public static String getCurrentDatatimeFormatString() {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) Toolkit.getDateFormat(DATE_FORMAT_PATTERN);
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDatatimeFormatString(long currentTime) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) Toolkit.getDateFormat(DATE_FORMAT_PATTERN);
        return simpleDateFormat.format(new Date(currentTime));
    }

    public static String getCurrentDatatimeFormatString(String pattern) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) Toolkit.getDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    public static String getCurrentDatatimeFormatString(long currentTime, String pattern) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) Toolkit.getDateFormat(pattern);
        return simpleDateFormat.format(new Date(currentTime));
    }

    public static long parseDate(final String dateStr, String patternStr) {
        try {
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) Toolkit.getDateFormat(patternStr);
            return simpleDateFormat.parse(dateStr).getTime();
        } catch (ParseException e) {
            throw new RuntimeException("parseDate string excption:[" + dateStr + "].");
        }
    }

    //不正常的日期格式
    public static long parseDate(String dateStr) {
        dateStr = dateStr.trim();


        SimpleDateFormat simpleDateFormat = null;
        String tempStr = DATE_FORMAT_PATTERN;
        try {
            if (dateStr.length() <= "yyyy-MM-dd".length()) {
                tempStr = "yyyy-MM-dd";
                simpleDateFormat = (SimpleDateFormat) Toolkit.getDateFormat(tempStr);
            } else if (dateStr.length() == DATE_FORMAT_PATTERN.length()) {
                simpleDateFormat = (SimpleDateFormat) Toolkit.getDateFormat(tempStr);
            } else {
                //处理得到的是毫秒，前台处理用的是秒，所以除了1000
                return formatDateTimeStrWithRegex(dateStr) / 1000;
            }
            if (simpleDateFormat == null) {
                throw new RuntimeException("不支持的日期格式:[" + dateStr + "].");
            }
            //处理得到的是毫秒，前台处理用的是秒，所以除了1000
            return simpleDateFormat.parse(dateStr).getTime() / 1000;
        } catch (ParseException e) {
            throw new RuntimeException("parseDate string excption:[" + dateStr + "].");
        }
    }


    private static String DATE_MATCH_REG_0 = "^\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}";
    private static Pattern PATTERN_0 = Pattern.compile(DATE_MATCH_REG_0);
    private static String DATE_TMPL_0 = "yyyy-MM-dd HH:mm:ss";

    private static String DATE_MATCH_REG_1 = "^\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}";
    private static Pattern PATTERN_1 = Pattern.compile(DATE_MATCH_REG_1);
    private static String DATE_TMPL_1 = "yyyy-MM-dd HH:mm";

    private static String DATE_MATCH_REG_2 = "^\\d{4}\\-\\d{1,2} \\d{1,2}:\\d{1,2}";
    private static Pattern PATTERN_2 = Pattern.compile(DATE_MATCH_REG_2);
    private static String DATE_TMPL_2 = "yyyy-MM HH:mm";

    private static String DATE_MATCH_REG_3 = "^\\d{4}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}";
    private static Pattern PATTERN_3 = Pattern.compile(DATE_MATCH_REG_3);
    private static String DATE_TMPL_3 = "yyyy-MM HH:mm:ss";

    private static String DATE_MATCH_REG_4 = "^\\d{4}\\-\\d{1,2}\\-\\d{1,2}";
    private static Pattern PATTERN_4 = Pattern.compile(DATE_MATCH_REG_4);
    private static String DATE_TMPL_4 = "yyyy-MM-dd";

    private static String DATE_MATCH_REG_5 = "^\\d{4}\\-\\d{1,2}";
    private static Pattern PATTERN_5 = Pattern.compile(DATE_MATCH_REG_5);
    private static String DATE_TMPL_5 = "yyyy-MM";

    //需要补全的时间格式
    private static String DATE_MATCH_REG_6 = "^(\\d{0,2})\\-?(\\d{1,2})\\-(\\d{1,2}) ?(\\d{0,2}):?(\\d{0,2}):?(\\d{0,2})";
    private static Pattern PATTERN_6 = Pattern.compile(DATE_MATCH_REG_6);

    private static Pattern[] ALLPATTERN = new Pattern[6];
    private static String[] FORMAT_TMPL = new String[6];

    static {
        ALLPATTERN[0] = PATTERN_0;
        ALLPATTERN[1] = PATTERN_1;
        ALLPATTERN[2] = PATTERN_2;
        ALLPATTERN[3] = PATTERN_3;
        ALLPATTERN[4] = PATTERN_4;
        ALLPATTERN[5] = PATTERN_5;

        FORMAT_TMPL[0] = DATE_TMPL_0;
        FORMAT_TMPL[1] = DATE_TMPL_1;
        FORMAT_TMPL[2] = DATE_TMPL_2;
        FORMAT_TMPL[3] = DATE_TMPL_3;
        FORMAT_TMPL[4] = DATE_TMPL_4;
        FORMAT_TMPL[5] = DATE_TMPL_5;
    }


    public static long formatDateTimeStrWithRegex(String dateStr) throws ParseException {
        String tempStr = null;
        for (int i = 0; i < ALLPATTERN.length; i++) {
            if (isMatch(dateStr, ALLPATTERN[i])) {
                tempStr = FORMAT_TMPL[i];
                break;
            }
        }

        if (null == tempStr) {
            throw new RuntimeException("unsupportted date string:[" + dateStr + "].");
        }

        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) Toolkit.getDateFormat(tempStr);
        long timg = simpleDateFormat.parse(dateStr).getTime();

        //测试
//        SIMPLEDATEFORMAT_TMP = simpleDateFormat;
        return timg;
    }


    private static boolean isMatch(final String dateStr, final Pattern pattern) {
        Matcher matcher = pattern.matcher(dateStr);
        return matcher.matches();
    }


//    private static SimpleDateFormat SIMPLEDATEFORMAT_TMP = null;


    public static void main(String[] args) throws ParseException {
//        String dateMatchReg = "^\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}";
//        Pattern pattern = Pattern.compile(dateMatchReg);

//        String testDataStr = "2014-09-07 09:10:20";
//        String testDataStr = "2014-09-07 9:10:20";
//        String testDataStr = "2014-09-07 09:8:20";
//        String testDataStr = "2014-09-07 9:1:20";

        //

//        String testDataStr = "2014-09 9:10";
//        String testDataStr = "2014-09-07 9:8";
//        String testDataStr = "2014-09 9:8";
//        String testDataStr = "2014-09 9:8:10";

        String testDataStr = "2014-09";

        //long timg = formatDateTimeStrWithRegex(testDataStr);

        //System.out.println("" + timg);

        Matcher result = PATTERN_6.matcher(testDataStr);
        if (result.matches()) {
            System.out.println("sdfasdf");
            int groupCount = result.groupCount();
            String groupi = null;
            for (int i = 1; i < groupCount; i++) {
                groupi = result.group(i);
                System.out.println("group:[" + i + "] value:[" + groupi + "]");
            }
        }


        StringBuffer sb = new StringBuffer();
        int idx = 0;
        while (result.find()) {
            result.appendReplacement(sb, "xx");
            idx++;
        }
        result.appendTail(sb);

        //out: Garfield really needs some coffee.
        System.out.println(sb.toString());

//        String result = SIMPLEDATEFORMAT_TMP.format(new Date(timg));
//        System.out.println(result);
    }
}
