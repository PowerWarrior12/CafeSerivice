package com.simbirsoft.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "price")
    private float price;
    @Column(name = "description")
    private String description;
    @Column(name = "category")
    private String category;
    @Column(name = "producer")
    private String producer;
    @Column(name = "availability")
    private boolean availability;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @BatchSize(size = 20)
    private Set<OrderItem> orderItemSet;
}
