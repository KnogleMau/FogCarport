package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestMapper {

    public static void requestMapper(int carportWidth, int carportLength, int customerId ,ConnectionPool connectionPool) throws DatabaseException {


        String sql = "insert into orders (carport_width, carport_length, customer_id) values (?,?,?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, carportWidth);
            ps.setInt(2, carportLength);
            ps.setInt(3, customerId);

            ResultSet rsThree = ps.executeQuery();
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indsætning af customerId med mere i requests", e.getMessage());

        }
    }

}
