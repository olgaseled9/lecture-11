package by.itacademy.javaenterprise.seledtsova.dao.Impl;

import by.itacademy.javaenterprise.seledtsova.connection.PoolConnectionToDataBase;
import by.itacademy.javaenterprise.seledtsova.dao.CustomerDao;
import by.itacademy.javaenterprise.seledtsova.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.postgresql.util.JdbcBlackHole.close;

public class CustomerDaoImpl implements CustomerDao {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);

    public static final String SELECT_FROM_CUSTOMER_TABLE = "SELECT customer_id, first_name, last_name FROM Customers ORDER BY last_name LIMIT 100 OFFSET 3";
    public static final String DELETE_CUSTOMER_FROM_CUSTOMER_TABLES = "DELETE FROM Customers WHERE customer_id = ?";
    public static final String SELECT_FROM_CUSTOMER_TABLE_CUSTOMER_ID = "SELECT customer_id, first_name, last_name FROM Customers WHERE customer_id=?;";
    private static final String ADD_NEW_CUSTOMER = "INSERT INTO Customers (customer_id, first_name, last_name) VALUES (?,?,?)";

    @Override
    public Customer addCustomer(Customer customer) {
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = PoolConnectionToDataBase.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(ADD_NEW_CUSTOMER);
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, customer.getCustomerId());
            preparedStatement.setString(2, customer.getFirstName());
            preparedStatement.setString(3, customer.getLastName());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Not able to add  " + customer.getClass().getName(), e);
        } finally {
            close(preparedStatement);
        }
        return customer;
    }


    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        Statement statement = null;
        try {
            Connection connection = PoolConnectionToDataBase.getDataSource().getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_FROM_CUSTOMER_TABLE);
            connection.setAutoCommit(false);
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(resultSet.getInt("customer_id"));
                customer.setFirstName(resultSet.getString("first_name"));
                customer.setLastName(resultSet.getString("last_name"));
                customers.add(customer);
                connection.commit();
            }
        } catch (SQLException exception) {
            logger.error("Not able to add  customer", exception);
            throw new RuntimeException("Connection is not available", exception);
        } finally {
            close(statement);
        }
        return customers;
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = PoolConnectionToDataBase.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(DELETE_CUSTOMER_FROM_CUSTOMER_TABLES);
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, customerId);
            int affectedRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Deleting customer from database failed", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Customer findCustomerByCustomerId(Integer customerId) {
        PreparedStatement preparedStatement = null;
        Customer customer = new Customer();
        try {
            Connection connection = PoolConnectionToDataBase.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(SELECT_FROM_CUSTOMER_TABLE_CUSTOMER_ID);
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            customer.setCustomerId(resultSet.getInt("customer_id"));
            customer.setFirstName(resultSet.getString("first_name"));
            customer.setLastName(resultSet.getString("last_name"));
            connection.commit();
        } catch (SQLException e) {
            logger.error("SQL exception (request or table failed): " + e);
        } finally {
            close(preparedStatement);
        }
        return customer;
    }
}
