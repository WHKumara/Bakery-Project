package lk.ijse.finalproject.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigation {
    private static AnchorPane pane;

    public static void navigate(Routes route,AnchorPane pane) throws IOException {
        Navigation.pane = pane;
        Navigation.pane.getChildren().clear();
        Stage window = (Stage) Navigation.pane.getScene().getWindow();

        switch (route){
            case CUSTOMER:
                window.setTitle("Customer Manage Form");
                selectUI("CustomerForm.fxml");
                break;
            case LOGIN:
                window.setTitle("Loging Form");
                selectUI("LoginForm.fxml");
                break;
            case EMPLOYEE:
                window.setTitle("Manage Employee Form");
                selectUI("ManageEmployeeForm.fxml");
                break;
            case ADMIN:
                window.setTitle("Admin Form");
                selectUI("AdminForm.fxml");
                break;
            case HOME:
                window.setTitle("Home Form");
                selectUI("AdminHomeForm.fxml");
                break;
            case ADDEMPLOYEE:
                window.setTitle("Add Employee Form");
                selectUI("AddEmployeeForm.fxml");
                break;
            case STORE:
                window.setTitle("Store Manage Form");
                selectUI("StoreManageForm.fxml");
                break;
            case ADDITEM:
                window.setTitle("Add Item Form");
                selectUI("AddItemForm.fxml");
                break;
            case RAWMATERIAL:
                window.setTitle("Raw Material Form");
                selectUI("RawMaterialForm.fxml");
                break;
            case PAYMENT:
                window.setTitle("Payment Form");
                selectUI("PaymentsForm.fxml");
                break;
            case SUPPLIER:
                window.setTitle("Supplier Form");
                selectUI("SupplierForm.fxml");
                break;
            case ADDSUPPLIER:
                window.setTitle("Add Supplier Form");
                selectUI("AddSupplierForm.fxml");
                break;
            case ADDCUSTOMER:
                window.setTitle("Add Customer Form");
                selectUI("AddCustomerForm.fxml");
                break;
            case ADDMATERIAL:
                window.setTitle("Add Material Form");
                selectUI("AddMaterialForm.fxml");
                break;
            case ADDPAYMENT:
                window.setTitle("Add Payment Form");
                selectUI("AddPaymentForm.fxml");
                break;
            case ORDER:
                window.setTitle("Order Form");
                selectUI("OrderForm.fxml");
                break;
            case ORDERDETAIL:
                window.setTitle("Order Detail Form");
                selectUI("OrderDetailForm.fxml");
                break;
            default:
                new Alert(Alert.AlertType.ERROR, "Not suitable UI found!").show();
        }
    }

    private static void selectUI(String location) throws IOException {
        Navigation.pane.getChildren().add(FXMLLoader.load(Navigation.class
                .getResource("/lk/ijse/finalproject/view/" + location)));
    }
}
