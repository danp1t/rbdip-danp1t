package com.rbdip.bookstore.reference;

import static org.assertj.core.api.Assertions.assertThat;

import com.rbdip.bookstore.order.PricingCalculator;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Эталонный тест покрывает только базовый путь расчёта цены (обычный
 * клиент, без купона, без оптовой скидки). Остальные ветки (VIP,
 * wholesale, купоны, отрицательный итог, скидка за объём, потолок
 * 1000) намеренно НЕ покрыты - это задание ЛР2: написать
 * характеризационные тесты на эти случаи перед рефакторингом класса.
 */
class PricingCalculatorReferenceTest {

    private final PricingCalculator calculator = new PricingCalculator();

    @Test
    void calculatesSimpleRegularOrder() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("10.00"), 2)), "regular", null);

        assertThat(total).isEqualByComparingTo("20.00");
    }

    @Test
    void calculatesBigOrder() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("10.00"), 11)), "regular", null);

        assertThat(total).isEqualByComparingTo("104.50");
    }

    @Test
    void calculatesVipOrder() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("10.00"), 2)), "vip", null);

        assertThat(total).isEqualByComparingTo("18.00");
    }

    @Test
    void calculatesWholesaleOrder() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("10.00"), 2)), "wholesale", null);

        assertThat(total).isEqualByComparingTo("17.00");
    }

    @Test
    void calculatesCouponSave10Order() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("10.00"), 2)), "regular", "SAVE10");

        assertThat(total).isEqualByComparingTo("10.00");
    }

    @Test
    void calculatesCouponSave20PercentOrder() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("10.00"), 2)), "regular", "SAVE20PERCENT");

        assertThat(total).isEqualByComparingTo("16.00");
    }

    @Test
    void calculatesLeastZeroOrder() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("-10.00"), 2)), "regular", null);

        assertThat(total).isEqualByComparingTo("0.00");
    }

    @Test
    void calculatesBigTotalOrder() {
        BigDecimal total = calculator.calculateOrderTotal(
                List.of(new PricingCalculator.LineItem(new BigDecimal("100.00"), 20)), "regular", null);

        assertThat(total).isEqualByComparingTo("1862.00");
    }
}
