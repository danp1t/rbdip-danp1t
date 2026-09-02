package com.rbdip.bookstore.order;

import com.rbdip.bookstore.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    void saveOrderItem(Order order, Product product, Integer quantity) {
        orderItemRepository.save(new OrderItem(order.getId(), product.getName(), product.getPrice(), quantity));
    }

    List<OrderItem> findByOrderId(Long id) {
        return orderItemRepository.findByOrderId(id);
    }
}
