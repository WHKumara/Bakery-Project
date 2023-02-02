package lk.ijse.finalproject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalproject.model.SupplierModal;
import lk.ijse.finalproject.to.Supplier;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddSupplierFromController {
    public AnchorPane pane;
    public Label lblSuppId;
    public TextField txtSupName;
    public TextField txtSuppAddress;
    public TextField txtSuppContact;
    public TextField txtSuppEmail;
    public Label lblContactWarning;
    public Label lblMailWarning;
    public Label lblNameWarning;

    public void initialize(){
        loadNextSupplierId();
    }

    public void btnAddAction(ActionEvent actionEvent) {
        boolean isMatchContact = txtContactAction(actionEvent);
        boolean isMatchMail = txtmailOnAction(actionEvent);
        boolean isMatchName = txtNameAction(actionEvent);

        if(isMatchContact && isMatchMail & isMatchName){
            String supName = txtSupName.getText();
            int contact = Integer.parseInt(txtSuppContact.getText());
            String supAddress = txtSuppAddress.getText();
            String supEmail = txtSuppEmail.getText();

            //methanata to pakge eke object walta data danawa
            Supplier supp = new Supplier(lblSuppId.getText(),supName,contact,supAddress,supEmail);
            try {
                SupplierModal.addSupplier(supp);

                Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure whether do you want to ADD this ?",
                        ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get()==ButtonType.YES){
                    Alert alert1 = new Alert(Alert.AlertType.WARNING, "Supplier is Added !.",
                            ButtonType.OK);
                    Optional<ButtonType> result1 = alert1.showAndWait();
                    if (result1.get()==ButtonType.OK){
                        clearText();
                        loadNextSupplierId();
                    }

                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Try again").show();
        }

    }

    public void btnClearAction(ActionEvent actionEvent) throws IOException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether do you want to cancle this ?",
                ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()==ButtonType.YES){
            Alert alert1 = new Alert(Alert.AlertType.WARNING, "Supplier is not Added !.",
                    ButtonType.OK);
            Optional<ButtonType> result1 = alert1.showAndWait();
            if (result1.get()==ButtonType.OK) {
                clearText();
            }
        }
    }

    public void backAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.SUPPLIER,pane);
    }

    private void loadNextSupplierId() {
        try {
            String supId = SupplierModal.generateNextSupplierId();
            lblSuppId.setText(supId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearText(){
        txtSupName.clear();
        txtSuppAddress.clear();
        txtSuppContact.clear();
        txtSuppEmail.clear();
    }

    public boolean txtmailOnAction(ActionEvent actionEvent) {
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(txtSuppEmail.getText());

        boolean isMatch = matcher.matches();

        if (!isMatch){
            txtSuppEmail.clear();
            txtSuppEmail.requestFocus();
            lblMailWarning.setText("Invalid Input !");
        }else{
           lblMailWarning.setText("");
        }
        return  isMatch;
    }

    public boolean txtContactAction(ActionEvent actionEvent) {
        Pattern pattern = Pattern.compile("^(07)([0-9]{8,8})$");
        Matcher matcher = pattern.matcher(txtSuppContact.getText());

        boolean isMatch = matcher.matches();

        if (!isMatch){
            txtSuppContact.clear();
            txtSuppContact.requestFocus();
            lblContactWarning.setText("Invalid Input !");
        }else{
            lblContactWarning.setText("");
        }
        return isMatch;
    }

    public boolean txtNameAction(ActionEvent actionEvent) {
        Pattern pattern = Pattern.compile("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+");
        Matcher matcher = pattern.matcher(txtSupName.getText());

        boolean isMatch = matcher.matches();

        if (!isMatch){
            txtSupName.clear();
            txtSupName.requestFocus();
            lblNameWarning.setText("Invalid Input !");
        }else{
            lblNameWarning.setText("");
        }
        return isMatch;
    }

}
