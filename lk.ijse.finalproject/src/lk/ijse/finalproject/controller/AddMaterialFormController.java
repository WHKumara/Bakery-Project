package lk.ijse.finalproject.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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

public class AddMaterialFormController {
    public AnchorPane pane;
    public Label lblItemCode;
    public TextField txtMaterialType;
    public TextField txtQuantity;
    public JFXButton btnAdd;
    public JFXButton btnCancel;
    public ComboBox cmbSupplier;
    public Label lblQtyWarning;

    public void initialize() throws SQLException, ClassNotFoundException {
        loadSupplierIds();
        loadNextMaterialId();
    }

    public void btnAddAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(isMatchQty()){
            if(!txtMaterialType.getText().equals("")){
                String type = txtMaterialType.getText();
                int  qty = Integer.parseInt(txtQuantity.getText());
                String supId = (String) cmbSupplier.getValue();

                Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure whether do you want to ADD this ?",
                        ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get()==ButtonType.YES){
                    RawMaterialModal.addItem(new RawMaterial(lblItemCode.getText(),type,qty,supId));
                    Alert alert1 = new Alert(Alert.AlertType.WARNING, "Item is Added !.",
                            ButtonType.OK);
                    Optional<ButtonType> result1 = alert1.showAndWait();
                    if (result1.get()==ButtonType.OK){
                        loadNextMaterialId();
                        clearTextFeild();
                    }
                }
            }
        }


    }

    private void clearTextFeild() {
        txtMaterialType.clear();
        txtQuantity.clear();
    }

    public void backAction(MouseEvent event) throws IOException {
        Navigation.navigate(Routes.RAWMATERIAL,pane);
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
    private void loadNextMaterialId() {
        try {
            String materialId = RawMaterialModal.generateNextMterialId();
            lblItemCode.setText(materialId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnCancelAction(ActionEvent actionEvent) throws IOException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure whether do you want to cancle this ?",
                ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()==ButtonType.YES){
            Alert alert1 = new Alert(Alert.AlertType.WARNING, "Material is not Added !.",
                    ButtonType.OK);
            Optional<ButtonType> result1 = alert1.showAndWait();
            if (result1.get()==ButtonType.OK)
                Navigation.navigate(Routes.RAWMATERIAL,pane);
        }
    }

    public boolean isMatchQty(){
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(txtQuantity.getText());

        boolean isMatchQty = matcher.matches();

        if (!isMatchQty) {
            lblQtyWarning.setText("Invalid Input.");
            txtQuantity.clear();
            txtQuantity.requestFocus();
        } else {
            lblQtyWarning.setText("");
        }
        return isMatchQty;
    }
}
