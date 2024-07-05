package view;

import business.CustomerController;
import core.Helper;
import entity.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerUI extends JFrame {
    private JPanel container;
    private JLabel lbl_title;
    private JLabel lbl_customer_name;
    private JTextField fld_customer_name;
    private JLabel lbl_customer_type;
    private JComboBox<Customer.TYPE> cmb_customer_type;
    private JLabel lbl_customer_phone;
    private JTextField fld_customer_phone;
    private JLabel lbl_customer_mail;
    private JTextField fdl_customer_mail;
    private JLabel lbl_customer_address;
    private JButton btn_customer_save;
    private JTextArea txt_customer_address;
    private Customer customer;
    private CustomerController customerController;

    public CustomerUI(Customer customer) {
        this.customer = customer;
        this.customerController = new CustomerController();
        this.add(container);
        this.setTitle("Customer Add/Update");
        this.setSize(400, 500);
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2;
        this.setLocation(x, y);
        this.setVisible(true);

        this.cmb_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));

        if (this.customer.getId() == 0) {
            this.lbl_title.setText("Add Customer");
        } else {
            this.lbl_title.setText("Update Customer");
            this.fld_customer_name.setText(this.customer.getName());
            this.fdl_customer_mail.setText(this.customer.getEmail());
            this.fld_customer_phone.setText(this.customer.getPhone());
            this.txt_customer_address.setText(this.customer.getAddress());
            this.cmb_customer_type.getModel().setSelectedItem(this.customer.getType());

        }

        btn_customer_save.addActionListener(e -> {
            JTextField[] checkList = {this.fld_customer_name, this.fld_customer_phone};
            if (Helper.isFieldListEmpty(checkList)) {
                Helper.showMessage("fill");
            } else if (!Helper.isFieldEmpty(this.fdl_customer_mail) && !Helper.isEmailValid(this.fdl_customer_mail.getText())) {
                Helper.showMessage("Please enter a valid email address");
            } else {
                boolean result = false;
                this.customer.setName(this.fld_customer_name.getText());
                this.customer.setPhone(this.fld_customer_phone.getText());
                this.customer.setEmail(this.fdl_customer_mail.getText());
                this.customer.setAddress(this.txt_customer_address.getText());
                this.customer.setType((Customer.TYPE) this.cmb_customer_type.getSelectedItem());
                if (this.customer.getId() == 0) {
                    result = this.customerController.save(this.customer);
                } else {
                    result = this.customerController.update(this.customer);
                }
                if (result) {
                    Helper.showMessage("done");
                    dispose();
                } else {
                    Helper.showMessage("error");
                }
            }
        });
    }
}
