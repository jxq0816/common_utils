/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: Administrator
 * Created: 2017/06/07
 * Description:
 *
 */

package com.inter3i.test.base;

import com.alibaba.fastjson.JSONArray;

import java.util.Iterator;

public class FilterTest {
    public static void main(String[] args) {
        JSONArray array = new JSONArray();
        array.add(0, 0);
        array.add(1, 1);
        array.add(2, 2);
        array.add(3, 3);
        array.add(4, 4);

        Iterator it = array.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }

    }
}
