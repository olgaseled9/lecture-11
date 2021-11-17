package by.itacademy.javaenterprise.seledtsova.dao.impl;

import by.itacademy.javaenterprise.seledtsova.connection.PoolConnectionToDataBase;
import by.itacademy.javaenterprise.seledtsova.dao.OrderDao;
import by.itacademy.javaenterprise.seledtsova.entity.Order;
import by.itacademy.javaenterprise.seledtsova.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.postgresql.util.JdbcBlackHole.close;

public class OrderDaoImpl implements OrderDao {

    private static final Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);
    private static final String SELECT_FROM_ORDERS_TABLE = "SELECT order_id, customer_id, quantity FROM Orders ORDER BY order_id LIMIT 100 OFFSET 1;";
    private static final String DELETE_ORDER_FROM_ORDER_TABLES = "DELETE FROM Orders WHERE order_id = ?";
    private static final String ADD_NEW_ORDER = "INSERT INTO Orders (order_id, customer_id, quantity) VALUES (?,?,?)";

    @Override
    public Order saveOrder(Order order) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = PoolConnectionToDataBase.getDataSource().getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(ADD_NEW_ORDER);
            preparedStatement.setLong(1, order.getOrderId());
            preparedStatement.setLong(2, order.getCustomerId());
            preparedStatement.setInt(3, order.getQuantity());
            preparedStatement.executeUpdate();
            connection.commit();
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException e) {
                logger.error("Not able to rollback connection", e);
            }
        } catch (SQLException e) {
            logger.error("Not able to add  " + order.getClass().getName(), e);
            throw new DaoException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
        return order;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        try {
            connection = PoolConnectionToDataBase.getDataSource().getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_FROM_ORDERS_TABLE);
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getLong("order_id"));
                order.setCustomerId(resultSet.getLong("customer_id"));
                order.setQuantity(resultSet.getInt("quantity"));
                orders.add(order);
                connection.commit();
            }
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException e) {
                logger.error("Not able to rollback connection", e);
            }
        } catch (SQLException exception) {
            logger.error("Not able to add  order", exception);
            throw new DaoException(exception);
        } finally {
            close(statement);
            close(connection);
        }
        return orders;
    }

    @Override
    public void deleteOrderById(Long orderId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = PoolConnectionToDataBase.getDataSource().getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(DELETE_ORDER_FROM_ORDER_TABLES);
            preparedStatement.setLong(1, orderId);
            int affectedRows = preparedStatement.executeUpdate();
            connection.commit();
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException e) {
                logger.error("Not able to rollback connection", e);
            }
        } catch (SQLException e) {
            logger.error("Deleting order from database failed", e);
            throw new DaoException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }
}
