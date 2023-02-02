package lk.ijse.finalproject.to;

public class Customer {
    private String CustID;
    private String CustName;
    private int CustContact;
    private String CustAddress;

    public Customer(String custID, String custName, int custContact, String custAddress) {
        this.CustID = custID;
        this.CustName = custName;
        this.CustContact = custContact;
        this.CustAddress = custAddress;
    }

    public String getCustID() {
        return CustID;
    }

    public void setCustID(String custID) {
        CustID = custID;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public int getCustContact() {
        return CustContact;
    }

    public void setCustContact(int custContact) {
        CustContact = custContact;
    }

    public String getCustAddress() {
        return CustAddress;
    }

    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "CustID='" + CustID + '\'' +
                ", CustName='" + CustName + '\'' +
                ", CustContact=" + CustContact +
                ", CustAddress='" + CustAddress + '\'' +
                '}';
    }
}
