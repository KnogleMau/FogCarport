package app.persistence;

import app.entities.AdminUser;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.Main.connectionPool;

public class AdminUserMapper {


    public static AdminUser login(String email, String password, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "SELECT * FROM admin_users WHERE email = ? AND password = ? AND admin = true";

        try(Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1,email); // Det er her vi henter fra html siden og sætter email og password i sql Stringen
                ps.setString(2,password);

                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    return new AdminUser(email);
                } else {
                    throw new DatabaseException("Forkert Login, ellers har du ikke Admin permission");
                }
        } catch(SQLException e) {
        System.out.println(e.getMessage());
        throw new DatabaseException("Databasefejl", e.getMessage());
        }
    }


    public static void createUser(String email, String password, boolean admin, ConnectionPool connectionPool) throws DatabaseException {

        String sql = "INSERT INTO admin_users (email, password, admin) values (?,?,?)";

        try(Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,email);
            ps.setString(2, password);
            ps.setBoolean(3,admin);

            ps.executeUpdate();


        } catch(SQLException e){
            System.out.println(e.getMessage());
        }


    }
}
