package com.simbirsoft.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "count")
    private int count;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "order_code")
    private UUID orderCode;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_code", referencedColumnName = "code", updatable = false, insertable = false)
    private Order order;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Product product;
}
