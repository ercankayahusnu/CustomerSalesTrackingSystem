package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import business.BasketController;
import business.CustomerController;
import business.ProductController;
import core.Helper;
import core.Item;
import entity.Basket;
import entity.Customer;
import entity.Product;
import entity.User;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DashboardUI extends JFrame {
    private JPanel container;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTabbedPane tabbedPane1;
    private JPanel pnl_customer;
    private JScrollPane scrl_customer;
    private JTable tbl_customer;
    private JPanel pnl_customer_filter;
    private JTextField fld_f_customer_name;
    private JComboBox<Customer.TYPE> cmb_filter_customer_type;
    private JButton btn_customer_filter;
    private JButton btn_customer_filter_reset;
    private JButton btn_customer_new;
    private JLabel lbl_f_customer_name;
    private JLabel lbl_f_customer_type;
    private JPanel pnl_product;
    private JScrollPane scrl_product;
    private JTable tbl_product;
    private JPanel pnl_product_filter;
    private JTextField fld_product_filter_code;
    private JComboBox<Item> cmb_product_filter_stock;
    private JButton btn_produck_filter_search;
    private JButton btn_product_filter_reset;
    private JButton btn_produck_filter_add;
    private JLabel lbl_product_filter_name;
    private JTextField fld_filter_product_name;
    private JLabel lbl_product_filter_code;
    private JLabel lbl_product_filter_stock;
    private JPanel pnl_basket;
    private JPanel pnl_basket_top;
    private JScrollPane scr_basket;
    private JComboBox<Item> cmb_basket_customerSelect;
    private JButton btn_basket_clean;
    private JButton createOrderButton;
    private JLabel fld_basket_selectCustomer;
    private JLabel lbl_basket_totalPrice_text;
    private JLabel lbl_basket_totalPrice;
    private JLabel lbl_basket_numberProduct_tex;
    private JLabel lbl_basket_numberProducts;
    private JTable tbl_basket_table;
    private User user;
    private CustomerController customerController;
    private ProductController productController;
    private BasketController basketController;
    private DefaultTableModel tmdl_customer = new DefaultTableModel();
    private DefaultTableModel tmdl_product = new DefaultTableModel();
    private DefaultTableModel tmdl_basket = new DefaultTableModel();
    private JPopupMenu popup_customer = new JPopupMenu();
    private JPopupMenu popup_product = new JPopupMenu();


    public DashboardUI(User user) {
        this.user = user;
        this.customerController = new CustomerController();
        this.productController = new ProductController();
        this.basketController = new BasketController();

        if (user == null) {
            Helper.showMessage("error");
            dispose();
        }

        this.add(container);
        this.setTitle("Customer Management System");
        this.setSize(1000, 500);
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x, y);
        this.setVisible(true);
        System.out.println(this.user.toString());
        this.lbl_welcome.setText("Welcome " + this.user.getName());

        this.btn_logout.addActionListener(e -> {

            dispose();
            LoginUI loginUI = new LoginUI();
        });

        //CUSTOMER TAB
        loadCustomerTable(null);
        loadCustomerPopupMenu();
        loadCustomerButtonEvent();
        this.cmb_filter_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));
        this.cmb_filter_customer_type.setSelectedItem(null);
        //PRODUCT TAB
        loadProductTable(null);
        loadProductPopupMenu();
        loadProductButtonEvent();
        this.cmb_product_filter_stock.addItem(new Item(1, "in stock"));
        this.cmb_product_filter_stock.addItem(new Item(2, "Out of stock"));
        this.cmb_product_filter_stock.setSelectedItem(null);
        //BASKET TAB
        loadBasketTable();
        loadBasketButtonEvent();
        loadBasketCustomerCombo();
    }

    //CUSTOMER
    private void loadCustomerButtonEvent() {
        btn_customer_new.addActionListener(e -> {
            CustomerUI customerUI = new CustomerUI(new Customer());
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }
            });
        });
        this.btn_customer_filter.addActionListener(e -> {
            ArrayList<Customer> filteredCustomers = this.customerController.fiter(
                    this.fld_f_customer_name.getText(),
                    (Customer.TYPE) this.cmb_filter_customer_type.getSelectedItem()
            );
            loadCustomerTable(filteredCustomers);
        });
        btn_customer_filter_reset.addActionListener(e -> {

            loadCustomerTable(null);
            this.fld_f_customer_name.setText(null);
            this.cmb_filter_customer_type.setSelectedItem(null);
        });
    }

    private void loadCustomerPopupMenu() {
        this.tbl_customer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    int selectedRow = tbl_customer.rowAtPoint(e.getPoint());
                    if (selectedRow >= 0 && selectedRow < tbl_customer.getRowCount()) {
                        tbl_customer.setRowSelectionInterval(selectedRow, selectedRow);
                        popup_customer.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
        this.popup_customer.add("Update").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
            CustomerUI customerUI = new CustomerUI(this.customerController.getById(selectId));
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }
            });
        });
        this.popup_customer.add("Delete").addActionListener(e -> {
            int selectId = Integer.parseInt(tbl_customer.getValueAt(tbl_customer.getSelectedRow(), 0).toString());
            if (Helper.confirm("sure")) {
                if (this.customerController.delete(selectId)) {
                    Helper.showMessage("done");
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                } else {
                    Helper.showMessage("error");
                }
            }

        });
    }

    private void loadCustomerTable(ArrayList<Customer> customers) {
        Object[] columnCustomer = {"ID", "Name", "Type", "Phone", "E-posta", "Address"};
        if (customers == null) {
            customers = this.customerController.findAll();
        }

        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_customer.getModel();
        clearModel.setRowCount(0);

        this.tmdl_customer.setColumnIdentifiers(columnCustomer);
        for (Customer customer : customers) {
            Object[] rowObject = {
                    customer.getId(),
                    customer.getName(),
                    customer.getType(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getAddress()
            };
            this.tmdl_customer.addRow(rowObject);
        }
        this.tbl_customer.setModel(this.tmdl_customer);
        this.tbl_customer.getTableHeader().setReorderingAllowed(false);
        this.tbl_customer.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_customer.setEnabled(false);
    }

    //PRODUCT

    private void loadProductButtonEvent() {
        this.btn_produck_filter_add.addActionListener(e -> {
            ProductUI productUI = new ProductUI(new Product());
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                }
            });
        });
        this.btn_produck_filter_search.addActionListener(e -> {
            ArrayList<Product> filteredProducts = this.productController.filter(
                    this.fld_filter_product_name.getText(),
                    this.fld_product_filter_code.getText(),
                    (Item) this.cmb_product_filter_stock.getSelectedItem()
            );
            loadProductTable(filteredProducts);

        });
        btn_product_filter_reset.addActionListener(e -> {
            this.fld_filter_product_name.setText(null);
            this.fld_product_filter_code.setText(null);
            this.cmb_product_filter_stock.setSelectedItem(null);
            loadProductTable(null);
        });
    }

    private void loadProductPopupMenu() {
        this.tbl_product.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    int selectedRow = tbl_product.rowAtPoint(e.getPoint());
                    if (selectedRow >= 0 && selectedRow < tbl_product.getRowCount()) {
                        tbl_product.setRowSelectionInterval(selectedRow, selectedRow);
                        popup_product.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        this.popup_product.add("Update").addActionListener(e -> {
            int selectId = Integer.parseInt(this.tbl_product.getValueAt(this.tbl_product.getSelectedRow(), 0).toString());
            ProductUI productUI = new ProductUI(this.productController.getById(selectId));
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                    loadBasketTable();
                }
            });
        });
        this.popup_product.add("Delete").addActionListener(e -> {
            int selectId = Integer.parseInt(this.tbl_product.getValueAt(this.tbl_product.getSelectedRow(), 0).toString());
            if (Helper.confirm("sure")) {
                if (this.productController.delete(selectId)) {
                    Helper.showMessage("done");
                    loadProductTable(null);
                    loadBasketTable();
                } else {
                    Helper.showMessage("error");
                }
            }
        });
        this.tbl_product.setComponentPopupMenu(this.popup_product);

        this.popup_product.add("Add Basket").addActionListener(e -> {
            int selectId = Integer.parseInt(this.tbl_product.getValueAt(this.tbl_product.getSelectedRow(), 0).toString());
            Product basketProduct = this.productController.getById(selectId);
            if (basketProduct.getStock() <= 0) {
                Helper.showMessage("This product is out of stock");
            } else {
                Basket basket = new Basket(basketProduct.getId());
                if (this.basketController.save(basket)) {
                    Helper.showMessage("done");
                    loadBasketTable();
                } else {
                    Helper.showMessage("error");
                }
            }
        });
    }

    private void loadProductTable(ArrayList<Product> products) {
        Object[] columnProducts = {"ID", "Product Name", "Product Code", "Price", "Stock"};
        if (products == null) {
            products = this.productController.findAll();
        }

        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_product.getModel();
        clearModel.setRowCount(0);

        this.tmdl_product.setColumnIdentifiers(columnProducts);
        for (Product product : products) {
            Object[] rowObject = {
                    product.getId(),
                    product.getName(),
                    product.getCode(),
                    product.getPrice(),
                    product.getStock()
            };
            this.tmdl_product.addRow(rowObject);
        }
        this.tbl_product.setModel(this.tmdl_product);
        this.tbl_product.getTableHeader().setReorderingAllowed(false);
        this.tbl_product.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_product.setEnabled(false);
    }

    //BASKET
    private void loadBasketButtonEvent() {
        this.btn_basket_clean.addActionListener(e -> {
            if (this.basketController.clear()) {
                Helper.showMessage("done");
                loadBasketTable();
            } else {
                Helper.showMessage("error");
            }
        });

    }

    private void loadBasketTable() {
        Object[] columnBasket = {"ID", "Product Name", "Product Code", "Price", "Stock"};
        ArrayList<Basket> baskets = this.basketController.findAll();
        DefaultTableModel clearModel = (DefaultTableModel) this.tbl_basket_table.getModel();
        clearModel.setRowCount(0);

        this.tmdl_basket.setColumnIdentifiers(columnBasket);
        int totalPrice = 0;
        for (Basket basket : baskets) {
            Object[] rowObject = {
                    basket.getId(),
                    basket.getProduct().getName(),
                    basket.getProduct().getCode(),
                    basket.getProduct().getPrice(),
                    basket.getProduct().getStock()
            };
            this.tmdl_basket.addRow(rowObject);
            totalPrice += basket.getProduct().getPrice();
        }
        this.lbl_basket_totalPrice.setText(totalPrice + " TL");
        this.lbl_basket_numberProducts.setText(baskets.size() + " Adet");

        this.tbl_basket_table.setModel(this.tmdl_basket);
        this.tbl_basket_table.getTableHeader().setReorderingAllowed(false);
        this.tbl_basket_table.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tbl_basket_table.setEnabled(false);
    }

    private void loadBasketCustomerCombo() {
        ArrayList<Customer> customers = this.customerController.findAll();
        this.cmb_basket_customerSelect.removeAllItems();
        for (Customer customer : customers) {
            int comboKey = customer.getId();
            String comboValue = customer.getName();
            this.cmb_basket_customerSelect.addItem(new Item(comboKey, comboValue));
        }
        this.cmb_basket_customerSelect.setSelectedItem(null);
    }
}
