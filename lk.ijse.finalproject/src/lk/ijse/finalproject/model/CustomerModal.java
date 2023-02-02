package lk.ijse.finalproject.model;

import lk.ijse.finalproject.to.Customer;
import lk.ijse.finalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModal {

    public static ArrayList<Customer> getDetail() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM customer";
        ResultSet result = CrudUtil.execute(sql);

        ArrayList<Customer> customerList = new ArrayList<>();

        while (result.next()) {
            customerList.add(new Customer(
                    result.getString(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getString(4)
            ));
        }
        return customerList;
    }

    public static boolean addCustomer(Customer cust) throws SQLException, ClassNotFoundException {
        String sql ="INSERT INTO customer VALUES (?,?,?,?)";

        return CrudUtil.execute(sql,cust.getCustID(),cust.getCustName(),cust.getCustContact(),cust.getCustAddress());
    }

    public static boolean updateData(Customer cust) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE customer SET CustName=?,CustContact=?,CustAddress=? WHERE CustID=?";

        return CrudUtil.execute(sql,cust.getCustName(),cust.getCustContact(),cust.getCustAddress(),cust.getCustID());
    }

    public static boolean deleteCustomer(String custId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM customer WHERE CustID=?";

        return CrudUtil.execute(sql,custId);
    }

    public static ArrayList<String> loadCustId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT CustID FROM customer";
        ResultSet result = CrudUtil.execute(sql);
        ArrayList<String> data = new ArrayList<>();

        while(result.next()){
            data.add(result.getString(1));
        }
        return data;
    }

    public static Customer search(String code) throws SQLException, ClassNotFoundException {
        String sql = "SELECT  * FROM customer WHERE CustID = ?";
        ResultSet result = CrudUtil.execute(sql, code);

        if (result.next()) {
            return new Customer(
                    result.getString(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getString(4)
            );
        }
        return null;
    }
    public static int customerCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) from customer";
        ResultSet result = CrudUtil.execute(sql);

        if (result.next())
            return result.getInt(1);
        return 0;
    }
    public static String generateNextEmployeeId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT CustID FROM customer ORDER BY CustID DESC LIMIT 1";
        ResultSet result = CrudUtil.execute(sql);

        if (result.next()) {
            return generateNextEmployeeId((result.getString(1)));
        }
        return generateNextEmployeeId((result.getString(null)));
    }

    private static String generateNextEmployeeId(String currentOrderId) {
        if (currentOrderId != null) {
            String[] split = currentOrderId.split("C0");
            int id = Integer.parseInt(split[1]);
            id += 1;
            return "C0" + id;
        }
        return "C01";

    }
}