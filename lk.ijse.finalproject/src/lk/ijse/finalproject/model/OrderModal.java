package lk.ijse.finalproject.model;

import lk.ijse.finalproject.to.Order;
import lk.ijse.finalproject.util.CrudUtil;
import lk.ijse.finalproject.view.tm.OrderDetailTm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderModal {

    public static ArrayList<OrderDetailTm> getData() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM orders";
        ResultSet result = CrudUtil.execute(sql);

        ArrayList<OrderDetailTm> orderData = new ArrayList<>();

        while (result.next()){
            orderData.add(new OrderDetailTm(
                    result.getString(1),
                    result.getString(2),
                    result.getDate(3),
                    result.getTime(4),
                    result.getDouble(5))
            );
        }
        return orderData;
    }
    public static boolean save(Order order) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO orders VALUES(?, ?, now(), now(), ?)";
        return CrudUtil.execute(sql, order.getOrderId(),order.getCustId(),order.getAmount());
    }

    public static double getTotalAmount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(Amount) FROM orders";

        ResultSet result = CrudUtil.execute(sql);

        if(result.next()){
            return result.getDouble(1);
        }
        return 0;
    }
    public static int orderCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) from orders";
        ResultSet result = CrudUtil.execute(sql);

        if (result.next())
            return result.getInt(1);
        return 0;
    }
    public static String generateNextOrderId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT ID FROM orders ORDER BY ID DESC LIMIT 1";
        ResultSet result = CrudUtil.execute(sql);

        if (result.next()) {
            return generateNextOrderId((result.getString(1)));
        }
        return generateNextOrderId((result.getString(null)));
    }

    private static String generateNextOrderId(String currentOrderId) {
        if (currentOrderId != null) {
            String[] split = currentOrderId.split("O-");
            int id = Integer.parseInt(split[1]);
            id += 1;
            return "O-" + id;
        }
        return "O-001";

    }
}
