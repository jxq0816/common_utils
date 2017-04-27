/*
 *
 * Copyright (c) 2017, inter3i.com. All rights reserved.
 * All rights reserved.
 *
 * Author: Administrator
 * Created: 2017/04/14
 * Description:
 *
 */

package com.inter3i.base;

import java.util.Collection;
import java.util.Iterator;

public class ListUtils {
    public static String list2StringBy(final String sep, Collection list) {
        String resultListStr = "";
        int size = list.size();

        Iterator<String> iterator = list.iterator();
        String curValue = null;
        int idx = 0;
        while (iterator.hasNext()) {
            curValue = iterator.next();
            resultListStr = resultListStr.concat(curValue);
            if (idx < size - 1) {
                resultListStr = resultListStr.concat(sep); // " OR "
            }
            idx++;
        }
        return resultListStr;
    }
}
