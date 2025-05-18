package com.rolin.orangesmart.constant;

/**
 * StorageConstant
 */
public class StorageConstant {

    private StorageConstant() {

    }

    public final static String PERIOD = ".";
    public final static String SEPARATOR = "/";

    public final static String WINDOWS_SEPARATOR = "\\\\";

    public final static String STATIC_STORAGE_PATH = SEPARATOR + "static";

    public final static String STAGING_STORAGE_PATH = SEPARATOR + "staging";

    public final static String YEAR_PARAM =  "${year}";
    public final static String MONTH_PARAM = "${month}";
    public final static String DAY_PARAM = "${day}";

    public final static String YEAR_MONTH_DAY_PARAM_PATH =
                    SEPARATOR + YEAR_PARAM +
                    SEPARATOR + MONTH_PARAM +
                    SEPARATOR + DAY_PARAM;

}
