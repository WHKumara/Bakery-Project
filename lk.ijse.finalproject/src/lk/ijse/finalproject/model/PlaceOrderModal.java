package lk.ijse.finalproject.model;

import lk.ijse.finalproject.db.DBConnection;
import lk.ijse.finalproject.to.Order;

import java.sql.SQLException;

public class PlaceOrderModal {

    public static boolean placeOrder(Order order) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getDBConnection().getConnection().setAutoCommit(false);
            boolean isOrderAdded = OrderModal.save(new Order(order.getOrderId(),order.getCustId(),null,null,order.getOrderDetails(),order.getAmount()));
            if (isOrderAdded) {
                boolean isUpdated = ItemModal.updateQty(order.getOrderDetails());
                if (isUpdated) {
                    boolean isOrderDetailAdded = OrderDetailModal.saveOrderDetails(order.getOrderDetails());
                    if (isOrderDetailAdded) {
                        DBConnection.getDBConnection().getConnection().commit();
                        return true;
                    }
                }
            }
            DBConnection.getDBConnection().getConnection().rollback();
            return false;
        } finally {
            DBConnection.getDBConnection().getConnection().setAutoCommit(true);
        }
    }
}
