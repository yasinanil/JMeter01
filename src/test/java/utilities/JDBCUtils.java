package utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtils {

    private static Connection connection;
    private static Statement statement;

    // Static block to initialize connection and statement when the class is loaded
    static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://89.252.146.3:5432/qa-ecommerce-app", "qa-readonly", "QBTvdbiK2A9zMUu");
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to close the database connection and statement
    public static void closeConnection() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to execute a given SQL query and return a boolean result
    public static boolean execute(String query) {
        try {
            return statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to execute an update (e.g., INSERT, UPDATE, DELETE) and return the number of affected rows
    public static int executeUpdate(String query) {
        try {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to retrieve a specific column from a table and return the values as a list
    public static List<Object> getColumnAsList(String tableName, String columnName) {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT " + columnName + " FROM " + tableName);
            List<Object> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getObject(1));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to print the entire contents of a table to the console
    public static void printTable(String table) {
        String query = "SELECT * FROM " + table;
        try {
            ResultSet resultSet = statement.executeQuery(query);
            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getObject(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getRecordByEmail(String email) {

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE email = '" + email + "'");
            resultSet.next();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
