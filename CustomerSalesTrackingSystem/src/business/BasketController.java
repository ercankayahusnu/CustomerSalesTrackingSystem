package business;

import dao.BasketDao;
import entity.Basket;

import java.util.ArrayList;

public class BasketController {
    private final BasketDao basketDAO = new BasketDao();

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
