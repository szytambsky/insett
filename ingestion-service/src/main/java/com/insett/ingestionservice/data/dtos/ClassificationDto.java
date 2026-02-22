package com.insett.ingestionservice.data.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ClassificationDto {
    public String type;
    public Object supertype;
    public String rarity;
    public List<String> domain;
}
