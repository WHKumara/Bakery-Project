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
import lk.ijse.finalproject.model.SupplierModal;
import lk.ijse.finalproject.to.Supplier;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupplierFormController {
    public AnchorPane pane;
    public TableView tblSupplier;
    public TableColumn colSuppId;
    public TableColumn colName;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colEmail;
    public ProgressIndicator prgCircle;
    public Pane paneDelete;
    public Pane paneUpdate;
    public TextField txtName;
    public TextField txtContact;
    public ImageView imgUpdate;
    public Label lblContact;
    public Label lblName;
    public Label lblSupId;
    public JFXButton btnUpdate;
    public TextArea txtAddress;
    public Label lblAddressWarning;
    public Label lblContactWarning;
    public TextField txtSearch;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setSupplierTable();
        selectedItem();
    }
    public void addSupplierAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.ADDSUPPLIER,pane);
    }

    public void setCellValueFactory(){
        colSuppId.setCellValueFactory(new PropertyValueFactory<>("supId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supName"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("supContact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("supAddress"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("supEmail"));
    }

    public void setSupplierTable() throws SQLException, ClassNotFoundException {
        ObservableList<Supplier> data = FXCollections.observableArrayList();
        ArrayList<Supplier> suppList = SupplierModal.getDetail();
        data.addAll(suppList);
        tblSupplier.setItems(data);

    }

    public void onClickUpdate(MouseEvent event) {
        if(lblSupId.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Please select row in table !").show();
        }else {
            imgUpdate.setVisible(false);
            prgCircle.setVisible(true);
            paneUpdate.setVisible(true);
            paneDelete.setVisible(false);
        }
    }

    public void selectedItem(){
        tblSupplier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (null!= newValue){
                        setData((Supplier) newValue);
                        getAddress((Supplier) newValue);
                    }
                }
        );
    }
    private void setData(Supplier newValue) {
        lblSupId.setText(newValue.getSupId());
        lblName.setText(newValue.getSupName());
        lblContact.setText(String.valueOf(newValue.getSupContact()));
        txtAddress.setText(newValue.getSupAddress());
        txtName.setText(newValue.getSupName());
        txtContact.setText(String.valueOf(newValue.getSupContact()));
    }

    public void btnUpdateAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if(isMatchContact()){

        }
        String name = txtName.getText();
        int contact = Integer.parseInt(txtContact.getText());
        String address = txtAddress.getText();

        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether do you want to UPDATE ?",
                ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()==ButtonType.YES){
            Supplier supplier = new Supplier(lblSupId.getText(),name,contact,address,"");
            try {
                SupplierModal.updateSupplier(supplier);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }finally {
                paneUpdate.setVisible(false);
                prgCircle.setVisible(false);
                imgUpdate.setVisible(true);
                paneDelete.setVisible(true);
                txtName.clear();
                txtContact.clear();
                txtAddress.clear();
                setSupplierTable();
            }
        }
    }

    public void onNameTyped(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public void onContactTyped(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public void onAddressTyped(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }
    public void getAddress(Supplier newValue){

    }

    public void onCancelAction(MouseEvent event) {
        paneUpdate.setVisible(false);
        paneDelete.setVisible(true);
        prgCircle.setVisible(false);
        imgUpdate.setVisible(true);
    }

    public void onDeleteAction(MouseEvent event) throws SQLException, ClassNotFoundException {
        if(lblSupId.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Please select row in table !").show();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this ?",ButtonType.YES,ButtonType.NO);
            Optional<ButtonType> reault = alert.showAndWait();
            if(reault.get()==ButtonType.YES){
                SupplierModal.deleteSupp(lblSupId.getText());
                setSupplierTable();
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted !").show();
            }
        }
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

    public void txtSearchAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        search();
    }

    public void imgSearchAction(MouseEvent event) {
        txtSearch.requestFocus();
    }

    public void search() throws SQLException, ClassNotFoundException {
        ArrayList<Supplier> list = SupplierModal.getDetail();
        ObservableList<Supplier> data = FXCollections.observableArrayList();

        data.addAll(list);

        FilteredList<Supplier> filteredList = new FilteredList(data, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplier.getSupId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (supplier.getSupName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }else if (supplier.getSupEmail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(supplier.getSupContact()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<Supplier> sortedList = new SortedList(filteredList);
        sortedList.comparatorProperty().bind(tblSupplier.comparatorProperty());
        tblSupplier.setItems(sortedList);
    }
}
