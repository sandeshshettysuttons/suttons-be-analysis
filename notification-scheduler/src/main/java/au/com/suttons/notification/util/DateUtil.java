package au.com.suttons.notification.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

public class DateUtil
{
    // Formats
    public static final String DIGITS8_DTOY_FORMAT = "ddMMyyyy";
    public static final String DIGITS8_yyyyMMdd_FORMAT = "yyyyMMdd";
    public static final String DIGITS6_yyyyMM_FORMAT = "yyyyMM";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String SHORT_DATE_FORMAT = "d/M/yy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_TIME_ZONE_FORMAT = "dd/MM/yyyy'T'HH:mm:ss'Z'";
    public static final String FULL_TIME_FORMAT = "HH:mm:ss";
    public static final String DIGITS8_TIME_FORMAT = "HHmmss";
    public static final String PARTIAL_TIME_FORMAT = "HH:mm";
    public static final String SHORT_TIME_FORMAT = "H:mm";
    public static final String SHORT_TIME_12HR_FORMAT = "h:mm a";
    public static final String RPG_DATE_FORMAT = "yyyyMMdd";
    public static final String RPG_TIMESTAMP_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String US_DATE_SIZE10 = "MM/dd/yyyy";
    public static final String FILE_TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    public static final String DIGITS10_yyyy_MM_dd_FORMAT = "yyyy-MM-dd";

    public static DateFormat digits8DtoYformatter = new SimpleDateFormat(DIGITS8_DTOY_FORMAT);
    public static DateFormat digits8YtoDformatter = new SimpleDateFormat(DIGITS8_yyyyMMdd_FORMAT);
    public static DateFormat digits6YtoMformatter = new SimpleDateFormat(DIGITS6_yyyyMM_FORMAT);
    public static DateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
    public static DateFormat shortDateFormatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
    public static DateFormat dateTimeFormatter = new SimpleDateFormat(DATE_TIME_FORMAT);
    public static DateFormat dateTimeZoneFormatter = new SimpleDateFormat(DATE_TIME_ZONE_FORMAT);
    public static DateFormat fullTimeFormatter = new SimpleDateFormat(FULL_TIME_FORMAT);
    public static DateFormat digits8TimeFormatter = new SimpleDateFormat(DIGITS8_TIME_FORMAT);
    public static DateFormat partialTimeFormatter = new SimpleDateFormat(PARTIAL_TIME_FORMAT);
    public static DateFormat shortTimeFormatter = new SimpleDateFormat(SHORT_TIME_FORMAT);
    public static DateFormat shortTime12HrFormatter = new SimpleDateFormat(SHORT_TIME_12HR_FORMAT);
    public static DateFormat rpgDateFormatter = new SimpleDateFormat(RPG_DATE_FORMAT);
    public static DateFormat rpgTimestampFormatter = new SimpleDateFormat(RPG_TIMESTAMP_FORMAT);
    public static DateFormat usDateSize10Formatter = new SimpleDateFormat(US_DATE_SIZE10);
    public static DateFormat fileTimestampFormatter = new SimpleDateFormat(FILE_TIMESTAMP_FORMAT);
    public static DateFormat digits10YtoDformatter = new SimpleDateFormat(DIGITS10_yyyy_MM_dd_FORMAT);

    public static Date formatShortDate(String strValue) {
        try {
            Date result = null;
            if (StringUtils.isNotBlank(strValue))
            {
                result = shortDateFormatter.parse(strValue);
            }
            return result;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseStringToDate(String value)
    {
        try {
        	Date result = null;
            if (value != null)
            {
            	result = DateUtil.dateFormatter.parse(value);
            }
            return result;
		} catch (ParseException e) {
			return null;
		}
    }

    public static Date parseStringToDate(String value, DateFormat dateFormat)
    {
        try {
            Date result = null;
            if (value != null)
            {
                result = dateFormat.parse(value);
            }
            return result;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Format Date to String -> 'yyyy-MM-dd'
     * @param date
     * @return
     */
    public static String formatDateToString(Date date)
    {
    	String result = null;
        if (date != null)
        {
            result = DateUtil.dateFormatter.format(date);
        }
        return result;
    }

    /**
     * Format Date to String -> 'yyyy-MM-dd'
     * @param date
     * @return
     */
    public static String formatDateToString(Date date, DateFormat dateFormat)
    {
    	String result = null;
        if (date != null)
        {
            result = dateFormat.format(date);
        }
        return result;
    }

    public static Date stringSize8ToDate(String value)
    {
        try {
            Date result = null;
            if (StringUtils.isNotBlank(value))
            {
            	result = DateUtil.digits8DtoYformatter.parse(value);
            }
            return result;
	} catch (ParseException e) {
            return null;
        }
    }
    public static Date stringSize8YearToDate(String value)
    {
        try {
            Date result = null;
            if (StringUtils.isNotBlank(value))
            {
            	result = DateUtil.digits8YtoDformatter.parse(value);
            }
            return result;
	} catch (ParseException e) {
            return null;
        }
    }
    public static String stringYYYYmmDD(Date date)
    {
       String result = null;
       if (date == null)
       {
            date = getCurrentDate();
       }
       result = DateUtil.digits8YtoDformatter.format(date);
       return result;
    }

    public static Date stringSize6FullTime(String value)
    {
        try {
            Date result = null;
            if (StringUtils.isNotBlank(value))
            {
            	result = DateUtil.digits8TimeFormatter.parse(value);
            }
            return result;
	} catch (ParseException e) {
            return null;
        }
    }

    /**
     * Format Date to Time String -> 'yyyy-MM-dd'
     * @param date
     * @return
     */
    public static String dateToShortTimeString(Date date)
    {
        return formatDateToString(date, DateUtil.shortTime12HrFormatter);
    }


    /**
     * Format Date to String -> 'MM/dd/yyyy'
     * @param strDate
     * @return
     */
    public static Date USdateSize10(String strDate)
    {
    	Date result = null;
        try 
        {
	    	if (StringUtils.isNotBlank(strDate))
	        {
	            result = DateUtil.usDateSize10Formatter.parse(strDate);
	        }
        }
        catch (ParseException e)
        {
        	return null;
        }
        return result;
    }

    public static String formatYYYY_MM_DD(Date date) {
        String result = null;
        if (date == null)
        {
            date = getCurrentDate();
        }
        result = DateUtil.digits10YtoDformatter.format(date);
        return result;
    }

    public static Date formatYYYY_MM_DD(String date) {
        try {
            Date result = null;
            if (StringUtils.isNotBlank(date))
            {
                result = DateUtil.digits10YtoDformatter.parse(date);
            }
            return result;
        } catch (ParseException e) {
            return null;
        }
    }

    /* ********************** */
    /* *** HELPER METHODS *** */
    /* ********************** */

    public static Date getCurrentDate()
    {
    	return new Date(System.currentTimeMillis());
    }

    public static Long getRPGDate(Date date){
        return Long.parseLong(DateUtil.formatDateToString(date, DateUtil.rpgDateFormatter));
    }

    public static Long getRPGTimestamp(){
        return Long.parseLong(DateUtil.formatDateToString(DateUtil.getCurrentDate(), DateUtil.rpgTimestampFormatter));
    }

    public static Date getStandardDate(String strDate){
        Date result = null;
        try {
           if (StringUtils.isNotBlank(strDate)) {
               result = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
           }
        } catch (ParseException e){e.printStackTrace();}
        return result;
    }

    public static String auSize8Date(Date date)
    {
        String result = null;
        if (date == null){
            date = getCurrentDate();
        }
        result = DateUtil.digits8DtoYformatter.format(date);
        return result;
    }

    public static String getFileTimestamp(){
        return DateUtil.formatDateToString(DateUtil.getCurrentDate(), DateUtil.fileTimestampFormatter);
    }

    public static java.sql.Date getCurrentSqlDate()
    {
        return new java.sql.Date(getCurrentDate().getTime());
    }

    public static Date getCurrentDateWithoutTime()
    {
        Calendar today =  Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today.getTime();
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

}
