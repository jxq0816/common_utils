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

public interface ILogicOperator extends IBinaryOperator {

    // and
    @FunctionalInterface
    public static interface AND extends ILogicOperator {
    }

    // or
    @FunctionalInterface
    public static interface OR extends ILogicOperator {
    }


    // ==
    @FunctionalInterface
    public interface EQU extends ILogicOperator {
    }

    // !=
    @FunctionalInterface
    public interface NEQU extends ILogicOperator {
    }

    // >
    @FunctionalInterface
    public static interface GT extends ILogicOperator {
    }

    // <
    @FunctionalInterface
    public static interface LT extends ILogicOperator {
    }

    // >=
    @FunctionalInterface
    public static interface GTE extends ILogicOperator {
    }

    // <=
    @FunctionalInterface
    public static interface LTE extends ILogicOperator {
    }
}
