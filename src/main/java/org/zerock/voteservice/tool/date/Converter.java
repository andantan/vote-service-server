package org.zerock.voteservice.tool.date;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.time.ZoneId;
import java.time.LocalDateTime;

public class Converter {
    private static final String SOUTH_KOREA_TIME_ZONE_ID = "Asia/Seoul";
    private static final ZoneId KST_ZONE_ID = ZoneId.of(SOUTH_KOREA_TIME_ZONE_ID);

    public static LocalDateTime toKstLocalDateTime(Timestamp timestamp) throws NullPointerException {
        if (timestamp == null) {
            throw new NullPointerException("null-timestamp");
        }

        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());

        return LocalDateTime.ofInstant(instant, KST_ZONE_ID);
    }
}
