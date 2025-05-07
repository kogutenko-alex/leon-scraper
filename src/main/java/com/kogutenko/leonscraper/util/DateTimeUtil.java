package com.kogutenko.leonscraper.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'").withZone(ZoneOffset.UTC);

    public static String formatUtc(long epochMillis) {
        return FORMATTER.format(Instant.ofEpochMilli(epochMillis));
    }
} 