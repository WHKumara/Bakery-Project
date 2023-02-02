package lk.ijse.finalproject.model;

import lk.ijse.finalproject.to.Supplier;
import lk.ijse.finalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModal {

    public static ArrayList<Supplier> getDetail() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM supplier";
        ResultSet result = CrudUtil.execute(sql);

        ArrayList<Supplier> suppList = new ArrayList<>();

        while (result.next()) {
            suppList.add(new Supplier(
                    result.getString(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getString(4),
                    result.getString(5)
            ));
        }
        return suppList;
    }

    public static ArrayList<String> loadSupId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT SupID FROM supplier";
        ResultSet result = CrudUtil.execute(sql);
        ArrayList<String> data = new ArrayList<>();

        while(result.next()){
            data.add(result.getString(1));
        }
        return data;
    }

    public static boolean addSupplier(Supplier supp) throws SQLException, ClassNotFoundException {
        String sql ="INSERT INTO supplier VALUES (?,?,?,?,?)";

        return CrudUtil.execute(sql,supp.getSupId(),supp.getSupName(),supp.getSupContact(),supp.getSupAddress(),
                supp.getSupEmail());
    }

    public static boolean updateSupplier(Supplier supplier) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE supplier SET SupName=?,SupContact=?,SupAddress=? WHERE SupID=?";

        return CrudUtil.execute(sql,supplier.getSupName(),supplier.getSupContact(),supplier.getSupAddress(),
                supplier.getSupId());
    }

    public static boolean deleteSupp(String supId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM supplier WHERE SupID=?";

        return CrudUtil.execute(sql,supId);
    }

    public static String generateNextSupplierId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT SupID FROM supplier ORDER BY SupID DESC LIMIT 1";
        ResultSet result = CrudUtil.execute(sql);

        if (result.next()) {
            return generateNextSupplierId((result.getString(1)));
        }
        return generateNextSupplierId((result.getString(null)));
    }

    /*public static ArrayList<String> getSupId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT SupID FROM supplier";
        ResultSet result = CrudUtil.execute(sql);
        ArrayList<String> data = new ArrayList<>();

        while(result.next()){
            data.add(result.getString(1));
        }
        return data;
    }*/

    private static String generateNextSupplierId(String currentOrderId) {
        if (currentOrderId != null) {
            String[] split = currentOrderId.split("S0");
            int id = Integer.parseInt(split[1]);
            id += 1;
            return "S0" + id;
        }
        return "S01";

    }
}
