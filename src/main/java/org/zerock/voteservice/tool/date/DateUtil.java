package org.zerock.voteservice.tool.date;

import java.util.Date;

public class DateUtil {
    public static Date now() {
        return new Date();
    }

    public static Date after(long millis) {
        return new Date(System.currentTimeMillis() + millis);
    }
}
