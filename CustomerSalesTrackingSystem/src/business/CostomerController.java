package business;

import dao.CustomerDAO;
import entity.Customer;

import java.util.ArrayList;

public class CostomerController {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public ArrayList<Customer> findAll() {
        return this.customerDAO.findAll();
    }
}
