/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: wangchaochao3i
 * Created: 2017/03/20
 * Description:
 *
 */

package com.inter3i.base.mapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.inter3i.base.DataType;
import com.inter3i.base.ParamHelper;
import com.inter3i.base.ValidateUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataMappingEngine {
    private final String TARGET_DATA_TYPE_JSON = "jons";
    private final String TARGET_DATA_TYPE_MAP = "map";

    private Logger logger;

    private UserDefinedFunctionCenter funcExecuter = new UserDefinedFunctionCenter();


    private List<FieldMappingRule> rules;
    private Map content;

    public DataMappingEngine(List<FieldMappingRule> rules, final Logger logger) {
        this.rules = rules;
        this.logger = logger;
    }

    public DataMappingEngine(List<FieldMappingRule> rules) {
    }

    public Object mapping(Object source, Object target) throws JSONException, NoSuchMethodException {
        FieldMappingRule mappingRule = null;
        try {
            if (!(source instanceof JSONObject) && !(source instanceof Map)) {
                throw new RuntimeException("unsupport sourceData type when value mapping:[" + source.getClass().getSimpleName() + "].");
            }

            if (!(target instanceof JSONObject) && !(target instanceof Map)) {
                throw new RuntimeException("unsupport targetData type when value mapping:[" + target.getClass().getSimpleName() + "].");
            }

            this.content = new HashMap(4);

            Object localSource = null;
            Object localTarget = null;

            for (int i = 0; i < rules.size(); i++) {
                mappingRule = rules.get(i);

                //1.进行规则映射时候，会根据 fromPathExps 获取[原始值]，如果获取不到则获取[默认值]
                localSource = getDataValueBy(mappingRule.getSourceValueType(), source, target, content);
                localTarget = getDataValueBy(mappingRule.getTargetValueType(), source, target, content);

                switch (mappingRule.getMappingType()) {
                    case 1:
                        //映射规则 -- 字段映射(默认)
                        this.valueMapping(mappingRule, localSource, localTarget, false);
                        break;
                    case 2:
                        //映射规则 -- 字段映射[脚本,functions]
                        this.valueMapping(mappingRule, localSource, localTarget, true);
                        break;
                    case 3:
                        //映射规则 -- 直接赋值
                        break;
                    case 4:
                        //映射规则 -- 直接赋值[脚本,functions]
                        break;
                    default:
                        throw new RuntimeException("Unsupportted mapping rule type:[" + mappingRule.getMappingType() + "].");
                }
            }
            return target;
        } catch (RuntimeException e) {
            logger.error("data mapping exception:[" + e.getMessage() + "] MappingRule:[" + mappingRule + "].", e);
            throw new RuntimeException("data mapping exception:[" + e.getMessage() + "] MappingRule:[" + mappingRule + "].", e);
        } finally {
        }
    }


    public Object mapping(Object source) throws JSONException, NoSuchMethodException {
        return mapping(source, new JSONObject());
    }

    public Object mapping(Object source, String targetType) throws JSONException, NoSuchMethodException {
        if (targetType.equalsIgnoreCase(TARGET_DATA_TYPE_MAP)) {
            return mapping(source, new HashMap());
        } else if (targetType.equalsIgnoreCase(TARGET_DATA_TYPE_JSON)) {
            return mapping(source, new JSONObject());
        } else {
            throw new RuntimeException("unsupport targetData type when value mapping:[" + targetType + "].");
        }
    }

    private void valueMapping(FieldMappingRule mappingRule, Object source, Object target, boolean isFunc) throws JSONException, NoSuchMethodException {
        Object value = null;
        if (isFunc) {
            //TODO
            //excute function
            FieldMappingRule.MappingFuncConfig funcConfig = null;
            if (mappingRule.getSourceFuncConfig() != null) {
                //执行source function
                funcConfig = mappingRule.getSourceFuncConfig();

                List<String> paramPaths = funcConfig.getParamName();
                List<Object> paramValues = new ArrayList<Object>();


                Object paramValue = null;
                for (int i = 0; i < paramPaths.size(); i++) {
                    //获取第i个参数
                    //判断类型 必要时 做强制类型转化
                    paramValue = ParamHelper.ParamGetterHelper.getValueBy(paramPaths.get(i), source);

                    if (!ValidateUtils.isNullOrEmpt(funcConfig.getTrasfRuleList().get(i))) {
                        paramValue = transfValueBy(funcConfig.getTrasfRuleList().get(i), paramValue);
                    }
                    paramValues.add(i, paramValue);
                }

                //判断是否需要出来List
                boolean isMutiValue = false;
                int mutiValueSize = -1;

                List<DataType> transfTypes = funcConfig.getTransfDataTypes();
                boolean isNedTras = !ValidateUtils.isNullOrEmpt(transfTypes);
                int invokTimes = ParamHelper.getSize4InnerArray(paramValues.get(0));

                //转化每个参数的类型
                for (int i = 0; i < funcConfig.getParamDataTypes().size(); i++) {
                    //处理每一个参数
                    if (isNedTras && !ValidateUtils.isNullOrEmpt(transfTypes.get(i))) {
                        //
                        Object originalValue = paramValues.get(i);

                        if (ParamHelper.isInnerSupportArray(originalValue)) {
                            isMutiValue = true;
                            //将list中的每个值进行类型转化
                            Object trasfResult = transefeDataType(originalValue, funcConfig.getParamDataTypes().get(i), transfTypes.get(i));
                            paramValues.set(i, trasfResult);
                        } else {
                            //类型转化
                            Object trasfResult = transefeDataType(originalValue, funcConfig.getParamDataTypes().get(i), transfTypes.get(i));
                            paramValues.set(i, trasfResult);
                        }
                    }
                }

                if (isMutiValue) {
                    value = ParamHelper.createResultValueBy(paramValues.get(0));

                    //执行函数
                    Object[] requesValues = new Object[paramValues.size()];
                    Object tempValue = null;
                    for (int i = 0; i < invokTimes; i++) {
                        for (int j = 0; j < paramValues.size(); j++) {
                            requesValues[j] = ParamHelper.getValueByIdx(paramValues.get(j), i);
                        }
                        tempValue = funcExecuter.invokeByName(funcConfig.getFunctionName(), requesValues);
                        ParamHelper.setValueByIdx(value, i, tempValue);
                    }
                } else {
                    value = funcExecuter.invokeByName(funcConfig.getFunctionName(), paramValues.toArray());
                }

            } else {
                //source 函数为空
            }
        } else {
            if (ValidateUtils.isNullOrEmpt(mappingRule.getFromPathExps()) && ValidateUtils.isNullOrEmpt(mappingRule.getFormDefaultValue())) {
                //没有配置 sourcePathExpress 也没有 设置默认值
                throw new RuntimeException("cannot get from value! [both fromPathExps and formValue is null!] ");
            }

            value = ParamHelper.ParamGetterHelper.getValueBy(mappingRule.getFromPathExps(), source);
            if (!ValidateUtils.isNullOrEmpt(mappingRule.getTrasfRules())) {//按照值转化规则进行值转化
                value = transfValueBy(mappingRule.getTrasfRules(), value);
            }
            //判断类型 必要时 做强制类型转化
            if ((!ValidateUtils.isNullOrEmpt(mappingRule.getFromDataType())) && (!ValidateUtils.isNullOrEmpt(mappingRule.getToDataType())) && mappingRule.getFromDataType() != mappingRule.getToDataType()) {
                //
                value = transefeDataType(value, mappingRule.getFromDataType(), mappingRule.getToDataType());
            }
        }

        //set value to target data
        if (value != null) {
            ParamHelper.ParamSetterHelper.setValueBy(mappingRule.getToPathExps(), target, value);
        }
    }

    private Object transfValueBy(final Map trasfRules, Object sourceValue) {
        if (ParamHelper.isInnerSupportArray(sourceValue)) {
            if (!(sourceValue instanceof JSONArray)) {
                List result = new ArrayList(ParamHelper.getSize4InnerArray(sourceValue));
                List originalValues = (List) sourceValue;
                for (int i = 0; i < originalValues.size(); i++) {
                    result.add(i, transfValueBy(trasfRules, originalValues.get(i)));
                }
                return result;
            } else {
                JSONArray result = new JSONArray(ParamHelper.getSize4InnerArray(sourceValue));
                JSONArray originalValues = (JSONArray) sourceValue;
                for (int i = 0; i < originalValues.size(); i++) {
                    result.add(i, transfValueBy(trasfRules, originalValues.get(i)));
                }
                return result;
            }
        } else {
            if (trasfRules.containsKey(sourceValue)) {
                return trasfRules.get(sourceValue);
            }
            if (trasfRules.containsKey(FieldMappingRule.TR_DEFAULT_VALUE_KEY)) {
                return trasfRules.get(FieldMappingRule.TR_DEFAULT_VALUE_KEY);
            }
        }
        return null;
    }

    private Object getDataValueBy(final int sourceType, Object source, Object target, Object content) {
        //获取 源数据  和 目标数据
        if (sourceType == FieldMappingRule.DS_TYPE_SOURCE) {
            return source;
        } else if (sourceType == FieldMappingRule.DS_TYPE_TARGET) {
            return target;
        } else if (sourceType == FieldMappingRule.DS_TYPE_CONTENT) {
            return content;
        } else {
            throw new RuntimeException("get dataValue by type excption,unsupport dataSourceType:[" + sourceType + "].");
        }
    }


    /**
     * 将参数值进行强制类型转化
     *
     * @param value
     * @param fromDataType
     * @param toDataType
     * @return
     */
    private Object transefeDataType(Object value, DataType fromDataType, DataType toDataType) throws JSONException {
        if (ValidateUtils.isNullOrEmpt(value)) {
            throw new RuntimeException("transefeDataType exception,value is null or empty!");
        }

        //将String 类型转化为对应类型
        if (ParamHelper.isInnerSupportArray(value)) {
            int size = ParamHelper.getSize4InnerArray(value);
            Object tempValue = null;
            Object resultValue = null;

            if (value instanceof JSONArray) {
                JSONArray result = (JSONArray) value;
                JSONArray transferResult = new JSONArray(result.size());
                for (int i = 0; i < size; i++) {
                    tempValue = result.get(i);
                    if (ValidateUtils.isNullOrEmpt(tempValue)) {
                        transferResult.add(i, tempValue);
                        continue;
                    }
                    DataType dataType = DataType.fromString(tempValue.getClass().getSimpleName());
                    resultValue = dataType.toTargetDatType(toDataType, tempValue);
                    transferResult.add(i, resultValue);
                }
                return transferResult;
            } else {
                List result = (List) value;
                ArrayList transferResult = new ArrayList(result.size());
                for (int i = 0; i < size; i++) {
                    tempValue = result.get(i);
                    DataType dataType = DataType.fromString(tempValue.getClass().getSimpleName());
                    resultValue = dataType.toTargetDatType(toDataType, tempValue);
                    transferResult.add(i, resultValue);
                }
                return transferResult;
            }
        }

        DataType dataType = DataType.fromString(value.getClass().getSimpleName());
        Object result = dataType.toTargetDatType(toDataType, value);
        return result;

        /*if (fromDataType == DataType.STRING) {
            result = DataType.STRING.toTargetDatType(toDataType, value);
        } else if (fromDataType == DataType.INT) {
            result = DataType.INT.toTargetDatType(toDataType, value);
        } else if (fromDataType == DataType.INT) {
            result = DataType.INT.toTargetDatType(toDataType, value);
        }*/
    }
}
