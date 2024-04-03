package dev.distributed.contract;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimestampHelper {
    public static Timestamp getCurrentTimestamp(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return Timestamp.valueOf(localDateTime);
    }
}
