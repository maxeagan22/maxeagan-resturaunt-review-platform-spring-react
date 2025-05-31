package com.maxeagan.restaurant.domain.dtos;

import com.maxeagan.restaurant.domain.entities.TimeRange;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a restaurant's weekly operating hours.
 * <p>
 * Weekdays use {@link TimeRangeDto}, while weekends currently use {@link TimeRange}.
 * Adjust to match your intended structure (e.g., use all DTOs if this is an oversight).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperatingHoursDto {

    @Valid
    private TimeRangeDto monday;

    @Valid
    private TimeRangeDto tuesday;

    @Valid
    private TimeRangeDto wednesday;

    @Valid
    private TimeRangeDto thursday;

    @Valid
    private TimeRangeDto friday;

    @Valid
    private TimeRangeDto saturday;

    @Valid
    private TimeRangeDto sunday;
}
