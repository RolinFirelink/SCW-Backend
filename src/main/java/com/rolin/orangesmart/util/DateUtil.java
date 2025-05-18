package com.rolin.orangesmart.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class DateUtil {

  private DateUtil() {
  }

  public static final String DATE_DEFAULT_PATTERN = "yyyyMMdd";

  public static final String DATE_yyyyMM_PATTERN = "yyyyMM";

  public static final String DATE_CDR_DEFAULT_PATTERN = "yyyyMMddHHmmss";

  public static final String DATETIME_ZH_DEFAULT = "yyyy-MM-dd HH:mm:ss";

  public static final String DATE_ZH_DEFAULT = "yyyy-MM-dd";

  public static final String DATE_MM_dd_yyyy_PATTERN = "MM/dd/yyyy";

  public static final String MONTHLY_REPORT_DATE_PATTERN = "MM-yyyy";

  public static final String MINUTELY_APPROVE_PATTERN = "yyyy-MM-dd HH:mm";

  public static final String MINUTELY_ONLY_APPROVE_PATTERN = "HH:mm";

  public static String getTimePeriod(Date startDate, Date endDate) {
    if (startDate == null || endDate == null) {
      log.error("开始时间或结束时间为空");
      return null;
    }
    if (endDate.before(startDate)) {
      log.error("结束时间小于开始时间");
      return null;
    }
    String start = date2string(startDate, DATE_ZH_DEFAULT);
    String end = date2string(endDate, DATE_ZH_DEFAULT);
    return start + "~" + end;
  }

  //获取傳入时间的前一天时间
  public static Date getLastDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day = calendar.get(Calendar.DATE);
    calendar.set(Calendar.DATE, day - 1);
    return calendar.getTime();
  }

  //获取傳入时间的当天时间
  public static Date getToday(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day = calendar.get(Calendar.DATE);
    calendar.set(Calendar.DATE, day);
    return calendar.getTime();
  }

  //获取傳入时间的前一天零点时间
  public static Date getLastDayZero(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day = calendar.get(Calendar.DATE);
    calendar.set(Calendar.DATE, day - 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public static Date getLastWeekMondayZero(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    // 获得当前日期是一个星期的第几天
    int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
    if (1 == dayWeek) {
      calendar.add(Calendar.DAY_OF_MONTH, -1);
    }
    // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    // 获得当前日期是一个星期的第几天
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
    calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
    calendar.add(Calendar.DATE, -7);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public static Date getThisWeekMondayZero(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    // 获得当前日期是一个星期的第几天
    int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
    if (1 == dayWeek) {
      calendar.add(Calendar.DAY_OF_MONTH, -1);
    }
    // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    // 获得当前日期是一个星期的第几天
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
    calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public static Date getLastMonthStartDayZero(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public static Date getThisMonthStartDayZero(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public static Date getNextDay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day = calendar.get(Calendar.DATE);
    calendar.set(Calendar.DATE, day + 1);
    return calendar.getTime();
  }

  public static Date getNextDayZero(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int day = calendar.get(Calendar.DATE);
    calendar.set(Calendar.DATE, day + 1);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public static LocalDate getLocalDate(Date date) {
    Instant instant = date.toInstant();
    ZoneId zone = ZoneId.systemDefault();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
    return localDateTime.toLocalDate();
  }

  public static LocalDate getLastWeekLocalDate(Date date) {
    Instant instant = date.toInstant();
    ZoneId zone = ZoneId.systemDefault();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
    return localDateTime.minusWeeks(1).toLocalDate();
  }

  public static Date getDateZero(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public static Date getDateZero(String dateString, String pattern) {
    if (StringUtils.isEmpty(pattern)) {
      pattern = DATE_DEFAULT_PATTERN;
    }
    Calendar calendar = Calendar.getInstance();
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(pattern);
      calendar.setTime(sdf.parse(dateString));
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      return calendar.getTime();
    } catch (ParseException e) {
      log.error("DateUtils dateParse Error", e);
    }
    return null;
  }

  public static Date getNextMonthLastDayZero(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.HOUR_OF_DAY, -1);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }


  public static String dateFormat(Date date, String pattern) {
    if (StringUtils.isEmpty(pattern)) {
      pattern = DATE_DEFAULT_PATTERN;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);
  }

  public static Date string2date(String dateStr, String dateFormat) {
    return string2date(dateStr, dateFormat, Locale.getDefault(Locale.Category.FORMAT));
  }

  public static Date string2date(String dateStr, String dateFormat, Locale locale) {
    if (!StringUtils.hasText(dateStr) || !StringUtils.hasText(dateFormat)) {
      return null;
    }
    if (dateStr.length() < dateFormat.length()) {
      return null;
    }
    dateStr = dateStr.substring(0, dateFormat.length());

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat, locale);
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

  public static Date localDateTime2Date(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    ZoneId zoneId = ZoneId.systemDefault();
    ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
    return Date.from(zonedDateTime.toInstant());
  }

  public static Date localDate2Date(LocalDate localDate) {
    if (localDate == null) {
      return null;
    }
    ZoneId zoneId = ZoneId.systemDefault();
    ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
    return Date.from(zonedDateTime.toInstant());
  }

  public static String date2string(Date date, String dateFormat) {
    if (date == null) {
      return null;
    }
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
    LocalDateTime localDateTime = date2LocalDateTime(date);
    return localDateTime.format(dtf);
  }

  public static LocalDateTime date2LocalDateTime(Date date) {
    if (date == null) {
      return null;
    }
    Instant instant = date.toInstant();
    ZoneId zoneId = ZoneId.systemDefault();
    return instant.atZone(zoneId).toLocalDateTime();
  }

  public static List<String> getDays(int size) {
    List<String> sevenDays = new ArrayList<>();
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    for (int i = 0; i < size; i++) {
      LocalDate date = today.minusDays(i);
      sevenDays.add(date.format(formatter));
    }

    return sevenDays;
  }

}