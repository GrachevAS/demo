package com.example.demo.model.db.entity;

import com.example.demo.model.enums.CarStatus;
import com.example.demo.model.enums.Color;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name ="cars")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "brand")
    String brand;

    @Column(name = "model")
    String model;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    Color color;

    @Column(name = "year")
    Integer year;

    @Column(name = "price")
    BigDecimal price;

    @Column(name = "is_new")
    Boolean isNew;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    CarStatus status;

    @ManyToOne
    @JsonBackReference(value = "driver_cars")
    User user;
}
