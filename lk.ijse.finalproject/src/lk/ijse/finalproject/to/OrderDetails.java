package lk.ijse.finalproject.to;

public class OrderDetails {
    private String orderId;
    private String itemId;
    private String detail;
    private double unitPrice;
    private double qty;

    public OrderDetails() {
    }

    public OrderDetails(String orderId, String itemId, String detail, double unitPrice, double qty) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.detail = detail;
        this.unitPrice = unitPrice;
        this.qty = qty;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderId='" + orderId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", detail='" + detail + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                '}';
    }
}
