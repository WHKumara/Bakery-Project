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
import lk.ijse.finalproject.model.RawMaterialModal;
import lk.ijse.finalproject.model.SupplierModal;
import lk.ijse.finalproject.to.RawMaterial;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RawMaterialFormController {
    public AnchorPane pane;
    public JFXButton btnUpdate;
    public ProgressIndicator prgUpdate;
    public Label lblMaterialId;
    public Label lblType;
    public Label lblQty;
    public TextField txtQty;
    public TableView tblRawMaterial;
    public TableColumn colMterialId;
    public TableColumn colType;
    public TableColumn colQty;
    public TableColumn colSupId;
    public Label lblSupId;
    public Label lblUpdateDetails;
    public ImageView imgUpdate;
    public ProgressIndicator prgCircle;
    public Pane updatePane;
    public TextField txtSupId;
    public ComboBox cmbSupId;
    public Label lblNewSupId;
    public Pane paneDelete;
    public Label lblQtyWarning;
    public TextField txtSearch;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setMaterilTable();
        selectedItem();
        loadSupplierIds();

    }

    public void setCellValueFactory(){
        colMterialId.setCellValueFactory(new PropertyValueFactory<>("materialId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supId"));
    }

    public void setMaterilTable() throws SQLException, ClassNotFoundException {
        ObservableList<RawMaterial> data = FXCollections.observableArrayList();
        ArrayList<RawMaterial> materialList = RawMaterialModal.getDetail();
        data.addAll(materialList);
        tblRawMaterial.setItems(data);

        System.out.println(data);
    }

    public void onAddMaterialAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.ADDMATERIAL,pane);
    }

    public void selectedItem(){
        tblRawMaterial.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (null!= newValue){
                        setData((RawMaterial) newValue);
                    }
                }
        );
    }

    private void setData(RawMaterial newValue) {
        lblMaterialId.setText(newValue.getMaterialId());
        lblType.setText(newValue.getType());
        lblQty.setText(String.valueOf(newValue.getQty()));
        lblSupId.setText(newValue.getSupId());
        txtQty.setText(String.valueOf(newValue.getQty()));
        lblNewSupId.setText(newValue.getSupId());
    }

    public void onClickUpdate(MouseEvent event) {
        if(lblMaterialId.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Please select row in table !").show();
        }else {
            imgUpdate.setVisible(false);
            prgCircle.setVisible(true);
            updatePane.setVisible(true);
            paneDelete.setVisible(false);
        }
    }

    public void btnUpdateAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if(isMatch()){
            int newQty = Integer.parseInt(txtQty.getText());
            String newSupId = lblNewSupId.getText();

            Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure whether do you want to UPDATE ?",
                    ButtonType.YES, ButtonType.NO);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get()==ButtonType.YES){
                RawMaterial material = new RawMaterial(lblMaterialId.getText(),lblType.getText(),newQty,newSupId);
                try {
                    RawMaterialModal.updateData(material);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }finally {
                    updatePane.setVisible(false);
                    prgCircle.setVisible(false);
                    imgUpdate.setVisible(true);
                    paneDelete.setVisible(true);
                    txtQty.clear();
                    lblNewSupId.setText("");
                    setMaterilTable();
                }
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Try again !");
        }

    }
    private void loadSupplierIds() {
        try {
            ObservableList<String> observableList = FXCollections.observableArrayList();
            ArrayList<String> idList = SupplierModal.loadSupId();

            for (String id : idList) {
                observableList.add(id);
            }
            cmbSupId.setItems(observableList);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void cmbOnAction(ActionEvent actionEvent) {
        lblNewSupId.setText((String) cmbSupId.getValue());
        btnUpdate.setVisible(true);
    }

    public void onKeyTyped(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public void onClickDelete(MouseEvent event) throws SQLException, ClassNotFoundException {
        if(lblMaterialId.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Please select row in table !").show();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this ?",ButtonType.YES,ButtonType.NO);
            Optional<ButtonType> reault = alert.showAndWait();
            if(reault.get()==ButtonType.YES){
                RawMaterialModal.deleteMateril(lblMaterialId.getText());
                setMaterilTable();
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted !").show();
            }
        }
    }

    public void onCancelAction(MouseEvent event) {
        updatePane.setVisible(false);
        paneDelete.setVisible(true);
        prgCircle.setVisible(false);
        imgUpdate.setVisible(true);
    }

    public boolean isMatch() {
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(txtQty.getText());

        boolean isMatchPrice = matcher.matches();

        if (!isMatchPrice) {
            lblQtyWarning.setText("Invalid Input.");
            txtQty.clear();
            txtQty.requestFocus();
        } else {
            lblQtyWarning.setText("");
        }
        return isMatchPrice;
    }

    public void txtSearchAtion(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        search();
    }

    public void imgSearchAction(MouseEvent event) {
        txtSearch.requestFocus();
    }

    public void search() throws SQLException, ClassNotFoundException {
        ArrayList<RawMaterial> list = RawMaterialModal.getDetail();
        ObservableList<RawMaterial> data = FXCollections.observableArrayList();

        data.addAll(list);

        FilteredList<RawMaterial> filteredList = new FilteredList(data, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(material -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (material.getMaterialId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (material.getType().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<RawMaterial> sortedList = new SortedList(filteredList);
        sortedList.comparatorProperty().bind(tblRawMaterial.comparatorProperty());
        tblRawMaterial.setItems(sortedList);
    }
}

