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

package com.inter3i.base.expression;

import com.inter3i.base.DataType;
import com.inter3i.base.ParamHelper;
import com.inter3i.base.ValidateUtils;
import com.inter3i.base.expression.impl.OperatorImpl;

import java.util.ArrayList;
import java.util.List;

public class ExpressionEngine {

    public static abstract class Expression {
        //操作符类型 单目操作符:[] 双目操作符:[] 多目操作符:[]
        protected String operatorName;

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public abstract Object evel(Object content);
    }

    public static class ValueTermExpression extends ExpressionEngine.Expression {
        public static final int VALUE_TYPE_CONSTANT = 11;
        public static final int VALUE_TYPE_PATHEXP = 12;

        // 取值表达式 或者 常量
        private int valueType;
        private String value;
        private String dataType; //数据类型

        public Object evel(Object content) {
            switch (this.valueType) {
                case VALUE_TYPE_CONSTANT:
                    //类型转化
                    return DataType.STRING.toTargetDatType(DataType.fromString(dataType), value);
                case VALUE_TYPE_PATHEXP:
                    Object tempValue = ParamHelper.ParamGetterHelper.getValueBy(value, content);
                    if (ValidateUtils.isNullOrEmpt(tempValue)) {
                        System.out.println("get constant value null:[" + value + "].");
                        return null;
                    }
                    //类型转化
                    return DataType.fromString(tempValue.getClass().getSimpleName()).toTargetDatType(DataType.fromString(dataType), tempValue);
                default:
                    throw new RuntimeException("unsupport valueType for ValueTerm:[" + valueType + "].");
            }
        }

        public int getValueType() {
            return valueType;
        }

        public void setValueType(int valueType) {
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

    /**
     * 单目表达式运算
     */
    public static class UnaryExpression extends Expression {
        private Expression valueTerm;

        @Override
        public Object evel(Object content) {
            Object value = valueTerm.evel(content);
            IUnaryOperator operator = (IUnaryOperator) OperatorImpl.valueof(operatorName);
            Object result = operator.calculate(value);
            return result;
        }

        public Expression getValueTerm() {
            return valueTerm;
        }

        public void setValueTerm(Expression valueTerm) {
            this.valueTerm = valueTerm;
        }
    }

    /**
     * 双目表达是计算
     */
    public static class BinaryExpression extends Expression {
        private Expression leftValueTerm;
        private Expression rightValueTerm;

        @Override
        public Object evel(Object content) {
            Object leftValue = leftValueTerm.evel(content);
            Object rightValue = rightValueTerm.evel(content);
            IBinaryOperator operator = (IBinaryOperator) OperatorImpl.valueof(operatorName);
            Object result = operator.calculate(leftValue, rightValue);
            return result;
        }

        public Expression getLeftValueTerm() {
            return leftValueTerm;
        }

        public void setLeftValueTerm(Expression leftValueTerm) {
            this.leftValueTerm = leftValueTerm;
        }

        public Expression getRightValueTerm() {
            return rightValueTerm;
        }

        public void setRightValueTerm(Expression rightValueTerm) {
            this.rightValueTerm = rightValueTerm;
        }
    }

    /**
     * 双目表达是计算
     */
    public static class LogicExpression extends BinaryExpression {
        private List<Expression> coculatValues;

        @Override
        public Object evel(Object content) {
            //短路处理
            boolean breakCheck = false;
            if (this.operatorName.equalsIgnoreCase("and")) {
                breakCheck = false;
            } else if (this.operatorName.equalsIgnoreCase("or")) {
                breakCheck = true;
            } else {
                throw new RuntimeException("no supportted opratorName:[" + this.operatorName + "] for LogicExpression!");
            }

            boolean resultTmp = (Boolean) coculatValues.get(0).evel(content);
            if (resultTmp == breakCheck) {
                return resultTmp;
            }
            for (int i = 1; i < coculatValues.size(); i++) {
                boolean resultInner = (Boolean) coculatValues.get(i).evel(content);
                IBinaryOperator operator = (IBinaryOperator) OperatorImpl.valueof(operatorName);
                resultTmp = (Boolean) operator.calculate(resultInner, resultTmp);
                if (resultTmp == breakCheck) {
                    return resultTmp;
                }
            }
            return resultTmp;
        }

        public List<Expression> getCoculatValues() {
            return coculatValues;
        }

        public void setCoculatValues(List<Expression> coculatValues) {
            this.coculatValues = coculatValues;
        }
    }

    /**
     * 三目表达式计算
     */
    public static class TernaryExpression extends Expression {
        private BinaryExpression innerExpression;
        private Expression trueTerm;
        private Expression falseTerm;

        @Override
        public Object evel(Object content) {
            Object reusltTmp = innerExpression.evel(content);
            ITernaryOperator operator = (ITernaryOperator) OperatorImpl.valueof(operatorName);
            Object trueValue = trueTerm.evel(content);
            Object falseValue = falseTerm.evel(content);
            Object result = operator.calculate((Boolean) reusltTmp, trueValue, falseValue);
            return result;
        }

        public BinaryExpression getInnerExpression() {
            return innerExpression;
        }

        public void setInnerExpression(BinaryExpression innerExpression) {
            this.innerExpression = innerExpression;
        }

        public Expression getTrueTerm() {
            return trueTerm;
        }

        public void setTrueTerm(Expression trueTerm) {
            this.trueTerm = trueTerm;
        }

        public Expression getFalseTerm() {
            return falseTerm;
        }

        public void setFalseTerm(Expression falseTerm) {
            this.falseTerm = falseTerm;
        }
    }

    /**
     * 多目表达式计算
     */
    public static class MultipleExpression extends Expression {
        private Expression leftTerm;
        private List<Expression> valueTerms;

        @Override
        public Object evel(Object content) {
            Object result = null;

            Object leftValue = leftTerm.evel(content);
            List values = new ArrayList(valueTerms.size());
            Object valueTmp = null;
            for (int i = 0; i < valueTerms.size(); i++) {
                valueTmp = valueTerms.get(i).evel(content);
                values.add(valueTmp);
            }
            IMutiOperator operator = (IMutiOperator) OperatorImpl.valueof(operatorName);
            result = operator.calculate(leftValue, values);
            return result;
        }

        public Expression getLeftTerm() {
            return leftTerm;
        }

        public void setLeftTerm(Expression leftTerm) {
            this.leftTerm = leftTerm;
        }

        public List<Expression> getValueTerms() {
            return valueTerms;
        }

        public void setValueTerms(List<Expression> valueTerms) {
            this.valueTerms = valueTerms;
        }
    }

}
