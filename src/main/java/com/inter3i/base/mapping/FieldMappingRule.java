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


import com.inter3i.base.DataType;

import java.util.List;
import java.util.Map;

public class FieldMappingRule {

    //映射规则 -- 字段映射(默认)
    public static final int MT_CONTEXT_MAP = 1;
    //映射规则 -- 字段映射[脚本,functions]
    public static final int MT_CONTEXT_MAP_FUNC = 2;
    //映射规则 -- 直接赋值
    public static final int MT_CONTEXT_ASSIGN = 3;
    //映射规则 -- 值转换
    public static final int MT_CONTEXT_ASSIGN_FUNC = 4;

    public static final String TR_DEFAULT_VALUE_KEY = "DEFAULT_VALUE";

    private String fromPathExps;
    //如果没有配置 fromPathExps 则需要配置 formValue:[即缺省值]
    private Object formDefaultValue;

    private DataType fromDataType;

    private String toPathExps;//目标路径表达式
    private DataType toDataType;//目标值的类型 如果源值的数据类型和目标值的数据类型不相同 则需要进行类型转化 如果两个类型都没有配置 即使类型不一样 也不进行转化

    //private boolean isTransform;

    private boolean transfFun;//是否有转化函数

    //值转化规则
    private Map trasfRules;

    private int mappingType;

    /**
     * 使用该字段 来重定向 source 和 target<BR>
     * 在每个映射规则执行期间数据源总共有三种<BR>
     * 1.source(源数据)
     * 2.target(目标数据)
     * 3.content(所有映射规则执行期间的上下文)
     * 在进行字段映射时候，默认情况下是从 源数据中获取值 在根据 赋值表达式 将值赋值到 目标数据中<BR>
     * 但在一些特殊情况下并不是这样的，例如：从源数据中取值 经过转化 在赋值到上下文中 最后在赋值到目标数据中去
     * 下面这些字段用来配置这些特殊情况
     */
    public static final int DS_TYPE_SOURCE = 1;
    public static final int DS_TYPE_TARGET = 2;
    public static final int DS_TYPE_CONTENT = 3;

    private final int sourceValueType;
    private final int targetValueType;


    private MappingFuncConfig sourceFuncConfig;
    private MappingFuncConfig tgtFuncConfig;

    public FieldMappingRule() {
        mappingType = MT_CONTEXT_MAP;
        sourceValueType = DS_TYPE_SOURCE;
        targetValueType = DS_TYPE_TARGET;
    }

    public FieldMappingRule(final int mappingType) {
        this.mappingType = mappingType;
        this.sourceValueType = DS_TYPE_SOURCE;
        this.targetValueType = DS_TYPE_TARGET;
    }

    public FieldMappingRule(int sourceType, int targetType) {
        mappingType = MT_CONTEXT_MAP;
        sourceValueType = sourceType;
        targetValueType = targetType;
    }

    public FieldMappingRule(final String fromPathExps, final String toPathExps) {
        this.fromPathExps = fromPathExps;
        this.toPathExps = toPathExps;
        mappingType = MT_CONTEXT_MAP;

        sourceValueType = DS_TYPE_SOURCE;
        targetValueType = DS_TYPE_TARGET;
    }

    public Map getTrasfRules() {
        return trasfRules;
    }

    public void setTrasfRules(Map trasfRules) {
        this.trasfRules = trasfRules;
    }

    public String getFromPathExps() {
        return fromPathExps;
    }

    public void setFromPathExps(String fromPathExps) {
        this.fromPathExps = fromPathExps;
    }

    public Object getFormDefaultValue() {
        return formDefaultValue;
    }

    public void setFormDefaultValue(Object formDefaultValue) {
        this.formDefaultValue = formDefaultValue;
    }

    public String getToPathExps() {
        return toPathExps;
    }

    public void setToPathExps(String toPathExps) {
        this.toPathExps = toPathExps;
    }

    public DataType getFromDataType() {
        return fromDataType;
    }

    public void setFromDataType(DataType fromDataType) {
        this.fromDataType = fromDataType;
    }

    public DataType getToDataType() {
        return toDataType;
    }

    public void setToDataType(DataType toDataType) {
        this.toDataType = toDataType;
    }

/*    public boolean isTransform() {
        return isTransform;
    }

    public void setTransform(boolean transform) {
        isTransform = transform;
    }*/

    public boolean isTransfFun() {
        return transfFun;
    }

    public void setTransfFun(boolean transfFun) {
        this.transfFun = transfFun;
        mappingType = MT_CONTEXT_MAP_FUNC;
    }

    public int getSourceValueType() {
        return sourceValueType;
    }

    public int getTargetValueType() {
        return targetValueType;
    }

    public int getMappingType() {
        return mappingType;
    }

    public MappingFuncConfig getSourceFuncConfig() {
        return sourceFuncConfig;
    }

    public void setSourceFuncConfig(MappingFuncConfig sourceFuncConfig) {
        this.sourceFuncConfig = sourceFuncConfig;
    }

    public MappingFuncConfig getTgtFuncConfig() {
        return tgtFuncConfig;
    }

    public void setTgtFuncConfig(MappingFuncConfig tgtFuncConfig) {
        this.tgtFuncConfig = tgtFuncConfig;
    }

    @Override
    public String toString() {
        return "FieldMappingRule{" +
                "fromPathExps='" + fromPathExps + '\'' +
                ", toPathExps='" + toPathExps + '\'' +
                ", fromDataType=" + fromDataType +
                ", toDataType=" + toDataType +
                //", isTransform=" + isTransform +
                ", transfFun=" + transfFun +
                ", mappingType=" + mappingType +
                ", sourceValueType=" + sourceValueType +
                ", targetValueType=" + targetValueType +
                '}';
    }


    public static class MappingFuncConfig {
        private Map<String, Object> innerDefaultParams;
        private String functionName;
        private List<String> paramName; // all param path express

        private List<DataType> paramDataTypes;
        private List<DataType> transfDataTypes;

        private List<Map> trasfRuleList;

        public String getFunctionName() {
            return functionName;
        }

        public void setFunctionName(String functionName) {
            this.functionName = functionName;
        }

        public List<String> getParamName() {
            return paramName;
        }

        public void setParamName(List<String> paramName) {
            this.paramName = paramName;
        }

        public List<DataType> getParamDataTypes() {
            return paramDataTypes;
        }

        public void setParamDataTypes(List<DataType> paramDataTypes) {
            this.paramDataTypes = paramDataTypes;
        }

        public List<DataType> getTransfDataTypes() {
            return transfDataTypes;
        }

        public void setTransfDataTypes(List<DataType> transfDataTypes) {
            this.transfDataTypes = transfDataTypes;
        }

        public List<Map> getTrasfRuleList() {
            return trasfRuleList;
        }

        public void setTrasfRuleList(List<Map> trasfRuleList) {
            this.trasfRuleList = trasfRuleList;
        }

        public Map<String, Object> getInnerDefaultParams() {
            return innerDefaultParams;
        }

        public void setInnerDefaultParams(Map<String, Object> innerDefaultParams) {
            this.innerDefaultParams = innerDefaultParams;
        }
    }
}
