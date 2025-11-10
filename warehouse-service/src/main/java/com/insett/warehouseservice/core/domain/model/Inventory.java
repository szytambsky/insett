package com.insett.warehouseservice.core.domain.model;

import com.insett.warehouseservice.core.domain.qualifier.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID inventoryId;

    private BigInteger quantity;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Location location;

    @ManyToMany(mappedBy = "inventories")
    private List<Product> products = new ArrayList<>();

    private BigInteger addStock(BigInteger qty) {
        this.quantity = this.quantity.add(qty);
        return this.quantity;
    }

    private BigInteger removeStock(BigInteger qty) {
        this.quantity = this.quantity.subtract(qty);
        return this.quantity;
    }

    private boolean reserveStock(BigInteger qty) {
        BigInteger quantityLeft = this.quantity.subtract(qty);
        return quantityLeft.intValue() > 0;
    }
}
