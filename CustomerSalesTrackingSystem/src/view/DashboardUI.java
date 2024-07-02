package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import business.CostomerController;
import core.Helper;
import entity.Customer;
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
    private JComboBox cmb_customer_type;
    private JButton btn_customer_filter;
    private JButton btn_customer_filter_reset;
    private JButton btn_customer_new;
    private JLabel lbl_f_customer_name;
    private JLabel lbl_f_customer_type;
    private User user;
    private CostomerController costomerController;
    private DefaultTableModel tmdl_customer = new DefaultTableModel();
    private JPopupMenu popup_customer = new JPopupMenu();

    public DashboardUI(User user) {
        this.user = user;
        this.costomerController = new CostomerController();

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


        loadCustomerTable(null);
        loadCustomerPopupMenu();

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
            System.out.println(selectId);
        });
        this.popup_customer.add("Delete").addActionListener(e -> {

        });


    }

    private void loadCustomerTable(ArrayList<Customer> customers) {
        Object[] columnCustomer = {"ID", "Name", "Type", "Phone", "E-posta", "Address"};
        if (customers == null) {
            customers = this.costomerController.findAll();
        }
        //tablo sıfırlama
        DefaultTableModel clearModel = (DefaultTableModel) tbl_customer.getModel();
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
}
