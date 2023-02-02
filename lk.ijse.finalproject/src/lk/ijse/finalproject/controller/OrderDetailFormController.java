package lk.ijse.finalproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalproject.model.OrderModal;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;
import lk.ijse.finalproject.view.tm.OrderDetailTm;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailFormController {


    public AnchorPane pane;
    public TableView tblOrderDetail;

    public TableColumn coleOrderId;
    public TableColumn colDate;
    public TableColumn colTime;
    public TableColumn colAmount;
    public TableColumn colCostId;
    public Label lblIncome;
    public TextField txtSearch;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setData();

        double amount = OrderModal.getTotalAmount();
        lblIncome.setText(String.valueOf(amount ));

    }

    private void setData() throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetailTm> list = OrderModal.getData();
        ObservableList<OrderDetailTm> data = FXCollections.observableArrayList();

        data.addAll(list);

        tblOrderDetail.setItems(data);

        System.out.println(data);
    }

    public void setCellValueFactory(){
        coleOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCostId.setCellValueFactory(new PropertyValueFactory<>("CustId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    }
    public void imgBackAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.HOME,pane);
    }
    public void search() throws SQLException, ClassNotFoundException {
        ArrayList<OrderDetailTm> list = OrderModal.getData();
        ObservableList<OrderDetailTm> data = FXCollections.observableArrayList();

        data.addAll(list);

        FilteredList<OrderDetailTm> filteredList = new FilteredList(data, b -> true);

            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(orderDetailTm -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (orderDetailTm.getOrderId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (orderDetailTm.getCustId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(orderDetailTm.getDate()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<OrderDetailTm> sortedList = new SortedList(filteredList);
        sortedList.comparatorProperty().bind(tblOrderDetail.comparatorProperty());
        tblOrderDetail.setItems(sortedList);
    }

    public void txtSearchAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        search();
        //System.out.println(AdminFromController.txtSearch.getText());
    }

    public void imgSearchAction(MouseEvent event) {
        txtSearch.requestFocus();
    }
}
