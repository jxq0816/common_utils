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

public interface IUnaryOperator extends IOperator {

    Object calculate(Object value);

    // ++
    @FunctionalInterface
    public interface SELFAND extends IUnaryOperator {
    }

    // --
    @FunctionalInterface
    public interface SELFSUB extends IUnaryOperator {
    }

}
