package com.rbdip.bookstore.order;

import com.rbdip.bookstore.product.Product;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    void saveOrderItem(Order order, Product product, Integer quantity) {
        orderItemRepository.save(new OrderItem(order.getId(), product.getName(), product.getPrice(), quantity));
    }
}
