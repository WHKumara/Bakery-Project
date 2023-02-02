package lk.ijse.finalproject.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.finalproject.model.PaymentModal;
import lk.ijse.finalproject.model.SupplierModal;
import lk.ijse.finalproject.to.Payment;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddPaymentFormController {
    public AnchorPane pane;
    public JFXButton btnAdd;
    public JFXButton btnCancel;
    public ComboBox cmbSupplier;
    public Label lblDate;
    public Label lblTime;
    public Label lblSupId;
    public Label lblPaymentId;
    public TextField txtAmount;
    public Label lblAmountWarning;

    public void initialize(){
        loadSupplierIds();
        showDateTime();
        loadNextPaymentId();
    }

    public void btnAddAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if(isMatchAmount()){
            double amount = Double.parseDouble(txtAmount.getText());
            String supId = lblSupId.getText();

            if(!txtAmount.getText().equals("") && !lblSupId.getText().equals("suppId")){
                TextInputDialog tiDialog = new TextInputDialog();
                tiDialog.setTitle("password conformation");
                tiDialog.setHeaderText("Please enter your Password :");
                tiDialog.setContentText("Pssword: ");

                Optional<String> result = tiDialog.showAndWait();

                if(result.get().equals("admin")){
                    Payment payment = new Payment(lblPaymentId.getText(),amount,null,null,supId);
                    PaymentModal.addPayment(payment);
                    new Alert(Alert.AlertType.INFORMATION,"Payment is added !").show();
                    txtAmount.clear();
                    lblSupId.setText("suppId");
                    loadNextPaymentId();
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Password is incorrect !",ButtonType.OK);
                    alert.show();
                }
            }else{
                new Alert(Alert.AlertType.WARNING,"Invalid input, please try again !",ButtonType.OK).show();
            }
        }


    }

    public void btnCancleAction(ActionEvent actionEvent) throws IOException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether do you want to cancle this ?",
                ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()==ButtonType.YES){
            Alert alert1 = new Alert(Alert.AlertType.WARNING, "Payment is not Added !.",
                    ButtonType.OK);
            Optional<ButtonType> result1 = alert1.showAndWait();
            if (result1.get()==ButtonType.OK)
                Navigation.navigate(Routes.PAYMENT,pane);
        }
    }

    public void backAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.PAYMENT,pane);
    }

    public void cmbSelctAction(ActionEvent actionEvent) {
        String supId = (String) cmbSupplier.getValue();
        lblSupId.setText(supId);
    }
    private void loadSupplierIds() {
        try {
            ObservableList<String> observableList = FXCollections.observableArrayList();
            ArrayList<String> idList = SupplierModal.loadSupId();

            for (String id : idList) {
                observableList.add(id);
            }
            cmbSupplier.setItems(observableList);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void showDateTime(){
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
        String date = s.format(d);
        lblDate.setText(date);

        Timeline timeline=new Timeline(
                new KeyFrame(Duration.ZERO, e->{
                    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("HH:mm:ss");
                    lblTime.setText(LocalDateTime.now().format(formatter));
                }),new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    private void loadNextPaymentId() {
        try {
            String itemId = PaymentModal.generateNextPaymentId();
            lblPaymentId.setText(itemId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isMatchAmount(){
        Pattern pattern = Pattern.compile("^[0-9]+\\.[0-9]{2}$");
        Matcher matcher = pattern.matcher(txtAmount.getText());

        boolean isMatchAmount = matcher.matches();

        if (!isMatchAmount) {
            lblAmountWarning.setText("Invalid Input.");
            txtAmount.clear();
            txtAmount.requestFocus();
        } else {
            lblAmountWarning.setText("");
        }
        return isMatchAmount;
    }
}
