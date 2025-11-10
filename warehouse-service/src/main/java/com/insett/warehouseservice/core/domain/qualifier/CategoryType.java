package com.insett.warehouseservice.core.domain.qualifier;

import lombok.Getter;

@Getter
public enum CategoryType {
    BUSINESS(15),
    REAL_ESTATE(22),
    ELECTRONICS(30),
    FASHION(30),
    SPORTS(40),
    MUSIC(12),
    COLLECTIBLES(22),
    HEALTH(10),
    FOR_CHILDREN(15);

    CategoryType(int value) {
        this.taxRate = value;
    }

    private final int taxRate;

    public boolean isForAdults() {
        return !this.equals(CategoryType.FOR_CHILDREN);
    }

    public boolean isPhysicalProduct() {
        return switch(this) {
            case REAL_ESTATE, ELECTRONICS, FASHION, SPORTS, COLLECTIBLES, FOR_CHILDREN -> true;
            default -> false;
        };
    }
}
