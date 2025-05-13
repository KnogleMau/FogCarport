package app.persistence;

import app.entities.Product;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.Main.connectionPool;


public class ProductMapper {


    public Product selectProduct(int id) throws DatabaseException {
        String sql = "select * from material_list WHERE material_id = ?";

        try(Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int productId = rs.getInt("material_id");
                String name = rs.getString("material_name");
                String variant = rs.getString("material_type");
                int price = rs.getInt("material_price");
                return new Product(productId, name, variant, price);
            } else{
                throw new DatabaseException("Product with ID" + id + " not found");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
