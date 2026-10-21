package com.codacy.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountRepository {

    private final Connection connection;

    public AccountRepository(Connection connection) {
        this.connection = connection;
    }

    public boolean checkPassword(String user, String password) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT id FROM accounts WHERE name = '" + user + "' AND password_hash = '" + md5(password) + "'");
        return rs.next();
    }

    public String findByName(String name) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT name, email, balance FROM accounts WHERE name LIKE '%" + name + "%'");

        String result = "";
        while (rs.next()) {
            result += rs.getString("name") + " <" + rs.getString("email") + "> " + rs.getDouble("balance") + "\n";
        }
        return result;
    }

    private String md5(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(value.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return value;
        }
    }
}
