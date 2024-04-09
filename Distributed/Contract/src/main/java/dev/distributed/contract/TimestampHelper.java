package dev.distributed.contract;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimestampHelper {
    /**
     * Quickly get the current timestamp.
     */
    public static Timestamp getCurrentTimestamp(){
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
