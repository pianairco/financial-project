package ir.piana.financial.commons.utilities;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class GMTCalendarUtility {
    private static final ULocale PERSIAN_LOCALE = new ULocale("fa_IR@calendar=persian");
    private static final ULocale GREGORIAN_LOCALE = new ULocale("en@calendar=gregorian");

    private static final ZoneId ASIA_TEHRAN_ZONE_ID;
    private static final ZoneId GMT_ZONE_ID;

    private static final TimeZone ASIA_TEHRAN_TIME_ZONE;
    private static final TimeZone GMT_TIME_ZONE;

    private static final Calendar gregorianCalendar;
    private static final Calendar persianCalendar;

    private static final SimpleDateFormat DEFAULT_PERSIAN_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd", PERSIAN_LOCALE);
    private static final SimpleDateFormat DEFAULT_GREGORIAN_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd", GREGORIAN_LOCALE);

    private static final Map<String, SimpleDateFormat> persinaSimpleDateFormatMap;
    private static final Map<String, SimpleDateFormat> gregorianSimpleDateFormatMap;

    static {
        /*Stream.of(Calendar.getAvailableLocales()).forEach(System.out::println);*/
        GMT_ZONE_ID = ZoneId.of("GMT");
        GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");

        ASIA_TEHRAN_ZONE_ID = ZoneId.of("Asia/Tehran");
        ASIA_TEHRAN_TIME_ZONE = TimeZone.getTimeZone("Asia/Tehran");

        gregorianCalendar = new GregorianCalendar(GREGORIAN_LOCALE);
        gregorianCalendar.clear();
        gregorianCalendar.setLenient(false);
        gregorianCalendar.setTimeZone(GMT_TIME_ZONE);

        persianCalendar = Calendar.getInstance(PERSIAN_LOCALE);
        persianCalendar.clear();
        persianCalendar.setLenient(false);
        persianCalendar.setTimeZone(ASIA_TEHRAN_TIME_ZONE);

        persinaSimpleDateFormatMap = new LinkedHashMap<>();
        persinaSimpleDateFormatMap.computeIfAbsent(null, k -> DEFAULT_PERSIAN_DATE_FORMAT);
        gregorianSimpleDateFormatMap = new LinkedHashMap<>();
        gregorianSimpleDateFormatMap.computeIfAbsent(null, k -> DEFAULT_GREGORIAN_DATE_FORMAT);
    }

    public static SimpleDateFormat getPersianSimpleDateFormat(String format) {
        return persinaSimpleDateFormatMap.computeIfAbsent(format, k -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, PERSIAN_LOCALE);
            simpleDateFormat.setTimeZone(ASIA_TEHRAN_TIME_ZONE);
            return simpleDateFormat;
        });
    }

    public static SimpleDateFormat getGregorianSimpleDateFormat(String format) {
        return gregorianSimpleDateFormatMap.computeIfAbsent(format, k -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, GREGORIAN_LOCALE);
            simpleDateFormat.setTimeZone(GMT_TIME_ZONE);
            return simpleDateFormat;
        });
    }

    public static void main(String[] args) throws ParseException {
        Date date = fromGregorianFormatted("2025/03/21");
        String persianFormatted = toPersianFormat(date);
        System.out.println(persianFormatted);

        System.out.println(fromGregorianFormattedToPersianFormat("2025-03-21", "yyyy-MM-dd", "yyyy/MM/dd"));
        System.out.println(fromGregorianFormattedToPersianFormat("2025-03-21", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss"));
        System.out.println(toGregorianFormat(fromGregorianFormatted("2025-03-21", "yyyy-MM-dd"), "yyyy/MM/dd HH:mm:ss"));

        Date now = new Date();
        System.out.println(toPersianFormat(now, "yyyy/MM/dd HH:mm:ss"));
        System.out.println(toGregorianFormat(now, "yyyy/MM/dd HH:mm:ss"));
        System.out.println(fromPersianFormattedToGregorianFormat(persianFormatted, "yyyy/MM/dd", "yyyy/MM/dd"));

        /*gregorianCalendar.setTime(gregorianSimpleDateFormat.parse("2025/03/21"));
        gregorianCalendar.setTimeZone(PERSIAN_TIME_ZONE);
        System.out.println(persianSimpleDateFormat.format(gregorianCalendar.getTime()));
        System.out.println(gregorianSimpleDateFormat.format(gregorianCalendar.getTime()));*/
    }

    public static String toPersianFormat(Date date) {
        return DEFAULT_PERSIAN_DATE_FORMAT.format(date);
    }

    public static String toPersianFormat(Date date, String format) {
        return getPersianSimpleDateFormat(format).format(date);
    }

    public static String toGregorianFormat(Date date) {
        return DEFAULT_GREGORIAN_DATE_FORMAT.format(date);
    }

    public static String toGregorianFormat(Date date, String format) {
        return getGregorianSimpleDateFormat(format).format(date);
    }

    public static Date fromPersianFormatted(String persianFormatted) throws ParseException {
        return getPersianSimpleDateFormat(null).parse(persianFormatted);
    }

    public static Date fromPersianFormatted(String persianFormatted, String format) throws ParseException {
        return getPersianSimpleDateFormat(format).parse(persianFormatted);
    }

    public static Date fromGregorianFormatted(String gregorianFormatted) throws ParseException {
        return getGregorianSimpleDateFormat(null).parse(gregorianFormatted);
    }

    public static Date fromGregorianFormatted(String gregorianFormatted, String format) throws ParseException {
        return getGregorianSimpleDateFormat(format).parse(gregorianFormatted);
    }

    public static String fromGregorianFormattedToPersianFormat(String gregorianFormatted, String gregorianFormat, String persianFormat) throws ParseException {
        return toPersianFormat(getGregorianSimpleDateFormat(gregorianFormat).parse(gregorianFormatted), persianFormat);
    }

    public static String fromPersianFormattedToGregorianFormat(String persianFormatted, String persianFormat, String gregorianFormat) throws ParseException {
        return toGregorianFormat(getPersianSimpleDateFormat(persianFormat).parse(persianFormatted), gregorianFormat);
    }
}
