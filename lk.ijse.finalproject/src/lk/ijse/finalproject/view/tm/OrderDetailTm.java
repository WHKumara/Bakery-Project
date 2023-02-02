package lk.ijse.finalproject.view.tm;

import java.sql.Date;
import java.sql.Time;

public class OrderDetailTm {
    private String orderId;
    private String custId;
    private Date date;
    private Time time;
    private double amount;

    public OrderDetailTm() {
    }

    public OrderDetailTm(String orderId, String custId, Date date, Time time, double amount) {
        this.orderId = orderId;
        this.custId = custId;
        this.date = date;
        this.time = time;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderDetailTm{" +
                "orderId='" + orderId + '\'' +
                ", custId='" + custId + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", amount=" + amount +
                '}';
    }
}
