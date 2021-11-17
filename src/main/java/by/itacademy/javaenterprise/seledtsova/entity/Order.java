package by.itacademy.javaenterprise.seledtsova.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long orderId;
    private Long customerId;
    private Integer quantity;
    private List<Customer> customers;

    public Order(Long orderId, Long customerId, Integer quantity) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.quantity = quantity;
    }
}

