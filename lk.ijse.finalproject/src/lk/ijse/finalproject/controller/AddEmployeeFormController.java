package lk.ijse.finalproject.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalproject.model.EmployeeModal;
import lk.ijse.finalproject.to.Employee;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEmployeeFormController {
    public AnchorPane pane;
    public TextField txtEmpEmail;
    public TextField txtEmpContact;
    public Label lblEmpId;
    public TextField txtEmpName;
    public TextArea txtEmpAddress2;
    public Label lblNameWarning;
    public Label lblMailWarning;
    public Label lblContactWarning;

    public void initialize(){
        loadNextEmployeeId();
    }

    public void btnClearAction(ActionEvent actionEvent) throws IOException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether do you want to cancle this ?",
                ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()==ButtonType.YES){
            Alert alert1 = new Alert(Alert.AlertType.WARNING, "Employee is not Added !.",
                    ButtonType.OK);
            Optional<ButtonType> result1 = alert1.showAndWait();
            if (result1.get()==ButtonType.OK)
            Navigation.navigate(Routes.EMPLOYEE,pane);
        }
    }

    public void btnAddAction(ActionEvent actionEvent) {
        if(isMatchContact() & isMatchMail() & isMatchName()){
            if(!txtEmpName.getText().equals("")){
                String empName = txtEmpName.getText();
                int empContact = Integer.parseInt(txtEmpContact.getText());
                String empAddress = txtEmpAddress2.getText();
                String empMail = txtEmpEmail.getText();

                //methanata to pakge eke object walta data danawa
                Employee emp = new Employee(lblEmpId.getText(),empName,empContact,empAddress,empMail);

                try {
                    //methnin model eke quary thiyen thanta object ek pass kranwa
                    EmployeeModal.addEmployee(emp);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"Employee is added!",ButtonType.OK);
                    Optional<ButtonType> result = alert.showAndWait();

                    if(result.get() == ButtonType.OK){
                        txtEmpName.clear();
                        txtEmpAddress2.clear();
                        txtEmpEmail.clear();
                        txtEmpContact.clear();
                        loadNextEmployeeId();
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }else{
                new Alert(Alert.AlertType.WARNING,"Invalid Input");
            }
        }

    }

    public void backAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.EMPLOYEE,pane);
    }

    private void loadNextEmployeeId() {
        try {
            String orderId = EmployeeModal.generateNextEmployeeId();
            lblEmpId.setText(orderId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isMatchMail() {
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(txtEmpEmail.getText());

        boolean isMatchMail = matcher.matches();

        if (!isMatchMail){
            txtEmpEmail.clear();
            txtEmpEmail.requestFocus();
            lblMailWarning.setText("Invalid Input.");
        }else{
            lblMailWarning.setText("");
        }
        return  isMatchMail;
    }

    public boolean isMatchContact() {
        Pattern pattern = Pattern.compile("^(07)([0-9]{8,8})$");
        Matcher matcher = pattern.matcher(txtEmpContact.getText());

        boolean isMatchContact = matcher.matches();

        if (!isMatchContact){
            txtEmpContact.clear();
            txtEmpContact.requestFocus();
            lblContactWarning.setText("Invalid Input.");
        }else{
            lblContactWarning.setText("");
        }
        return isMatchContact;
    }

    public boolean isMatchName() {
        Pattern pattern = Pattern.compile("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+");
        Matcher matcher = pattern.matcher(txtEmpName.getText());

        boolean isMatchName = matcher.matches();

        if (!isMatchName){
            txtEmpName.clear();
            txtEmpName.requestFocus();
            lblNameWarning.setText("Invalid Input !");
        }else{
            lblNameWarning.setText("");
        }
        return isMatchName;
    }
}
