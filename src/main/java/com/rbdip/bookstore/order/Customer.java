package com.rbdip.bookstore.order;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_full_name", nullable = false)
    private String customerFullName;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "customer_phone")
    private String customerPhone;

    public Customer(String customerFullName, String customerAddress, String customerPhone) {
        this.customerFullName = customerFullName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
    }

    protected Customer() {
        // for JPA
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }
}
