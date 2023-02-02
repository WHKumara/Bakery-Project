package lk.ijse.finalproject.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lk.ijse.finalproject.db.DBConnection;
import lk.ijse.finalproject.model.PaymentModal;
import lk.ijse.finalproject.model.SupplierModal;
import lk.ijse.finalproject.to.Payment;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentsFormController {
    public AnchorPane pane;
    public Label lblPaymentId;
    public Label lblAmount;
    public Label lblSuppId;
    public Label lblDate;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public Pane updatePane;
    public ImageView icnUpdate;
    public TextField txtAmount;
    public TextField txtSuppId;
    public TextField txtDate;
    public ProgressIndicator prgCircle;
    public TableColumn colPayId;
    public TableColumn colAmount;
    public TableColumn colDate;
    public TableColumn colTime;
    public TableColumn colSupId;
    public ComboBox cmbSuppId;
    public Label lblSupId;
    public TableView tblPayments;
    public Label lblAmountWarning;
    public TextField txtSearch;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setPaymentTable();
        loadSupplierIds();
        selectedItem();
    }

    public void btnUpdateAction(ActionEvent actionEvent) {
        if(isMatch()){
            double amount = Double.parseDouble(txtAmount.getText());
            String supId = lblSupId.getText();

            Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure whether do you want to UPDATE ?",
                    ButtonType.YES, ButtonType.NO);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get()==ButtonType.YES){
                Payment payment = new Payment(lblPaymentId.getText(),amount,null,null,supId);
                try {
                    PaymentModal.updateData(payment);
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION,"Details are Updated !",ButtonType.OK);
                    Optional<ButtonType> result2 = alert2.showAndWait();
                    if(result2.get()==ButtonType.OK){
                        updatePane.setVisible(false);
                        icnUpdate.setVisible(true);
                        prgCircle.setVisible(false);
                        txtAmount.clear();
                        lblSupId.setText("");
                        setPaymentTable();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Try again !").show();
        }

        }


    public void updateClickAction(MouseEvent event) {
            if(!lblPaymentId.getText().equals("")){
                TextInputDialog tiDialog = new TextInputDialog();
                tiDialog.setTitle("password conformation");
                tiDialog.setHeaderText("Please enter your Password :");
                tiDialog.setContentText("Pssword: ");

                Optional<String> result = tiDialog.showAndWait();

                if(result.get().equals("admin")){
                    updatePane.setVisible(true);
                    icnUpdate.setVisible(false);
                    prgCircle.setVisible(true);
                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Password is incorrect !",ButtonType.OK);
                    alert.show();
                }
            }else {
                new Alert(Alert.AlertType.WARNING,"Please select a row in table !").show();
            }
    }
    public void addClickAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.ADDPAYMENT,pane);
    }

    public void setCellValueFactory(){
        colPayId.setCellValueFactory(new PropertyValueFactory<>("payId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
    }

    public void setPaymentTable() throws SQLException, ClassNotFoundException {
        ObservableList<Payment> data = FXCollections.observableArrayList();
        ArrayList<Payment> paymentlList = PaymentModal.getDetail();
        data.addAll(paymentlList);
        tblPayments.setItems(data);

        System.out.println(data);
    }

    public void cmbSuppIdAction(ActionEvent actionEvent) {
        lblSupId.setText((String) cmbSuppId.getValue());
        btnUpdate.setVisible(true);
    }

    private void loadSupplierIds() {
        try {
            ObservableList<String> observableList = FXCollections.observableArrayList();
            ArrayList<String> idList = SupplierModal.loadSupId();

            for (String id : idList) {
                observableList.add(id);
            }
            cmbSuppId.setItems(observableList);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectedItem(){
        tblPayments.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (null!= newValue){
                        setData((Payment) newValue);
                    }
                }
        );
    }
    private void setData(Payment newValue) {
        lblPaymentId.setText(newValue.getPayId());
        lblAmount.setText(String.valueOf(newValue.getAmount()));
        lblDate.setText(String.valueOf(newValue.getDate()));
        lblSuppId.setText(newValue.getSupId());
        txtAmount.setText(String.valueOf(newValue.getAmount()));
        lblSupId.setText(newValue.getSupId());
    }

    public void onCancelAction(MouseEvent event) {
        updatePane.setVisible(false);
        prgCircle.setVisible(false);
        icnUpdate.setVisible(true);
    }

    public void onKeyTyped(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public boolean isMatch() {
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(txtAmount.getText());

        boolean isMatchPrice = matcher.matches();

        if (!isMatchPrice) {
            lblAmountWarning.setText("Invalid Input.");
            txtAmount.clear();
            txtAmount.requestFocus();
        } else {
            lblAmountWarning.setText("");
        }
        return isMatchPrice;
    }

    public void txtSearchAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        search();
    }

    public void imgSearchAction(MouseEvent event) {
        txtSearch.requestFocus();
    }

    public void search() throws SQLException, ClassNotFoundException {
        ArrayList<Payment> list = PaymentModal.getDetail();
        ObservableList<Payment> data = FXCollections.observableArrayList();

        data.addAll(list);

        FilteredList<Payment> filteredList = new FilteredList(data, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(payment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (payment.getPayId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (payment.getSupId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(payment.getDate()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<Payment> sortedList = new SortedList(filteredList);
        sortedList.comparatorProperty().bind(tblPayments.comparatorProperty());
        tblPayments.setItems(sortedList);
    }

    public void imgPrintAction(MouseEvent event) {
        InputStream resourses = this.getClass().getResourceAsStream("/lk/ijse/finalproject/reports/PaymentReport.jrxml");

        try {
            JasperReport report = JasperCompileManager.compileReport(resourses);
            JasperPrint print = JasperFillManager.fillReport(report,null, DBConnection.getDBConnection().getConnection());
            JasperViewer.viewReport(print,false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
