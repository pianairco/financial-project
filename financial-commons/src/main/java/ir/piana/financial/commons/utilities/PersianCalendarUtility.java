package ir.piana.financial.commons.utilities;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class PersianCalendarUtility {
    private static final ULocale PERSIAN_LOCALE = new ULocale("fa_IR@calendar=persian");
    private static final ULocale GREGORIAN_LOCALE = new ULocale("en@calendar=gregorian");

    private static final ZoneId ASIA_TEHRAN_ZONE_ID = ZoneId.of("Asia/Tehran");
    private static final TimeZone ASIA_TEHRAN_TIME_ZONE;

    private static final Calendar gregorianCalendar;
    private static final Calendar persianCalendar;

    /*private static DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd-MMM-yy hh.mm.ss.nnnnnnnnn a", Locale.ENGLISH);*/

    private static final SimpleDateFormat DEFAULT_PERSIAN_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd", PERSIAN_LOCALE);
    private static final SimpleDateFormat DEFAULT_GREGORIAN_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd", GREGORIAN_LOCALE);

    private static final Map<String, SimpleDateFormat> persinaSimpleDateFormatMap;
    private static final Map<String, SimpleDateFormat> gregorianSimpleDateFormatMap;

    static {
        /*Stream.of(Calendar.getAvailableLocales()).forEach(System.out::println);*/
        ASIA_TEHRAN_TIME_ZONE = TimeZone.getTimeZone("Asia/Tehran");

        gregorianCalendar = new GregorianCalendar(GREGORIAN_LOCALE);
        gregorianCalendar.clear();
        gregorianCalendar.setLenient(false);
        gregorianCalendar.setTimeZone(ASIA_TEHRAN_TIME_ZONE);

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
            simpleDateFormat.setTimeZone(ASIA_TEHRAN_TIME_ZONE);
            return simpleDateFormat;
        });
    }

    public static void main(String[] args) throws ParseException {
        Date date = fromGregorianFormatted("2025/03/21");
        String persianFormatted = toPersianFormat(date);
        System.out.println(persianFormatted);

        System.out.printf("instant.now => %s%n", toPersianFormat(Instant.now()));

        System.out.println(fromGregorianFormattedToPersianFormat("2025-03-21", "yyyy-MM-dd", "yyyy/MM/dd"));
        System.out.println(fromGregorianFormattedToPersianFormat("2025-03-21", "yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss"));
        System.out.println(toGregorianFormat(fromGregorianFormatted("2025-03-21", "yyyy-MM-dd"), "yyyy/MM/dd HH:mm:ss"));

        Date now = new Date();
        System.out.printf("now persian => %s%n", toPersianFormat(now, "yyyy/MM/dd hh:mm:ss"));
        System.out.printf("now persian => %s%n", toGregorianFormat(now, "yyyy/MM/dd hh:mm:ss"));

        System.out.printf("now persian => %s%n", fromPersianFormattedToGregorianFormat(persianFormatted, "yyyy/MM/dd", "yyyy/MM/dd"));
        /*gregorianCalendar.setTime(gregorianSimpleDateFormat.parse("2025/03/21"));
        gregorianCalendar.setTimeZone(PERSIAN_TIME_ZONE);
        System.out.println(persianSimpleDateFormat.format(gregorianCalendar.getTime()));
        System.out.println(gregorianSimpleDateFormat.format(gregorianCalendar.getTime()));*/
    }

    public static String toPersianFormat(Date date) {
        return DEFAULT_PERSIAN_DATE_FORMAT.format(date);
    }

    public static String toPersianFormat(Instant instant) {
        return DEFAULT_PERSIAN_DATE_FORMAT.format(Date.from(instant));
    }

    public static String toPersianFormat(Timestamp timestamp) {
        return DEFAULT_PERSIAN_DATE_FORMAT.format(Date.from(timestamp.toInstant()));
    }

    public static String toPersianFormat(LocalDateTime localDateTime) {
        return DEFAULT_PERSIAN_DATE_FORMAT.format(Date.from(localDateTime.atZone(ASIA_TEHRAN_ZONE_ID).toInstant()));
    }

    public static String toPersianFormat(LocalDate localDate) {
        return DEFAULT_PERSIAN_DATE_FORMAT.format(Date.from(localDate.atStartOfDay().atZone(ASIA_TEHRAN_ZONE_ID).toInstant()));
    }

    public static String toPersianFormat(Date date, String format) {
        return getPersianSimpleDateFormat(format).format(date);
    }

    public static String toPersianFormat(Instant instant, String format) {
        return getPersianSimpleDateFormat(format).format(Date.from(instant));
    }

    public static String toPersianFormat(Timestamp timestamp, String format) {
        return getPersianSimpleDateFormat(format).format(Date.from(timestamp.toInstant()));
    }

    public static String toPersianFormat(LocalDateTime localDateTime, String format) {
        return getPersianSimpleDateFormat(format).format(Date.from(localDateTime.atZone(ASIA_TEHRAN_ZONE_ID).toInstant()));
    }

    public static String toPersianFormat(LocalDate localDate, String format) {
        return getPersianSimpleDateFormat(format).format(Date.from(localDate.atStartOfDay().atZone(ASIA_TEHRAN_ZONE_ID).toInstant()));
    }

    public static String toGregorianFormat(Date date) {
        return DEFAULT_GREGORIAN_DATE_FORMAT.format(date);
    }

    public static String toGregorianFormat(Instant instant) {
        return DEFAULT_GREGORIAN_DATE_FORMAT.format(Date.from(instant));
    }

    public static String toGregorianFormat(Timestamp timestamp) {
        return DEFAULT_GREGORIAN_DATE_FORMAT.format(Date.from(timestamp.toInstant()));
    }

    public static String toGregorianFormat(LocalDateTime localDateTime) {
        return DEFAULT_GREGORIAN_DATE_FORMAT.format(Date.from(localDateTime.atZone(ASIA_TEHRAN_ZONE_ID).toInstant()));
    }

    public static String toGregorianFormat(LocalDate localDate) {
        return DEFAULT_GREGORIAN_DATE_FORMAT.format(Date.from(localDate.atStartOfDay().atZone(ASIA_TEHRAN_ZONE_ID).toInstant()));
    }

    public static String toGregorianFormat(Date date, String format) {
        return getGregorianSimpleDateFormat(format).format(date);
    }

    public static String toGregorianFormat(Instant instant, String format) {
        return getGregorianSimpleDateFormat(format).format(Date.from(instant));
    }

    public static String toGregorianFormat(Timestamp timestamp, String format) {
        return getGregorianSimpleDateFormat(format).format(Date.from(timestamp.toInstant()));
    }

    public static String toGregorianFormat(LocalDateTime localDateTime, String format) {
        return getGregorianSimpleDateFormat(format).format(Date.from(localDateTime.atZone(ASIA_TEHRAN_ZONE_ID).toInstant()));
    }

    public static String toGregorianFormat(LocalDate localDate, String format) {
        return getGregorianSimpleDateFormat(format).format(Date.from(localDate.atStartOfDay().atZone(ASIA_TEHRAN_ZONE_ID).toInstant()));
    }

    public static Date fromPersianFormatted(String persianFormatted) throws ParseException {
        return DEFAULT_PERSIAN_DATE_FORMAT.parse(persianFormatted);
    }

    public static Instant fromPersianFormattedToInstant(String persianFormatted) throws ParseException {
        return DEFAULT_PERSIAN_DATE_FORMAT.parse(persianFormatted).toInstant();
    }

    public static Timestamp fromPersianFormattedToTimestamp(String persianFormatted) throws ParseException {
        return Timestamp.from(DEFAULT_PERSIAN_DATE_FORMAT.parse(persianFormatted).toInstant());
    }

    public static LocalDateTime fromPersianFormattedToLocalDateTime(String persianFormatted) throws ParseException {
        return LocalDateTime.ofInstant(DEFAULT_PERSIAN_DATE_FORMAT.parse(persianFormatted).toInstant(), ASIA_TEHRAN_ZONE_ID);
    }

    public static LocalDate fromPersianFormattedToLocalDate(String persianFormatted) throws ParseException {
        return LocalDateTime.ofInstant(DEFAULT_PERSIAN_DATE_FORMAT.parse(persianFormatted).toInstant(), ASIA_TEHRAN_ZONE_ID).toLocalDate();
    }

    public static Date fromPersianFormatted(String persianFormatted, String format) throws ParseException {
        return getPersianSimpleDateFormat(format).parse(persianFormatted);
    }

    public static Instant fromPersianFormattedToInstant(String persianFormatted, String format) throws ParseException {
        return getPersianSimpleDateFormat(format).parse(persianFormatted).toInstant();
    }

    public static Timestamp fromPersianFormattedToTimestamp(String persianFormatted, String format) throws ParseException {
        return Timestamp.from(getPersianSimpleDateFormat(format).parse(persianFormatted).toInstant());
    }

    public static LocalDateTime fromPersianFormattedToLocalDateTime(String persianFormatted, String format) throws ParseException {
        return LocalDateTime.ofInstant(getPersianSimpleDateFormat(format).parse(persianFormatted).toInstant(), ASIA_TEHRAN_ZONE_ID);
    }

    public static LocalDate fromPersianFormattedToLocalDate(String persianFormatted, String format) throws ParseException {
        return LocalDateTime.ofInstant(getPersianSimpleDateFormat(format).parse(persianFormatted).toInstant(), ASIA_TEHRAN_ZONE_ID).toLocalDate();
    }

    public static Date fromGregorianFormatted(String gregorianFormatted) throws ParseException {
        return DEFAULT_GREGORIAN_DATE_FORMAT.parse(gregorianFormatted);
    }

    public static Instant fromGregorianFormattedToInstant(String gregorianFormatted) throws ParseException {
        return DEFAULT_GREGORIAN_DATE_FORMAT.parse(gregorianFormatted).toInstant();
    }

    public static Timestamp fromGregorianFormattedToTimestamp(String gregorianFormatted) throws ParseException {
        return Timestamp.from(DEFAULT_GREGORIAN_DATE_FORMAT.parse(gregorianFormatted).toInstant());
    }

    public static LocalDateTime fromGregorianFormattedToLocalDateTime(String gregorianFormatted) throws ParseException {
        return LocalDateTime.ofInstant(DEFAULT_GREGORIAN_DATE_FORMAT.parse(gregorianFormatted).toInstant(), ASIA_TEHRAN_ZONE_ID);
    }

    public static LocalDate fromGregorianFormattedToLocalDate(String gregorianFormatted) throws ParseException {
        return LocalDateTime.ofInstant(DEFAULT_GREGORIAN_DATE_FORMAT.parse(gregorianFormatted).toInstant(), ASIA_TEHRAN_ZONE_ID).toLocalDate();
    }

    public static Date fromGregorianFormatted(String gregorianFormatted, String format) throws ParseException {
        return getGregorianSimpleDateFormat(format).parse(gregorianFormatted);
    }

    public static Instant fromGregorianFormattedToInstant(String gregorianFormatted, String format) throws ParseException {
        return getGregorianSimpleDateFormat(format).parse(gregorianFormatted).toInstant();
    }

    public static Timestamp fromGregorianFormattedToTimestamp(String gregorianFormatted, String format) throws ParseException {
        return Timestamp.from(getGregorianSimpleDateFormat(format).parse(gregorianFormatted).toInstant());
    }

    public static LocalDateTime fromGregorianFormattedToLocalDateTime(String gregorianFormatted, String format) throws ParseException {
        return LocalDateTime.ofInstant(getGregorianSimpleDateFormat(format).parse(gregorianFormatted).toInstant(), ASIA_TEHRAN_ZONE_ID);
    }

    public static LocalDate fromGregorianFormattedToLocalDate(String gregorianFormatted, String format) throws ParseException {
        return LocalDateTime.ofInstant(getGregorianSimpleDateFormat(format).parse(gregorianFormatted).toInstant(), ASIA_TEHRAN_ZONE_ID).toLocalDate();
    }

    public static String fromGregorianFormattedToPersianFormat(String gregorianFormatted, String gregorianFormat, String persianFormat) throws ParseException {
        return toPersianFormat(getGregorianSimpleDateFormat(gregorianFormat).parse(gregorianFormatted), persianFormat);
    }

    public static String fromPersianFormattedToGregorianFormat(String persianFormatted, String persianFormat, String gregorianFormat) throws ParseException {
        return toGregorianFormat(getPersianSimpleDateFormat(persianFormat).parse(persianFormatted), gregorianFormat);
    }
}
