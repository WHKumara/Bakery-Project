package lk.ijse.finalproject.controller;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalproject.model.CustomerModal;
import lk.ijse.finalproject.model.OrderModal;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;

import java.io.IOException;
import java.sql.SQLException;

public class AdminHomeFormController {
    public AnchorPane pane1;
    public Label lblOrders;
    public Label lblCustomerCount;

    private volatile boolean stop = false;

    public void initialize() throws SQLException, ClassNotFoundException {
        setOrderCount();
        lblCustomerCount.setText(String.valueOf(CustomerModal.customerCount()));
    }

    public void setOrderCount() throws SQLException, ClassNotFoundException {
        int orderCount = OrderModal.orderCount();

        lblOrders.setText(String.valueOf(orderCount));
    }

    public void imgOrderAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.ORDERDETAIL,pane1);
    }

    public void imgCustomerAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.CUSTOMER,pane1);
    }
}
