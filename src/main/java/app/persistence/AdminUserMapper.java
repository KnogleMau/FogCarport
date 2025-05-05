package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.Main.connectionPool;

public class AdminUserMapper {


    public static void login(String email, String password, boolean admin){

        String sql = "SELECT * FROM admin_users WHERE email = ? AND password = ? AND admin = true";

        try(Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1,email); // Det er her vi henter fra html siden og sætter email og password i sql Stringen
                ps.setString(2,password);

                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                }
        }
        catch(SQLException e) {
        System.out.println(e.getMessage());
        }
    }


    public static void CreateUser(String email, String password, boolean admin) throws DatabaseException {

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
