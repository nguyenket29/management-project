package com.hau.ketnguyen.it.common.util;

import java.time.*;
import java.util.TimeZone;

public class DateUtils {

    private DateUtils() {
        // do nothing
    }
    public static TimeZone getVnTimeZone() {
        return TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
    }

    public static ZoneId getVnZoneId() {
        return getVnTimeZone().toZoneId();
    }

    public static Instant getStartOfDay(Instant date, String zone) {
        LocalDateTime localDateTime = convertInstantToLocalDateTime(date, zone);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return convertLocalDateTimeToInstant(startOfDay, zone);
    }

    public static Instant getEndOfDay(Instant date, String zone) {
        LocalDateTime localDateTime = convertInstantToLocalDateTime(date, zone);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return convertLocalDateTimeToInstant(endOfDay, zone);
    }

    public static LocalDateTime convertInstantToLocalDateTime(Instant date, String zone) {
        return LocalDateTime.ofInstant(date, ZoneId.of(zone));
    }

    public static Instant convertLocalDateTimeToInstant(LocalDateTime localDateTime, String zone) {
        return localDateTime.atZone(ZoneId.of(zone)).toInstant();
    }

    public static Instant convertLocalDateToInstant(LocalDate localDateTime, String zone) {
        return localDateTime.atTime(0,0,0).atZone(ZoneId.of(zone)).toInstant();
    }
}
