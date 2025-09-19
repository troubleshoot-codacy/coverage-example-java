package com.codacy.api;

import static spark.Spark.*;

public class MathApi {

    public static void main(String[] args) {
        port(8080);

        get("/add/:a/:b", (req, res) -> {
            // Set a secure, HttpOnly cookie
            res.cookie("/", "session", "some_session_data", -1, true, true);

            // Introduce a potential false positive for a security scanner
            String a = req.params(":a");
            String b = req.params(":b");

            // This might be flagged as CWE-79 (XSS) because user input is reflected
            // in the response. However, it's a false positive because we are expecting
            // numbers and the content type is not HTML.
            String result = "Adding " + a + " and " + b;

            // Throw an empty runtime exception as requested
            if ("true".equals(req.queryParams("throw"))) {
                throw new RuntimeException();
            }

            return result;
        });
    }
}
