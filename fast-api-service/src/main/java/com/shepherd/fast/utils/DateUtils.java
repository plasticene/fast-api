package com.shepherd.fast.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/21 16:36
 */
public class DateUtils {
    /** 日期格式化参数:yyyy-MM-dd */
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

    public static final String NEW_DATE_FORMAT = "yyyy/MM/dd";

    /** 日期格式化参数:yyyy-MM-dd HH:mm:ss */
    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 日期格式化参数:yyyy-MM-dd HH:mm */
    public static final String FULL_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";

    /** 日期格式化参数:yyyy-MM */
    public static final String YEAR_MONTH_DATE_FORMAT = "yyyy-MM";

    /** 日期格式化参数:yyyyMMddHHmmss */
    public static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    private static SimpleDateFormat fullDateFormat = new SimpleDateFormat(FULL_DATE_FORMAT);
    private static SimpleDateFormat yearMonthDateFormat =
            new SimpleDateFormat(YEAR_MONTH_DATE_FORMAT);
    private static SimpleDateFormat dateTimeFormat =
            new SimpleDateFormat(DATE_TIME_FORMAT);

    /**
     * 将日期格式化相相应的格式. <br>
     *
     * @param date 日期
     * @param format 格式
     * @return 格式化的时间字符串
     * @author unknow
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static String formatDate(Date date, String format) {
        String datestr = "";
        if (date != null && StringUtils.isNotBlank(format)) {
            if (SIMPLE_DATE_FORMAT.equals(format)) {
                datestr = simpleDateFormat.format(date);
            } else if (FULL_DATE_FORMAT.equals(format)) {
                datestr = fullDateFormat.format(date);
            } else if (YEAR_MONTH_DATE_FORMAT.equals(format)) {
                datestr = yearMonthDateFormat.format(date);
            } else if (DATE_TIME_FORMAT.equals(format)) {
                datestr = dateTimeFormat.format(date);
            }
        }
        return datestr;
    }

//  public static void main(String[] args) {
//    String s = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
//    Date d = getDayBeforeXDay(getStartTimeOfDate(new Date()),-3);
//    System.out.println(s);
//  }

    /**
     * 解析日期时间.将时间日期字符串通过相应的格式进行解析.
     *
     * @param strDate 某种格式的时间字符串形式
     * @param format 时间格式
     * @return 解析之后的时间对象
     * @author unknow
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static Date parseDate(String strDate, String format) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.parse(strDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 将日期时间{@code date}格式化为yyyy-MM-dd形式
     *
     * @param date 日期时间参数
     * @return 格式化之后的时间字符串，形式为yyyy-MM-dd
     * @author unknow
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static String formatDate(Date date) {
        return formatDate(date, SIMPLE_DATE_FORMAT);
    }

    /**
     * 按照格式yyyy-MM-dd将时间字符串解析成相应的时间
     *
     * @param strDate 某种格式的时间字符串
     * @return 解析之后的时间对象
     * @author unkonw
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static Date parseDate(String strDate) {
        return parseDate(strDate, SIMPLE_DATE_FORMAT);
    }

    /**
     * 将日期时间格式化为yyyy-MM-dd HH:mm:ss形式
     *
     * @param date 日期时间参数
     * @return 格式化之后的时间字符串，形式为yyyy-MM-dd HH:mm:ss
     * @author unknow
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static String formatFullDate(Date date) {
        return formatDate(date, FULL_DATE_FORMAT);
    }

    /**
     * 按照格式yyyy-MM-dd HH:mm:ss将时间字符串解析成相应的时间
     *
     * @param strDate 某种格式的时间字符串
     * @return 解析之后的时间对象
     * @author unkonw
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static Date parseDateFull(String strDate) {
        return parseDate(strDate, FULL_DATE_FORMAT);
    }

    /**
     * 按照格式yyyy-MM将时间字符串解析成相应的时间
     *
     * @param strDate 某种格式的时间字符串
     * @return 解析之后的时间对象
     * @author unkonw
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static Date parseDateYearMonth(String strDate) {
        return parseDate(strDate, YEAR_MONTH_DATE_FORMAT);
    }

    /**
     * 将日期时间格式化为yyyy-MM形式
     *
     * @param date 日期时间参数
     * @return 格式化之后的时间字符串，形式为yyyy-MM
     * @author unknow
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static String formatDateYearDay(Date date) {
        return formatDate(date, YEAR_MONTH_DATE_FORMAT);
    }

    /**
     * 按照格式 <b>yyyy-MM-dd</b> 将 <b>当前时间</b> 进行格式化<br>
     *
     * @return 格式化之后的时间字符串
     * @author unkonw
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static String formatNow() {
        return formatDate(new Date(), FULL_DATE_FORMAT);
    }

    /**
     * 将传入时间和时间纪元的时间（<i>1970-01-01 00:00:00</i>）进行比较.</br>时间相同,返回true;否则返回false<br>
     *
     * @param date
     * @return true or false
     * @author unkonw
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static boolean isDefaultTime(Date date) {
        Date parseDate = parseDate("1970-01-01 00:00:00", FULL_DATE_FORMAT);
        if (parseDate == null) {
            return false;
        }
        return parseDate.equals(date);
    }

    /**
     * 将时间戳格式化为对应格式的时间字符串<i>yyyy-MM-dd HH:mm:ss</i> <br>
     *
     * @param timestamp
     * @return 相应格式的时间字符串
     * @author unkonw
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static String timeStampToStr(Long timestamp) {
        if (timestamp != null) {
            return DateFormatUtils.format(timestamp, FULL_DATE_FORMAT);
        } else {
            return "";
        }
    }

    /**
     * 计算间隔的小时数.</br>
     *
     * @param smdate
     * @param bdate
     * @return
     * @author handanian
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static long computingHours(Date smdate, Date bdate) {
        long times = bdate.getTime() - smdate.getTime();
        return times / (60 * 60 * 1000);
    }

    /**
     * 将日期中的某个值增加或者减少 <br>
     * exp: date="1994-12-06 23:13:46", field={@link Calendar#YEAR YEAR } ,value=1</br> 就是将年份增加一年</br>
     *
     * @param date
     * @param field
     * @param value
     * @return
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static Date getdate(Date date, int field, int value) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int v = c.get(field);
        c.set(field, v + value);
        return c.getTime();
    }

    /** 当前日期减1s，一般用在快速从当前日期的00:00:00到上一天23:59:59 */
    public static void minusOneSecond(Date date) {
        getdate(date, Calendar.SECOND, -1);
    }

    /**
     * 获得指定日期的后一天
     *
     * @param date 待操作的时间
     * @return 操作之后的时间
     * @since JDK 1.8
     */
    public static Date getdateAfter(Date date) {
        return getdate(date, Calendar.DATE, 1);
    }

    /**
     * 获得指定日期的前一天
     *
     * @param date 待操作的时间
     * @return 操作之后的时间
     * @since JDK 1.8
     */
    public static Date getdateBefore(Date date) {
        return getdate(date, Calendar.DATE, -1);
    }

    /**
     * 获取当前时间前若干天的时间
     *
     * @param day
     * @return
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static String getBeforeDate(int day) {
        return formatDate(getDayBeforeXDay(new Date(), day)).replace("-", "");
    }

    /**
     * 获取指定日期的后一个月
     *
     * @param date
     * @return
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static Date getDateOfLastMonth(Date date) {
        return getdate(date, Calendar.MONTH, -1);
    }

    /**
     * 获得指定日期的前若干天时间
     *
     * @param date
     * @return
     */
    public static Date getDayBeforeXDay(Date date, int days) {
        return getdate(date, Calendar.DATE, days);
    }

    /**
     * 获取当天零点的时间
     *
     * @param date
     * @param dayOfMonth
     * @return
     * @author Dengchuanzhe
     * @since JDK 1.8
     */
    public static Date truncate(Date date, int dayOfMonth) {

        return null;
    }

    /**
     * 判断当前时间是否在规定时间内
     *
     * @param date
     * @param strDateBegin
     * @param strDateEnd
     * @return
     * @throws ParseException
     * @author lizhen
     */
    public static boolean isInDate(Date date, String strDateBegin, String strDateEnd)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);
        String dateBegin = strDate.substring(0, 11) + strDateBegin + ":00";
        String dateEnd = strDate.substring(0, 11) + strDateEnd + ":00";

        Date dateBeginDate = sdf.parse(dateBegin);
        Date dateEndDate = sdf.parse(dateEnd);

        if (compare(date, dateBeginDate) && compare(dateEndDate, date)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比对两日期大小
     *
     * @param DATE1
     * @param DATE2
     * @return DATE1>=DATE2 : true (DATE1 在 DATE2之后)
     */
    public static boolean compare(Date DATE1, Date DATE2) {
        if (DATE1.getTime() >= DATE2.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param start 较小的时间
     * @param end 较大的时间
     * @return 相差天数, 向上取整
     * @throws ParseException
     */
    public static int daysBetween(Date start, Date end) {
        long time1 = start.getTime();
        long time2 = end.getTime();
        long num = (time2 - time1) % (1000 * 3600 * 24);
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
        if (num != 0) {
            betweenDays = betweenDays + 1;
        }
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * 获取指定时间所处的季度 ,从1开始</br>
     *
     * @param date
     * @return
     * @since JDK 1.8
     */
    public static int getSeason(Date date) {

        int season = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     * 获取某个指定时间的季度的值[yy+季度值] 例如：19941206123343 为94年第四季度,为944<br>
     *
     * @return
     * @since JDK 1.8
     */
    public static String getYearSeason(String timeStr) {
        String yearSeason = timeStr.substring(2, 4) + getSeason(parseDate(timeStr, "yyyyMMddHHmmss"));
        return yearSeason;
    }

    /**
     * 某年某季度的上一个季度的值(考虑跨年)
     *
     * @param dateConvertString 当前日期
     * @param season 当前季度
     * @return
     */
    public static String getLastYearSeason(String dateConvertString, int season) {
        String year = dateConvertString.substring(2, 4);
        if (season == 1) {
            return (Integer.valueOf(year) - 1) + "4";
        } else {
            return year + (season - 1);
        }
    }

    /**
     * 某年某季度的下一个季度的值(考虑跨年 )
     *
     * @param dateConvertString 当前日期
     * @param season 当前季度
     * @return
     * @since JDK 1.8
     */
    public static String getNextYearSeason(String dateConvertString, int season) {
        String year = dateConvertString.substring(2, 4);
        if (season == 4) {
            return (Integer.valueOf(year) + 1) + "1";
        } else {
            return year + (season + 1);
        }
    }

    /**
     * 获取指定的时间字段的值
     *
     * @param date 时间
     * @param field
     * @return
     * @since JDK 1.8
     */
    public static int getField(Date date, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(field);
    }

    /**
     * 获取指定日期的小时
     *
     * @param date
     * @return
     * @since JDK 1.8
     */
    public static int getHour(Date date) {
        return getField(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取指定日期的分钟
     *
     * @param date
     * @return
     * @since JDK 1.8
     */
    public static int getMinute(Date date) {
        return getField(date, Calendar.MINUTE);
    }

    /**
     * 获取整点的数据,dateStr格式必须为yyyy-MM-dd HH:mm
     *
     * @param dateStr
     * @return
     * @since JDK 1.8
     */
    public static String integralPoint(String dateStr) {
        Date date = parseDate(dateStr, "yyyy-MM-dd HH:mm");
        return integralPoint(date);
    }

    /**
     * 获取整点数据（整点指0分和30分整的数据）
     *
     * @param date 传入日期可能是 28分34秒或者不整点的时间值（例如：2017-12-31 23:28:34）
     * @return
     */
    public static String integralPoint(Date date) {
        int minute = minValue(date);
        String returnStr = addMinite(date, minute);
        return returnStr;
    }

    /**
     * 按分钟加时间值
     *
     * @param date
     * @param minite
     * @return
     */
    public static String addMinite(Date date, int minite) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minite);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    public static Date addHour(Date date, Integer hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    /**
     * 获取距离0、30分这个值最近的值（绝对值判断最近后返回实际距离值：可能是正数或者负数）
     *
     * @param date
     * @return
     */
    public static int minValue(Date date) {
        int getMinute = getMinute(date);
        int minute0 = getMinute - 0; // 减去0分后的值
        int minute30 = getMinute - 30; // 减去30分后的值
        int minute60 = getMinute - 60; // 减去60分后的值
        int absminute0 = Math.abs(minute0); // 减去0分后的绝对值
        int absminute30 = Math.abs(minute30); // 减去30分后的绝对值
        int absminute60 = Math.abs(minute60); // 减去60分后的绝对值
        Integer opValue = 0;
        int min = 0;
        if (absminute0 < absminute30) {
            opValue = minute0;
            min = absminute0;
        } else {
            min = absminute30;
            opValue = minute30;
        }

        if (absminute60 < min) {
            opValue = minute60;
            min = absminute60;
        }
        return opValue;
    }

    /**
     * 获取date所在日的00:00:00
     *
     * @param date
     * @return
     */
    public static Date getStartTimeOfDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取日期所在的年的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取date所在日的23:59:59
     *
     * @param date
     * @return
     */
    public static Date getEndTimeOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        setCalendarToEndTime(cal);
        return cal.getTime();
    }

    /**
     * 统计两个日期之间的天数
     *
     * @param date1: 较早的日期
     * @param date2：较晚的日期
     * @return
     */
    public static Long countDays(Date date1, Date date2) {
        Date startTime1 = getStartTimeOfDate(date1);
        Date startTime2 = getStartTimeOfDate(date2);
        long days = (int) ((startTime2.getTime() - startTime1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 加n天后的日期
     *
     * @param date
     * @param n
     * @return
     */
    public static Date addDays(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, n);
        return cal.getTime();
    }

    /**
     * 获取date所在周的周一的00:00:00
     *
     * @param date
     * @return
     */
    public static Date getStartTimeOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        setCalendarToStartTime(cal);
        return cal.getTime();
    }

    /**
     * 获取date所在周的周日的23:59:59
     *
     * @param date
     * @return
     */
    public static Date getEndTimeOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        setCalendarToEndTime(cal);
        return cal.getTime();
    }

    /**
     * 获取date所在月的起始时间
     *
     * @param date
     * @return
     */
    public static Date getStartTimeOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        setCalendarToStartTime(cal);
        return cal.getTime();
    }

    /**
     * 获取date所在月的结束时间
     *
     * @param date
     * @return
     */
    public static Date getEndTimeOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        setCalendarToEndTime(cal);
        return cal.getTime();
    }

    /**
     * 获取date所在的月份
     *
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取给定日期所在的周一
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        int delta = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            delta = -6;
        } else {
            delta = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, delta);
        return cal.getTime();
    }

    /**
     * 获取给定日期所在的周日
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        cal.add(Calendar.DATE, -dayOfWeek + 7);
        return cal.getTime();
    }

    /**
     * 计算两个日期之间的周数，按自然周计算，第1周的周六和第3周的周一，差2周，而不是一周
     *
     * @param date1 : 较早的日期
     * @param date2 : 靠近现在的日期
     * @return
     */
    public static Integer countWeeks(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setFirstDayOfWeek(Calendar.MONDAY);
        c2.setFirstDayOfWeek(Calendar.MONDAY);
        c1.setTime(date1);
        c2.setTime(date2);

        Date date11 = getFirstDayOfWeek(date1);
        Date date22 = getFirstDayOfWeek(date2);
        Long weeks = (date22.getTime() - date11.getTime()) / (7 * 24 * 60 * 60 * 1000);
        return weeks.intValue();
    }

    /**
     * 加n周后的日期
     *
     * @param date
     * @param n
     * @return
     */
    public static Date addWeeks(Date date, Integer n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.WEEK_OF_YEAR, n);
        return cal.getTime();
    }

    /**
     * 计算两个日期之间的月份
     *
     * @param date1 : 较早的日期
     * @param date2 : 靠近现在的日期
     * @return
     */
    public static int countMonths(Date date1, Date date2) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        if (year < 0) {
            year = -year;
            return year * 12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        }
        return year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
    }

    /**
     * 加n月后的日期
     *
     * @param date
     * @param n
     * @return
     */
    public static Date addMonth(Date date, Integer n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 取得月最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 两个日期之间一共有多少年
     *
     * @param date1 : 较早的日期
     * @param date2 : 靠近现在的日期
     * @return
     */
    public static Integer countYears(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return null;
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(date1);
        c2.setTime(date2);

        Integer year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

        return year;
    }

    /**
     * 在date上加n年
     *
     * @param date
     * @param n
     * @return
     */
    public static Date addYears(Date date, int n) {
        Date firstDay = getStartTimeOfDate(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(firstDay);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.add(Calendar.YEAR, n);
        return cal.getTime();
    }

    /**
     * date是否在startDate和endDate区间内
     *
     * @param date
     * @param startDate
     * @param endDate
     * @return
     */
    public static Boolean between(Date date, Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        if (date == null) {
            return false;
        }
        Long startDateMills = startDate.getTime();
        Long endDateMills = endDate.getTime();
        Long dateMills = date.getTime();
        if (dateMills >= startDateMills && dateMills <= endDateMills) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 日期格式化
     *
     * @param date 日期对象
     * @param format 格式化格式
     * @return
     */
    public static String format(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 获取当前日期的时分秒换算成毫秒数
     *
     * @param cal
     * @return
     */
    private static long getTimeMillis(Calendar cal) {
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        // 时分秒（毫秒数）
        long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
        return millisecond;
    }

    /**
     * 设置成当前日期 00：00：00
     *
     * @param cal
     */
    private static void setCalendarToStartTime(Calendar cal) {
        // 凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis() - getTimeMillis(cal));
    }


    /**
     * 设置当前日期 23：59：59
     *
     * @param cal
     */
    private static void setCalendarToEndTime(Calendar cal) {
        // 凌晨00:00:00
        cal.setTimeInMillis(cal.getTimeInMillis() - getTimeMillis(cal));
        // 凌晨23:59:59
        cal.setTimeInMillis(cal.getTimeInMillis() + 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000);
    }

    public static final Date getTodayDateFormat() {
        return parseDate(formatDate(new Date(), SIMPLE_DATE_FORMAT));
    }

    public static Date getDate(String sFormat, String sDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
        Date date = null;
        try {
            sdf.setLenient(false);
            date = sdf.parse(sDate);
        } catch (Exception e) {
//            log.warn("日期格式不正确:{}", sDate);
        }

        return date;
    }

    public static Date convertToDate(String sDate) {
        sDate = sDate.replaceAll("-", "/");
        String[] formats = {"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd"};

        for (String format : formats) {
            Date date = getDate(format, sDate);
            if (date != null) {
                return date;
            }
        }
        return null;
    }

    /**
     * 自动补全日期
     *
     * @param list
     * @param begin
     * @param daySub
     * @return
     */
    public static List<String> completionDate(List<String> list, String begin, int daySub) {
        ArrayList<String> dateResult = new ArrayList<>();
        // 时间增加一天
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_MONTH, 1);

        // 字符串转化为时间
        Calendar calendar10 = Calendar.getInstance();
        Calendar calendar5 = Calendar.getInstance();
        // 循环处理日期数据，把缺失的日期补全。10是时间段内的天数,5是要处理的日期集合的天数
        try {
            Date date = sdf.parse(begin);
            calendar10.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int curr = 0; curr < daySub; curr++) {

            boolean dbDataExist = false;
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                try {
                    Date date2 = sdf.parse(list.get(i));
                    calendar5.setTime(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (calendar10.compareTo(calendar5) == 0) {
                    dbDataExist = true;
                    index = i;
                    break;
                }
            }
            if (dbDataExist) {
                dateResult.add(list.get(index));
            } else {
                dateResult.add(sdf.format(calendar10.getTime()));
            }
            // 还原calendar10
            calendar10.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dateResult;
    }


    /**
     * 当前日期的时间设置为00:00:00
     * @param date
     * @return
     */
    public static Date getStartDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setCalendarToStartTime(calendar);
        return calendar.getTime();
    }


    public static void main(String[] args) {
        Date startDateTime = getStartDateTime(new Date());
        System.out.println(startDateTime.getTime());
        Date date = DateUtils.parseDate("2021-08-18 00:00:00", "yyyy-MM-dd HH:mm:ss");
        System.out.println(date.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(startDateTime));
    }
}
