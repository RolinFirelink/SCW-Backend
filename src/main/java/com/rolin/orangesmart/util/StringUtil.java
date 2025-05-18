package com.rolin.orangesmart.util;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

    private static Logger logger = LoggerFactory.getLogger(StringUtil.class);

    private StringUtil() {

    }

    public static String underline2Camel(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        String temp = value.toLowerCase();
        final StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile("_(\\w)");// 表示一个“字”（数字，字符，下划线）
        Matcher m = p.matcher(temp);
        while (m.find()) {
            // appendReplacement：将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        // appendTail：将最后一次匹配工作后剩余的字符串添加到一个StringBuffer对象里
        m.appendTail(sb);

        String rtnValue = sb.toString();
        logger.info("下划线转驼峰：" + value + " --> " + rtnValue);
        return rtnValue;
    }

    public static String leftPatchZero(int length, long value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("0");
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        String str = df.format(value);
        return str;
    }

    public static String replaceUnderlineOrPercent(String content) {
        final String SOURCE_UNDERLINE = "_";
        final String SOURCE_PERCENT = "%";
        final String SOURCE_SLASHLINE = "\\\\";
        final String TARGET_UNDERLINE = "\\\\_";
        final String TARGET_PERCENT = "\\\\%";
        final String TARGET__SLASHLINE = "\\\\\\\\";
        if (StringUtils.hasText(content)) {
            String result = content.replaceAll(SOURCE_SLASHLINE, TARGET__SLASHLINE)
                    .replaceAll(SOURCE_UNDERLINE, TARGET_UNDERLINE)
                    .replaceAll(SOURCE_PERCENT, TARGET_PERCENT);
            return result;
        } else {
            return content;
        }
    }

//
//    public static String join(final Object[] array, final char separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            if (array[i] != null) {
//                buf.append(array[i]);
//            }
//        }
//        return buf.toString();
//    }
//
//    public static String join(final Object[] array, String separator, final int startIndex, final int endIndex) {
//        if (array == null) {
//            return null;
//        }
//        if (separator == null) {
//            separator = EMPTY;
//        }
//        final int noOfItems = endIndex - startIndex;
//        if (noOfItems <= 0) {
//            return EMPTY;
//        }
//        final StringBuilder buf = new StringBuilder(noOfItems * 16);
//        for (int i = startIndex; i < endIndex; i++) {
//            if (i > startIndex) {
//                buf.append(separator);
//            }
//            if (array[i] != null) {
//                buf.append(array[i]);
//            }
//        }
//        return buf.toString();
//    }
//
//    public static String join(final Object[] array, final String separator) {
//        if (array == null) {
//            return null;
//        }
//        return join(array, separator, 0, array.length);
//    }

    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }
    public static String subString(String content, int maxLength) {
        if(StringUtils.hasText(content)) {
            content = content.length() > maxLength ? content.substring(0, maxLength) : content;
        }
        return content;
    }

}