package com.simbirsoft.common;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TimeProvider {
    public Instant getCurrentTimeOfCurrentTimeZone() {
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        return localDateTimeNow.atZone(ZoneId.systemDefault()).toInstant();
    }

    public Instant getTimeOfCurrentTimeZoneWithOffset(Instant currentTime, Duration offset) {
        return currentTime.plusNanos(offset.toNanos()).atZone(ZoneId.systemDefault()).toInstant();
    }
}
