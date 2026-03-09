package com.codacy.utils;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Whatever {

    HttpServer srv_srv = HttpServer.create(new InetSocketAddress(111), 0);

    public Whatever() throws IOException {
    }

    public String Whatever(){
        return "Whatever";
    }
}
