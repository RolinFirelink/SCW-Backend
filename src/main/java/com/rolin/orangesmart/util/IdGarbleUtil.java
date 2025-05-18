package com.rolin.orangesmart.util;

import org.springframework.util.ObjectUtils;

/**
 * id混淆工具类
 */
public class IdGarbleUtil {

    private IdGarbleUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static final Integer DATETIME_8_BIT = 8;
    public static final Integer DATETIME_15_BIT = 15;

    public static final Integer DATETIME_17_BIT = 17;


    public static Long idEncrypt(Long id) {
        if (!ObjectUtils.isEmpty(id) && id > 0 && String.valueOf(id).length() <= DATETIME_8_BIT) {
            // 获取8位的随机数
            String assistStr = getAssistNumberStr();
            // 获取补充的ID数据
            String complementStr = getComplement(id);
            StringBuilder rest = new StringBuilder();
            char[] assist = assistStr.toCharArray();
            char[] complement = complementStr.toCharArray();
            for (int i = 0; i < complement.length; i++) {
                if (i < assist.length) {
                    rest.append(assist[i]);
                }
                rest.append(complement[complement.length - 1 - i]);
            }
            rest.append(String.valueOf(id).length());
            return Long.valueOf(rest.toString());
        }
        return id;
    }

    public static Long idDecrypt(Long id) {
        if (!ObjectUtils.isEmpty(id) && id > 0 && String.valueOf(id).length() == DATETIME_15_BIT) {
            StringBuilder rest = new StringBuilder();
            char[] arr = String.valueOf(id).toCharArray();
            for (int i = 0; i < DATETIME_15_BIT - 1; i++) {
                if (i % 2 != 0 && i < 12) {
                    rest.append(arr[i]);
                }
                if (i >= 12) {
                    rest.append(arr[i]);
                }
            }
            rest.reverse();
            //char转int使用的是ASCII码，因此需减48
            String result = rest.substring(DATETIME_8_BIT - (Integer.valueOf(arr[arr.length - 1]) - 48));
            String lastValue = String.valueOf(arr[arr.length - 1]);
            if (String.valueOf(Long.valueOf(result)).length() == Integer.valueOf(lastValue)) {
                return Long.valueOf(result);
            }
        }
        //兼容csms17位加密形式
        if (!ObjectUtils.isEmpty(id) && id > 0 && String.valueOf(id).length() == DATETIME_17_BIT) {
            StringBuilder rest = new StringBuilder();
            char[] arr = String.valueOf(id).toCharArray();
            for (int i = arr.length - 2; i > 0; i = i - 2) {
                rest.append(arr[i]);
            }
            String lastValue = String.valueOf(arr[arr.length - 1]);
            if (String.valueOf(Long.valueOf(rest.toString())).length() == Integer.valueOf(lastValue)) {
                return Long.valueOf(rest.toString());
            }
        }
        return id;
    }

    // 获取6位的随机数
    private static String getAssistNumberStr() {
        StringBuilder sb = new StringBuilder("1");
        sb.append(RandomUtil.obtainRandomStr(5, RandomUtil.NUMBER_ARR));
        return sb.toString();
    }

    // 获取补充的ID数据
    private static String getComplement(Long id) {
        StringBuilder complement = new StringBuilder();
        complement.append(String.format("%08d", Math.abs(id)));
        return complement.toString();
    }

//  public static void main(String[] args) {
//    long t = IdGarbleUtil.idEncrypt(123L);
//    System.out.println(t);
//    System.out.println(IdGarbleUtil.idDecrypt(t));
//  }
}