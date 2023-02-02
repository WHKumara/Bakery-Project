package lk.ijse.finalproject.model;

import lk.ijse.finalproject.to.Employee;
import lk.ijse.finalproject.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModal {

    public static ArrayList<Employee> getDetails() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM employee";
        ResultSet resultSet = CrudUtil.execute(sql);

        ArrayList<Employee> employeeList =new ArrayList<>();

        while (resultSet.next()){
            employeeList.add(new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return employeeList;
    }

    public  static boolean addEmployee(Employee emp) throws SQLException, ClassNotFoundException {
        String sql ="INSERT INTO employee VALUES (?,?,?,?,?)";

        return CrudUtil.execute(sql,emp.getEmpID(),emp.getEmpName(),emp.getEmpContact(),emp.getEmpAddress()
                ,emp.getEmpEmail());
    }

    public static boolean updateEmployee(Employee emp) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE employee SET EmpName=?,EmpContact=?,EmpAddress=? WHERE EmpID=?";

        return CrudUtil.execute(sql,emp.getEmpName(),emp.getEmpContact(),emp.getEmpAddress(),emp.getEmpID());
    }

    public static boolean deleteEmployee(String empId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM employee WHERE EmpID=?";

        return CrudUtil.execute(sql,empId);
    }
    public static String generateNextEmployeeId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT EmpID FROM employee ORDER BY EmpID DESC LIMIT 1";
        ResultSet result = CrudUtil.execute(sql);

        if (result.next()) {
            return generateNextEmployeeId((result.getString(1)));
        }
        return generateNextEmployeeId((result.getString(null)));
    }

    private static String generateNextEmployeeId(String currentOrderId) {
        if (currentOrderId != null) {
            String[] split = currentOrderId.split("E0");
            int id = Integer.parseInt(split[1]);
            id += 1;
            return "E0" + id;
        }
        return "E01";

    }
}
