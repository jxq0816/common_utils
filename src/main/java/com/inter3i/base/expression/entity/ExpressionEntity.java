/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: wangchaochao3i
 * Created: 2017/03/17
 * Description:
 *
 */

package com.inter3i.base.expression.entity;

import com.inter3i.base.ValidateUtils;
import com.inter3i.base.expression.ExpressionEngine;

import java.util.ArrayList;
import java.util.List;

public class ExpressionEntity {

    public static final class ValueTermEntity extends ExpressionEntity {
        private String valueType;
        private String value;
        private String dataType;

        public String getValueType() {
            return valueType;
        }

        public void setValueType(String valueType) {
            this.valueType = valueType;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }
    }

    public static final String OPERATOR_TYPE_UNARY = "unary"; //单目运算符
    public static final String OPERATOR_TYPE_BINARY = "binary"; //双目运算符
    public static final String OPERATOR_TYPE_LOGIC = "logic"; //逻辑运算符
    public static final String OPERATOR_TYPE_TERNARY = "ternary"; //三目运算符
    public static final String OPERATOR_TYPE_MULTIPLE = "multiple"; //多目运算符

    public static final String OPERATOR_TYPE_VALUE = "value"; //取值表达式

    private String type; //单目/双目/三目/多目
    private String operator;// + - * / != == >= <= .....
    private List<ExpressionEntity> childExpressions;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<ExpressionEntity> getChildExpressions() {
        return childExpressions;
    }

    public void setChildExpressions(List<ExpressionEntity> childExpressions) {
        this.childExpressions = childExpressions;
    }

    public static ExpressionEngine.Expression conver2Expression(ExpressionEntity entity) {
        if (OPERATOR_TYPE_UNARY.equalsIgnoreCase(entity.getType())) {
            //单目运算符
            if (ValidateUtils.isNullOrEmpt(entity.getChildExpressions()) || entity.getChildExpressions().size() != 1) {
                throw new RuntimeException("UnaryExpression child expression num must is 1");
            }
            ExpressionEngine.UnaryExpression result = new ExpressionEngine.UnaryExpression();
            ExpressionEngine.Expression innnerTerm = conver2Expression(entity.getChildExpressions().get(0));
            result.setValueTerm(innnerTerm);
            result.setOperatorName(entity.getOperator());
            return result;
        } else if (OPERATOR_TYPE_BINARY.equalsIgnoreCase(entity.getType())) {
            if (ValidateUtils.isNullOrEmpt(entity.getChildExpressions()) || entity.getChildExpressions().size() != 2) {
                throw new RuntimeException("LogicExpression child expression num must more than 2");
            }
            ExpressionEngine.BinaryExpression result = new ExpressionEngine.BinaryExpression();
            ExpressionEngine.Expression leftTerm = conver2Expression(entity.getChildExpressions().get(0));
            ExpressionEngine.Expression rightTerm = conver2Expression(entity.getChildExpressions().get(1));
            result.setLeftValueTerm(leftTerm);
            result.setRightValueTerm(rightTerm);
            result.setOperatorName(entity.getOperator());
            return result;
        } else if (OPERATOR_TYPE_LOGIC.equalsIgnoreCase(entity.getType())) {
            if (ValidateUtils.isNullOrEmpt(entity.getChildExpressions()) || entity.getChildExpressions().size() < 2) {
                throw new RuntimeException("LogicExpression child expression num must more than 2");
            }
            ExpressionEngine.LogicExpression result = new ExpressionEngine.LogicExpression();
            ExpressionEngine.Expression childTerm = null;
            List<ExpressionEngine.Expression> childExpressions = new ArrayList(entity.getChildExpressions().size());
            for (int i = 0; i < entity.getChildExpressions().size(); i++) {
                childTerm = conver2Expression(entity.getChildExpressions().get(i));
                childExpressions.add(childTerm);
            }
            result.setCoculatValues(childExpressions);
            result.setOperatorName(entity.getOperator());
            return result;
        } else if (OPERATOR_TYPE_TERNARY.equalsIgnoreCase(entity.getType())) {
            if (ValidateUtils.isNullOrEmpt(entity.getChildExpressions()) || entity.getChildExpressions().size() != 3) {
                throw new RuntimeException("TernaryExpression child expression num must is 3");
            }
            ExpressionEngine.TernaryExpression result = new ExpressionEngine.TernaryExpression();
            ExpressionEngine.Expression innerExpression = conver2Expression(entity.getChildExpressions().get(0));
            result.setInnerExpression((ExpressionEngine.BinaryExpression) innerExpression);

            ExpressionEngine.Expression falseExpression = conver2Expression(entity.getChildExpressions().get(1));
            ExpressionEngine.Expression trueExpression = conver2Expression(entity.getChildExpressions().get(2));

            result.setFalseTerm(falseExpression);
            result.setTrueTerm(trueExpression);
            result.setOperatorName(entity.getOperator());
            return result;
        } else if (OPERATOR_TYPE_MULTIPLE.equalsIgnoreCase(entity.getType())) {
            ExpressionEngine.MultipleExpression result = new ExpressionEngine.MultipleExpression();
            if (ValidateUtils.isNullOrEmpt(entity.getChildExpressions()) || entity.getChildExpressions().size() < 3) {
                throw new RuntimeException("TernaryExpression child expression num must is 3");
            }
            ExpressionEngine.Expression leftExpression = conver2Expression(entity.getChildExpressions().get(0));
            result.setLeftTerm(leftExpression);

            ExpressionEngine.Expression childTerm = null;
            List<ExpressionEngine.Expression> childExpressions = new ArrayList(entity.getChildExpressions().size());
            for (int i = 1; i < entity.getChildExpressions().size(); i++) {
                childTerm = conver2Expression(entity.getChildExpressions().get(i));
                childExpressions.add(childTerm);
            }
            result.setValueTerms(childExpressions);
            result.setOperatorName(entity.getOperator());
            return result;
        } else if (entity instanceof ValueTermEntity) {
            //取值表达式
            ExpressionEngine.ValueTermExpression valueTermExpression = new ExpressionEngine.ValueTermExpression();
            ValueTermEntity valueTermEntity = (ValueTermEntity) entity;
            valueTermExpression.setDataType(valueTermEntity.getDataType());
            valueTermExpression.setValue(valueTermEntity.getValue());
            if (valueTermEntity.getValueType().equalsIgnoreCase("constants")) {
                valueTermExpression.setValueType(ExpressionEngine.ValueTermExpression.VALUE_TYPE_CONSTANT);
            } else if (valueTermEntity.getValueType().equalsIgnoreCase("pathExpress")) {
                valueTermExpression.setValueType(ExpressionEngine.ValueTermExpression.VALUE_TYPE_PATHEXP);
            }
            return valueTermExpression;
        } else {
            throw new RuntimeException("unsupportted operator tyep:[" + entity.getType() + "].");
        }
    }
}
