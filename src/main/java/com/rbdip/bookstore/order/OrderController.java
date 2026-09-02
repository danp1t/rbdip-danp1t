package com.rbdip.bookstore.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rbdip.bookstore.product.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public OrderController(
            OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @PostMapping("/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createOrder(@RequestBody CreateOrderRequest request) {
        List<Product> products = new ArrayList<>();
        List<PricingCalculator.LineItem> lineItems = new ArrayList<>();

        orderService.createCheck(products, lineItems, request);
        orderService.validateRequest(request);
        BigDecimal total = orderService.calculatePrice(lineItems, request);
        Order order = orderService.createOrder(request, products, lineItems);

        orderService.sendConfirmationEmail(request.customerFullName(), order.getId(), total);
        return Map.of("id", order.getId(), "status", order.getStatus());
    }

    @GetMapping("/orders")
    public List<Map<String, Object>> listOrders() {
        List<Order> orders = orderService.getOrders();
        return orders.stream()
                .map(order -> {
                    // N+1: отдельный запрос на позиции для каждого заказа вместо
                    // одного JOIN FETCH / batch-запроса. Цель для ЛР4.
                    List<OrderItem> items = orderItemService.findByOrderId(order.getId());
                    return Map.<String, Object>of(
                            "id", order.getId(),
                            "customerFullName", order.getCustomerFullName(),
                            "status", order.getStatus(),
                            "items", items.stream()
                                    .map(i -> Map.of("productName", i.getProductName(), "quantity", i.getQuantity()))
                                    .toList());
                })
                .toList();
    }
}
