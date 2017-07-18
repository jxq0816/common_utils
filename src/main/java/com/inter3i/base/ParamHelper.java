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

package com.inter3i.base;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class ParamHelper {
    private static final Object NULL_VALUE = null;


    public static class InvalidParamTypeExcption extends RuntimeException {
        private String errorCode;


        InvalidParamTypeExcption(String errorMsg) {
            super(errorMsg);
        }

        InvalidParamTypeExcption(String errorMsg, Throwable cause) {
            super(errorMsg, cause);
        }

        InvalidParamTypeExcption(String errorMsg, String errorCode) {
            super(errorMsg);
            this.errorCode = errorCode;
        }

        InvalidParamTypeExcption(String errorMsg, String errorCode, Throwable cause) {
            super(errorMsg, cause);
            this.errorCode = errorCode;
        }
    }

    private static class PathExprs {

        public static final String PATH_EXPR_ARRAY_IDX_ALL = "all";
        public static final String PATH_EXPR_ARRAY_IDX_MAX = "n";

        public static final String PATH_EXPR_ARRAY_IDX_SOURCE_APPEND = "apd";

        //直接指向map中的某个key
        private final String propertyName;

        /**
         * 如果通过propertyName属性取出来的值是一个数据，则还需要在从数组中根据下标valueIdx再次取一次值<BR>
         * <p>
         * 1.如果是设置的时候，可以支持将数组中的多个值进行设值操作，如果该数组的长度为1,且第一个元素是-1则为数组中的所有值赋值
         * 2.n表示数组中最大下标
         * 3.多个下标需要用","分开，如：name.students[1,2,5,7,9,n]
         * 4.多段的小标设置也是用","分开如:name.sudents[1-10,30-n]
         * </P>
         */
        private IndxIterable valueIdx;

        /**
         *数组处理，去除【】，设置URL请求参数
         * @param propertyNameFullStr
         */
        PathExprs(final String propertyNameFullStr) {
            if (propertyNameFullStr.contains("[") && propertyNameFullStr.contains("]")) {
                this.propertyName = propertyNameFullStr.substring(0, propertyNameFullStr.indexOf("["));
                String arrayIdxExp = propertyNameFullStr.substring(propertyNameFullStr.indexOf("[") + 1, propertyNameFullStr.indexOf("]"));
                this.valueIdx = new IndxIterable(arrayIdxExp);
            } else {
                this.propertyName = propertyNameFullStr;
            }
        }

        /**
         * 将pathStr以“.”为分隔符，拆分为数组
         * @param pathStr
         * @return
         */
        public static PathExprs[] createPathExprs(final String pathStr) {
            PathExprs[] result = null;
            if (pathStr.contains(".")) {
                String[] paths = pathStr.split("\\.");
                result = new PathExprs[paths.length];
                for (int i = 0; i < paths.length; i++) {
                    result[i] = new PathExprs(paths[i]);
                }
            } else {
                result = new PathExprs[1];
                result[0] = new PathExprs(pathStr);
            }
            return result;
        }

        //

    }

    private static class IndxIterable implements Iterable {
        //路径表达式: name.sudents[1-10,30-n] name.students[1,2,5,7,9,n]
        private final String arrayIdxExprStr;
        private int maxIdx = -1;

        IndxIterable(final String pathStr) {
            if (pathStr == null || 0 == pathStr.length()) {
                throw new RuntimeException("pathStr can not be null.");
            }
            this.arrayIdxExprStr = pathStr;
        }

        public IdxIterator iterator() {
            return iterator(-1);
        }

        private boolean isAppend;

        public IdxIterator iterator(int maxIdx) {
            this.maxIdx = maxIdx;

            IdxIterator result = new IdxIterator();

            Cursor cursorTmp = null;

            //处理特殊的
            if (arrayIdxExprStr.equalsIgnoreCase(PathExprs.PATH_EXPR_ARRAY_IDX_ALL)) {
                if (maxIdx < 0) {
                    throw new RuntimeException("Get IdxIterator exception,the idxExps:[" + PathExprs.PATH_EXPR_ARRAY_IDX_ALL + "] can not use with the maxIdx:[" + maxIdx + "].");
                }
                cursorTmp = new Cursor(0, maxIdx, -1);
                result.addCursor(cursorTmp);
                return result;
            }

            String[] paths = arrayIdxExprStr.split("\\,");

            //找出所有下边变量中 的 n 使用 maxIdx来代替n
            for (int i = 0; i < paths.length; i++) {
                if (paths[i].equalsIgnoreCase(PathExprs.PATH_EXPR_ARRAY_IDX_MAX)) {
                    paths[i] = String.valueOf(maxIdx);
                } else if (paths[i].startsWith(PathExprs.PATH_EXPR_ARRAY_IDX_MAX + "-")) {
                    String subStr = paths[i].substring(paths[i].indexOf(PathExprs.PATH_EXPR_ARRAY_IDX_MAX + "-") + 2);
                    int resultIdx = maxIdx - Integer.valueOf(subStr);
                    paths[i] = String.valueOf(resultIdx);
                } else if (paths[i].equalsIgnoreCase(PathExprs.PATH_EXPR_ARRAY_IDX_SOURCE_APPEND)) {
                    paths[i] = "0-" + String.valueOf(maxIdx);
                    this.isAppend = true;
                } else if (paths[i].contains(PathExprs.PATH_EXPR_ARRAY_IDX_MAX)) {
                    throw new RuntimeException("unsupportted arrayIdxExps:[" + paths[i] + "] with variable:[n]!");
                }
            }

            String pathStr = null;

            int startSerialIdx = -1;
            int endSerialIdx = -1;

            //用来校验全局的下标，符合后面出现的下标序列总要比前面的大
            int globalIdx = -1;

            for (int i = 0; i < paths.length; i++) {
                pathStr = paths[i];
                if (pathStr.contains("-")) {
                    int splitIdx = pathStr.indexOf("-");
                    startSerialIdx = Integer.valueOf(paths[i].substring(0, splitIdx));
                    endSerialIdx = Integer.valueOf(paths[i].substring(splitIdx + 1));
                    cursorTmp = new Cursor(startSerialIdx, endSerialIdx, globalIdx);
                    globalIdx = endSerialIdx;
                    result.addCursor(cursorTmp);
                } else {
                    startSerialIdx = Integer.valueOf(pathStr);
                    endSerialIdx = Integer.valueOf(pathStr);
                    cursorTmp = new Cursor(startSerialIdx, endSerialIdx, globalIdx);
                    globalIdx = endSerialIdx;
                    result.addCursor(cursorTmp);
                }
            }
            result.setAppend(isAppend);
            return result;
        }

        public boolean isAppend() {
            return this.isAppend;
        }
    }

    /*private class IntervalIterator implements IndxIterable {
        public IdxIterator iterator() {
            return
        }
    }*/

    private static class IdxIterator implements Iterator<Integer> {
        private List<Cursor> allCursor;
        private int curCursorIdx = 0;

        private boolean append;

        IdxIterator() {
        }

        public void addCursor(Cursor cursor) {
            if (allCursor == null) {
                allCursor = new ArrayList<Cursor>(4);
            }
            allCursor.add(cursor);
        }

        public boolean hasNext() {
            if (allCursor == null || 0 == allCursor.size()) {
                return false;
            }
            if (!isInEndCursor()) {
                return false;
            }
            return true;
        }

        private boolean isInEndCursor() {
            if (curCursorIdx >= allCursor.size()) {
                return false;
            }
            return true;
        }

        public Integer next() {
            if (allCursor.get(curCursorIdx).curIdx > allCursor.get(curCursorIdx).endIdx) {
                throw new RuntimeException("IdxIterator next excption:[maybe an inner error!]");
            }
            int curIdx = allCursor.get(curCursorIdx).curIdx;
            allCursor.get(curCursorIdx).curIdx++;
            if (allCursor.get(curCursorIdx).curIdx > allCursor.get(curCursorIdx).endIdx) {
                curCursorIdx++;
            }
            return curIdx;
        }

        public void reset() {
            curCursorIdx = 0;
            for (int i = 0; i < allCursor.size(); i++) {
                allCursor.get(i).curIdx = allCursor.get(i).startIdx;
            }
        }

        public boolean isAppend() {
            return this.append;
        }

        public void setAppend(boolean isAppend) {
            this.append = isAppend;
        }

        public void remove() {
            throw new UnsupportedOperationException("IdxIterator can not suppurt remove!");
        }
    }

    private static class Cursor {
        private int startIdx;
        private int endIdx;
        private int curIdx;

        Cursor(final int startIdx, final int endIdx) {
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.curIdx = startIdx;
            if (endIdx < startIdx) {
                throw new RuntimeException("construct Cursor error,endIdx:[" + endIdx + "] can not little than startIdx:[" + startIdx + "].");
            }
        }

        Cursor(final int startIdx, final int endIdx, final int globalIdx) {
            if (startIdx <= globalIdx) {
                throw new RuntimeException("construct Cursor error,endIdx:[" + endIdx + "] can not little than startIdx:[" + startIdx + "].");
            }
            this.startIdx = startIdx;
            this.endIdx = endIdx;
            this.curIdx = startIdx;
            if (endIdx < startIdx) {
                throw new RuntimeException("construct Cursor error,endIdx:[" + endIdx + "] can not little than startIdx:[" + startIdx + "].");
            }
        }
    }


    public static class ParamSetterHelper {
        /**
         * 根据路径表达是设置value值到sourceValue对应的属性
         *
         * @param pathExpStr
         * @param sourceValue
         */
        public static void setValueBy(String pathExpStr, Object sourceValue, Object value) throws JSONException {
            PathExprs[] pathExprs = null;
            if (sourceValue instanceof JSONObject || sourceValue instanceof Map) {
                pathExprs = PathExprs.createPathExprs(pathExpStr);
                setValueLoop(pathExprs, (JSONObject) sourceValue, value, 0);
            } else {
                throw new InvalidParamTypeExcption("setValueBypathExpress exception,unsupportted sourceValue type:[" + sourceValue.getClass().getSimpleName() + "], only support Map or JSONObject.");
            }
        }

        private static void setValueLoop(final PathExprs[] pathExps, final Object sourceValue, final Object value, int curPathIdx) throws JSONException {
            PathExprs curPathExp = pathExps[curPathIdx];
            boolean isArray = curPathExp.valueIdx == null ? false : true;

            if (curPathIdx >= (pathExps.length - 1)) {
                //last level
                if (isArray) {
                    Object targetValue = getValue2Obj(sourceValue, curPathExp.propertyName, true, true);
                    IdxIterator it = curPathExp.valueIdx.iterator();
                    int nedSetValueObjIdx = -1;
                    //根据数组下标表达式，将值设置到所有对应的下标上
                    while (it.hasNext()) {
                        nedSetValueObjIdx = it.next();
                        // 校验，最后一级的值为数组时候，里面的对象必须是简单类型
                        if (!ValidateUtils.isSimpleDataType(getValue2Array(targetValue, nedSetValueObjIdx, true))) {
                            throw new RuntimeException("setValueLoop in last level value in array mast be simple data type! not suppurt:[" + getValue2Array(targetValue, nedSetValueObjIdx, true).getClass().getSimpleName() + "].");
                        }
                        //将value设置到对应的下标上
                        setValue2Array(targetValue, nedSetValueObjIdx, value);
                    }
                } else {
                    setValue2Obj(sourceValue, curPathExp.propertyName, value);
                }
            } else {
                Object targetValue = getValue2Obj(sourceValue, curPathExp.propertyName, true, isArray);

                //targetValue instanceof JSONArray || targetValue instanceof List
                if (isInnerSupportArray(targetValue)) {//根据 数据源 中 当前level：datas[] 中的下标来决定 iterator循环多少次

                    IdxIterator it = null;
                    boolean isValueArray = false;
                    //当需要设置到目标数据的 数组结构上的时候，对应的值也必须是数据类型
                    if (!ParamHelper.isInnerSupportArray(value)) {
                        //将单个值 赋值到数组的多个下标上
                        it = curPathExp.valueIdx.iterator();
                    } else {
                        int size = ParamHelper.getSize4InnerArray(value);
                        it = curPathExp.valueIdx.iterator(size - 1);
                        isValueArray = true;
                    }


                    int nedSetValueObjIdx = -1;
                    Object innerTartgetValue = null;
                    //根据数组下标表达式，将值设置到所有对应的下标上
                    Object innerValue = null;
                    int targetIdxTmp = -1;
                    while (it.hasNext()) {
                        nedSetValueObjIdx = it.next();

                        if (isValueArray) {
                            innerValue = getValueByIdx(value, nedSetValueObjIdx);
                        } else {
                            innerValue = value;
                        }


                        //将value设置到对应的下标上
                        if (it.isAppend()) {
                            targetIdxTmp = -1;
                        } else {
                            targetIdxTmp = nedSetValueObjIdx;
                        }
                        innerTartgetValue = getValue2Array(targetValue, targetIdxTmp, true);
                        setValueLoop(pathExps, innerTartgetValue, innerValue, curPathIdx + 1);
                    }
                } else {
                    setValueLoop(pathExps, targetValue, value, curPathIdx + 1);
                }
            }
        }
    }

    private static void setValue2Array(Object targetValue, final int idx, final Object newValue) throws JSONException {
        if (targetValue instanceof JSONArray) {
            if (idx >= 0) {
                ((JSONArray) targetValue).add(idx, newValue);
            } else {
                ((JSONArray) targetValue).add(newValue);
            }
        } else if (targetValue instanceof List) {
            if (idx >= 0) {
                ((List) targetValue).add(idx, newValue);
            } else {
                ((List) targetValue).add(newValue);
            }
        } else {
            throw new RuntimeException("setValue2Array excption,targetValue type invalid:[" + targetValue.getClass().getSimpleName() + "] Only support:[JSONArray|List].");
        }
    }

    private static Object getValue2Array(Object targetValue, final int idx, boolean isExpand) throws JSONException {
        Object result = null;
        if (targetValue instanceof JSONArray) {
            if (idx >= 0) {
                if (isExpand && ((JSONArray) targetValue).size() <= idx) {
                    //对于JsonArray类型的变量，不用向list那样一个个扩充，只需要设置最大的下标即可
                    ((JSONArray) targetValue).add(idx, new JSONObject());
                }

                if (idx >= ((JSONArray) targetValue).size()) {
                    return result;
                }

                result = ((JSONArray) targetValue).get(idx);
                if (isExpand && result == null) {
                    //不允许list<List<String>> 这样没有办法取值，当前规则不支持
                    //需要把list<String>最为key1放大Object1中 这样上面的结构就会变成List<Object1> list.key1[1,3,5]
                    result = new JSONObject();
                    ((JSONArray) targetValue).add(idx, result);
                }
            } else {
                // 直接扩充数组
                result = new JSONObject();
                ((JSONArray) targetValue).add(result);
            }
        } else if (targetValue instanceof List) {
            if (idx >= 0) {
                if (isExpand && ((List) targetValue).size() <= idx) {
                    int len = ((List) targetValue).size();
                    for (int t = 0; t < (idx + 1 - len); t++) {
                        ((List) targetValue).add(NULL_VALUE);
                    }
                }

                if (idx >= ((List) targetValue).size()) {
                    return result;
                }

                result = ((List) targetValue).get(idx);

                if (isExpand && result == null) {
                    result = new HashMap();
                    ((List) targetValue).add(idx, result);
                }
            } else {
                result = new HashMap();
                ((List) targetValue).add(result);
            }
        } else {
            throw new RuntimeException("getValue2Array excption,targetValue type invalid:[" + targetValue.getClass().getSimpleName() + "] Only support:[JSONArray|List].");
        }
        return result;
    }

    private static Object getValue2Obj(Object targetValue, final String propertyName) throws JSONException {
        return getValue2Obj(targetValue, propertyName, false, false);
    }

    private static Object getValue2Obj(Object targetValue, final String propertyName, final boolean isCreateIsEmpt, final boolean isArray) throws JSONException {
        Object result = null;
        if (targetValue instanceof JSONObject) {
            if (isCreateIsEmpt && !((JSONObject) targetValue).containsKey(propertyName)) {
                if (isArray) {
                    ((JSONObject) targetValue).put(propertyName, new JSONArray());
                } else {
                    ((JSONObject) targetValue).put(propertyName, new JSONObject());
                }
            }

            if (!((JSONObject) targetValue).containsKey(propertyName)) {
                return result;
            }
            result = ((JSONObject) targetValue).get(propertyName);
        } else if (targetValue instanceof Map) {
            if (isCreateIsEmpt && !((Map) targetValue).containsKey(propertyName)) {
                if (isArray) {
                    ((Map) targetValue).put(propertyName, new ArrayList());
                } else {
                    ((Map) targetValue).put(propertyName, new HashMap());
                }
            }
            if (!((Map) targetValue).containsKey(propertyName)) {
                return result;
            }
            result = ((Map) targetValue).get(propertyName);
        } else {
            throw new RuntimeException("getValue2Obj excption,targetValue type invalid:[" + targetValue.getClass().getSimpleName() + "] Only support:[JSONObject|Map]");
        }
        return result;
    }

    private static void setValue2Obj(Object targetValue, final String propertyName, Object value) throws JSONException {
        if (targetValue instanceof JSONObject) {
            ((JSONObject) targetValue).put(propertyName, value);
        } else if (targetValue instanceof Map) {
            ((Map) targetValue).put(propertyName, value);
        } else {
            throw new RuntimeException("setValue2Obj excption,targetValue type invalid:[" + targetValue.getClass().getSimpleName() + "] Only support:[JSONObject|Map]");
        }
    }

    public static class ParamGetterHelper {
        /**
         * 根据路径表达是获取目标对象的属性
         *
         * @param pathExpStr
         * @param sourceValue
         * @return
         * @throws JSONException
         */
        public static Object getValueBy(String pathExpStr, Object sourceValue) throws JSONException {
            PathExprs[] pathExprs = null;
            if (sourceValue instanceof JSONObject || sourceValue instanceof Map) {
                pathExprs = PathExprs.createPathExprs(pathExpStr);
                return getValueLoop(pathExprs, (JSONObject) sourceValue, 0);
            } else {
                throw new InvalidParamTypeExcption("getValueByValueBypathExpress exception,unsupportted sourceValue type:[" + sourceValue.getClass().getSimpleName() + "], only support Map or JSONObject.");
            }
        }

        private static Object getValueLoop(final PathExprs[] pathExps, final Object sourceValue, int curPathIdx) throws JSONException {
            PathExprs curPathExp = pathExps[curPathIdx];
            if (curPathIdx >= (pathExps.length - 1)) {
                //last level
                Object targetValue = getValue2Obj(sourceValue, curPathExp.propertyName);

                // 获取
                if (ValidateUtils.isNullOrEmpt(targetValue)) {
                    return null;
                }
                Object result = null;

                //targetValue instanceof JSONArray || targetValue instanceof List
                if (isInnerSupportArray(targetValue) && !ValidateUtils.isNullOrEmpt(curPathExp.valueIdx)) {
                    result = createResultValueBy(targetValue);
                    int maxIdx = getSize4InnerArray(targetValue);

                    IdxIterator it = curPathExp.valueIdx.iterator(maxIdx - 1);
                    int nedSetValueObjIdx = -1;
                    Object tempValue = null;
                    //根据数组下标表达式，将值设置到所有对应的下标上
                    while (it.hasNext()) {
                        nedSetValueObjIdx = it.next();
                        // 校验，最后一级的值为数组时候，里面的对象必须是简单类型
                        if (!ValidateUtils.isSimpleDataType(getValue2Array(targetValue, nedSetValueObjIdx, false))) {
                            throw new RuntimeException("getValueLoop in last level value in array mast be simple data type! not suppurt:[" + getValue2Array(targetValue, nedSetValueObjIdx, false).getClass().getSimpleName() + "].");
                        }
                        //将value设置到对应的下标上
                        tempValue = getValue2Array(targetValue, nedSetValueObjIdx, false);
                        setValue2Array(result, -1, tempValue);
                    }
                } else {
                    result = targetValue;
                }
                return result;
            } else {
                Object targetValue = getValue2Obj(sourceValue, curPathExp.propertyName);

                //如果原数据中不包含该key 则返回空的对象
                if (ValidateUtils.isNullOrEmpt(targetValue)) {
                    return null;
                }

                //
                Object result = null;
                if (isInnerSupportArray(targetValue)) {
                    result = createResultValueBy(targetValue);
                    int maxIdx = getSize4InnerArray(targetValue);
                    IdxIterator it = curPathExp.valueIdx.iterator(maxIdx - 1);
                    int nedSetValueObjIdx = -1;
                    Object innerTartgetValue = null;

                    Object tempValue = null;

                    //根据数组下标表达式，将值设置到所有对应的下标上
                    while (it.hasNext()) {
                        nedSetValueObjIdx = it.next();
                        //将value设置到对应的下标上
                        innerTartgetValue = getValue2Array(targetValue, nedSetValueObjIdx, false);
                        tempValue = getValueLoop(pathExps, innerTartgetValue, curPathIdx + 1);
                        setValue2Array(result, -1, tempValue);
                    }

                    return result;
                } else {
                    return getValueLoop(pathExps, targetValue, curPathIdx + 1);
                }
            }
        }
    }

    public static int getSize4InnerArray(final Object value) {
        int size = 0;
        if (value instanceof JSONArray) {
            size = ((JSONArray) value).size();
        } else if (value instanceof List) {
            size = ((List) value).size();
        } else {
            throw new RuntimeException("getSize4InnerArray exception,unsupport data type:[" + value.getClass().getSimpleName() + "].");
        }
        return size;
    }

    public static boolean isInnerSupportArray(final Object value) {
        if (value instanceof JSONArray || value instanceof List) {
            return true;
        } else {
            return false;
        }
    }

    public static Object getValueByIdx(final Object value, final int idx) {
        Object result = null;
        if (value instanceof JSONArray) {
            result = ((JSONArray) value).get(idx);
        } else if (value instanceof List) {
            result = ((List) value).get(idx);
        }
        return result;
    }

    public static void setValueByIdx(final Object sourceData, final int idx, final Object newValue) {
        if (sourceData instanceof JSONArray) {
            ((JSONArray) sourceData).add(idx, newValue);
        } else if (sourceData instanceof List) {
            ((List) sourceData).add(idx, newValue);
        }
    }

    public static Object createResultValueBy(final Object targetValue) {
        Object result = null;
        if (targetValue instanceof JSONArray) {
            result = new JSONArray();
        } else if (targetValue instanceof List) {
            result = new ArrayList();
        }
        return result;
    }
}
