package com.example.disney_time02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MysqlConnect {
    private static final String URL = "jdbc:mysql://remotemysql.com:3306/tXlqRJzGk9";
    private static final String USER = "tXlqRJzGk9";
    private static final String PASSWORD = "fe045fDKSp";

    public Map<Integer, Map<String, String>> select(String sql) {
        Map<Integer, Map<String, String>> resultSelect = new HashMap<>();
        try (Connection connection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", resultSet.getString("id"));
                row.put("name", resultSet.getString("name"));
                row.put("genre", resultSet.getString("genre"));
                row.put("trailer", resultSet.getString("trailer"));
                resultSelect.put(i++, row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSelect;
    }

    public String selectUser(String sql) {
        try (Connection connection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insert(String sql) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
