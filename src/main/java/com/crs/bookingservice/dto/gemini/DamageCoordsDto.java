package com.crs.bookingservice.dto.gemini;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DamageCoordsDto {
    private int x;
    private int y;
    private int width;
    private int height;
}
