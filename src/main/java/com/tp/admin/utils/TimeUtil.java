package com.tp.admin.utils;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtil {

    public static String time(Timestamp time) {
        Date date = new Date(time.getTime());
        SimpleDateFormat Shanghaidf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");     // 上海
        Shanghaidf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return Shanghaidf.format(date);
    }

    public static String shanghaiDayTime(Timestamp time) {
        Date date = new Date(time.getTime());
        SimpleDateFormat Shanghaidf = new SimpleDateFormat("yyyy-MM-dd");     // 上海
        Shanghaidf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return Shanghaidf.format(date);
    }


    /**
     * 获取当天的开始时间
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    public static Date getDayBegin() {
        Date date = new Date();
        return getDayStartTime(date);
    }

    /**
     * 获取当天的结束时间
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    public static Date getDayEnd() {
        Date date = new Date();
        return getDayEndTime(date);
    }


    //获取本月的开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    //获取本月的结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 返回某个日期前几天的日期
     * @param date
     * @param i
     * @return
     */
    public static Date getFrontDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
        return cal.getTime();
    }

    //获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    //获取本月是哪一月
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    /**
     * 获取某个日期的开始时间
     *
     * @param d
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取某个日期的结束时间
     *
     * @param d
     * @return yyyy-MM-dd HH:mm:ss  格式
     */
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 判断两个日期是否同一天
     * @param a
     * @param b
     * @return
     */
    public static boolean dayEquals(Date a , Date b){
        Calendar aTime = new GregorianCalendar();
        aTime.setTime(a);
        Calendar bTime = Calendar.getInstance();
        bTime.setTime(b);
        if (aTime.get(Calendar.YEAR) == bTime.get(Calendar.YEAR) &&
                aTime.get(Calendar.MONTH) == bTime.get(Calendar.MONTH) &&
                aTime.get(Calendar.DAY_OF_MONTH) == bTime.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    /**
     * 两个日期相减得到的天数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDiffDays(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }
        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);
        int days = new Long(diff).intValue();
        return days;
    }

}
