package business;

import core.Helper;
import dao.CustomerDAO;
import entity.Customer;

import java.util.ArrayList;

public class CustomerController {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public ArrayList<Customer> findAll() {
        return this.customerDAO.findAll();
    }

    public boolean save(Customer customer) {
        return this.customerDAO.save(customer);
    }

    public Customer getById(int id) {

        return this.customerDAO.getById(id);
    }

    public boolean update(Customer customer) {
        if (this.getById(customer.getId()) == null) {
            Helper.showMessage(customer.getId() + " Id not found");
            return false;
        }
        return this.customerDAO.update(customer);
    }

    public boolean delete(int id) {
        if (this.getById(id) == null) {
            Helper.showMessage(id + " Id not found");
            return false;
        }
        return this.customerDAO.delete(id);
    }
}
