package lk.ijse.finalproject.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;

import java.io.IOException;

import static javafx.scene.paint.Color.RED;

public class LogingFromController {
    public AnchorPane pane;
    public JFXButton btnLogin;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public Label lblNameWarning;
    public Label lblPassWarning;
    public ProgressIndicator prgCircle;

    public void btnLoginAction(ActionEvent actionEvent) throws IOException {
        if(txtUserName.getText().equals("")){
            if(txtPassword.getText().equals("")){
                Navigation.navigate(Routes.ADMIN,pane);
            }else{
                txtPassword.setFocusColor(RED);
                lblPassWarning.setText("Invalid Password");
            }
        } else  {
           txtUserName.setFocusColor(RED);
           lblNameWarning.setText("Invalid User Name");

           txtPassword.setFocusColor(RED);
           lblPassWarning.setText("Invalid Password");
        }

    }

    public void txtUserNameAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) throws IOException {
        btnLoginAction(actionEvent);
    }
}
