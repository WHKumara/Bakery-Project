package lk.ijse.finalproject.to;

public class Employee {
    private String empID;
    private String empName;
    private int empContact;
    private String empAddress;
    private String empEmail;

    public Employee() {
    }

    public Employee(String empID, String empName, int empContact, String empAddress, String empEmail) {
        this.empID = empID;
        this.empName = empName;
        this.empContact = empContact;
        this.empAddress = empAddress;
        this.empEmail = empEmail;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getEmpContact() {
        return empContact;
    }

    public void setEmpContact(int empContact) {
        this.empContact = empContact;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empID='" + empID + '\'' +
                ", empName='" + empName + '\'' +
                ", empContact=" + empContact +
                ", empAddress='" + empAddress + '\'' +
                ", empEmail='" + empEmail + '\'' +
                '}';
    }
}