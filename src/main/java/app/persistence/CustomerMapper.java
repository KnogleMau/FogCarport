package app.persistence;

import app.exceptions.DatabaseException;
import org.eclipse.jetty.util.thread.TryExecutor;

import java.sql.*;

public class CustomerMapper {

    public static void customerMapper( String firstname, String lastname, String address, String zipcode, String city, String phonenumber, String email, ConnectionPool connectionPool) throws DatabaseException {
        int customerId;

        try (Connection connection = connectionPool.getConnection()) {

            String sqlOne = "select city from zip_code where zip_code = ?";

            try (PreparedStatement psTwo = connection.prepareStatement(sqlOne)) {
                psTwo.setString(1, zipcode);

                ResultSet rs = psTwo.executeQuery();

                if (rs.next()) {
                    String correctCity = rs.getString("city");

                    if (correctCity.equals(city)) {

                        String sqlTwo = "insert into customer_information ( first_name, last_name, address, zip_code, phone_number, customer_email ) values (?,?,?,?,?,?)";

                        try (
                                PreparedStatement ps = connection.prepareStatement(sqlTwo);
                        ) {
                            ps.setString(1, firstname);
                            ps.setString(2, lastname);
                            ps.setString(3, address);
                            ps.setString(4, zipcode);
                            ps.setString(5, phonenumber);
                            ps.setString(6, email);

                            ps.executeUpdate();

                        } catch (SQLException e) {
                            throw new DatabaseException("Database fejl ved indsætning af kundens data i customer_information");
                        }
                    } }
                else{
                    throw new DatabaseException("Din indtastede by matchede ikke med postnummeret");
                }
            }
        }
        catch (SQLException e) {
            System.out.println("customerMapper SQL catch m3: ");

            throw new DatabaseException("Din by og postnummer skal udfyldes");
        }
    }

    public static int getCustomerId(String firstname, String lastname, String email, String phonenumber, ConnectionPool connectionPool) throws DatabaseException {
        int customerId = 0;

        String sql = "select customer_id from customer_information where first_name = ? and last_name = ? and customer_email = ? and phone_number = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {

            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, email);
            ps.setString(4, phonenumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                customerId = rs.getInt("customer_id");
            }
        }
        catch (SQLException e)
        {
            // throw new DatabaseException("Could not find customerID in database", e.getMessage());
            throw new DatabaseException("Fejl i databasen, prøv senere.");
        }
        return customerId;
    }
}
