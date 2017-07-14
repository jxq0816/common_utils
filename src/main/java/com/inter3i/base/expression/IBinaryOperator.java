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

public interface IBinaryOperator extends IOperator {

    /**
     * 定义二目运算符
     */
    Object calculate(Object left, Object right);

    // +
    @FunctionalInterface
    public static interface ADD extends IBinaryOperator {
    }

    // -
    @FunctionalInterface
    public interface SUB extends IBinaryOperator {
    }

    // *
    @FunctionalInterface
    public interface MUL extends IBinaryOperator {
    }

    // /
    @FunctionalInterface
    public interface DIV extends IBinaryOperator {
    }

    // %
    @FunctionalInterface
    public interface MOD extends IBinaryOperator {
    }

    // [] : 包含
    @FunctionalInterface
    public interface CT extends IBinaryOperator {
    }

    // [!]:不包含
    @FunctionalInterface
    public interface NCT extends IBinaryOperator {
    }
}
