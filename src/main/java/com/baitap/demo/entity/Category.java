package com.baitap.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String images;

    @ManyToMany(mappedBy = "categories")
    private List<User> users;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}