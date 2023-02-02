package lk.ijse.finalproject.model;

import lk.ijse.finalproject.to.Item;
import lk.ijse.finalproject.to.OrderDetails;
import lk.ijse.finalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ItemModal {
    public static ArrayList<Item> getDetail() throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM item ";

        ResultSet result = CrudUtil.execute(sql);
   ArrayList<Item> itemList = new ArrayList<>();

        while (result.next()) {
            itemList.add(new Item(
                    result.getString(1),
                    result.getInt(2),
                    result.getString(3),
                    result.getDouble(4)
            ));
        }
        return itemList;
    }
    public static String generateNextItemId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT ItemID FROM item ORDER BY ItemID DESC LIMIT 1";
        ResultSet result = CrudUtil.execute(sql);

        if (result.next()) {
            return generateNextItemId((result.getString(1)));
        }
        return generateNextItemId((result.getString(null)));
    }

    public static boolean addItem(Item item) throws SQLException, ClassNotFoundException {
        String sql ="INSERT INTO item VALUES (?,?,?,?)";

        return CrudUtil.execute(sql,item.getItemId(),item.getItemQty(),item.getDetail(),item.getUnitPrice());
    }
    public static boolean updateData(Item item) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE item SET Qty=?,UnitPrice=? WHERE ItemID=?";

        return CrudUtil.execute(sql,item.getItemQty(),item.getUnitPrice(),item.getItemId());
    }

    public static boolean deleteItem(String itemId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM item WHERE ItemID=?";

        return CrudUtil.execute(sql,itemId);
    }

    public static ArrayList<String> loadItemId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT ItemID FROM item";
        ResultSet result = CrudUtil.execute(sql);
        ArrayList<String> data = new ArrayList<>();

        while(result.next()){
            data.add(result.getString(1));
        }
        return data;
    }

    public static Item search(String code) throws SQLException, ClassNotFoundException {
        String sql = "SELECT  * FROM Item WHERE ItemID = ?";
        ResultSet result = CrudUtil.execute(sql, code);

        if (result.next()) {
            return new Item(
                    result.getString(1),
                    result.getInt(2),
                    result.getString(3),
                    result.getDouble(4)
            );
        }
        return null;
    }
    private static String generateNextItemId(String currentOrderId) {
        if (currentOrderId != null) {
            String[] split = currentOrderId.split("I0");
            int id = Integer.parseInt(split[1]);
            id += 1;
            return "I0" + id;
        }
        return "O001";

    }

    public static boolean updateQty(ArrayList<OrderDetails> orderDetails) throws SQLException, ClassNotFoundException {
        for (OrderDetails orderdetail : orderDetails) {
            if (!updateQty(new Item(orderdetail.getItemId(), (int) orderdetail.getQty(),"",0.0))) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(Item item) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE item SET qty = qty - ? WHERE ItemID = ?";
        return CrudUtil.execute(sql, item.getItemQty(),item.getItemId());
    }
}
