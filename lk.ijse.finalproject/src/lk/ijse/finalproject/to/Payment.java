package lk.ijse.finalproject.to;

import java.sql.Date;
import java.sql.Time;

public class Payment {
    private String payId;
    private double amount;
    private Date date;
    private Time time;
    private String supId;

    public Payment() {
    }
    public Payment(String payId, double amount, Date date, Time time, String supId) {
        this.payId = payId;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.supId = supId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }
}
