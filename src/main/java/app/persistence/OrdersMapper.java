package app.persistence;

import app.entities.Orders;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.Main.connectionPool;

public class OrdersMapper {



    public void insertIntoOrders(int customerId, int requestId, double price, String orderStatus) throws DatabaseException {

        String sql = "INSERT INTO orders (customer_id, request_id, order_totalprice, order_status) VALUES (?, ?, ?, ?)";

            try(Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setInt(1, customerId);
                ps.setInt(2, requestId);
                ps.setDouble(3, price);
                ps.setString(4, orderStatus);

                ps.executeUpdate();

            } catch (SQLException e) {
                throw new DatabaseException(e, "Fejl ved insert af orders");
            }

    }


    public static int getOrderId(int customerId, int requestId) throws DatabaseException {

        String sql = "SELECT order_id FROM orders WHERE customer_id = ? AND request_id = ?";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, requestId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("order_id");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e, "fejl ved hent af ID");
        }

        return 0; // returner 0 så vi altid vil få det smidt i databasen, men vi kan se hvilke ordrers der er forkerte og det er her man kan lave en funktion som manuelt opdaterer order_id i order_Details så kunden har den rigtige order_id i sin bestilling.

    }

    public void updatePriceOrder(int id, int price) throws DatabaseException {
        String sql = "UPDATE orders SET order_totalprice = ? WHERE order_id = ?";

        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql) ) {
            ps.setInt(1, price);
            ps.setInt(2, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e, "Fejl ved opdatering af pris");
        }
    }
}
