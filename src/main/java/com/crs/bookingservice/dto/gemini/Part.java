package com.crs.bookingservice.dto.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Part {

    private String text;

    @JsonProperty("inline_data")
    private InlineData inlineData;

    public Part(String text) {
        this.text = text;
    }
}