package com.voip.calc.domain.model;

/**
 * Rate zone classification for call initiation time.
 * NIGHT_OFF_PEAK: 23:00–05:00 (next day) — reduced rate
 * DAYTIME: 05:01–22:59 — standard rate
 */
public enum RateZone {
    NIGHT_OFF_PEAK,
    DAYTIME;

    public boolean isNightOffPeak() {
        return this == NIGHT_OFF_PEAK;
    }
}
