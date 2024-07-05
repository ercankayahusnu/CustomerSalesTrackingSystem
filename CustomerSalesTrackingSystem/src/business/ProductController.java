package business;

import core.Helper;
import dao.ProductDAO;
import entity.Customer;
import entity.Product;

import java.util.ArrayList;

public class ProductController {
    private final ProductDAO productDAO = new ProductDAO();

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
}
