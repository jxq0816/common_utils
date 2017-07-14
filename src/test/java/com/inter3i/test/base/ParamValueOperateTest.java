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

package com.inter3i.test.base;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.inter3i.base.ParamHelper;

public class ParamValueOperateTest {

    public static void main(String[] args) throws JSONException {
        JSONObject testData = new JSONObject();
        JSONObject school = new JSONObject();
        testData.put("school", school);

        JSONObject employee = new JSONObject();
        testData.put("employee", employee);

        employee.put("name", "张三");
        employee.put("age", 20);
        employee.put("sex", "女");


        String express = "employee.name";
//        Object valuesss = ParamHelper.ParamGetterHelper.getValueBy(express, testData);
//        System.out.println(valuesss);


        express = "school.classess[1,3-9,10].name";
        ParamHelper.ParamSetterHelper.setValueBy(express, testData, "王超超");


        //Object value = ParamHelper.ParamGetterHelper.getValueBy(express, testData);
        System.out.println("ok, getValue by express:[" + express + "] is:[" + testData + "].");
//        String express = "school.classess[1,3-9,10].name";
        Object values = ParamHelper.ParamGetterHelper.getValueBy(express, testData);
        System.out.println(values);

//        List aa = new ArrayList();
//        aa.add(2, "s");
//        System.out.println(aa.get(2));

        /*JSONArray aa = new JSONArray();
        Object value = aa.get(2);
        System.out.println(aa.get(2));*/
    }
}
