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

public interface ITernaryOperator extends IOperator {

    Object calculate(Boolean result, Object tureValue, Object falseValue);

    //三目运算符
    @FunctionalInterface
    public static interface ISDO extends ITernaryOperator {
    }
}
