package com.insett.warehouseservice.core.domain.qualifier;

import lombok.Getter;

@Getter
public enum LocationQualifier {
    WARSAW("Warsaw", "Poland", "WAW"),
    TOKYO("Tokyo", "Japan", "TYO"),
    FRANKFURT("Frankfurt", "Germany", "FRA");

    private final String cityName;
    private final String country;
    private final String zipCode;

    LocationQualifier(String cityName, String country, String zipCode) {
        this.cityName = cityName;
        this.country = country;
        this.zipCode = zipCode;
    }
}
