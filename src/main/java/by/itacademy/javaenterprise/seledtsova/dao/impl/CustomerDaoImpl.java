package by.itacademy.javaenterprise.seledtsova.dao.impl;

import by.itacademy.javaenterprise.seledtsova.connection.PoolConnectionToDataBase;
import by.itacademy.javaenterprise.seledtsova.dao.CustomerDao;
import by.itacademy.javaenterprise.seledtsova.entity.Customer;
import by.itacademy.javaenterprise.seledtsova.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.postgresql.util.JdbcBlackHole.close;

public class CustomerDaoImpl implements CustomerDao {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);
    private static final String SELECT_FROM_CUSTOMER_TABLE = "SELECT customer_id, first_name, last_name FROM Customers ORDER BY last_name LIMIT 100 OFFSET 3";
    private static final String DELETE_CUSTOMER_FROM_CUSTOMER_TABLES = "DELETE FROM Customers WHERE customer_id = ?";
    private static final String SELECT_FROM_CUSTOMER_TABLE_CUSTOMER_ID = "SELECT customer_id, first_name, last_name FROM Customers WHERE customer_id=?;";
    private static final String ADD_NEW_CUSTOMER = "INSERT INTO Customers (customer_id, first_name, last_name) VALUES (?,?,?)";

    @Override
    public Customer saveCustomer(Customer customer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = PoolConnectionToDataBase.getDataSource().getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(ADD_NEW_CUSTOMER);
            preparedStatement.setLong(1, customer.getCustomerId());
            preparedStatement.setString(2, customer.getFirstName());
            preparedStatement.setString(3, customer.getLastName());
            preparedStatement.executeUpdate();
            connection.commit();
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException e) {
                logger.error("Not able to rollback connection", e);
            }
        } catch (SQLException e) {
            logger.error("Not able to add  " + customer.getClass().getName(), e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return customer;
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = PoolConnectionToDataBase.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_FROM_CUSTOMER_TABLE);
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(resultSet.getLong("customer_id"));
                customer.setFirstName(resultSet.getString("first_name"));
                customer.setLastName(resultSet.getString("last_name"));
                customers.add(customer);
                connection.commit();
                try {
                    if (connection != null)
                        connection.rollback();
                } catch (SQLException e) {
                    logger.error("Not able to rollback connection", e);
                }
            }
        } catch (SQLException exception) {
            logger.error("Not able to add  customer", exception);
            throw new DaoException(exception);
        } finally {
            close(statement);
            close(connection);
        }
        return customers;
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = PoolConnectionToDataBase.getDataSource().getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(DELETE_CUSTOMER_FROM_CUSTOMER_TABLES);
            preparedStatement.setLong(1, customerId);
            int affectedRows = preparedStatement.executeUpdate();
            connection.commit();
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException e) {
                logger.error("Not able to rollback connection", e);
            }
        } catch (SQLException e) {
            logger.error("Deleting customer from database failed", e);
            throw new DaoException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    @Override
    public Customer findCustomerByCustomerId(Long customerId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Customer customer = new Customer();
        try {
            connection = PoolConnectionToDataBase.getDataSource().getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(SELECT_FROM_CUSTOMER_TABLE_CUSTOMER_ID);
            preparedStatement.setLong(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            customer.setCustomerId(resultSet.getLong("customer_id"));
            customer.setFirstName(resultSet.getString("first_name"));
            customer.setLastName(resultSet.getString("last_name"));
            connection.commit();
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException e) {
                logger.error("Not able to rollback connection", e);
            }
        } catch (SQLException e) {
            logger.error("SQL exception (request or table failed): " + e);
            throw new DaoException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return customer;
    }
}
