package app.persistence;

import app.entities.Material;
import app.entities.MaterialVariant;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class MaterialMapper {


    public static Material selectProduct(int id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "select * from material_list WHERE material_id = ?";

        try(Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int materialId = rs.getInt("material_id");
                String name = rs.getString("material_name");
                String variant = rs.getString("material_type");
                int price = rs.getInt("material_price");

                return new Material(materialId, name, variant, price);
            } else{
                throw new DatabaseException("Product with ID" + id + " not found");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<MaterialVariant> selectMaterialVariant(int productId, int minLength, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM material_lengths WHERE material_id = ? AND material_length = ?";

        List<MaterialVariant> variants = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setInt(2, minLength);

            ResultSet rs = ps.executeQuery();

            Material material = selectProduct(productId, connectionPool); // kun én gang

            while (rs.next()) {
                int lengthId = rs.getInt("length_id");
                int length = rs.getInt("material_length");

                variants.add(new MaterialVariant(lengthId, length, material));
            }

            for(MaterialVariant variant : variants){
                System.out.println(variant);
            }
            return variants;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<MaterialVariant> selectAllMaterialVariant(int productId, int minLength, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM material_lengths WHERE material_id = ? AND material_length >= ?";

        List<MaterialVariant> variants = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setInt(2, minLength);

            ResultSet rs = ps.executeQuery();

            Material material = selectProduct(productId, connectionPool); // kun én gang

            while (rs.next()) {
                int lengthId = rs.getInt("length_id");
                int length = rs.getInt("material_length");

                variants.add(new MaterialVariant(lengthId, length, material));
            }

            for(MaterialVariant variant : variants){
                System.out.println(variant);
            }
            return variants;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
