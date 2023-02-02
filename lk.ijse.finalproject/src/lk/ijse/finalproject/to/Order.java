package lk.ijse.finalproject.to;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class Order {
    private String orderId;
    private String custId;
    private Date date;
    private Time time;
    private double amount;
    private ArrayList<OrderDetails> orderDetails;

    public Order() {
    }

    public Order(String orderId, String custId, Date date, Time time, ArrayList<OrderDetails> orderDetails, double amount) {
        this.orderId = orderId;
        this.custId = custId;
        this.date = date;
        this.time = time;
        this.orderDetails = orderDetails;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public ArrayList<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", custId='" + custId + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
