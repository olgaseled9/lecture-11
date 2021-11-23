package by.itacademy.javaenterprise.seledtsova.dao;

import by.itacademy.javaenterprise.seledtsova.entity.Order;

import java.util.List;

public interface OrderDao {

    Order saveOrder(Order order);

    List<Order> getAll();

    void deleteOrderById(Long orderId);
}
