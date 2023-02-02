package lk.ijse.finalproject.view.tm;


import javafx.scene.control.Button;

public class OrderTm {
    private String itemId;
    private String detail;
    private double qty;
    private double unitPrice;
    private Button btn;
    private double price;

    public OrderTm() {
    }

    public OrderTm(String itemId, String detail, double qty, double unitPrice, Button btn, double price) {
        this.itemId = itemId;
        this.detail = detail;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.btn = btn;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderTm{" +
                "itemId='" + itemId + '\'' +
                ", detail='" + detail + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", btn=" + btn +
                ", price=" + price +
                '}';
    }
}
