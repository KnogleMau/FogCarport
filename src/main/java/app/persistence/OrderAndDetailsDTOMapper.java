package app.persistence;

import app.dto.OrderCustomerDTO;
import app.dto.OrderDetailsMaterialLengthDTO;
import app.entities.OrderDetail;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderAndDetailsDTOMapper {

    public static ArrayList<OrderCustomerDTO> getAllOrderCustomerDTOsMapper() throws DatabaseException {

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
                    double totalPrice = rs.getDouble("order_totalprice");
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

    public static ArrayList<OrderDetailsMaterialLengthDTO> getOrderDetailsMapper(int orderId) throws DatabaseException {
        ArrayList<OrderDetailsMaterialLengthDTO> orderDetails = new ArrayList<>();
        System.out.println("5");
        String sqlOrderDetails = "SELECT order_details.quantity, material_name, material_unit, material_description, material_length " +
                "FROM orders INNER JOIN order_details ON orders.order_id = order_details.order_id INNER JOIN material_list ON " +
                "order_details.material_id = material_list.material_id INNER JOIN material_lengths ON order_details.length_id = " +
                "material_lengths.length_id WHERE orders.order_id = ?";
        System.out.println("4");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        System.out.println("3");
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sqlOrderDetails)) {

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String materialName = rs.getString("material_name");
                int materialLength = rs.getInt("material_length");
                int quantity = rs.getInt("quantity");
                String materialUnit = rs.getString("material_unit");
                String materialDescription = rs.getString("material_description");

                orderDetails.add(new OrderDetailsMaterialLengthDTO(materialName, materialLength, quantity, materialUnit, materialDescription));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new DatabaseException("Der er ikke nogen forbindelse til databasen lige nu. Prøv igen senere.");

        }
        return orderDetails;
    }
}
