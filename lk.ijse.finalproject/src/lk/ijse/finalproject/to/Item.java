package lk.ijse.finalproject.to;

public class Item {
    private String itemId;
    private int itemQty;
    private String detail;
    private double unitPrice;

    public Item() {
    }

    public Item(String itemId, int itemQty, String detail, double unitPrice) {
        this.itemId = itemId;
        this.itemQty = itemQty;
        this.detail = detail;
        this.unitPrice = unitPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
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

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", itemQty=" + itemQty +
                ", detail='" + detail + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
