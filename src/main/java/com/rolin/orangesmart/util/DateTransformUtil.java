package com.rolin.orangesmart.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Slf4j
public final class DateTransformUtil {


    private DateTransformUtil() {

    }

    public static final String DATETIME_ZH_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_ZH_WITH_ZONE = "yyyy-MM-dd HH:mm:ssZ";

    /**
     * @param date
     * @param dateFormat 代时区格式，必须使用ZonedDateTime，而不是LocalDateTime
     * @return
     */
    public static String date2string(Date date, String dateFormat) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat, Locale.getDefault(Locale.Category.FORMAT));
        ZonedDateTime zonedDateTime = ZonedDateTime.of(DateTransformUtil.date2LocalDateTime(date), ZoneId.systemDefault());
        String str = zonedDateTime.format(formatter);
        return str;
    }


    /**
     * @return Date
     * @Description: <b>（日期）字符串转日期（yyyy-MM-dd）</b>
     * @author Sera/vision
     * @date 2018年10月19日14:13:02
     */
    public static Date string2date(String dateStr, String dateFormat) {
        if (!StringUtils.hasText(dateStr) || !StringUtils.hasText(dateFormat)) {
            return null;
        }
        if (dateStr.length() < dateFormat.length()) {
            return null;
        }
        dateStr = dateStr.substring(0, dateFormat.length());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat, Locale.getDefault(Locale.Category.FORMAT));
        Date date = null;
        if (dateFormat.length() <= 10) {
            //只有日期
            LocalDate localDate = LocalDate.parse(dateStr, dtf);
            date = localDate2Date(localDate);
        } else {
            //日期+时间
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, dtf);
            date = localDateTime2Date(localDateTime);
        }
        return date;
    }


    public static LocalDateTime date2LocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalDate date2LocalDate(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    public static Date localDate2Date(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static void main(String[] args) {

        Date date = new Date();
        String dateString = DateTransformUtil.date2string(date, DATETIME_ZH_WITH_ZONE);
        System.out.println(dateString);
        dateString = DateTransformUtil.date2string(date,"yyyy-MM-dd HH:mm:ss");
        System.out.println(dateString);


    }
}