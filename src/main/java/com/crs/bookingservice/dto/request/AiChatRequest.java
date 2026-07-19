package com.crs.bookingservice.dto.request;

import lombok.Data;

@Data
public class AiChatRequest {
    private String message;
    private String context; // e.g. destination name or trip context
}
