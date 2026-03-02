package com.overcode250204.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "product_table")
@Getter
@Setter
public class Product {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;
}
