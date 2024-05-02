package bd3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkerTransferManager {
    public static void main(String[] args) {
        // Database connection parameters
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=qwer;trustServerCertificate=true";
        String username = "sa1";
        String password = "lhagva";

        try (Connection con = DriverManager.getConnection(connectionUrl, username, password)) {
            System.out.println("Connected successfully!");

            // Fetch workers from UBworkers table
            List<Worker> workersToMove = fetchWorkers(con, "UBworkers");

            // Process workers to move
            List<Worker> workersToRemove = new ArrayList<>();
            for (Worker worker : workersToMove) {
                moveWorkerToDarhan(worker, con);  // Move worker to DarhanWorkers
                workersToRemove.add(worker);
            }

            // Remove moved workers from UBworkers table
            if (!workersToRemove.isEmpty()) {
                removeWorkers(con, "UBworkers", workersToRemove);
            }

            // Fetch workers from DAworkers table
            List<Worker> workersToMoveBack = fetchWorkers(con, "DAworkers");

            // Process workers to move back to UBworkers
            List<Worker> workersToAddBack = new ArrayList<>();
            for (Worker worker : workersToMoveBack) {
                int yearsOfWork = calculateYearsOfWork(worker.getDate());
                if (yearsOfWork > 2) {
                    moveWorkerToUlaanbaatar(worker, con);  // Move worker back to UBworkers
                    workersToAddBack.add(worker);
                }
            }

            // Remove moved workers from DAworkers table
            if (!workersToAddBack.isEmpty()) {
                removeWorkers(con, "DAworkers", workersToAddBack);
            }

        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    // Method to fetch workers from a specific table
    public static List<Worker> fetchWorkers(Connection con, String tableName) throws SQLException {
        List<Worker> workers = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;
        try (PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String work = resultSet.getString("work");
                String date = resultSet.getString("date");
                workers.add(new Worker(name, id, work, date));
            }
        }
        return workers;
    }

    // Method to move worker to DarhanWorkers table
    public static void moveWorkerToDarhan(Worker worker, Connection con) throws SQLException {
        String insertQuery = "INSERT INTO DAworkers (id, name, work, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {
            insertStatement.setString(1, worker.getId());
            insertStatement.setString(2, worker.getName());
            insertStatement.setString(3, worker.getWork());
            insertStatement.setString(4, worker.getDate());
            insertStatement.executeUpdate();
        }
    }

    // Method to move worker back to UBworkers table
    public static void moveWorkerToUlaanbaatar(Worker worker, Connection con) throws SQLException {
        String insertQuery = "INSERT INTO UBworkers (id, name, work, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {
            insertStatement.setString(1, worker.getId());
            insertStatement.setString(2, worker.getName());
            insertStatement.setString(3, worker.getWork());
            insertStatement.setString(4, worker.getDate());
            insertStatement.executeUpdate();
        }
    }

    // Method to remove workers from a specific table
    public static void removeWorkers(Connection con, String tableName, List<Worker> workersToRemove) throws SQLException {
        String deleteQuery = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement deleteStatement = con.prepareStatement(deleteQuery)) {
            for (Worker worker : workersToRemove) {
                deleteStatement.setString(1, worker.getId());
                deleteStatement.addBatch();
            }
            deleteStatement.executeBatch();
        }
    }

    // Method to calculate years of work based on start date
    public static int calculateYearsOfWork(String startDate) {
        int startYear = Integer.parseInt(startDate.substring(0, 4));
        int currentYear = java.time.Year.now().getValue();
        return currentYear - startYear;
    }
}
