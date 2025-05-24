package app.persistence;

import app.entities.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static app.Main.connectionPool;

public class OrderDetailsMapper {

    public void insertOrderDetails(List<OrderDetail> details, ConnectionPool connectionPool) {

        String sql = "INSERT INTO order_details (order_id, material_id, quantity, length_id, detail_price) values (?,?,?,?,?)";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            for (OrderDetail detail : details) {;
                ps.setInt(1, detail.getOrderId());
                ps.setInt(2, detail.getMaterialId());
                ps.setInt(3, detail.getQuantity());
                ps.setInt(4, detail.getLengthId());
                ps.setDouble(5, detail.getPrice());

                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
