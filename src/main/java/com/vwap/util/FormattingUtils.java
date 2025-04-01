package com.vwap.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

public final class FormattingUtils {

    /* The time formatter to convert the timestamp from the input. */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a");

    /* The logger for the class. */
    private static final Logger LOGGER = Logger.getLogger(FormattingUtils.class.getName());

    /* The time zone to be used. */
    public static final String ZONE_ID = "Australia/Sydney";

    /**
     * Convert the time from string format to {@link Instant}.
     *
     * @param timeAsString time represented as a string
     * @return the time as an {@link Instant}
     */
    public static Instant timeAsInstant(final String timeAsString) {
        try {
            LocalTime localTime = LocalTime.parse(timeAsString.toLowerCase(), TIME_FORMATTER);
            LocalDate today = LocalDate.now(ZoneId.of(ZONE_ID));
            return LocalDateTime.of(today, localTime).toInstant(ZoneOffset.UTC);
        } catch (DateTimeParseException e) {
            LOGGER.severe("Invalid time format: " + timeAsString);
            LOGGER.severe(e.toString());
            return null;
        }
    }

    /**
     * Convert the volume from string format to long.
     *
     * @param volumeAsString volume represented as a string
     * @return the volume as a long
     */
    public static long volumeFormat(final String volumeAsString) {
        try {
            // Parse volume (remove any commas in numbers like "106,198")
            String volumeStr = volumeAsString.trim().replace(",", "");
            return Long.parseLong(volumeStr);
        } catch (DateTimeParseException e) {
            LOGGER.severe("Invalid volume format: " + volumeAsString);
            LOGGER.severe(e.toString());
            return 0;
        }
    }
}
