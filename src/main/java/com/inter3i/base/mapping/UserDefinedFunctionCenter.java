/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: Administrator
 * Created: 2017/06/07
 * Description:
 *
 */

package com.inter3i.base.mapping;

import com.inter3i.base.Base64Util;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * DESCRIPTION : 
 * USER : zhouhui
 * DATE : 2017/3/1 11:50
 */
@Resource
public class UserDefinedFunctionCenter {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

    /**
     * 获得字符串yyyy格式的年份，从long类型的时间戳
     *
     * @param time 时间戳
     * @return 字符串
     */
    public String getYearStringFromTimeLong(long time) {
        Date date = new Date(time);
        return sdf.format(date);
    }

    public String generateGuid(String url, Integer floor, Integer paragraphId) {
        String guidStr = url + floor + paragraphId;
        String result = Base64Util.encode(guidStr, "utf8");
        return result;
    }


    public String generateRefKey(String url, Integer floor, Integer paragraphId) {
        if (floor == 0) {
            return null;
        }
        if (paragraphId != 0) {
            throw new RuntimeException("当前文章的段落号不为0");
        }
        String guidStr = url + 0 + 0;
        String result = Base64Util.encode(guidStr, "utf8");
        return result;
    }

    /**
     * 调用转换函数
     *
     * @param methodName 方法名
     * @param params     参数
     * @return 结果值
     */
    public Object invokeByName(String methodName, Object[] params) throws NoSuchMethodException {
        Class cls = com.inter3i.base.mapping.UserDefinedFunctionCenter.class;
        Object result = null;

        Class[] paramstype = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramstype[i] = params[i].getClass();
        }
        Method method = cls.getMethod(methodName, paramstype);
        try {
            result = method.invoke(this, params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }
}
