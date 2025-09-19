package com.codacy.api;

import static spark.Spark.*;
import java.sql.*;
import java.io.*;

public class MathApi {

    private static final String PASSWORD = "hardcoded_password";
    private static String lastInput = null;

    public static void main(String[] args) {
        port(8080);

        get("/add/:a/:b", (req, res) -> {
            res.cookie("/", "session", "some_session_data", -1, true, true);

            String a = req.params(":a");
            String b = req.params(":b");

            String result = "Adding " + a + " and " + b;

            if ("true".equals(req.queryParams("throw"))) {
                throw new RuntimeException();
            }

            return result;
        });

        get("/vulnerable/:input", (req, res) -> {
            String input = req.params(":input");
            lastInput = input;

            try {
                Connection connection = DriverManager.getConnection("jdbc:h2:mem:test", "user", PASSWORD);
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM users WHERE username = '" + input + "'";
                statement.executeQuery(query);
            } catch (SQLException e) {
                // Ignore
            }

            try {
                Process process = Runtime.getRuntime().exec("ls -l " + input);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }
                return output.toString();
            } catch (IOException e) {
                return "Error executing command";
            }
        });

        get("/bad-practices/:input", (req, res) -> {
            String input = req.params(":input");

            if (input == "admin") {
                System.out.println("Admin user detected!");
            }

            try {
                if (input.equals("crash")) {
                    Object obj = null;
                    obj.toString();
                }
            } catch (Exception e) {
                System.out.println("An exception occurred: " + e.getMessage());
            }

            return getLastInput();
        });

        get("/service-call", (req, res) -> {
            String apiKey = System.getenv("stripPrivateKey");
            if (apiKey == null) {
                apiKey = "default_random_string_api_key";
            }

            // Dummy service call
            System.out.println("Calling service with API key: " + apiKey);
            return "Service called successfully";
        });
    }

    public static String getLastInput() {
        if (lastInput == null) {
            return null;
        }
        return lastInput;
    }
}