package business;

import core.Helper;
import core.Item;
import dao.ProductDao;
import entity.Product;

import java.util.ArrayList;

public class ProductController {
    private final ProductDao productDAO = new ProductDao();

    public ArrayList<Product> findAll() {
        return this.productDAO.findAll();
    }

    public boolean save(Product product) {
        return this.productDAO.save(product);
    }

    public boolean update(Product product) {
        if (this.getById(product.getId()) == null) {
            Helper.showMessage(product.getId() + " Id not found");
            return false;
        }
        return this.productDAO.update(product);
    }

    public boolean delete(int id) {
        if (this.getById(id) == null) {
            Helper.showMessage(id + " Id not found");
            return false;
        }
        return this.productDAO.delete(id);
    }

    public Product getById(int id) {

        return this.productDAO.getById(id);
    }

    public ArrayList<Product> filter(String name, String code, Item isStock) {
        String query = "SELECT * FROM product";
        ArrayList<String> whereList = new ArrayList<>();
        if (name.length() > 0) {
            whereList.add("name LIKE '%" + name + "%'");
        }
        if (code.length() > 0) {
            whereList.add("code LIKE '%" + code + "%'");
        }
        if (isStock != null) {
            if (isStock.getKey() == 1) {
                whereList.add("stock > 0");
            } else {
                whereList.add("stock <= 0");
            }
        }
        if (whereList.size() > 0) {
            String whereQuery = String.join("AND", whereList);
            query += " WHERE " + whereQuery;
        }
        return this.productDAO.query(query);
    }
}

