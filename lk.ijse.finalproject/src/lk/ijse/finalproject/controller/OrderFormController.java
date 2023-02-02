package lk.ijse.finalproject.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.finalproject.db.DBConnection;
import lk.ijse.finalproject.model.CustomerModal;
import lk.ijse.finalproject.model.ItemModal;
import lk.ijse.finalproject.model.OrderModal;
import lk.ijse.finalproject.model.PlaceOrderModal;
import lk.ijse.finalproject.to.Customer;
import lk.ijse.finalproject.to.Item;
import lk.ijse.finalproject.to.Order;
import lk.ijse.finalproject.to.OrderDetails;
import lk.ijse.finalproject.util.Navigation;
import lk.ijse.finalproject.util.Routes;
import lk.ijse.finalproject.view.tm.OrderTm;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderFormController {
    public ComboBox cmbCustomer;
    public Label lblCustID;
    public Label lblOrderId;
    public Label lblQtyHand;
    public Label lblDesc;
    public Label lblName;
    public Label lblContact;
    public TableView<OrderTm> tblOrder;
    public TableColumn colItemId;
    public TableColumn colDetail;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colPrice;
    public Label lblTotal;
    public ComboBox cmbItem;
    public Label lblItemId;
    public Label lblUnitPrice;
    public TextField txtQty;
    public TextField txtAmount;
    public Label lblBalance;
    public JFXButton btnPalceOrder;
    public TableColumn colOption;
    public AnchorPane pane;
    public ProgressBar prgBarCustomer;
    public ProgressBar prgBarItem;
    public Label lblQtyWaning;
    public Label lblAmountWarning;
    private ObservableList<OrderTm> obList = FXCollections.observableArrayList();

    public void initialize() {
        loadCustomerId();
        loadItemId();
        loadNextOrderId();
        setCellValueFactory();
    }

    public void txtQtyAction(ActionEvent actionEvent) {
        btnAddAction(actionEvent);
        setTotal();
        //enoughAlert();
    }

    public void enoughAlert(){
        double qtyHand = Double.parseDouble(lblQtyHand.getText());
        double qty = Double.parseDouble(txtQty.getText());
       if(qtyHand<qty){
            new Alert(Alert.AlertType.WARNING,"Quantity is not enough !").show();
            txtQty.requestFocus();
        }
    }

    private void setCellValueFactory() {
        colItemId.setCellValueFactory(new PropertyValueFactory("itemId"));
        colDetail.setCellValueFactory(new PropertyValueFactory("detail"));
        colQty.setCellValueFactory(new PropertyValueFactory("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));
        colOption.setCellValueFactory(new PropertyValueFactory("btn"));
    }

    public boolean txtAmountAction(ActionEvent actionEvent) {

        double balance = Double.parseDouble(txtAmount.getText())-Double.parseDouble(lblTotal.getText());
        lblBalance.setText(String.valueOf(balance));
       if(balance<0){
            txtAmount.requestFocus();
            return false;
        }
        return true;
    }

    public void btnAddAction(ActionEvent actionEvent) {

        if(isMatchQty()){
            boolean isEnough2 = Double.parseDouble(txtQty.getText()) <= Double.parseDouble(lblQtyHand.getText());
            boolean isEnough = Double.parseDouble(lblQtyHand.getText()) >0;

            //check item and customer are selected kiyl
            if(!lblCustID.getText().equals("") && !lblItemId.getText().equals("")){
                if (isEnough && isEnough2){
                    String code = String.valueOf(cmbItem.getValue());
                    int qty = Integer.parseInt(txtQty.getText());
                    String desc = lblDesc.getText();
                    double unitPrice = Double.parseDouble(lblUnitPrice.getText());
                    double total = unitPrice * qty;
                    Button btnDelete = new Button("Delete");
                    double netTot =  0;
                    txtQty.setText("");

                    if (!obList.isEmpty()) {
                        /* check same item has been in table. If so, update that row instead of adding new row to the table */
                        for (int i = 0; i < tblOrder.getItems().size(); i++) {
                            if (colItemId.getCellData(i).equals(code)) {
                                qty += (double) colQty.getCellData(i);
                                total = unitPrice * qty;
                                obList.get(i).setQty(qty);
                                obList.get(i).setPrice(total);
                                tblOrder.refresh();
                                return;
                            }
                        }
                    }

                    /* set delete button to some action before it put on obList */
                    btnDelete.setOnAction((e) -> {
                        ButtonType ok = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure ?", ok, no);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.orElse(no) == ok) {
                            OrderTm tm = tblOrder.getSelectionModel().getSelectedItem();
                            tblOrder.getItems().removeAll(tblOrder.getSelectionModel().getSelectedItem());
                            setTotal();
                            txtAmountAction(actionEvent);
                        }
                    });
                    obList.add(new OrderTm(code, desc, qty, unitPrice, btnDelete, total));
                    tblOrder.setItems(obList);
                    setTotal();
                    System.out.println(obList);

                }else {
                    //new Alert(Alert.AlertType.WARNING,"Please select item or customer !").show();
                    enoughAlert();
                }
            }else {
                try{
                    new Alert(Alert.AlertType.WARNING,"Please select item or customer !").show();
                }catch (RuntimeException e){
                    throw new RuntimeException(e);
                }

            }
        }
    }
     public void setTotal(){
        tblOrder.refresh();
        double total = 0;
         for (int i = 0; i < tblOrder.getItems().size(); i++) {
             total+= Double.parseDouble(String.valueOf(colPrice.getCellData(i)));
             lblTotal.setText(String.valueOf(total));
         }
         if (tblOrder.getItems().size()==0){
             lblTotal.setText("0.0");
         }
    }
    private void loadCustomerId() {
        try {
            ObservableList<String> observableList = FXCollections.observableArrayList();
            ArrayList<String> idList = CustomerModal.loadCustId();

            for (String id : idList) {
                observableList.add(id);
            }
            cmbCustomer.setItems(observableList);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadItemId() {
        try {
            ObservableList<String> observableList = FXCollections.observableArrayList();
            ArrayList<String> idList = ItemModal.loadItemId();

            for (String id : idList) {
                observableList.add(id);
            }
            cmbItem.setItems(observableList);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void cmbItemAction(ActionEvent actionEvent) {
        prgBarItem.setVisible(false);
        String code = String.valueOf(cmbItem.getValue());
        try {
            Item item = ItemModal.search(code);
            fillItemFields(item);
            txtQty.requestFocus();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillItemFields(Item item) {
        lblItemId.setText(item.getItemId());
        lblDesc.setText(item.getDetail());
        lblQtyHand.setText(String.valueOf(item.getItemQty()));
        lblUnitPrice.setText(String.valueOf(item.getUnitPrice()));
    }

    public void cmbCustomerAction(ActionEvent actionEvent) {
        prgBarCustomer.setVisible(false);
        String code = String.valueOf(cmbCustomer.getValue());
        try {
            Customer cust = CustomerModal.search(code);
            fillCustomerFields(cust);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillCustomerFields(Customer cust) {
        lblCustID.setText(cust.getCustID());
        lblContact.setText(String.valueOf(cust.getCustContact()));
        lblName.setText(cust.getCustName());
    }

    private void loadNextOrderId() {
        try {
            String orderId = OrderModal.generateNextOrderId();
            lblOrderId.setText(orderId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnNewAction(ActionEvent actionEvent) throws IOException {
        Navigation.navigate(Routes.ADDCUSTOMER, pane);
    }


    public void btnPlaceOrderAction(ActionEvent actionEvent) {
        if(isMatchAmount()){
            String orderId = lblOrderId.getText();
            String customerId = String.valueOf(cmbCustomer.getValue());
            double amount = Double.parseDouble(lblTotal.getText());

            ArrayList<OrderDetails> orderData = new ArrayList<>();

            /* load all cart items' to orderDetails arrayList */
            for (int i = 0; i < tblOrder.getItems().size(); i++) {
                /* get each row details to (PlaceOrderTm)tm in each time and add them to the orderDetails */
                OrderTm tm = obList.get(i);
                orderData.add(new OrderDetails(orderId, tm.getItemId(),tm.getDetail(), tm.getUnitPrice(),tm.getQty()));
            }

            Order order = new Order(orderId,customerId,null,null,orderData,amount);
            try {
                if (txtAmountAction(actionEvent) && !txtAmount.getText().equals("")){
                    boolean isPlaced = PlaceOrderModal.placeOrder(order);
                    if (isPlaced) {
                        /* to clear table */
                        obList.clear();
                        loadNextOrderId();

                       Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order Placed!",ButtonType.OK);
                        Optional<ButtonType> result =alert.showAndWait();
                        if(result.get()==ButtonType.OK){

                            InputStream resourses = this.getClass().getResourceAsStream("/lk/ijse/finalproject/reports/Order.jrxml");

                            HashMap<String, Object> hm = new HashMap<>();
                            hm.put("orderId",lblOrderId.getText());
                            hm.put("custId",lblCustID.getText());
                            hm.put("name",lblName.getText());
                            hm.put("total",Double.parseDouble(lblTotal.getText()));
                            hm.put("amount",txtAmount.getText());
                            hm.put("balance",lblBalance.getText());


                            try {
                                JasperReport jasperReport = JasperCompileManager.compileReport(resourses);
                                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, hm, DBConnection.getDBConnection().getConnection());
                                JasperViewer.viewReport(jasperPrint,false);
                                //JasperPrintManager.printReport(jasperPrint,true);
                            } catch (JRException | SQLException | ClassNotFoundException e) {
                                System.out.println(e.toString());
                            }
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Order Not Placed!").show();
                    }
                }else {
                    new Alert(Alert.AlertType.WARNING, "Amount is not enough !").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public boolean isMatchQty() {
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(txtQty.getText());

        boolean isMatchPrice = matcher.matches();

        if (!isMatchPrice) {
            lblQtyWaning.setText("Invalid Input.");
            txtQty.clear();
            txtQty.requestFocus();
        } else {
            lblQtyWaning.setText("");
        }
        return isMatchPrice;
    }

    public boolean isMatchAmount(){
        Pattern pattern = Pattern.compile("^[0-9]+\\.[0-9]{2}$");
        Matcher matcher = pattern.matcher(txtAmount.getText());

        boolean isMatchAmount = matcher.matches();

        if (!isMatchAmount) {
            lblAmountWarning.setText("Invalid Input.");
            txtAmount.clear();
            txtAmount.requestFocus();
        } else {
            lblAmountWarning.setText("");
        }
        return isMatchAmount;
    }

}
