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
import lk.ijse.finalproject.model.CustomerModal;
import lk.ijse.finalproject.to.Customer;
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

public class CustomerFormController {
    public AnchorPane pane;
    public Label lblPaymentId;
    public Label lblAmount;
    public Label lblSuppId;
    public Label lblDate;
    public TextField txtAmount;
    public TextField txtSuppId;
    public TextField txtDate;
    public JFXButton btnUpdate;
    public ProgressIndicator prgCircle;
    public Pane updatePane;
    public ImageView icnUpdate;
    public TableView tblCustomer;
    public TableColumn colCustId;
    public TableColumn colCustName;
    public TableColumn colCustAddress;
    public TableColumn colCustContact;
    public Label lblSelectedId;
    public Label lblAddress;
    public Label lblContact;
    public Label lblName;
    public TextField txtName;
    public TextField txtContact;
    public TextField txtAddress;
    public Pane paneDelete;
    public Label lblContactWarning;
    public TextField txtSearch;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setCusotmerTable();
        selectedItem();
    }
    public void addCustomerAction(MouseEvent event) {
    }

    public void updateClickAction(MouseEvent event) {
        if(lblSelectedId.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Please select row in table !").show();
        }else {
            icnUpdate.setVisible(false);
            prgCircle.setVisible(true);
            updatePane.setVisible(true);
            paneDelete.setVisible(false);
        }


    }

    public void btnUpdateAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(isMatchContact()){
            String name = txtName.getText();
            int contact = Integer.parseInt(txtContact.getText());
            String address = txtAddress.getText();

            Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure whether do you want to UPDATE ?",
                    ButtonType.YES, ButtonType.NO);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get()==ButtonType.YES){
                Customer cust = new Customer(lblSelectedId.getText(),name,contact,address);
                try {
                    CustomerModal.updateData(cust);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }finally {
                    updatePane.setVisible(false);
                    prgCircle.setVisible(false);
                    icnUpdate.setVisible(true);
                    paneDelete.setVisible(true);
                    txtName.clear();
                    txtContact.clear();
                    txtAddress.clear();
                    setCusotmerTable();
                }
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Try again !").show();
        }

    }

    public void addClickAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.ADDCUSTOMER,pane);

    }
    public void setCellValueFactory(){
        colCustId.setCellValueFactory(new PropertyValueFactory<>("CustID"));
        colCustName.setCellValueFactory(new PropertyValueFactory<>("CustName"));
        colCustContact.setCellValueFactory(new PropertyValueFactory<>("CustContact"));
        colCustAddress.setCellValueFactory(new PropertyValueFactory<>("CustAddress"));
    }

    public void setCusotmerTable() throws SQLException, ClassNotFoundException {
        ObservableList<Customer> data = FXCollections.observableArrayList();
        ArrayList<Customer> custList = CustomerModal.getDetail();
        data.addAll(custList);
        tblCustomer.setItems(data);
    }

    public void onCancelAction(MouseEvent event) {
        updatePane.setVisible(false);
        prgCircle.setVisible(false);
        icnUpdate.setVisible(true);
        paneDelete.setVisible(true);

    }

    public void clickDeleteAction(MouseEvent event) throws SQLException, ClassNotFoundException {
        if(lblSelectedId.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Please select row in table !").show();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this ?",ButtonType.YES,ButtonType.NO);
            Optional<ButtonType> reault = alert.showAndWait();
            if(reault.get()==ButtonType.YES){
                CustomerModal.deleteCustomer(lblSelectedId.getText());
                setCusotmerTable();
                new Alert(Alert.AlertType.INFORMATION,"Customer Deleted !").show();
            }
        }
    }

    public void selectedItem(){
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (null!= newValue){
                        setData((Customer) newValue);
                    }
                }
        );
    }

    private void setData(Customer newValue) {
        lblSelectedId.setText(newValue.getCustID());
        lblName.setText(newValue.getCustName());
        lblAddress.setText(newValue.getCustAddress());
        lblContact.setText(String.valueOf(newValue.getCustContact()));
        txtName.setText(newValue.getCustName());
        txtContact.setText(String.valueOf(newValue.getCustContact()));
        txtAddress.setText(newValue.getCustAddress());
    }

    public void onNameTyped(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public void onAddressTyped(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public void onConatcttyped(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public boolean isMatchContact() {
        Pattern pattern = Pattern.compile("^(07)([0-9]{8,8})$");
        Matcher matcher = pattern.matcher(txtContact.getText());

        boolean isMatchContact = matcher.matches();

        if (!isMatchContact) {
            lblContactWarning.setText("Invalid Input.");
            txtContact.clear();
            txtContact.requestFocus();
        } else {
            lblContactWarning.setText("");
        }
        return isMatchContact;
    }

    public void imgSearchAction(MouseEvent event) {
        txtSearch.requestFocus();
    }

    public void txtSearchAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        search();
    }

    public void search() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> list = CustomerModal.getDetail();
        ObservableList<Customer> data = FXCollections.observableArrayList();

        data.addAll(list);

        FilteredList<Customer> filteredList = new FilteredList(data, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (customer.getCustID().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (customer.getCustName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(customer.getCustContact()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<Customer> sortedList = new SortedList(filteredList);
        sortedList.comparatorProperty().bind(tblCustomer.comparatorProperty());
        tblCustomer.setItems(sortedList);
    }

    public void imgPrintAction(MouseEvent event) {
        InputStream resourses = this.getClass().getResourceAsStream("/lk/ijse/finalproject/reports/CustomerReport.jrxml");

        try {
            JasperReport report = JasperCompileManager.compileReport(resourses);
            JasperPrint print = JasperFillManager.fillReport(report,null, DBConnection.getDBConnection().getConnection());
            JasperViewer.viewReport(print,false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
        /*try {
            JasperReport jasperReport = JasperCompileManager.compileReport(resourses);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getDBConnection().getConnection());
            JasperViewer.viewReport(jasperPrint);
            //JasperPrintManager.printReport(jasperPrint,true);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*/
    }
}
