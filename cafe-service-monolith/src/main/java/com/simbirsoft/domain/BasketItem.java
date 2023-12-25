package com.simbirsoft.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "basket_item")
@Getter
@Setter
public class BasketItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "count")
    private int count;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, insertable = false)
    private User user;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Product product;
}
