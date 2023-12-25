package com.simbirsoft.domain;

import com.simbirsoft.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cafe_order")
@Getter
@Setter
public class Order implements Serializable {
    @Id
    @Column(name = "code")
    private UUID code;
    @Column(name = "date")
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;
    @Column(name = "comment")
    private String comment;
    @Column(name = "customer_id")
    private int customerId;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private Set<OrderItem> orderItemSet;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", updatable = false, insertable = false)
    private User customer;
}
