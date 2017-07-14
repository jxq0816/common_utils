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

package com.inter3i.base.mapping.entity;

import com.inter3i.base.DataType;
import com.inter3i.base.ValidateUtils;
import com.inter3i.base.mapping.DataMappingEngine;
import com.inter3i.base.mapping.FieldMappingRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingRuleEntity {

    public static abstract class ValueTrasferConfig {
        public static final String TR_TYPE_DIRECT = "direct";//直接转化
        public static final String TR_TYPE_REGEXP = "regexp";//根据正则表达式转化

        private String type;

        ValueTrasferConfig(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /**
     * 直接转化 mapping 中根据key 来 取值
     */
    public static class DirectTransferConfig extends ValueTrasferConfig {
        public DirectTransferConfig() {
            super(ValueTrasferConfig.TR_TYPE_DIRECT);
        }

        private Map values;
        private String defaultValue;

        public Map getValues() {
            return values;
        }

        public void setValues(Map values) {
            this.values = values;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }
    }

    public static class SourceFieldConfig {
        private String fieldName;
        private String sourceType;
        private String targetType;

        //parameter 类型的节点 默认参数时候用
        private String value;

        private ValueTrasferConfig valueTrasferConfig;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getTargetType() {
            return targetType;
        }

        public void setTargetType(String targetType) {
            this.targetType = targetType;
        }

        public ValueTrasferConfig getValueTrasferConfig() {
            return valueTrasferConfig;
        }

        public void setValueTrasferConfig(ValueTrasferConfig valueTrasferConfig) {
            this.valueTrasferConfig = valueTrasferConfig;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class FuncConfig {
        private String funcName;
        private List<SourceFieldConfig> sourceFields;
        private Map<String, SourceFieldConfig> defaultParams;

        public String getFuncName() {
            return funcName;
        }

        public void setFuncName(String funcName) {
            this.funcName = funcName;
        }

        public List<SourceFieldConfig> getSourceFields() {
            return sourceFields;
        }

        public void setSourceFields(List<SourceFieldConfig> sourceFields) {
            this.sourceFields = sourceFields;
        }

        public Map getDefaultParams() {
            return defaultParams;
        }

        public void setDefaultParams(Map defaultParams) {
            this.defaultParams = defaultParams;
        }
    }

    public static class FieldMappingConfig {
        private String sourceName;
        private String sourceType;
        private String sourceDefaltValue;

        private String targetName;
        private String targetType;

        private ValueTrasferConfig valueTrasferConfig;
        private FuncConfig sourceFunc;
        private FuncConfig targetFunc;

        private String comment;

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getSourceDefaltValue() {
            return sourceDefaltValue;
        }

        public void setSourceDefaltValue(String sourceDefaltValue) {
            this.sourceDefaltValue = sourceDefaltValue;
        }

        public String getTargetName() {
            return targetName;
        }

        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }

        public String getTargetType() {
            return targetType;
        }

        public void setTargetType(String targetType) {
            this.targetType = targetType;
        }

        public ValueTrasferConfig getValueTrasferConfig() {
            return valueTrasferConfig;
        }

        public void setValueTrasferConfig(ValueTrasferConfig valueTrasferConfig) {
            this.valueTrasferConfig = valueTrasferConfig;
        }

        public FuncConfig getSourceFunc() {
            return sourceFunc;
        }

        public void setSourceFunc(FuncConfig sourceFunc) {
            this.sourceFunc = sourceFunc;
        }

        public FuncConfig getTargetFunc() {
            return targetFunc;
        }

        public void setTargetFunc(FuncConfig targetFunc) {
            this.targetFunc = targetFunc;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    private List<FieldMappingConfig> fieldMappingConfigs;

    public List<FieldMappingConfig> getFieldMappingConfigs() {
        return fieldMappingConfigs;
    }

    public void setFieldMappingConfigs(List<FieldMappingConfig> fieldMappingConfigs) {
        this.fieldMappingConfigs = fieldMappingConfigs;
    }

    public static FieldMappingRule convert(FieldMappingConfig fieldMappingConfig) {
        FieldMappingRule fieldMappingRule = new FieldMappingRule();

        fieldMappingRule.setFormDefaultValue(fieldMappingConfig.sourceDefaltValue);
        fieldMappingRule.setFromPathExps(fieldMappingConfig.sourceName);

        if (!ValidateUtils.isNullOrEmpt(fieldMappingConfig.sourceType)) {
            fieldMappingRule.setFromDataType(DataType.fromString(fieldMappingConfig.sourceType));
        }

        if (!ValidateUtils.isNullOrEmpt(fieldMappingConfig.targetType)) {
            fieldMappingRule.setToDataType(DataType.fromString(fieldMappingConfig.targetType));
        }

        fieldMappingRule.setToPathExps(fieldMappingConfig.targetName);

        if (!ValidateUtils.isNullOrEmpt(fieldMappingConfig.targetFunc) || !ValidateUtils.isNullOrEmpt(fieldMappingConfig.sourceFunc)) {
            fieldMappingRule.setTransfFun(true);

            if (!ValidateUtils.isNullOrEmpt(fieldMappingConfig.sourceFunc)) {
                FieldMappingRule.MappingFuncConfig sourceFunc = convertFunc(fieldMappingConfig.sourceFunc);
                fieldMappingRule.setSourceFuncConfig(sourceFunc);
            }
            if (!ValidateUtils.isNullOrEmpt(fieldMappingConfig.targetFunc)) {
                FieldMappingRule.MappingFuncConfig targetFunc = convertFunc(fieldMappingConfig.targetFunc);
                fieldMappingRule.setTgtFuncConfig(targetFunc);
            }
        }
        if (!ValidateUtils.isNullOrEmpt(fieldMappingConfig.valueTrasferConfig)) {
            fieldMappingRule.setTrasfRules(convert(fieldMappingConfig.valueTrasferConfig));
        }
        return fieldMappingRule;
    }

    public static FieldMappingRule.MappingFuncConfig convertFunc(FuncConfig funcConfig) {
        FieldMappingRule.MappingFuncConfig result = new FieldMappingRule.MappingFuncConfig();
        //result.setInnerDefaultParams(funcConfig.defaultParams);
        result.setFunctionName(funcConfig.funcName);

        List<SourceFieldConfig> sourceFields = funcConfig.sourceFields;

        List<String> paramName = new ArrayList<String>(sourceFields.size()); // all param path express
        List<DataType> paramDataTypes = new ArrayList<DataType>(sourceFields.size());
        List<DataType> transfDataTypes = new ArrayList<DataType>(sourceFields.size());
        List<Map> trasfRuleList = new ArrayList<Map>(sourceFields.size());

        int idx = 0;
        sourceFields.forEach((sourceFieldConfig) -> {
            paramName.add(sourceFieldConfig.fieldName);

            if (!ValidateUtils.isNullOrEmpt(sourceFieldConfig.targetType)) {
                paramDataTypes.add(DataType.fromString(sourceFieldConfig.sourceType));
            } else {
                paramDataTypes.add(null);
            }


            if (!ValidateUtils.isNullOrEmpt(sourceFieldConfig.targetType)) {
                transfDataTypes.add(DataType.fromString(sourceFieldConfig.targetType));
            } else {
                transfDataTypes.add(null);
            }

            if (ValidateUtils.isNullOrEmpt(sourceFieldConfig.valueTrasferConfig)) {
                trasfRuleList.add(convert(sourceFieldConfig.valueTrasferConfig));
            } else {
                trasfRuleList.add(null);
            }
        });

        result.setParamName(paramName);
        result.setTransfDataTypes(transfDataTypes);
        result.setParamDataTypes(paramDataTypes);
        result.setTrasfRuleList(trasfRuleList);
        return result;
    }

    public static Map convert(ValueTrasferConfig valueTrasferConfig) {
        if (ValidateUtils.isNullOrEmpt(valueTrasferConfig)) {
            return null;
        }
        final Map result = new HashMap(8);
        if (valueTrasferConfig instanceof DirectTransferConfig) {
            DirectTransferConfig tempTransConfig = (DirectTransferConfig) valueTrasferConfig;
            result.put(FieldMappingRule.TR_DEFAULT_VALUE_KEY, tempTransConfig.defaultValue);
            result.putAll(tempTransConfig.values);
        } else {
            throw new RuntimeException("not support ValueTrasferConfig:[" + valueTrasferConfig.getClass().getSimpleName() + "].");
        }
        return result;
    }

    public static DataMappingEngine converts(List<MappingRuleEntity.FieldMappingConfig> fieldMappingConfigs) {
        //List<FieldMappingConfig> fieldMappingConfigs = mappingRuleEntity.getFieldMappingConfigs();
        if (ValidateUtils.isNullOrEmpt(fieldMappingConfigs))
            throw new RuntimeException("converts FieldMappingConfig to FieldMappingRule exception! param:[" + fieldMappingConfigs + "] is null.");

        final List<FieldMappingRule> results = new ArrayList<>(fieldMappingConfigs.size());
        fieldMappingConfigs.forEach((config) -> {
            FieldMappingRule mappingRule = convert(config);
            results.add(mappingRule);
        });
        DataMappingEngine dataMappingEngine = new DataMappingEngine(results);
        return dataMappingEngine;
    }
}
