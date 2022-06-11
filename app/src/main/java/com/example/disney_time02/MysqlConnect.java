package com.example.disney_time02;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MysqlConnect extends AppCompatActivity {
    private static final String URL = "jdbc:mysql://sql6.freemysqlhosting.net:3306/sql6498100";
    private static final String USER = "sql6498100";
    private static final String PASSWORD = "jN3sV9fjuu";
    private Map<Integer, Map<String, String>> info;
    private String sql;

    public void start(String sql) {
        this.sql = sql;
        info = new HashMap<>();
        Thread threadExecution = new SampleThread();
        threadExecution.start();
        try {
            threadExecution.join();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Read from mysql thread interrupted. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void executeQuery() {
        try (Connection connection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            int i = 1;
            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", resultSet.getString("id"));
                row.put("name", resultSet.getString("name"));
                row.put("genre", resultSet.getString("genre"));
                row.put("trailer", resultSet.getString("trailer"));
                info.put(i++, row);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Connection to mysql database failed. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public Map<Integer, Map<String, String>> getResult() {
        return info;
    }

    class SampleThread extends Thread {
        SampleThread() {
        }

        @Override
        public void run() {
            executeQuery();
        }
    }
}


