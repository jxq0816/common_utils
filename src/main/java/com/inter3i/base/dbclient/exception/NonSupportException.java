/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: Administrator
 * Created: 2017/05/12
 * Description:
 *
 */

package com.inter3i.base.dbclient.exception;

public class NonSupportException extends RuntimeException {

    private static final long serialVersionUID = 178751939980700153L;

    public NonSupportException() {
    }

    public NonSupportException(String message) {
        super(message);
    }

    public NonSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonSupportException(Throwable cause) {
        super(cause);
    }

    public NonSupportException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
