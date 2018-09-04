package com.blackeye.worth.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateX {

	/***
	 * 
	 * param year,month,day
	 * 
	 * @param filed
	 * @param amount
	 * @return year-month-day
	 */
	public static String add(int filed, int amount) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		c.add(filed, amount);
		String result = sdf.format(c.getTime());
		return result;
	}

	public static Date getMonthFirstDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		return c.getTime();
	}

	public static String getMonthFirstDateStr() {
		return getDate(getMonthFirstDate());
	}

	/*
	 * 
	 * @return year
	 */
	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/*
	 * 
	 * @return month
	 */
	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/*
	 * 
	 * @return day
	 */
	public static int getDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static String getOrderNum() {
		Date date = new Date();
		int i = 1000 + new Random().nextInt(1000);
		String s = String.valueOf(i).substring(1);
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return df.format(date) + s;
	}

	// 获取日期，格式：yyyy-MM-dd HH:mm:ss
	public static String getDateTime() {
		return getDate("yyyy-MM-dd HH:mm:ss");
	}

	// 获取日期，格式：yyyy-MM-dd HH:mm:ss
	public static String getDateFormatter(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	// 获取日期，格式：yyyy-MM-dd HH:mm:ss
	public static String re_getDateFormatter(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	public static String getDate(String format) {
		return getDate(new Date(), format);
	}

	public static String getDate(Date date) {
		return getDate(date, "yyyy-MM-dd");
	}

	public static String getDate(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	// 产生随机的三位数
	public static String getThree() {
		Random rad = new Random();
		return (rad.nextInt(1000) + 1000 + "").substring(1);
	}

	public static String getAfterWorkDay(int count) {
		return getAfterWorkDay(new Date(), count);
	}

	public static String getAfter(int field, int count) {
		return getAfter(new Date(), field, count);
	}

	public static Date getDateAfter(int field, int count) {

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);

		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c.getTime();
	}

	public static String getAfter(Date date, int field, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(field, count);
		return getDate(c.getTime());
	}

	public static String getAfterDay(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, count);
		return getDate(c.getTime());
	}

	public static String getAfterHour(int count) {
		return getAfterHour(new Date(), count);
	}

	public static String getAfterWorkDay(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// SUNDAY=1;SATURDAY=7
		int rang = 6 - c.get(Calendar.DAY_OF_WEEK);
		int reality = 0;
		if (count <= rang) {
			reality = count;
		} else {
			int exceed = count - rang;
			reality = count + (exceed + 5) / 5 * 2;
		}
		c.add(Calendar.DATE, reality);
		return getDate(c.getTime());
	}

	public static String getAfterHour(Date date, int count) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, -count);
		return getDate(c.getTime());
	}

	public static Date parseDateTime(String dateTime) {
		String format = "yyyy-MM-dd HH:mm:ss.SSS".substring(0, dateTime.length());
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回日期描述：如“刚刚”，“10分钟前”
	 * 
	 * @param datetime
	 * @return
	 */
	public static String getDateState(String datetime) {
		Date d = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d2 = null;
		String r = "";
		try {
			d2 = sd.parse(datetime);
			long l = d.getTime() - d2.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);

			if (day > 0) {
				r = day + "天前";
			} else if (hour > 0) {
				r = hour + "小时前";
			} else if (min > 0) {
				r = min + "分前";
			} else {
				r = "刚刚";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * 字符串 - 日期时间
	 *
	 * @param yyyy
	 *            -MM-dd HH:mm:ss
	 * @return Date
	 */
	public static Date getDateTime(String datetime) {

		String f = "yyyy-MM-dd HH:mm:ss.S";
		f = f.substring(0, datetime.length() - 1);
		SimpleDateFormat sd = new SimpleDateFormat(f);

		Date d = null;
		try {
			d = sd.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}



	/**
	 * yyyy年MM月dd日 HH:mm
	 *
	 * @return
	 */
	public static String changeStr(String str) {
		Date d = getDateTime(str);
		String s = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(d);
		return s;
	}

	/**
	 * yyyy年MM月dd日 HH:mm
	 *
	 * @return
	 */
	public static String changeStr_v2(String str) {
		Date d = getDateTime(str);
		String s = new SimpleDateFormat("yyyy.MM.dd").format(d);
		return s;
	}
	/**
	 * yyyy年MM月dd日 HH:mm
	 *
	 * @return
	 */
	public static String changeDateToStr(Date date) {
		String s = new SimpleDateFormat("yyyy.MM.dd").format(date);
		return s;
	}

	/**
	 * 获取当天、本周、本月、前天、上周、上月时间
	 */
	public static String[] get_date_range(int date_type) {

		String[] v = new String[2];
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		switch (date_type) {
		case 1:// 当天
			v[0] = sdf.format(c.getTime());
			v[1] = v[0];
			break;
		case 2:// 本周
			c.set(Calendar.DAY_OF_WEEK, 2);
			v[0] = sdf.format(c.getTime());
			c.add(Calendar.DATE, 6);
			v[1] = sdf.format(c.getTime());
			break;
		case 3:// 本月
			c.set(Calendar.DAY_OF_MONTH, 1);
			v[0] = sdf.format(c.getTime());
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			v[1] = sdf.format(c.getTime());
			break;
		case 4:// 前天
			c.add(Calendar.DATE, -1);
			v[0] = sdf.format(c.getTime());
			v[1] = v[0];
			break;
		case 5:// 上周
			c.set(Calendar.DAY_OF_WEEK, 1);
			v[1] = sdf.format(c.getTime());
			c.add(Calendar.DATE, -6);
			v[0] = sdf.format(c.getTime());
			break;
		case 6:// 上月
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.DATE, -1);
			v[1] = sdf.format(c.getTime());
			c.set(Calendar.DAY_OF_MONTH, 1);
			v[0] = sdf.format(c.getTime());
			break;
		}
		return v;
	}

	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	public static int daysBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	public static int daysBetween2(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	public static String parseTimeInMillis(String timeInMillis) {
		if (StringX.isEmpty(timeInMillis))
			return null;
		return parseTimeInMillis(Long.valueOf(timeInMillis));
	}

	public static String parseTimeInMillis(long timeInMillis) {
		Date date = new Date(timeInMillis * 1000);
		return getDateTime(date);
	}

	public static String getyesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
		return yesterday;
	}

	public static String getAfterDateTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		return yesterday;
	}

	// 获取日期，格式：yyyy-MM-dd HH:mm:ss
	public static String getDateTime(Date date) {
		return getDate(date, "yyyy-MM-dd HH:mm:ss");
	}


	/** 
	* @Title: findDates
	* @Description:  查找两个日期之间的日期列表
	* @param
	* @author:jaybril-pro
	* @create_time:2017年11月9日 下午5:44:51  
	* @return List<Date>     
	* @throws 
	*/
	public static List<Date> findDates(Date dBegin, Date dEnd) {
		List lDate = new ArrayList();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	public static Date getAfterDayReturnDate(Date date, int count) {
		  Calendar c = Calendar.getInstance();
		  c.setTime(date);
		  c.add(Calendar.DATE, count);
		  return c.getTime();
		 }


	/**
	 * 获得指定日期的后n天
	 * @param specifiedDay
	 * @param n 可以为负数--表示前几天
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay,int n){
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.add(Calendar.DATE,n);

		String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

	/**
	 * 获取过去第几天的日期 2018-1-31 by utakata
	 *
	 * @param past
	 * @return
	 */
	public static String getPastDate(int past) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(today);
		return result;
	}


	/**
	 * 比较两个时间的大小
	 *
	 * @param past
	 * @return
	 */
	public static int compare_date(String DATE1, String DATE2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
}
