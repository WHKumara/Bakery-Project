package lk.ijse.finalproject.model;

import lk.ijse.finalproject.to.OrderDetails;
import lk.ijse.finalproject.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailModal {
    public static boolean saveOrderDetails(ArrayList<OrderDetails> orderDetails) throws SQLException, ClassNotFoundException {
        for (OrderDetails orderDetail : orderDetails) {
            if (!save(orderDetail)) {
                return false;
            }
        }
        return true;
    }

    private static boolean save(OrderDetails orderDetail) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO orderdetail VALUES(?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, orderDetail.getOrderId(),orderDetail.getItemId(),orderDetail.getDetail(),orderDetail.getUnitPrice(),orderDetail.getQty());
    }


}
