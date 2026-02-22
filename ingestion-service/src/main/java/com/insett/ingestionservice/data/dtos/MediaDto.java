package com.insett.ingestionservice.data.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MediaDto {
    @JsonProperty("image_url")
    public String imageUrl;
    public String artist;
    @JsonProperty("accessibility_text")
    public String accessibilityText;
}
