package by.itacademy.javaenterprise.seledtsova.dao;

import by.itacademy.javaenterprise.seledtsova.entity.Customer;

import java.util.List;

public interface CustomerDao {

    Customer addCustomer(Customer customer);

    List<Customer> getAll();

    void deleteCustomerById(Integer customerId);

    Customer findCustomerByCustomerId(Integer customerId);
}

