package app.persistence;

import app.dto.OrderCustomerDTO;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class DTOMapper {

    public static ArrayList<OrderCustomerDTO> getAllOrderCustomerDTOs() throws DatabaseException {

      ArrayList<OrderCustomerDTO> customerDTOs = new ArrayList<>();

        String sql = "select orders.order_id, order_totalprice, order_status, first_name, last_name, phone_number, customer_email\n" +
                "FROM orders INNER JOIN customer_information ON orders.customer_id = customer_information.customer_id";

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int orderID = rs.getInt("order_id");
                    float totalPrice = rs.getFloat("order_totalprice");
                    String orderStatus = rs.getString("order_status");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String phoneNumber = rs.getString("phone_number");
                    String email = rs.getString("customer_email");

                    customerDTOs.add(new OrderCustomerDTO(orderID, totalPrice, orderStatus, firstName, lastName, phoneNumber, email));
                }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Der er ikke nogen forbindelse til databasen lige nu. Prøv igen senere.");
        }
        return customerDTOs;
    }
}
