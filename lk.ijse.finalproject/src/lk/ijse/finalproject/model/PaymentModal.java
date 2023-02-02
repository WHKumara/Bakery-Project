package lk.ijse.finalproject.model;

import lk.ijse.finalproject.to.Payment;
import lk.ijse.finalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentModal {
    public static ArrayList<Payment> getDetail() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM payment";
        ResultSet result = CrudUtil.execute(sql);

        ArrayList<Payment> payList = new ArrayList<>();

        while (result.next()) {
            payList.add(new Payment(
                    result.getString(1),
                    result.getDouble(2),
                    result.getDate(3),
                    result.getTime(4),
                    result.getString(5)
            ));
        }
        return payList;
    }
    public static boolean addPayment(Payment payment) throws SQLException, ClassNotFoundException {
        String sql ="INSERT INTO payment VALUES (?,?,now(),now(),?)";

        return CrudUtil.execute(sql,payment.getPayId(),payment.getAmount(),payment.getSupId());
    }
    public static boolean updateData(Payment payment) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE payment SET PaymentAmount=?,SupID=? WHERE PaymentID=?";

        return CrudUtil.execute(sql,payment.getAmount(),payment.getSupId(),payment.getPayId());
    }

    public static String generateNextPaymentId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT PaymentID FROM payment ORDER BY PaymentID DESC LIMIT 1";
        ResultSet result = CrudUtil.execute(sql);

        if (result.next()) {
            return generateNextPaymentId((result.getString(1)));
        }
        return generateNextPaymentId((result.getString(null)));
    }

    private static String generateNextPaymentId(String currentOrderId) {
        if (currentOrderId != null) {
            //string ek 2kt wen krnw
            String[] split = currentOrderId.split("P-");
            int id = Integer.parseInt(split[1]);
            id += 1;
            return "P-" + id;
        }
        return "P-01";

    }

}
