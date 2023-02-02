package lk.ijse.finalproject.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.finalproject.model.CustomerModal;
import lk.ijse.finalproject.model.OrderModal;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;


public class AdminFromController {
    public AnchorPane pane;
    public Label lblDate;
    public AnchorPane homePane;
    public Label lblCurrentTime;
    public Label lblTotalOrders;
    public  Label lblTopic;
    public Label lblCustomerCount;
    public static TextField txtSearch;


    private volatile boolean stop = false;
    public void initialize() throws IOException, SQLException, ClassNotFoundException {
        showDate();
        showTime();
        setOrderCount();
        lblCustomerCount.setText(String.valueOf(CustomerModal.customerCount()));

    }

    public void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
        String date = s.format(d);
        lblDate.setText(date);

    }

 private void showTime() {

       Timeline timeline=new Timeline(
               new KeyFrame(Duration.ZERO, e->{
                   DateTimeFormatter formatter=DateTimeFormatter.ofPattern("HH:mm:ss");
                   lblCurrentTime.setText(LocalDateTime.now().format(formatter));
               }),new KeyFrame(Duration.seconds(1)));
       timeline.setCycleCount(Animation.INDEFINITE);
       timeline.play();

   }




    public void clickHome(MouseEvent mouseEvent) throws  IOException {
        lblTopic.setText("Home");
        Navigation.navigate(Routes.HOME,homePane);
    }

    public void clickEmployee(MouseEvent mouseEvent) throws IOException {
        lblTopic.setText("Employee");
        Navigation.navigate(Routes.EMPLOYEE,homePane);
    }

    public void clickSotre(MouseEvent mouseEvent) throws IOException {
        lblTopic.setText("Store");
        Navigation.navigate(Routes.STORE,homePane);
    }

    public void clickRawMaterial(MouseEvent mouseEvent) throws IOException {
        lblTopic.setText("Raw Materials");
        Navigation.navigate(Routes.RAWMATERIAL,homePane);
    }

    public void clickPayments(MouseEvent mouseEvent) throws IOException {
        lblTopic.setText("Payments");
        Navigation.navigate(Routes.PAYMENT,homePane);
    }

    public void clickSuppliers(MouseEvent mouseEvent) throws IOException {
        lblTopic.setText("Supplier");
        Navigation.navigate(Routes.SUPPLIER,homePane);
    }

    public void clickCustomers(MouseEvent mouseEvent) throws IOException {
        lblTopic.setText("Customer");
        Navigation.navigate(Routes.CUSTOMER,homePane);
    }

    public void clickOrder(MouseEvent mouseEvent) throws IOException {
        lblTopic.setText("Order");
        Navigation.navigate(Routes.ORDER,homePane);
    }

    public void setOrderCount() throws SQLException, ClassNotFoundException {
        int orderCount = OrderModal.orderCount();

        lblTotalOrders.setText(String.valueOf(orderCount));
    }

    public void imgOrderAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.ORDERDETAIL,homePane);
    }

    public void btnLogOutAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to logout ?", ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get()==ButtonType.YES){
            Navigation.navigate(Routes.LOGIN,pane);
        }

    }

    public void imgCustomerAcrion(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.CUSTOMER,homePane);
    }
}
