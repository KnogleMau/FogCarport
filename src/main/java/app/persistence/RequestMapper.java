package app.persistence;

import app.entities.CarportRequest;
import app.exceptions.DatabaseException;
import org.eclipse.jetty.util.thread.TryExecutor;

import java.nio.channels.SelectableChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static app.Main.connectionPool;

public class RequestMapper {

    public static void requestMapper(int carportWidth, int carportLength, int customerId) throws DatabaseException {

        int carportHeight = 210;  // As in delivered sketch because we currently dont let the customer select height:


        String sql = "insert into requests (request_width, request_length, request_height, customer_id) values (?, ?, ?, ?)";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, carportWidth);
            ps.setInt(2, carportLength);
            ps.setInt(3, carportHeight);
            ps.setInt(4, customerId);


            int rowsAffected = ps.executeUpdate();
            // ResultSet rsThree = ps.executeQuery();
            if (rowsAffected == 0) {
                throw new DatabaseException("Ingen rækker blev indsat i requests-tabellen.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved indsætning af customerId med mere i requests", e.getMessage());

        }
    }


    public static CarportRequest getCarportRequest(int userID) throws DatabaseException {
        CarportRequest carportRequest;

        String sql = "SELECT * FROM requests WHERE customer_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, userID);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int requestID = rs.getInt("request_id");
                int requestLength = rs.getInt("request_length");
                int requestWidth = rs.getInt("request_width");
                int requestHeight = rs.getInt("request_height");
                int customerID = rs.getInt("customer_id");

                carportRequest = new CarportRequest(requestID, requestLength, requestWidth, requestHeight, customerID);
            } else {
                throw new DatabaseException("Ingen forespørgsel fundet for bruger-id: " + userID);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fejl ved hentning af data fra requests", e.getMessage());
        }

        return carportRequest;
    }


}
