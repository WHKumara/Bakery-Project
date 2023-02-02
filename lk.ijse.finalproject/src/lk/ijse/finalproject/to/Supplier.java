package lk.ijse.finalproject.to;

public class Supplier {
    private String supId;
    private String supName;
    private int supContact;
    private String supAddress;
    private String supEmail;

    public Supplier() {
    }

    public Supplier(String supId, String supName, int supContact, String supAddress, String supEmail) {
        this.supId = supId;
        this.supName = supName;
        this.supContact = supContact;
        this.supAddress = supAddress;
        this.supEmail = supEmail;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public int getSupContact() {
        return supContact;
    }

    public void setSupContact(int supContact) {
        this.supContact = supContact;
    }

    public String getSupAddress() {
        return supAddress;
    }

    public void setSupAddress(String supAddreess) {
        this.supAddress = supAddress;
    }

    public String getSupEmail() {
        return supEmail;
    }

    public void setSupEmail(String supEmail) {
        this.supEmail = supEmail;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supId='" + supId + '\'' +
                ", supName='" + supName + '\'' +
                ", supContact=" + supContact +
                ", supAddreess='" + supAddress + '\'' +
                ", supEmail='" + supEmail + '\'' +
                '}';
    }
}
