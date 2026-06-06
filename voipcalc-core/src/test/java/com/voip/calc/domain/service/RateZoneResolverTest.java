package com.voip.calc.domain.service;

import com.voip.calc.domain.model.RateZone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RateZoneResolver")
class RateZoneResolverTest {

    @ParameterizedTest
    @CsvSource({
        "23:00:00, true",
        "23:30:00, true",
        "00:00:00, true",
        "04:59:59, true",
        "05:00:00, false",
        "05:01:00, false",
        "12:00:00, false",
        "22:59:59, false"
    })
    @DisplayName("Correctly classifies time into NIGHT_OFF_PEAK or DAYTIME")
    void rate_zone_classification(String timeStr, boolean expectedNight) {
        LocalTime time = LocalTime.parse(timeStr);
        RateZone zone = RateZoneResolver.resolve(time);
        assertEquals(expectedNight ? RateZone.NIGHT_OFF_PEAK : RateZone.DAYTIME, zone);
    }

    @Test
    @DisplayName("Night off-peak boundary: exactly 23:00 is included")
    void night_start_inclusive() {
        assertEquals(RateZone.NIGHT_OFF_PEAK, RateZoneResolver.resolve(LocalTime.of(23, 0, 0)));
    }

    @Test
    @DisplayName("Daytime boundary: exactly 05:00 is excluded")
    void day_start_exclusive() {
        assertEquals(RateZone.DAYTIME, RateZoneResolver.resolve(LocalTime.of(5, 0, 0)));
    }
}
