package bd3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectURL {
    public static void main(String[] args) {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=qwer;trustServerCertificate=true";
        String username = "sa1";
        String password = "lhagva";

        try (Connection con = DriverManager.getConnection(connectionUrl, username, password)) {
            System.out.println("Connected successfully!");
            String query = "SELECT * FROM UBworkers";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            List<Employee> employeesToMove = new ArrayList<>();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name1 = resultSet.getString("name");
                String work = resultSet.getString("work");
                String date = resultSet.getString("date");
                Employee employee = new Employee(name1, id, work, date);
                employeesToMove.add(employee);
            }

            // Process employees to move
            List<Employee> employeesToRemove = new ArrayList<>();
            for (Employee emp : employeesToMove) {
                int yearsOfWork = calculateYearsOfWork(emp.getDate());
                if (yearsOfWork > 2) {
                    moveEmployeeToDAWorkers(emp, con);
                    employeesToRemove.add(emp);
                }
            }

            // Remove moved employees from UBworkers table
            if (!employeesToRemove.isEmpty()) {
                removeEmployeesFromUBWorkers(employeesToRemove, con);
            }

            resultSet.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public static int calculateYearsOfWork(String startDate) {
        int startYear = Integer.parseInt(startDate.substring(0, 4));
        int currentYear = java.time.Year.now().getValue();
        return currentYear - startYear;
    }

    public static void moveEmployeeToDAWorkers(Employee employee, Connection con) throws SQLException {
        String insertQuery = "INSERT INTO DAworkers (id, name, work, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {
            insertStatement.setString(1, employee.getId());
            insertStatement.setString(2, employee.getName());
            insertStatement.setString(3, employee.getWork());
            insertStatement.setString(4, employee.getDate());
            insertStatement.executeUpdate();
        }
    }

    public static void removeEmployeesFromUBWorkers(List<Employee> employeesToRemove, Connection con) throws SQLException {
        String deleteQuery = "DELETE FROM UBworkers WHERE id = ?";
        try (PreparedStatement deleteStatement = con.prepareStatement(deleteQuery)) {
            for (Employee emp : employeesToRemove) {
                deleteStatement.setString(1, emp.getId());
                deleteStatement.addBatch();
            }
            deleteStatement.executeBatch();
        }
    }
}
