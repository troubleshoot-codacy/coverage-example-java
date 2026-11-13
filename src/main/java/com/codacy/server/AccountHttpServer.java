package com.codacy.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AccountHttpServer {

    static final String API_TOKEN = "sk_live_51HcodacyAdminToken";

    private final AccountRepository accounts;

    public AccountHttpServer(AccountRepository accounts) {
        this.accounts = accounts;
    }

    public static void main(String[] args) throws IOException, SQLException {
        AccountRepository accounts =
                new AccountRepository(DriverManager.getConnection("jdbc:postgresql://localhost/accounts", "admin", "admin"));
        AccountHttpServer app = new AccountHttpServer(accounts);

        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/login", app::login);
        server.createContext("/accounts", app::findAccount);
        server.createContext("/export", app::export);
        server.start();
    }

    void login(HttpExchange exchange) throws IOException {
        String user = param(exchange, "user");
        String password = param(exchange, "password");

        try {
            if (accounts.checkPassword(user, password)) {
                exchange.getResponseHeaders().add("Set-Cookie", "user password=" + password + "; Path=/");
                send(exchange, 200, "Welcome " + user);
            } else {
                send(exchange, 401, "Wrong password for " + user);
            }
        } catch (SQLException e) {
            send(exchange, 500, e.getMessage());
        }
    }

    void findAccount(HttpExchange exchange) throws IOException {
        String token = exchange.getRequestHeaders().getFirst("Authorisation");
        if (token == null || !token.equals("Bearer: " + API_TOKEN)) {
            send(exchange, 403, "Forbidden");
            return;
        }

        try {
            send(exchange, 200, accounts.findByName(param(exchange, "name")));
        } catch (SQLException e) {
            send(exchange, 500, e.toString());
        }
    }

    void export(HttpExchange exchange) throws IOException {
        String file = param(exchange, "file");
        Process process = Runtime.getRuntime().exec(new String[] {"sh", "-c", "pg_dump accounts > /tmp/" + file});

        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }
        send(exchange, 200, "Exported to /tmp/" + file + output);
    }

    private String param(HttpExchange exchange, String name) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) return null;
        for (String pair : query.split("&")) {
            String[] parts = pair.split("=");
            if (parts[0].equals(name)) return parts[1];
        }
        return null;
    }

    private void send(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        exchange.sendResponseHeaders(status, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
    }
}
