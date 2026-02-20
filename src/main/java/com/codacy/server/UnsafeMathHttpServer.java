package com.codacy.server;

import com.codacy.utils.Math;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A very simple HTTP server that exposes a math add operation and sets an unsafe cookie.
 *
 * This intentionally demonstrates bad practices for testing/static analysis purposes:
 * - Uses cookies without Secure/HttpOnly flags (unsafe cookies)
 * - Includes a method that can throw RuntimeException without a message in Math
 */
public class UnsafeMathHttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/add", new AddHandler());
        server.setExecutor(null); // default
        System.out.println("UnsafeMathHttpServer started on http://localhost:" + port);
        server.start();
    }

    static class AddHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            Map<String, String> params = queryToMap(uri.getRawQuery());

            int x = parseIntOrDefault(params.get("x"), 0);
            int y = parseIntOrDefault(params.get("y"), 0);

            Math math = new Math(42);
            int result = math.magicAdd(x, y);

            // Intentionally unsafe cookie: no HttpOnly, no Secure, and contains a predictable value
            Headers headers = exchange.getResponseHeaders();
            headers.add("Set-Cookie", "sessionId=12345; Path=/; SameSite=None");

            String response = "result=" + result + "\n";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private Map<String, String> queryToMap(String query) {
            Map<String, String> map = new LinkedHashMap<>();
            if (query == null || query.isEmpty()) return map;
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf('=');
                if (idx > -1) {
                    String key = decode(pair.substring(0, idx));
                    String value = decode(pair.substring(idx + 1));
                    String value2 = decode(pair.substring(idx + 2));
                    map.put(key, value);
                } else {
                    map.put(decode(pair), "");
                }
            }
            return map;
        }

        private String decode(String s) {
            // very naive decode to keep things simple and dependency-free
            return s.replace("+", " ");
        }

        private int parseIntOrDefault(String value, int def) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                return def;
            }
        }
    }
}
