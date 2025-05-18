package com.rolin.orangesmart.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 随机数/随机字符 工具包
 */
public class RandomUtil {

    private RandomUtil() {
    }

    private static Random rd = new Random();

    public static final Character[] CUSTOMIZED_ARR = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static final Character[] TOTAL_ARR = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    public static final Character[] CHARACTER_ARR = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static final Character[] CHARACTER_UPPER_ARR = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static final Character[] CHARACTER_LOWER_ARR = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static final Character[] NUMBER_ARR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static final Character[] BASE58_NUMBER_ARR = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static final Character[] BASE58_CHARACTER_UPPER_ARR = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public static final Character[] BASE58_CHARACTER_LOWER_ARR = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static String obtainRandomStr(int len, Character[]... charRangeArrays) {
        if (charRangeArrays == null) {
            return null;
        }
        if (len < charRangeArrays.length) {
            return null;
        }

        List<Character> lists = new ArrayList<Character>();
        char[] chArr = new char[len];
        //确保每个数组中选定一个
        for (int i = 0; i < charRangeArrays.length; i++) {
            final int maxNum = charRangeArrays[i].length;
            chArr[i] = charRangeArrays[i][rd.nextInt(maxNum)];

            lists.addAll(Arrays.asList(charRangeArrays[i]));
        }

        for (int i = charRangeArrays.length; i < len; i++) {
            chArr[i] = lists.get(rd.nextInt(lists.size()));
        }

        for (int i = 0; i < len; i++) {
            int r = i + rd.nextInt(len - i);
            char temp = chArr[i];
            chArr[i] = chArr[r];
            chArr[r] = temp;
        }

        return new String(chArr);
    }

    public static int obtainRandomNumber(int maxNumber) {
        int random = Math.abs(rd.nextInt(maxNumber));// maxNum=100时， 100是不包含在内的，只产生0~99之间的数
        random = random + 1;
        return random;
    }

    public static String fixedLengthRandomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(rd.nextInt(10));
        }
        return sb.toString();
    }

    public static int nextInt() {
        return rd.nextInt();
    }

    public static int nextInt(int bound) {
        return rd.nextInt(bound);
    }

}