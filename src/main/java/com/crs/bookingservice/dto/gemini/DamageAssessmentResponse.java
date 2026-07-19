package com.crs.bookingservice.dto.gemini;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DamageAssessmentResponse {
    private boolean hasDamage;
    private List<DamageDto> damages;
    private int confidence;
}
