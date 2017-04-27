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


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工具箱。 采用ThreadLocal缓存SAXParser、SimpleDateFormat等非线程安全的工具对象，以减少创建 这些工具对象的开销。
 *
 * @version 1.0
 */
public class Toolkit {
    /**
     * DATE_FORMART_CACHE_KEY
     */
    private static final String DATE_FORMART_CACHE_KEY = "$$$$_dateFormatCache$$$$_";
    /**
     * SAX_PARSER_KEY
     */
    private static final String SAX_PARSER_KEY = "$$$$_saxParser$$$$_";

    /**
     * SAX_PARSER_KEY
     */
    private static final String SAX_READER_KEY = "$$$$_saxReader$$$$_";
    /**
     * LOCALE_KEY
     */
    private static final String LOCALE_KEY = "$$$$_locale_key$$$$_";

    /**
     * BEANSHELL_ENGINE_KEY
     */
    private static final String BEANSHELL_ENGINE_KEY = "$$$$_beanshell_engine_key_$$$";

    /**
     * 当前线程中用于缓存工具对象的缓存
     */
    private static ThreadLocal<MyMap> cache = new ThreadLocal<MyMap>();

    /**
     * 已缓存工具对象的线程集合
     */
    private static Set<Thread> threads = new HashSet<Thread>(126);

    // private static Map<Thread, ThreadLocal> threads = new HashMap<Thread,
    // ThreadLocal>(
    // 512);

    /**
     *
     */
    @SuppressWarnings({"serial", "unchecked"})
    private static class MyMap extends HashMap {
    }

    /**
     * 取得当前线程缓存的DateFormat
     *
     * @param pattern 日期模板
     * @return 当前线程缓存的DateFormat
     */
    @SuppressWarnings("unchecked")
    public static DateFormat getDateFormat(String pattern) {
        if (null == pattern) {
            throw new IllegalArgumentException("pattern is null");
        }

        Map cacheMap = getCacheMap();
        Map<String, DateFormat> dateFormatCache = (Map<String, DateFormat>) cacheMap.get(DATE_FORMART_CACHE_KEY);
        if (null == dateFormatCache) {
            dateFormatCache = new HashMap<String, DateFormat>(32);
            cacheMap.put(DATE_FORMART_CACHE_KEY, dateFormatCache);
        }

        DateFormat dateFormat = dateFormatCache.get(pattern);
        if (null == dateFormat) {
            dateFormat = new SimpleDateFormat(pattern);
            dateFormatCache.put(pattern, dateFormat);
        }
        return dateFormat;
    }

    /**
     * 取得当前线程缓存的SAXParser
     *
     * @return 当前线程缓存的SAXParser
     */
//    public static SAXParser getSAXParser() {
//        Map cacheMap = getCacheMap();
//        SAXParser parser = (SAXParser) cacheMap.get(SAX_PARSER_KEY);
//        if (null == parser) {
//            SAXParserFactoryImpl saxParserFactory = new SAXParserFactoryImpl();
//            try {
//                parser = saxParserFactory.newSAXParser();
//            } catch (ParserConfigurationException e) {
//                ExceptionUtil.throwActualException(e);
//            }
//            cacheMap.put(SAX_PARSER_KEY, parser);
//        } else {
//            // 已使用过的SAXParser需清理一下再使用
//            parser.reset();
//        }
//        return parser;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static SAXReader getSAXReader() {
//        Map cacheMap = getCacheMap();
//        SAXReader localSAXReader = (SAXReader) cacheMap.get(SAX_READER_KEY);
//        if (null == localSAXReader) {
//            localSAXReader = new SAXReader();
//            cacheMap.put(SAX_READER_KEY, localSAXReader);
//        } else {
//            // 已使用过的SAXParser需清理一下再使用
//            // localSAXReader.
//        }
//        return localSAXReader;
//    }

    /**
     * 取得当前线程缓存的Locale
     *
     * @param localeString localeString
     * @return Locale
     */
    @SuppressWarnings("unchecked")
    public static Locale parseLocale(String localeString) {
        if (null == localeString || localeString.trim().length() == 0) {
            throw new IllegalArgumentException("pattern is null");
        }

        Map cacheMap = getCacheMap();
        Map<String, Locale> locales = (Map<String, Locale>) cacheMap.get(LOCALE_KEY);
        if (null == locales) {
            locales = new HashMap<String, Locale>(32);
            cacheMap.put(LOCALE_KEY, locales);
        }

        Locale locale = locales.get(localeString);
        if (null == locale) {
            if (localeString.length() == 2) {
                // two letter language code
                locale = new Locale(localeString);
            } else if (localeString.length() == 5) {
                // positions 0-1 language, 3-4 are country
                String language = localeString.substring(0, 2);
                String country = localeString.substring(3, 5);
                locale = new Locale(language, country);
            } else if (localeString.length() > 6) {
                // positions 0-1 language, 3-4 are country, 6 and on are
                // special extensions
                String language = localeString.substring(0, 2);
                String country = localeString.substring(3, 5);
                String extension = localeString.substring(6);
                locale = new Locale(language, country, extension);
            } else {
            }
            locales.put(localeString, locale);
        }
        return locale;
    }

    /**
     * 取得当前线程缓存的BeanShellEngine
     *
     * @return 当前线程缓存的BeanShellEngine
     */
//    public static BeanShellEngine getBeanShellEngine() {
//        Map cacheMap = getCacheMap();
//        BeanShellEngine engine = (BeanShellEngine) cacheMap.get(BEANSHELL_ENGINE_KEY);
//        if (null == engine) {
//            engine = new BeanShellEngineDecorator();
//            cacheMap.put(BEANSHELL_ENGINE_KEY, engine);
//        } else {
//            // 已使用过的engine需清理一下再使用
//            ((BeanShellEngineDecorator) engine).clearAllVariables();
//        }
//        return engine;
//    }

//    private static class BeanShellEngineDecorator extends BeanShellEngine {
//        private List<String> settedVariableNames = null;
//
//        @Override
//        public void set(String name, Object value) {
//            super.set(name, value);
//
//            if (settedVariableNames == null) {
//                settedVariableNames = new ArrayList<String>();
//            }
//            settedVariableNames.add(name);
//        }
//
//        public void clearAllVariables() {
//            if (settedVariableNames != null) {
//                for (String variableName : settedVariableNames) {
//                    // unset(variableName);
//                }
//            }
//        }
//
//    }
    private static Map getCacheMap() {
        MyMap cacheMap = cache.get();
        if (null == cacheMap) {
            cacheMap = new MyMap();
            cache.set(cacheMap);

            // 当前线程首次创建工具对象缓存时，需将自己登记到集合中，以便集中清理缓存
            synchronized (threads) {
                threads.add(Thread.currentThread());
            }
        }
        return cacheMap;
    }

    /**
     * 清理所有线程的工具箱(ThreadLocal)
     */
    public static void clearAllThreadsToolkit() {
        synchronized (threads) {
            if (0 == threads.size()) {
                return;
            }
            try {
                clearThreadLocals(threads, MyMap.class.getName());
            } catch (Exception e) {
                throw new RuntimeException("clearAllThreadsToolkit excption:[" + e.getMessage() + "].", e);
            }
        }
    }

    /**
     * @return 是否清除
     */
    public static boolean isClear() {
        return null == cache.get();
    }

    /**
     * 反射清理threadlocals变量
     *
     * @param targetThreadsSet       目标线程集合
     * @param targetThreadLocalClazz 目标threadlocal变量类型
     * @throws NoSuchFieldException     没有threadlocals变量时抛出
     * @throws SecurityException        访问threadlocals变量是出错抛出
     * @throws ClassNotFoundException   找不到ThreadLocalMap类时抛出
     * @throws IllegalAccessException   找不到线程的threadlocals变量时抛出
     * @throws IllegalArgumentException 找threadlocals变量中非法参数时抛出
     */
    @SuppressWarnings("unchecked")
    public static void clearThreadLocals(Set<Thread> targetThreadsSet, String targetThreadLocalClazz)
            throws SecurityException, NoSuchFieldException, ClassNotFoundException, IllegalArgumentException,
            IllegalAccessException {
        // System.out.println(targetThreadLocalClazz);
        for (Thread noClearedThread : targetThreadsSet) {
            Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);

            Class threadLocalMapKlazz = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            Field tableField = threadLocalMapKlazz.getDeclaredField("table");
            tableField.setAccessible(true);

            Object fieldLocal = threadLocalsField.get(noClearedThread);
            if (fieldLocal == null) {
                return;
            }
            Object table = tableField.get(fieldLocal);

            int threadLocalCount = Array.getLength(table);

            for (int i = 0; i < threadLocalCount; i++) {
                Object entry = Array.get(table, i);
                if (entry != null) {
                    Field valueField = entry.getClass().getDeclaredField("value");
                    valueField.setAccessible(true);
                    Object value = valueField.get(entry);
                    if (value != null) {
                        if (value.getClass().getName().equals(targetThreadLocalClazz)) {
                            valueField.set(entry, null);
                        }
                    }

                }
            }

            // targetThreadsSet.remove(noClearedThread);
        }

        targetThreadsSet.clear();
    }

    public static void clearThreadLocal4Thread(Thread noClearedThread) {
        try {
            // 清理当前线程缓存
            clearThreadLocal4Thread(threads, MyMap.class.getName(), noClearedThread);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void clearThreadLocal4Thread(Set<Thread> targetThreadsSet, String targetThreadLocalClazz,
                                               Thread noClearedThread) throws SecurityException, NoSuchFieldException, ClassNotFoundException,
            IllegalArgumentException, IllegalAccessException {
        // 先从当先集合总移除该线程
        targetThreadsSet.remove(noClearedThread);

        Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
        threadLocalsField.setAccessible(true);

        Class threadLocalMapKlazz = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
        Field tableField = threadLocalMapKlazz.getDeclaredField("table");
        tableField.setAccessible(true);

        Object fieldLocal = threadLocalsField.get(noClearedThread);
        if (fieldLocal == null) {
            return;
        }
        Object table = tableField.get(fieldLocal);

        int threadLocalCount = Array.getLength(table);

        for (int i = 0; i < threadLocalCount; i++) {
            Object entry = Array.get(table, i);
            if (entry != null) {
                Field valueField = entry.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                Object value = valueField.get(entry);
                if (value != null) {
                    if (value.getClass().getName().equals(targetThreadLocalClazz)) {
                        valueField.set(entry, null);
                    }
                }
            }
        }
    }
}
