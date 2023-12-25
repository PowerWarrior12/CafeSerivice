package com.simbirsoft.domain;

import com.simbirsoft.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cafe_user")
@Getter
@Setter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "address")
    private String address;
    @Column(name = "activated")
    private boolean activated;
    @Column(name = "activation_key")
    private String activationKey;
    @Column(name = "reset_key")
    private String resetPasswordKey;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "cafe_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private Set<Role> roles;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private Set<Order> orderSet;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private Set<BasketItem> basketItemSet;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
