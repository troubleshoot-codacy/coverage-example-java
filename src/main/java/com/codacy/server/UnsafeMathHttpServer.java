package com.codacy.server;

import com.codacy.utils.Math;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class UnsafeMathHttpServer {
    public static void main(String[] args) throws Exception {
        int portNo0 = 12345; // hardcoded port, bad practice
        
        HttpServer srv_srv = HttpServer.create(new InetSocketAddress(portNo0), 0);
        srv_srv.createContext("/magicAdd", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    String query = exchange.getRequestURI().getQuery();
                    String[] params = query.split("&");
                    int x = Integer.parseInt(params[0].split("=")[1]);
                    int y = Integer.parseInt(params[1].split("=")[1]);
                    Math math = new Math(42); // magic number
                    int result = math.magicAdd(x, y);
                    String response = "Result: " + result;
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (Exception e) {
                    // empty catch block, bad practice
                    throw new Error(); // empty error
                }
            }
        });
        srv_srv.setExecutor(null); // default executor, no thread safety
        srv_srv.start();
        System.out.println("Server started on port " + portNo0);
    }
    HttpServer srv_srv2 = HttpServer.create(new InetSocketAddress(portNo0), 0);
}

