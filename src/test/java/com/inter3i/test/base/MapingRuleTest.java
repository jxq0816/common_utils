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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inter3i.base.DataType;
import com.inter3i.base.mapping.DataMappingEngine;
import com.inter3i.base.mapping.FieldMappingRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapingRuleTest {
    public static void main(String[] args) throws NoSuchMethodException {
        //
        JSONObject sourceData = new JSONObject();
        JSONArray students = new JSONArray();
        sourceData.put("students", students);

        JSONObject sudentd1 = new JSONObject();
        sudentd1.put("name", "张三");
        sudentd1.put("age", "30");
        sudentd1.put("sex", "男");
        students.add(0, sudentd1);

        JSONObject sudentd2 = new JSONObject();
        sudentd2.put("name", "李四");
        sudentd2.put("age", "21");
        sudentd2.put("sex", "女");
        students.add(1, sudentd2);

        JSONObject sudentd3 = new JSONObject();
        sudentd3.put("name", "王五");
        sudentd3.put("age", "45");
        sudentd3.put("sex", "男");
        students.add(2, sudentd3);

        List fieldMappings = new ArrayList();

        FieldMappingRule fieldMappingRule = new FieldMappingRule();
        fieldMappingRule.setFromPathExps("students[all].name");
        fieldMappingRule.setToPathExps("datas[apd].name_aline");
        fieldMappings.add(fieldMappingRule);

        //
        FieldMappingRule fieldMappingRule2 = new FieldMappingRule();
        fieldMappingRule2.setFromPathExps("students[all].age");
        fieldMappingRule2.setToPathExps("datas[all].age1");
        fieldMappingRule2.setFromDataType(DataType.fromString("string"));
        fieldMappingRule2.setToDataType(DataType.fromString("int"));
        fieldMappings.add(fieldMappingRule2);


        FieldMappingRule fieldMappingRule3 = new FieldMappingRule(FieldMappingRule.DS_TYPE_SOURCE, FieldMappingRule.DS_TYPE_SOURCE);
        fieldMappingRule3.setFromPathExps("students[all].sex");
        fieldMappingRule3.setToPathExps("students[all].sex_int_str");
        Map trasfRules = new HashMap();
        trasfRules.put("男", "1");
        trasfRules.put("女", "2");
        fieldMappingRule3.setTrasfRules(trasfRules);
        fieldMappings.add(fieldMappingRule3);

        //生成guid
        FieldMappingRule fieldMappingRule4 = new FieldMappingRule(FieldMappingRule.MT_CONTEXT_MAP_FUNC);
        fieldMappingRule4.setToPathExps("datas[all].guid");
        FieldMappingRule.MappingFuncConfig funcConfig = new FieldMappingRule.MappingFuncConfig();
        fieldMappingRule4.setSourceFuncConfig(funcConfig);

        funcConfig.setFunctionName("generateGuid");

        List sourceDataType = new ArrayList();
        sourceDataType.add(null);
        sourceDataType.add(DataType.fromString("string"));
        sourceDataType.add(DataType.fromString("string"));
        funcConfig.setParamDataTypes(sourceDataType);

        List targetDataType = new ArrayList();
        targetDataType.add(null);
        targetDataType.add(DataType.fromString("int"));
        targetDataType.add(DataType.fromString("int"));
        funcConfig.setTransfDataTypes(targetDataType);
        fieldMappings.add(fieldMappingRule4);

        List sourceParamPaths = new ArrayList();
        sourceParamPaths.add("students[all].name");
        sourceParamPaths.add("students[all].age");
        sourceParamPaths.add("students[all].sex_int_str");
        funcConfig.setParamName(sourceParamPaths);

        List trasfList = new ArrayList();
        Map nameTrasRule = new HashMap();
        nameTrasRule.put("张三", "San.zhang");
        nameTrasRule.put("李四", "Si.li");
        nameTrasRule.put("王五", "Wu.wang");

        trasfList.add(nameTrasRule);
        trasfList.add(null);
        trasfList.add(null);
        funcConfig.setTrasfRuleList(trasfList);

        DataMappingEngine dataMappingEngine = new DataMappingEngine(fieldMappings);
        JSONObject result = new JSONObject();
        dataMappingEngine.mapping(sourceData, result);
    }
}
