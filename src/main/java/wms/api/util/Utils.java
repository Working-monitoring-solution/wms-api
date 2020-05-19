package wms.api.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.ObjectUtils;
import wms.api.exception.WMSException;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
    public static final String ddMMyyyy = "dd/MM/yyyy";

    public static final String DDmmYYYY = "ddMMyyyy";

    public static final String ddMMyyyyHHmmSS = "dd/MM/yyyy HH:mm:ss";

    public static final String HHmmSSddMMyyyy = "HH:mm:ss dd/MM/yyyy";

    public static final String DATE_TIME_MYSQL_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String HHmmddMM = "HH:mm dd/MM";

    public static final String yyyyMMdd = "yyyyMMdd";

    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    public static Date toBeginDate(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        toBeginDate(cal);
        date = cal.getTime();
        return date;
    }

    public static Date toEndDate(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        toEndDate(cal);
        date = cal.getTime();
        return date;
    }

    public static void toBeginDate(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public static void toEndDate(Calendar cal) {
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public static Double toDouble(String number) {
        if (ObjectUtils.isEmpty(number) || !NumberUtils.isParsable(number)) {
            throw new WMSException.InvalidInputException();
        }
        return Double.valueOf(number);
    }

    public static Long toLong(String number) {
        if (ObjectUtils.isEmpty(number) || !NumberUtils.isParsable(number)) {
            throw new WMSException.InvalidInputException();
        }
        return Long.valueOf(number);
    }

    public static Integer toInt(String number, String field) {
        if (ObjectUtils.isEmpty(number) || !NumberUtils.isParsable(number)) {
            throw new WMSException.InvalidInputException(field);
        }
        return Integer.valueOf(number);
    }

    public static Date toDate(String date, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date result = simpleDateFormat.parse(date);
            return result;
        } catch (ParseException e) {
            throw new WMSException.InvalidInputException("date");
        }
    }
}
