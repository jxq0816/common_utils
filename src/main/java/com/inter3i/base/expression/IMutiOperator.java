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

public interface IMutiOperator extends IOperator {
    Object calculate(Object leftTerm, Object... values);

    //多目运算符 in
    @FunctionalInterface
    public static interface IN extends IMutiOperator {
    }

    //多目运算符 not in
    @FunctionalInterface
    public static interface NIN extends IMutiOperator {
    }
}
