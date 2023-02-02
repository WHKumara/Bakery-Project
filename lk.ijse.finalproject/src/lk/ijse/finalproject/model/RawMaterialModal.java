package lk.ijse.finalproject.model;

import lk.ijse.finalproject.to.RawMaterial;
import lk.ijse.finalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RawMaterialModal {

    public static ArrayList<RawMaterial> getDetail() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM raw_material";
        ResultSet result = CrudUtil.execute(sql);

        ArrayList<RawMaterial> materialList = new ArrayList<>();

        while (result.next()) {
            materialList.add(new RawMaterial(
                    result.getString(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getString(4)
            ));
        }
        return materialList;
    }
    public static boolean addItem(RawMaterial material) throws SQLException, ClassNotFoundException {
        String sql ="INSERT INTO raw_material VALUES (?,?,?,?)";

        return CrudUtil.execute(sql,material.getMaterialId(),material.getType(),material.getQty(),material.getSupId());
    }

   public static boolean updateData(RawMaterial material) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE raw_material SET Qty=?,SupID=? WHERE MaterialID=?";

        return CrudUtil.execute(sql,material.getQty(),material.getSupId(),material.getMaterialId());
    }

    public static boolean deleteMateril(String materialId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM raw_material WHERE MaterialID=?";

        return CrudUtil.execute(sql,materialId);
    }

    public static String generateNextMterialId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT MaterialID FROM raw_material ORDER BY MaterialID DESC LIMIT 1";
        ResultSet result = CrudUtil.execute(sql);

        if (result.next()) {
            return generateNextMterialId((result.getString(1)));
        }
        return generateNextMterialId((result.getString(null)));
    }
    private static String generateNextMterialId(String currentOrderId) {
        if (currentOrderId != null) {
            String[] split = currentOrderId.split("R0");
            int id = Integer.parseInt(split[1]);
            id += 1;
            return "R0" + id;
        }
        return "R01";

    }

    public static int materialCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM raw_material";
        ResultSet resultSet = CrudUtil.execute(sql);

        if(resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }
}
