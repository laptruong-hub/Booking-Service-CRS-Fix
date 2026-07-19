package com.crs.bookingservice.dto.gemini;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DamageAssessmentRequest {
    private String base64Image;
    private String mimeType;
}
