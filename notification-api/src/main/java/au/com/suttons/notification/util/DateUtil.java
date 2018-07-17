package au.com.suttons.notification.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

public class DateUtil
{
    // Formats
    public static final String DIGITS8_DTOY_FORMAT = "ddMMyyyy";
    public static final String DIGITS8_yyyyMMdd_FORMAT = "yyyyMMdd";
    public static final String DIGITS10_yyyy_MM_dd_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String SHORT_DATE_FORMAT = "d/M/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_TIME_ZONE_FORMAT = "dd/MM/yyyy'T'HH:mm:ss'Z'";
    public static final String FULL_TIME_FORMAT = "HH:mm:ss";
    public static final String PARTIAL_TIME_FORMAT = "HH:mm";
    public static final String SHORT_TIME_FORMAT = "H:mm";
    public static final String SHORT_TIME_12HR_FORMAT = "h:mm a";
    public static final String RPG_DATE_FORMAT = "yyyyMMdd";
    public static final String RPG_TIMESTAMP_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String FILE_TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    public static final String ANGULAR_DATE_TIME_FORMAT = "EEE MMM d yyyy HH:mm:ss";

    public static DateFormat digits8DtoYformatter = new SimpleDateFormat(DIGITS8_DTOY_FORMAT);
    public static DateFormat digits8YtoDformatter = new SimpleDateFormat(DIGITS8_yyyyMMdd_FORMAT);
    public static DateFormat digits10YtoDformatter = new SimpleDateFormat(DIGITS10_yyyy_MM_dd_FORMAT);
    public static DateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
    public static DateFormat shortDateFormatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
    public static DateFormat dateTimeFormatter = new SimpleDateFormat(DATE_TIME_FORMAT);
    public static DateFormat dateTimeZoneFormatter = new SimpleDateFormat(DATE_TIME_ZONE_FORMAT);
    public static DateFormat fullTimeFormatter = new SimpleDateFormat(FULL_TIME_FORMAT);
    public static DateFormat partialTimeFormatter = new SimpleDateFormat(PARTIAL_TIME_FORMAT);
    public static DateFormat shortTimeFormatter = new SimpleDateFormat(SHORT_TIME_FORMAT);
    public static DateFormat shortTime12HrFormatter = new SimpleDateFormat(SHORT_TIME_12HR_FORMAT);
    public static DateFormat rpgDateFormatter = new SimpleDateFormat(RPG_DATE_FORMAT);
    public static DateFormat rpgTimestampFormatter = new SimpleDateFormat(RPG_TIMESTAMP_FORMAT);
    public static DateFormat fileTimestampFormatter = new SimpleDateFormat(FILE_TIMESTAMP_FORMAT);
    public static DateFormat angularDateTimeFormatter = new SimpleDateFormat(ANGULAR_DATE_TIME_FORMAT);

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

    public static Date parseStringToDate(String value, DateFormat formatter)
    {
        try {
        	Date result = null;
            if (value != null)
            {
            	result = formatter.parse(value);
            }
            return result;
		} catch (ParseException e) {
			return null;
		}
    }


    /**
     * Format Date to String -> 'dd/MM/yyyy'
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

    public static String formatDateTimeToString(Date date)
    {
    	String result = null;
        if (date != null)
        {
            result = DateUtil.dateTimeFormatter.format(date);
        }
        return result;
    }

    /**
     * Format Date to String
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

    public static String dateToShortTimeString(Date date)
    {
        return formatDateToString(date, DateUtil.shortTime12HrFormatter);
    }

    public static Date shortTimeStringToDate(String value)
    {
        try {
            Date result = null;
            if (StringUtils.isNotBlank(value))
            {
            	result = DateUtil.shortTime12HrFormatter.parse(value);
            }
            return result;
        } catch (ParseException e) {
            return null;
        }
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

    public static String auSize8Date(Date date)
    {
        String result = null;
        if (date == null){
            date = getCurrentDate();
        }
        result = DateUtil.digits8DtoYformatter.format(date);
        return result;
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

    public static Date stringShortDateToDate(String value)
    {
        try {
            Date result = null;
            if (StringUtils.isNotBlank(value))
            {
            	result = DateUtil.shortDateFormatter.parse(value);
            }
            return result;
	} catch (ParseException e) {
            return null;
        }
    }
    public static String stringShortDate(Date date)
    {
       String result = null;
       if (date == null)
       {
            date = getCurrentDate();
       }
       result = DateUtil.shortDateFormatter.format(date);
       return result;
    }
    
    
    
    /* ********************** */
    /* *** HELPER METHODS *** */
    /* ********************** */

    public static Date getCurrentDate()
    {
    	return new Date(System.currentTimeMillis());
    }

    public static java.sql.Date getCurrentSqlDate()
    {
    	return new java.sql.Date(getCurrentDate().getTime());
    }

    public static Long getRPGDate(Date date){
        return Long.parseLong(DateUtil.formatDateToString(date, DateUtil.rpgDateFormatter));
    }

    public static Long getRPGTimestamp(){
        return Long.parseLong(DateUtil.formatDateToString(DateUtil.getCurrentDate(), DateUtil.rpgTimestampFormatter));
    }

    public static String getFileTimestamp(){
        return DateUtil.formatDateToString(DateUtil.getCurrentDate(), DateUtil.fileTimestampFormatter);
    }

    // to convert Milliseconds into DD HH:MM:SS format.
    public static String getDateFromMsec(long diffMSec, boolean displayFormat, boolean ignoreSeconds) {
        
        int left = 0, ss = 0, mm = 0, hh = 0, dd = 0;
        
        String hours = displayFormat ? "hh " : ":";
        //String minutes = displayFormat ? "mm " : (ignoreSeconds ? "" : ":");
        //String seconds = ignoreSeconds ? "" : (displayFormat ? "ss " : "");
        String minutes = displayFormat ? "mm " : ":";
        String seconds = displayFormat ? "ss " : "";
        
        
        left = (int) (diffMSec / 1000);
        ss = left % 60;
        left = (int) left / 60;
        
        if (left > 0) {
            mm = left % 60;
            left = (int) left / 60;
            
            if (left > 0) {
                hh = left % 24;
                left = (int) left / 24;
                
                if (left > 0) {
                    dd = left;
                }
            }
        }
        
        String strSS = (ss > 0) ? ((ss > 9) ? Integer.toString(ss) : ("0" + Integer.toString(ss))) : "00"; 
        String strMM = (mm > 0) ? ((mm > 9) ? Integer.toString(mm) : ("0" + Integer.toString(mm))) : "00"; 
        String strHH = (hh > 0) ? ((hh > 9) ? Integer.toString(hh) : ("0" + Integer.toString(hh))) : "00"; 
        String strDD = (dd > 0) ? (Integer.toString(dd) + ((dd > 1) ? " days " : " day ")) : "";
        
        //return strDD + strHH + hours + strMM + minutes + (ignoreSeconds ? "" : (strSS + seconds));
        return strDD + strHH + hours + strMM + minutes + (ignoreSeconds ? "00" : strSS) + seconds;

    }

    // param age is the no of days added to current date
    public static Date getAgedDate(int age)
    {
        Calendar toDate =  Calendar.getInstance();
        toDate.add(Calendar.DATE,age);
        return toDate.getTime();
    }

    // returns current date - param date
    public static long getAgedDays(Date date)
    {
        Instant current = Instant.now();
        Instant intakeDate = date.toInstant();
        Duration duration = Duration.between(intakeDate,current);

        return duration.toDays();

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

    public static Date getCurrentDateWithoutTime()
    {
        Calendar today =  Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today.getTime();
    }

    public static Date mergeDateTime(Date date, Time time) {
        long tzoffset = -(Time.valueOf("00:00:00").getTime());
        return new Date(date.getTime() + time.getTime() + tzoffset);
    }
}
