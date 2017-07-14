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

package com.inter3i.base;

import java.math.BigDecimal;

public enum DataType {
    DOUBLE("double") {
        @Override
        public Object toTargetDatType(final DataType targetType, final Object value) {
            Double strValue = (Double) value;
            if (targetType == STRING) {
                return String.valueOf(value);
            } else if (targetType == DOUBLE) {
                return strValue;
            } else {
                //暂时不支持
                throw new RuntimeException("unsupport transfer DOUBLE:[" + strValue + "] to targetType:[" + targetType.innerTypaName + "].");
            }
        }
    },
    INT("int") {
        @Override
        public Object toTargetDatType(final DataType targetType, final Object value) {
            Integer strValue = (Integer) value;
            if (targetType == STRING) {
                return String.valueOf(value);
            } else if (targetType == LONG) {
                return Long.valueOf(strValue);
            } else if (targetType == DOUBLE) {
                return Double.valueOf(strValue);
            } else if (targetType == INT) {
                return strValue;
            } else {
                //暂时不支持
                throw new RuntimeException("unsupport transfer INT:[" + strValue + "] to targetType:[" + targetType.innerTypaName + "].");
            }
        }
    },
    STRING("string") {
        @Override
        public Object toTargetDatType(final DataType targetType, final Object value) {
            String strValue = (String) value;
            if (targetType == DOUBLE) {
                return Double.valueOf(strValue);
            } else if (targetType == INT) {
                return Integer.valueOf(strValue);
            } else if (targetType == LONG) {
                return Long.valueOf(strValue);
            } else if (targetType == FLOAT) {
                return Float.valueOf(strValue);
            } else if (targetType == BYTE) {
                //暂时不支持
                throw new RuntimeException("unsupport transfer StringValue:[" + strValue + "] to byte!");
            } else if (targetType == CHAR) {
                throw new RuntimeException("unsupport transfer StringValue:[" + strValue + "] to char!");
            } else if (targetType == SHORT) {
                return Short.valueOf(strValue);
            } else if (targetType == BOOLEAN) {
                return Boolean.valueOf(strValue);
            } else if (targetType == BIGDECIMAL) {
                return new BigDecimal(strValue);
            } else if (targetType == STRING) {
                return strValue;
            }
            throw new RuntimeException("unsupport transfer StringValue:[" + strValue + "] to targetType:[" + targetType.toString() + "].");
        }
    },
    LONG("long") {
        @Override
        public Object toTargetDatType(final DataType targetType, final Object value) {
            Long strValue = (Long) value;
            if (targetType == STRING) {
                return String.valueOf(value);
            } else if (targetType == LONG) {
                return strValue;
            } else {
                //暂时不支持
                throw new RuntimeException("unsupport transfer LONG:[" + strValue + "] to targetType:[" + targetType.innerTypaName + "].");
            }
        }
    },
    FLOAT("float") {
        @Override
        public Object toTargetDatType(final DataType targetType, final Object value) {
            Float strValue = (Float) value;
            if (targetType == STRING) {
                return String.valueOf(value);
            } else if (targetType == DOUBLE) {
                return Double.valueOf(String.valueOf(strValue));
            } else if (targetType == FLOAT) {
                return strValue;
            } else {
                //暂时不支持
                throw new RuntimeException("unsupport transfer FLOAT:[" + strValue + "] to targetType:[" + targetType.innerTypaName + "].");
            }
        }
    },
    BYTE("byte") {
        @Override
        public Object toTargetDatType(final DataType targetType, final Object value) {
            Byte strValue = (Byte) value;
            if (targetType == INT) {
                return (Integer) value;
            } else if (targetType == BYTE) {
                return strValue;
            } else {
                //暂时不支持
                throw new RuntimeException("unsupport transfer BYTE:[" + strValue + "] to targetType:[" + targetType.innerTypaName + "].");
            }
        }
    },
    CHAR("char") {
        @Override
        public Object toTargetDatType(final DataType targetType, final Object value) {
            Character strValue = (Character) value;
            if (targetType == BYTE) {
                return (Byte) value;
            } else if (targetType == CHAR) {
                return strValue;
            } else {
                //暂时不支持
                throw new RuntimeException("unsupport transfer CHAR:[" + strValue + "] to targetType:[" + targetType.innerTypaName + "].");
            }
        }
    },
    SHORT("short") {
        @Override
        public Object toTargetDatType(final DataType targetType, final Object value) {
            Short strValue = (Short) value;
            if (targetType == STRING) {
                return String.valueOf(value);
            } else if (targetType == SHORT) {
                return strValue;
            } else {
                //暂时不支持
                throw new RuntimeException("unsupport transfer SHORT:[" + strValue + "] to targetType:[" + targetType.innerTypaName + "].");
            }
        }
    },
    BOOLEAN("boolean") {
        @Override
        public Object toTargetDatType(final DataType targetType, final Object value) {
            Boolean strValue = (Boolean) value;
            if (targetType == STRING) {
                return String.valueOf(value);
            } else if (targetType == BOOLEAN) {
                return strValue;
            } else {
                //暂时不支持
                throw new RuntimeException("unsupport transfer BOOLEAN:[" + strValue + "] to targetType:[" + targetType.innerTypaName + "].");
            }
        }
    },
    BIGDECIMAL("bigdecimal") {
        @Override
        public Object toTargetDatType(final DataType targetType, final Object value) {
            BigDecimal strValue = (BigDecimal) value;
            if (targetType == STRING) {
                return strValue.toPlainString();
            } else if (targetType == BIGDECIMAL) {
                return strValue;
            } else if (targetType == DOUBLE) {
                return Double.valueOf(strValue.toPlainString());
            } else {
                //暂时不支持
                throw new RuntimeException("unsupport transfer BIGDECIMAL:[" + strValue + "] to targetType:[" + targetType.innerTypaName + "].");
            }
        }
    }, LIST("list") {

    }, JAONARRAY("JSONARRAY") {

    }, JSONOBJECT("JSONARRAY") {

    };

    private String innerTypaName;

    DataType(String value) {
        this.innerTypaName = value;
    }

    public Object toTargetDatType(final DataType targetType, final Object value) {
        return null;
    }

    public static DataType fromString(final String name) {
        if (ValidateUtils.isNullOrEmpt(name)) {
            throw new RuntimeException("Construct DataType from String exception:[" + name + "].");
        }
        DataType result = null;
        if (name.equalsIgnoreCase("DOUBLE")) {
            result = DOUBLE;
        } else if (name.equalsIgnoreCase("INT") || name.equalsIgnoreCase("Integer")) {
            result = INT;
        } else if (name.equalsIgnoreCase("STRING")) {
            result = STRING;
        } else if (name.equalsIgnoreCase("LONG")) {
            result = LONG;
        } else if (name.equalsIgnoreCase("FLOAT")) {
            result = FLOAT;
        } else if (name.equalsIgnoreCase("BYTE")) {
            result = BYTE;
        } else if (name.equalsIgnoreCase("CHAR")) {
            result = CHAR;
        } else if (name.equalsIgnoreCase("SHORT")) {
            result = SHORT;
        } else if (name.equalsIgnoreCase("BOOLEAN")) {
            result = BOOLEAN;
        } else if (name.equalsIgnoreCase("BIGDECIMAL")) {
            result = BIGDECIMAL;
        } else {
            throw new RuntimeException("unkwon dataType:[" + name + "].");
        }
        return result;
    }

    public static double getDoubleValue(final Object value) {
        if(value == null){
            return -1;
        }
        DataType valueDataType = DataType.fromString(value.getClass().getSimpleName());
        return (Double) valueDataType.toTargetDatType(DataType.DOUBLE, value);
    }

    @Override
    public String toString() {
        return innerTypaName;
    }
}
