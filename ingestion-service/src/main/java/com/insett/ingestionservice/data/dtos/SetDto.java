package com.insett.ingestionservice.data.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SetDto {
    @JsonProperty("set_id")
    private String setId;
    private String label;
}
