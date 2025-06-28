package org.zerock.voteservice.tool.time;

import com.google.protobuf.Timestamp;

import java.time.*;

public class DateConverter {
    private static final String SOUTH_KOREA_TIME_ZONE_ID = "Asia/Seoul";
    private static final ZoneId KST_ZONE_ID = ZoneId.of(SOUTH_KOREA_TIME_ZONE_ID);

    public static LocalDateTime toKstLocalDateTime(Timestamp timestamp) throws NullPointerException {
        if (timestamp == null) {
            throw new NullPointerException("null-timestamp");
        }

        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());

        return LocalDateTime.ofInstant(instant, KST_ZONE_ID);
    }

    public static Timestamp toTimestamp(LocalDate localDate) {
        long epochSeconds = localDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond();

        return Timestamp.newBuilder()
                .setSeconds(epochSeconds)
                .setNanos(0)
                .build();
    }
}
