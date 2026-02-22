package com.insett.ingestionservice.data.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CardDto {
    public String id;
    public String name;
    @JsonProperty("riftbound_id")
    public String riftboundId;
    @JsonProperty("tcgplayer_id")
    public String tcgplayerId;
    @JsonProperty("public_code")
    public String publicCode;
    @JsonProperty("collector_number")
    public int collectorNumber;
    public AttributesDto attributesDto;
    public ClassificationDto classificationDto;
    public TextDto text;
    public SetDto set;
    public MediaDto mediaDto;
    public List<String> tags;
    public String orientation;
    public MetadataDto metadata;
}


