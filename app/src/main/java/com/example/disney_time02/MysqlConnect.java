package com.example.disney_time02;

import com.mysql.jdbc.Connection;

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
    private String sql;
    private Map<Integer, Map<String, String>> resultSelect;
    private int resultInsert;
    private String resultPassword;

    protected enum SqlMode {
        selectMovie, selectUser, insert
    }

    public void select(String sql) {
        this.sql = sql;
        resultSelect = new HashMap<>();
        Thread threadExecution = new SampleThread(SqlMode.selectMovie);
        threadExecution.start();
        try {
            threadExecution.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void selectUser(String sql) {
        this.sql = sql;
        resultSelect = new HashMap<>();
        Thread threadExecution = new SampleThread(SqlMode.selectUser);
        threadExecution.start();
        try {
            threadExecution.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insert(String sql) {
        this.sql = sql;
        resultSelect = new HashMap<>();
        Thread threadExecution = new SampleThread(SqlMode.insert);
        threadExecution.start();
        try {
            threadExecution.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void executeSelect() {
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
    }

    protected void executeSelectUser() {
        resultPassword = null;
        try (Connection connection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                resultPassword = resultSet.getString("password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void executeInsert() {
        resultInsert = 0;
        try (Connection connection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            resultInsert = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Map<String, String>> getResultSelect() {
        return resultSelect;
    }

    public String getResultSelectUser() {
        return resultPassword;
    }

    public int getResultInsert() {
        return resultInsert;
    }

    class SampleThread extends Thread {
        private final SqlMode sqlMode;

        SampleThread(SqlMode sqlMode) {
            this.sqlMode = sqlMode;
        }

        @Override
        public void run() {
            switch (sqlMode) {
                case selectMovie:
                    executeSelect();
                    break;
                case selectUser:
                    executeSelectUser();
                    break;
                case insert:
                    executeInsert();
                    break;
                default:
                    break;
            }
        }
    }
}


