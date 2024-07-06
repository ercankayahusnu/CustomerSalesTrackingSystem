package dao;

import core.Database;
import entity.Basket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BasketDao {
    private Connection connection;
    private ProductDao productDAO;

    public BasketDao() {
        this.connection = Database.getInstance();
        this.productDAO = new ProductDao();
    }

    public boolean save(Basket basket) {
        String query = "INSERT INTO basket" +
                "(" +
                "product_id" +
                ")" +
                "VALUES (?)";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, basket.getProductId());
            return pr.executeUpdate() != -1;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return true;
    }

    public Basket match(ResultSet rs) throws SQLException {
        Basket basket = new Basket();
        basket.setId(rs.getInt("id"));
        basket.setProductId(rs.getInt("product_id"));
        basket.setProduct(this.productDAO.getById(rs.getInt("product_id")));
        return basket;
    }

    public boolean clear() {
        String query = "DELETE FROM basket";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            return pr.executeUpdate() != -1;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return true;
    }

    public ArrayList<Basket> findAll() {
        ArrayList<Basket> baskets = new ArrayList<>();
        try {
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT  * FROM  basket");
            while (rs.next()) {
                baskets.add(this.match(rs));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return baskets;
    }

}
