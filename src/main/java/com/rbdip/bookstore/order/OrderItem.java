package com.rbdip.bookstore.order;

import com.rbdip.bookstore.product.Product;
import jakarta.persistence.*;

/**
 * Намеренно денормализовано: дублирует название и цену товара вместо
 * ссылки на products (product_id есть, но name/price скопированы на
 * момент заказа) - цель нормализации в ЛР2.
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    protected OrderItem() {
        // for JPA
    }

    public OrderItem(Long orderId, Product product, Integer quantity) {
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Product getProduct() {return product;}

    public Integer getQuantity() {
        return quantity;
    }
}
