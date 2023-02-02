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
import lk.ijse.finalproject.model.EmployeeModal;
import lk.ijse.finalproject.to.Employee;
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


public class ManageEmployeeFromCotroller {
    public AnchorPane pane;
    public TableColumn colEmpId;
    public TableColumn colEmpName;
    public TableColumn colEmpAddress;
    public TableColumn colEmpContact;
    public TableView<Employee> tblEmpDetail;
    public Label lblPrint;
    public TableColumn colEmpEmail;
    public TextField txtName;
    public TextArea txtAddress;
    public TextField txtContact;
    public ProgressIndicator prgCircle;
    public ImageView imgUpdate;
    public Pane paneDelete;
    public Pane paneUpdate;
    public JFXButton btnUpdate;
    public Label lblId;
    public Label lblName;
    public Label lblContact;
    public Label lblContactWarning;
    public TextField txtSearch;

    private ObservableList<Employee> data;

    public void initialize() throws SQLException, ClassNotFoundException {
        tblEmpDetail.refresh();
       setCellValueFactory();
       setEmpTable();
       selectedItem();
    }
    public void lblAddEmployeeAction(MouseEvent event) throws IOException {
       Navigation.navigate(Routes.ADDEMPLOYEE,pane);

    }

    public void setCellValueFactory() {

        colEmpId.setCellValueFactory(new PropertyValueFactory<>("EmpID"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("EmpName"));
        colEmpContact.setCellValueFactory(new PropertyValueFactory<>("EmpContact"));
        colEmpAddress.setCellValueFactory(new PropertyValueFactory<>("EmpAddress"));
        colEmpEmail.setCellValueFactory(new PropertyValueFactory<>("EmpEmail"));

    }

    public void setEmpTable() throws SQLException, ClassNotFoundException {
        ObservableList<Employee> data = FXCollections.observableArrayList();

        ArrayList<Employee> emp = EmployeeModal.getDetails();

        data.addAll(emp);

        tblEmpDetail.setItems(data);
    }

    public void onUpdateAction(MouseEvent event) {
        if(!lblId.getText().equals("")){
            imgUpdate.setVisible(false);
            prgCircle.setVisible(true);
            paneDelete.setVisible(false);
            paneUpdate.setVisible(true);
        }else{
            new Alert(Alert.AlertType.WARNING,"Please select row in table !").show();
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
                Employee emp = new Employee(lblId.getText(),name,contact,address,null);
                try {
                    EmployeeModal.updateEmployee(emp);
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
                    setEmpTable();
                }
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Try again !");
        }

    }

    public void imgCloseAction(MouseEvent event) {
        paneUpdate.setVisible(false);
        paneDelete.setVisible(true);
        prgCircle.setVisible(false);
        imgUpdate.setVisible(true);
    }

    public void onAddressTypeAction(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public void onNameTypedAction(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public void onContactTypedAction(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public void selectedItem(){
        tblEmpDetail.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (null!= newValue){
                        setData(newValue);
                    }
                }
        );
    }

    private void setData(Employee newValue) {
        lblId.setText(newValue.getEmpID());
        lblName.setText(newValue.getEmpName());
        lblContact.setText(String.valueOf(newValue.getEmpContact()));
        txtAddress.setText(newValue.getEmpAddress());
        txtName.setText(newValue.getEmpName());
        txtContact.setText(String.valueOf(newValue.getEmpContact()));
    }

    public void onDeleteAction(MouseEvent event) throws SQLException, ClassNotFoundException {
        if(lblId.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Please select row in table !").show();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this ?",ButtonType.YES,ButtonType.NO);
            Optional<ButtonType> reault = alert.showAndWait();
            if(reault.get()==ButtonType.YES){
                EmployeeModal.deleteEmployee(lblId.getText());
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted !").show();
                setEmpTable();
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

    public void imgSearchAction(MouseEvent event) {
        txtSearch.requestFocus();
    }

    public void txtSearchAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        search();
    }

    public void search() throws SQLException, ClassNotFoundException {
        ArrayList<Employee> list = EmployeeModal.getDetails();
        ObservableList<Employee> data = FXCollections.observableArrayList();

        data.addAll(list);

        FilteredList<Employee> filteredList = new FilteredList(data, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(employee -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (employee.getEmpID().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (employee.getEmpName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(employee.getEmpContact()).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<Employee> sortedList = new SortedList(filteredList);
        sortedList.comparatorProperty().bind(tblEmpDetail.comparatorProperty());
        tblEmpDetail.setItems(sortedList);
    }

    public void imgPrintAction(MouseEvent event) {
        InputStream resourses = this.getClass().getResourceAsStream("/lk/ijse/finalproject/reports/EmployeeReport.jrxml");

        try {
            JasperReport report = JasperCompileManager.compileReport(resourses);
            JasperPrint print = JasperFillManager.fillReport(report,null, DBConnection.getDBConnection().getConnection());
            JasperViewer.viewReport(print,false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }
}
