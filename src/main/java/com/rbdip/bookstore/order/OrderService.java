package com.rbdip.bookstore.order;

import com.rbdip.bookstore.product.Product;
import java.math.BigDecimal;
import java.util.List;

import com.rbdip.bookstore.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * God-класс: валидация, расчёт цены, персистентность и "уведомление
 * клиента" смешаны в одном методе. Цель для рефакторинга по SRP в ЛР1.
 */
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final PricingCalculator pricingCalculator = new PricingCalculator();

    public OrderService(
            ProductService productService,
            OrderRepository orderRepository,
            OrderItemService orderItemService) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request, List<Product> products, List<PricingCalculator.LineItem> lineItems) {
        Order order = saveOrder(request);
        saveOrderItems(order, products, lineItems);

        return order;
    }

    public void sendConfirmationEmail(String customerName, Long orderId, BigDecimal total) {
        // Реальный почтовый транспорт не настроен в учебном проекте - здесь
        // просто эмулируется побочный эффект отправки письма.
        System.out.printf(
                "[email] Dear %s, your order #%d for %s has been placed.%n", customerName, orderId, total);
    }

    public void createCheck(List<Product> products,
                              List<PricingCalculator.LineItem> lineItems,
                              CreateOrderRequest request) {
        for (CreateOrderRequest.Item raw : request.items()) {
            Product product = productService.getProductById(raw.productId());
            Integer quantity = request.getQuantity(raw);
            products.add(product);
            lineItems.add(new PricingCalculator.LineItem(product.getPrice(), quantity));
        }
    }

    public BigDecimal calculatePrice(List<PricingCalculator.LineItem> lineItems,
                                      CreateOrderRequest request) {
        return pricingCalculator.calculateOrderTotal(
                lineItems, request.customerType() == null ? "regular" : request.customerType(), request.couponCode());
    }

    private Order saveOrder(CreateOrderRequest request) {
        Order order = new Order(
                request.customerFullName(), request.customerAddress(), request.customerPhone(), "new");
        return orderRepository.save(order);
    }

    private void saveOrderItems(Order order,
                                List<Product> products,
                                List<PricingCalculator.LineItem> lineItems) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int quantity = lineItems.get(i).quantity();
            orderItemService.saveOrderItem(order, product, quantity);
        }
    }

    public void validateRequest(CreateOrderRequest request) {
        request.validateCustomerInfo();
        request.validateProductInfo();
    }
}
