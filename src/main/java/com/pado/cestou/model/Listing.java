package com.pado.cestou.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @ManyToOne
    @JoinColumn(name="seller_id", nullable = false)
    private Employee seller;

    @ManyToOne
    @JoinColumn(name="buyer_id")
    private Employee buyer;

    @Column(nullable = false, updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setSeller(Employee seller) {
        this.seller = seller;
    }

    public void setBuyer(Employee buyer) {
        this.buyer = buyer;
    }

    public Long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Employee getSeller() {
        return seller;
    }

    public Employee getBuyer() {
        return buyer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
