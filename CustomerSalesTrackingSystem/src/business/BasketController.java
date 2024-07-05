package business;

import dao.BasketDAO;
import entity.Basket;

import java.util.ArrayList;

public class BasketController {
    private final BasketDAO basketDAO = new BasketDAO();

    public boolean save(Basket basket) {
        return this.basketDAO.save(basket);
    }

    public ArrayList<Basket> findAll() {
        return this.basketDAO.findAll();
    }

    public boolean clear() {
        return this.basketDAO.clear();
    }
}
