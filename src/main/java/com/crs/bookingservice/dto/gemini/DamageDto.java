package com.crs.bookingservice.dto.gemini;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DamageDto {
    private int id;
    private String type;
    private String location;
    private String severity;
    private DamageCoordsDto coords;
}
