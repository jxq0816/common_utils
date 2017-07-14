/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: wangchaochao3i
 * Created: 2017/03/16
 * Description:
 *
 */

package com.inter3i.base.expression.impl;

import com.inter3i.base.DataType;
import com.inter3i.base.expression.*;

import java.math.BigDecimal;

public class OperatorImpl {

    public static IBinaryOperator.ADD ADD = (Object left, Object right) -> {
        double leftValue = DataType.getDoubleValue(left);
        double rightValue = DataType.getDoubleValue(right);
        return leftValue + rightValue;
    };

    public static IBinaryOperator.ADD SUB = (Object left, Object right) -> {
        double leftValue = DataType.getDoubleValue(left);
        double rightValue = DataType.getDoubleValue(right);
        return leftValue - rightValue;
    };

    public static IBinaryOperator.ADD MUL = (Object left, Object right) -> {
        double leftValue = DataType.getDoubleValue(left);
        double rightValue = DataType.getDoubleValue(right);
        return leftValue * rightValue;
    };

    public static IBinaryOperator.ADD DIV = (Object left, Object right) -> {
        double leftValue = DataType.getDoubleValue(left);
        double rightValue = DataType.getDoubleValue(right);
        return leftValue / rightValue;
    };


    public static IBinaryOperator.MOD MOD = (Object left, Object right) -> {
        double leftValue = DataType.getDoubleValue(left);
        double rightValue = DataType.getDoubleValue(left);
        return leftValue * rightValue;
    };

    public static IBinaryOperator.CT CT = (Object left, Object right) -> {
        String leftValue = (String) left;
        String rightValue = (String) right;
        return leftValue.contains(rightValue);
    };

    public static IBinaryOperator.NCT NCT = (Object left, Object right) -> {
        return !((Boolean) CT.calculate(left, right));
    };


    public static ILogicOperator.EQU EQU = (Object left, Object right) -> {
        if (left == null || right == null) {
            return false;
        }
        if (left.getClass() != right.getClass()) {
            return false;
        }
        if (left instanceof String || left instanceof Character) {
            return left.equals(right);
        } else if (left instanceof BigDecimal) {
            return (((BigDecimal) left).compareTo((BigDecimal) right) == 0) ? true : false;
        }
        return left == right;
    };

    public static ILogicOperator.NEQU NEQU = (Object left, Object right) -> {
        Boolean result = (Boolean) EQU.calculate(left, right);
        return !result;
    };

    public static ILogicOperator.GT GT = (Object left, Object right) -> {
        double leftValue = DataType.getDoubleValue(left);
        double rightValue = DataType.getDoubleValue(right);
        return leftValue > rightValue;
    };
    public static ILogicOperator.LT LT = (Object left, Object right) -> {
        double leftValue = DataType.getDoubleValue(left);
        double rightValue = DataType.getDoubleValue(left);
        return leftValue < rightValue;
    };

    public static ILogicOperator.GTE GTE = (Object left, Object right) -> {
        double leftValue = DataType.getDoubleValue(left);
        double rightValue = DataType.getDoubleValue(left);
        return leftValue >= rightValue;
    };
    public static ILogicOperator.LTE LTE = (Object left, Object right) -> {
        double leftValue = DataType.getDoubleValue(left);
        double rightValue = DataType.getDoubleValue(left);
        return leftValue <= rightValue;
    };


    public static ITernaryOperator.ISDO ISDO = (Boolean result, Object tureValue, Object falseValue) -> {
        if (result) {
            return tureValue;
        } else {
            return falseValue;
        }
    };

    public static IUnaryOperator.SELFAND SELFAND = (Object value) -> {
        return (Double.valueOf(value.toString())) + 1;
    };

    public static IUnaryOperator.SELFSUB SELFSUB = (Object value) -> {
        return (Double.valueOf(value.toString())) - 1;
    };

    public static ILogicOperator.AND AND = (Object left, Object right) -> {
        return (Boolean) left && (Boolean) right;
    };

    public static ILogicOperator.OR OR = (Object left, Object right) -> {
        return (Boolean) left || (Boolean) right;
    };


    public static IMutiOperator.IN IN = (Object leftTerm, Object... values) -> {
        return null;
    };

    public static IMutiOperator.NIN NIN = (Object leftTerm, Object... values) -> {
        return null;
    };


    public static IOperator valueof(String operatorName) {
        if (operatorName.trim().equalsIgnoreCase("+")) {
            return ADD;
        } else if (operatorName.trim().equalsIgnoreCase("-")) {
            return SUB;
        } else if (operatorName.trim().equalsIgnoreCase("*")) {
            return MUL;
        } else if (operatorName.trim().equalsIgnoreCase("/")) {
            return DIV;
        } else if (operatorName.trim().equalsIgnoreCase("%")) {
            return MOD;
        } else if (operatorName.trim().equalsIgnoreCase("==")) {
            return EQU;
        } else if (operatorName.trim().equalsIgnoreCase("!=")) {
            return NEQU;
        } else if (operatorName.trim().equalsIgnoreCase("$gt")) {
            return GT;
        } else if (operatorName.trim().equalsIgnoreCase("$lt")) {
            return LT;
        } else if (operatorName.trim().equalsIgnoreCase("$gte")) {
            return GTE;
        } else if (operatorName.trim().equalsIgnoreCase("$lte")) {
            return LTE;
        } else if (operatorName.trim().equalsIgnoreCase("?")) {
            return ISDO;
        } else if (operatorName.trim().equalsIgnoreCase("in")) {
            return IN;
        } else if (operatorName.trim().equalsIgnoreCase("nin")) {
            return NIN;
        } else if (operatorName.trim().equalsIgnoreCase("++")) {
            return SELFAND;
        } else if (operatorName.trim().equalsIgnoreCase("--")) {
            return SELFSUB;
        } else if (operatorName.trim().equalsIgnoreCase("and")) {
            return AND;
        } else if (operatorName.trim().equalsIgnoreCase("or")) {
            return OR;
        } else if (operatorName.trim().equalsIgnoreCase("[]")) {
            return CT;
        } else if (operatorName.trim().equalsIgnoreCase("[!]")) {
            return NCT;
        } else {
            throw new RuntimeException("The operator:[" + operatorName + "] is not supported!");
        }
    }
}
