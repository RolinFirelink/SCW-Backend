package com.rolin.orangesmart.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public final class DateCommonUtil {

    private DateCommonUtil(){

    }

    private static Date maxDate;

    static {
        LocalDateTime localDateTime = LocalDateTime.of(2100, 12, 31, 23, 59, 59);
        maxDate = DateTransformUtil.localDateTime2Date(localDateTime);
    }


    public static String getCurrentYear() {
        return String.valueOf(LocalDateTime.now().getYear());
    }

    public static String now(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.now().format(formatter);
    }

    public static Date getThisMonthFirstDay() {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        Date date = DateTransformUtil.localDate2Date(localDate);
        return date;
    }

    public static Date getThisMonthLastDay() {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        Date date = DateTransformUtil.localDate2Date(localDate);
        return date;
    }

    /**
     * 指定时间的年份
     *
     * @return 年份整数值
     */
    public static int getYear(Date date) {
        return DateTransformUtil.date2LocalDateTime(date).getYear();
    }

    /**
     * 今年
     *
     * @return 今年年份整数值
     */
    public static int getThisYear() {
        return LocalDate.now().getYear();
    }

    /**
     * 明年
     *
     * @return 明年年份整数值
     */
    public static int getNextYear() {
        return LocalDate.now().plusYears(1L).getYear();
    }

    /**
     * 后年
     *
     * @return 后年年份整数值
     */
    public static int getAfterYear() {
        return LocalDate.now().plusYears(2L).getYear();
    }

    /**
     * 指定时间的月份
     *
     * @return 月份整数值
     */
    public static int getMonth(Date date) {
        return DateTransformUtil.date2LocalDateTime(date).getMonthValue();
    }

    /**
     * 当月
     *
     * @return 当月月份整数值
     */
    public static int getThisMonth() {
        return LocalDate.now().getMonthValue();
    }

    /**
     * 下月
     *
     * @return 下月月份整数值
     */
    public static int getNextMonth() {
        return LocalDate.now().plusMonths(1L).getMonthValue();
    }

    /**
     * 下下月
     *
     * @return 下下月份整数值
     */
    public static int getAfterMonth() {
        return LocalDate.now().plusMonths(2L).getMonthValue();
    }

    public static Date getMaxDate() {
        return maxDate;
    }

    /**
     * <b>根据出生日期返回年龄</b>
     * <b>需求：返回规则2014-01-01和2014-12-30 均返回（当前年份-2014）</b>
     *
     * @param birthdayDate
     * @return age
     */
    public static int getAgeByBirth(Date birthdayDate) {
        LocalDate todayLocalDate = LocalDate.now();
        LocalDate birthdayLocalDate = DateTransformUtil.date2LocalDate(birthdayDate);
        int year = (int) ChronoUnit.YEARS.between(birthdayLocalDate, todayLocalDate);
        if (year < 0) {
            year = 0;
        }
        return year;
    }

    /**
     * 得到开始日期到结束日期的所有日期
     *
     * @param start
     * @param end
     * @return
     */
    public static List<LocalDate> getAllDate(Date start, Date end) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate startDate = DateTransformUtil.date2LocalDate(start);
        LocalDate endDate = DateTransformUtil.date2LocalDate(end);

        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return Collections.emptyList();
        }
        Stream.iterate(startDate, d -> {
            return d.plusDays(1);
        }).limit(distance + 1).forEach(f -> {
            dates.add(f);
        });
        return dates;
    }

    public static Date getOffsetedDateWithSeconds(Date sourceDate, long offsetSeconds) {
        LocalDateTime localDateTime = DateTransformUtil.date2LocalDateTime(sourceDate);
        localDateTime = localDateTime.plusSeconds(offsetSeconds);
        return DateTransformUtil.localDateTime2Date(localDateTime);
    }

    public static Date getOffsetedDateWithDays(Date sourceDate, long offsetDays) {
        LocalDateTime localDateTime = DateTransformUtil.date2LocalDateTime(sourceDate);
        localDateTime = localDateTime.plusDays(offsetDays);
        return DateTransformUtil.localDateTime2Date(localDateTime);
    }

}