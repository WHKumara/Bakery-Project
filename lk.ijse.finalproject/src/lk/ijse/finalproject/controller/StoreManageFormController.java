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
import lk.ijse.finalproject.model.ItemModal;
import lk.ijse.finalproject.to.Item;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreManageFormController {
    public AnchorPane pane;
    public TableView<Item> tblItem;

    public TableColumn colItemType;
    public TableColumn colItemQty;
    public TableColumn colUnitPrice;
    public TextField txtNewPrice;
    public TextField txtNewQty;
    public JFXButton btnUpdate;
    public ProgressIndicator prgCircle;
    public Label lblAddNewItem;
    public Label lblQty;
    public Label lblPrice;
    public Label lblCode;
    public Label lblType;
    public TableColumn colItemCode;
    public Pane paneUpdate;
    public ImageView imgUpdate;
    public Pane panDelete;
    public Label lblPriceWarning;
    public Label lblQtyWarning;
    public TextField txtSearch;

    public void initialize() throws SQLException, ClassNotFoundException {
        setCellValueFactory();
        setItemTable();
        selectedItem();

    }

    public void selectedItem(){
        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (null!= newValue){
                        setData(newValue);
                    }
                }
        );
    }
    private void setData(Item newValue) {
        lblCode.setText(newValue.getItemId());
        lblType.setText(newValue.getDetail());
        lblQty.setText(String.valueOf(newValue.getItemQty()));
        lblPrice.setText(String.valueOf(newValue.getUnitPrice()));
        txtNewQty.setText(String.valueOf(newValue.getItemQty()));
        txtNewPrice.setText(String.valueOf(newValue.getUnitPrice()));

    }

    public void btnUpdateAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(isMatchQty() & isMatchPrice()){
            int newQty = Integer.parseInt(txtNewQty.getText());
            double newprice = Double.parseDouble(txtNewPrice.getText());

            Alert alert= new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure whether do you want to UPDATE ?",
                    ButtonType.YES, ButtonType.NO);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get()==ButtonType.YES){
                Item item = new Item(lblCode.getText(),newQty,"",newprice);
                try {
                    ItemModal.updateData(item);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }finally {
                    paneUpdate.setVisible(false);
                    prgCircle.setVisible(false);
                    imgUpdate.setVisible(true);
                    panDelete.setVisible(true);
                    txtNewPrice.clear();
                    txtNewQty.clear();
                    setItemTable();
                }
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Try again !");
        }

    }

        public void lblAddItemAction (MouseEvent event) throws IOException {
            Navigation.navigate(Routes.ADDITEM, pane);
        }


    public void setCellValueFactory(){
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemType.setCellValueFactory(new PropertyValueFactory<>("itemQty"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("detail"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
    }

    public void setItemTable() throws SQLException, ClassNotFoundException {
        ObservableList<Item> data = FXCollections.observableArrayList();
        ArrayList<Item> itemList = ItemModal.getDetail();
        data.addAll(itemList);
        tblItem.setItems(data);

    }


    public void onDeleteAction(MouseEvent event) throws SQLException, ClassNotFoundException {
        if(lblCode.getText().equals("")){
            new Alert(Alert.AlertType.WARNING,"Please select row in table !").show();
        }else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this ?",ButtonType.YES,ButtonType.NO);
            Optional<ButtonType> reault = alert.showAndWait();
            if(reault.get()==ButtonType.YES){
                ItemModal.deleteItem(lblCode.getText());
                setItemTable();
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted !").show();
            }
        }
    }

    public void onUpdateAction(MouseEvent event) {
        if(!lblCode.getText().equals("")){
            imgUpdate.setVisible(false);
            prgCircle.setVisible(true);
            panDelete.setVisible(false);
            paneUpdate.setVisible(true);
        }else{
            new Alert(Alert.AlertType.WARNING,"Please select row in table !").show();
        }

    }

    public void onKeyTypeQty(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public void OnKeyTypePrice(KeyEvent keyEvent) {
        btnUpdate.setVisible(true);
    }

    public void onCancelAction(MouseEvent event) {
        paneUpdate.setVisible(false);
        panDelete.setVisible(true);
        prgCircle.setVisible(false);
        imgUpdate.setVisible(true);
    }
    public boolean isMatchQty(){
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(txtNewQty.getText());

        boolean isMatchQty = matcher.matches();

        if(!isMatchQty){
            lblQtyWarning.setText("Invalid Input.");
            txtNewQty.clear();
            txtNewQty.requestFocus();
        }else{
            lblQtyWarning.setText("");
        }
        return  isMatchQty;
    }
    public boolean isMatchPrice(){
        Pattern pattern = Pattern.compile("^[0-9]+\\.[0-9]{2}$");
        Matcher matcher = pattern.matcher(txtNewPrice.getText());

        boolean isMatchAmount = matcher.matches();

        if (!isMatchAmount) {
            lblPriceWarning.setText("Invalid Input.");
            txtNewPrice.clear();
            txtNewPrice.requestFocus();
        } else {
            lblPriceWarning.setText("");
        }
        return isMatchAmount;
    }

    public void txtSearchAction(KeyEvent keyEvent) throws SQLException, ClassNotFoundException {
        search();
    }

    public void imgSearchAction(MouseEvent event) {
        txtSearch.requestFocus();
    }
    public void search() throws SQLException, ClassNotFoundException {
        ArrayList<Item> list = ItemModal.getDetail();
        ObservableList<Item> data = FXCollections.observableArrayList();

        data.addAll(list);

        FilteredList<Item> filteredList = new FilteredList(data, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (item.getItemId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (item.getDetail().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<Item> sortedList = new SortedList(filteredList);
        sortedList.comparatorProperty().bind(tblItem.comparatorProperty());
        tblItem.setItems(sortedList);
    }
}