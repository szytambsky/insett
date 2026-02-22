package com.insett.ingestionservice.data.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetadataDto {
    public String clean_name;
    @JsonProperty("alternate_art")
    public boolean alternateArt;
    public boolean overnumbered;
    public boolean signature;
}
