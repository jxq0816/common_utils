/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: wangchaochao
 * Created: 2017/04/10
 * Description:
 *
 */

package com.inter3i.base;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public class ValidateUtils {

    public static boolean isNullOrEmpt(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return ((String) object).length() > 0 ? false : true;
        } else if (object instanceof Map) {
            return ((Map) object).size() > 0 ? false : true;
        } else if (object instanceof List) {
            return ((List) object).size() > 0 ? false : true;
        } else if (object instanceof JSONObject) {
            return ((JSONObject) object).size() > 0 ? false : true;
        } else if (object instanceof JSONArray) {
            return ((JSONArray) object).size() > 0 ? false : true;
        }
        return false;
    }

    public static boolean isNullOrEmpt(Map map, String key) {
        if (isNullOrEmpt(key)) {
            throw new RuntimeException("isNullOrEmpt for map excption,key is null.");
        }
        if (isNullOrEmpt(map)) {
            return true;
        }
        if (map.containsKey(key)) {
            Object value = map.get(key);
            return isNullOrEmpt(value);
        } else {
            return true;
        }
    }

    public static boolean isNullOrEmpt(JSONObject map, String key) throws JSONException {
        if (isNullOrEmpt(key)) {
            throw new RuntimeException("isNullOrEmpt for map excption,key is null.");
        }
        if (isNullOrEmpt(map)) {
            return true;
        }
        if (!map.containsKey(key)) {
            Object value = map.get(key);
            return isNullOrEmpt(value);
        } else {
            return true;
        }
    }
}
