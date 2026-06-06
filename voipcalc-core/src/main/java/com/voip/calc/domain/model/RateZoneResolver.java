package com.voip.calc.domain.model;

import java.time.LocalTime;

/**
 * Resolves RateZone from a LocalTime.
 * Night off-peak window: 23:00:00 – 04:59:59
 */
public final class RateZoneResolver {

    private static final LocalTime NIGHT_START = LocalTime.of(23, 0, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(5, 0, 1);

    private RateZoneResolver() {}

    public static RateZone resolve(LocalTime time) {
        if (time.isAfter(NIGHT_START) || time.isBefore(NIGHT_END)) {
            return RateZone.NIGHT_OFF_PEAK;
        }
        return RateZone.DAYTIME;
    }
}
