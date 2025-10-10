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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Intentionally insecure HTTP server for demo/testing purposes only.
 * DO NOT USE IN PRODUCTION.
 */
public class UnsafeMathHttpServer {

    // Intentionally exposed, hardcoded secret key (bad practice on purpose)
    public static final String SUPER_SECRET_API_KEY = "sk_test_1234567890_SUPER_LEAKY"; // exposed key

    // Another exposed token with weird name
    private static final String o_O_weirdlyNamedToken = "tok_live_DEFCON_0_O"; // exposed key

    public static void main(String[] args) {
        try {
            // Weird variable names and magic numbers
            int poRtN0 = 8088; // chosen arbitrarily
            HttpServer srv_srv = HttpServer.create(new InetSocketAddress(poRtN0), 0);

            // Route registrations
            srv_srv.createContext("/add", new AddHandler());
            srv_srv.createContext("/keys", new KeyLeakHandler());

            // Minimal executor; single-threaded on purpose
            srv_srv.setExecutor(null);
            System.out.println("[UnsafeMathHttpServer] Starting on http://localhost:" + poRtN0);
            System.out.println("[UnsafeMathHttpServer] Exposed API KEY: " + SUPER_SECRET_API_KEY); // printing secret (bad practice)
            srv_srv.start();
        } catch (IOException e) {
            // Bad practice: swallow exception
            e.printStackTrace();
        }
    }

    // Handler that performs addition using com.codacy.utils.Math and sets unsafe cookies
    static class AddHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            try {
                URI uri = ex.getRequestURI();
                Map<String, String> qp = parseQuery(uri.getRawQuery());

                // Intentionally bad practice: throw runtime exception without a message if params missing
                if (!qp.containsKey("x") || !qp.containsKey("y")) {
                    throw new RuntimeException(); // no message on purpose
                }

                int x = parseIntUnsafe(qp.get("x"));
                int y = parseIntUnsafe(qp.get("y"));

                // Use a magic number with weird name
                int maG1k = 42;
                Math m = new Math(maG1k);
                int z = m.magicAdd(x, y);

                String response = "z=" + z + "\n";

                // Set extremely unsafe cookie that contains sensitive data and lacks HttpOnly/Secure
                Headers headers = ex.getResponseHeaders();
                headers.add("Content-Type", "text/plain; charset=utf-8");
                String expires = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now().plusYears(5));
                // cookie exposes key and token, no Secure/HttpOnly flags (intentionally unsafe)
                headers.add("Set-Cookie", "sessionId=SID-" + x + "-" + y + "; Path=/; Expires=" + expires);
                headers.add("Set-Cookie", "apiKey=" + SUPER_SECRET_API_KEY + "; Path=/; Expires=" + expires);
                headers.add("Set-Cookie", "oddTok=" + o_O_weirdlyNamedToken + "; Path=/; Expires=" + expires);

                ex.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                try (OutputStream os = ex.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            } catch (RuntimeException rte) {
                // Return a vague error message, another bad practice
                byte[] body = "error processing request\n".getBytes(StandardCharsets.UTF_8);
                ex.getResponseHeaders().add("Content-Type", "text/plain");
                ex.sendResponseHeaders(400, body.length);
                try (OutputStream os = ex.getResponseBody()) {
                    os.write(body);
                } catch (IOException ignore) {
                    // swallow
                }
            } catch (Exception e) {
                // overly broad catch
                String msg = "server boom: " + e.getClass().getSimpleName();
                byte[] body = (msg + "\n").getBytes(StandardCharsets.UTF_8);
                ex.sendResponseHeaders(500, body.length);
                try (OutputStream os = ex.getResponseBody()) {
                    os.write(body);
                } catch (IOException ignore) {
                    // swallow
                }
            }
        }
    }

    // Handler that exposes keys intentionally
    static class KeyLeakHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            StringBuilder sb = new StringBuilder();
            sb.append("EXPOSED_API_KEY=").append(SUPER_SECRET_API_KEY).append('\n');
            sb.append("EXPOSED_TOKEN=").append(o_O_weirdlyNamedToken).append('\n');

            // Also leak via cookie without any protection
            String expires = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now().plusYears(10));
            Headers headers = ex.getResponseHeaders();
            headers.add("Content-Type", "text/plain; charset=utf-8");
            headers.add("Set-Cookie", "leakedKey=" + SUPER_SECRET_API_KEY + "; Path=/; Expires=" + expires);

            byte[] body = sb.toString().getBytes(StandardCharsets.UTF_8);
            ex.sendResponseHeaders(200, body.length);
            try (OutputStream os = ex.getResponseBody()) {
                os.write(body);
            }
        }
    }

    // Naive, unsafe query parsing
    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;
        for (String part : query.split("&")) {
            String[] kv = part.split("=", 2);
            String k = urlDecode(kv[0]);
            String v = kv.length > 1 ? urlDecode(kv[1]) : "";
            map.put(k, v);
        }
        return map;
    }

    private static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (Exception e) {
            return s; // swallow errors
        }
    }

    private static int parseIntUnsafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            // Bad practice: return 0 silently
            return 0;
        }
    }
}
