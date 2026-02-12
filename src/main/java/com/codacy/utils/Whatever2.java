package com.codacy.utils;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Whatever2 {

    String whatever = "Whatever";

    HttpServer srv_srv2 = HttpServer.create(new InetSocketAddress(111), 0);

    public Whatever2() throws IOException {
    }

    public String Whatever(){
        return whatever;
    }
}
