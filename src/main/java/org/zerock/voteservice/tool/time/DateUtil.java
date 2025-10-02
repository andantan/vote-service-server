package org.zerock.voteservice.tool.time;

import java.util.Date;

public class DateUtil {
    public static Date now() {
        return new Date();
    }

    public static Date after(long seconds) {
        return new Date(System.currentTimeMillis() + (seconds * 1000L));
    }

    public static boolean isPast(Date date) {
        return date.before(new Date());
    }
}
