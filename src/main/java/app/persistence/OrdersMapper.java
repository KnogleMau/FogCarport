package app.persistence;

import app.entities.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static app.Main.connectionPool;

public class OrdersMapper {



    public void insertIntoOrders(int customerId, int requestId, double price, String orderStatus) {

        String sql = "INSERT INTO orders (customer_id, request_id, order_totalprice, order_status) VALUES (?, ?, ?, ?)";

            try(Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, customerId);
                ps.setInt(2, requestId);
                ps.setDouble(3, price);
                ps.setString(4, orderStatus);

                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());;
            }

    }
}
