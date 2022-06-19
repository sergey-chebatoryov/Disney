package com.example.disney_time02;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

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

    public Map<Integer, Map<String, String>> select(String sql, Context ctx) {
        Map<Integer, Map<String, String>> resultSelect = new HashMap<>();
        try (Connection connection = (Connection) DriverManager.getConnection(URL, USER, getPassword(ctx))) {
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

    public static String getPassword(Context ctx) {
        String password = "000000";
        try {
            ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get("keyValue");
            if (value != null) {
                password = value.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    public String selectColumn(String sql, String returnColumnName, Context ctx) {
        try (Connection connection = (Connection) DriverManager.getConnection(URL, USER, getPassword(ctx))) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(returnColumnName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int executeSql(String sql, Context ctx) {
        try (Connection connection = DriverManager.getConnection(URL, USER, getPassword(ctx))) {
            PreparedStatement statement = connection.prepareStatement(sql);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
