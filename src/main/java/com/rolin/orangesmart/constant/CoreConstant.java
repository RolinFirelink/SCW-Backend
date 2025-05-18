package com.rolin.orangesmart.constant;

/**
 * 安全常量
 */
public final class CoreConstant {

    private CoreConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CACHE_KEY_SEPARATOR = ":";

    public static final String CACHE_CURRENT_ACCOUNT_PREFIX = "user:current:";

    public static final String CACHE_SAME_ACCOUNT_PREFIX = "user:same:";

    public static final String CACHE_ALL_ACCOUNT = "user:all";

    public static final String CACHE_CURRENT_CLIENT_PREFIX = "client:current:";

    public static final String CODE_KEY_SEPARATOR = ":";

    public static final String DOT_KEY_SEPARATOR = ".";


}