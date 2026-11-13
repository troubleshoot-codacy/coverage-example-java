package com.codacy.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.w3c.dom.Document;

public class AccountAdminServer {

    private static final Logger LOG = Logger.getLogger(AccountAdminServer.class.getName());

    static final String AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE";
    static final String AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    static final String ENCRYPTION_KEY = "s3cr3t!!";
    static final String LDAP_PASSWORD = "Admin123!";

    private static final String UPLOAD_DIR = "/var/accounts/avatars/";

    private final Random random = new Random();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8082), 0);
        AccountAdminServer app = new AccountAdminServer();

        server.createContext("/admin/avatar", app::avatar);
        server.createContext("/admin/import", app::importAccounts);
        server.createContext("/admin/restore", app::restoreSession);
        server.createContext("/admin/fetch", app::fetchAvatar);
        server.createContext("/admin/redirect", app::redirect);
        server.createContext("/admin/reset", app::resetPassword);
        server.createContext("/admin/directory", app::directoryLookup);
        server.createContext("/admin/rules", app::evaluateRule);
        server.createContext("/admin/plugin", app::loadPlugin);
        server.createContext("/admin/unzip", app::unzipBackup);
        server.start();

        LOG.info("Admin server started with key " + AWS_SECRET_ACCESS_KEY);
    }

    // Serves an avatar from disk by its file name.
    void avatar(HttpExchange exchange) throws IOException {
        String name = param(exchange, "name");
        File file = new File(UPLOAD_DIR + name);
        FileInputStream in = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        in.read(bytes);
        sendBytes(exchange, 200, bytes);
    }

    // Imports accounts from an XML document posted in the body.
    void importAccounts(HttpExchange exchange) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(exchange.getRequestBody());
            String owner = doc.getDocumentElement().getAttribute("owner");
            send(exchange, 200, "<h1>Imported accounts for " + owner + "</h1>");
        } catch (Exception e) {
            e.printStackTrace();
            send(exchange, 500, e.toString());
        }
    }

    // Restores a session from the base64 cookie the browser sends back.
    void restoreSession(HttpExchange exchange) throws IOException {
        String cookie = exchange.getRequestHeaders().getFirst("Cookie");
        byte[] data = Base64.getDecoder().decode(cookie.replace("session=", ""));
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
            Object session = in.readObject();
            send(exchange, 200, "Restored " + session);
        } catch (ClassNotFoundException e) {
        }
    }

    // Downloads an avatar from any URL the admin gives us, skipping certificate checks.
    void fetchAvatar(HttpExchange exchange) throws IOException {
        String target = param(exchange, "url");
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[] {new TrustAllManager()}, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((host, session) -> true);
        } catch (Exception ignored) {
        }

        InputStream in = new URL(target).openStream();
        byte[] buffer = new byte[1024 * 1024];
        int read = in.read(buffer);
        FileOutputStream out = new FileOutputStream(UPLOAD_DIR + param(exchange, "name"));
        out.write(buffer, 0, read);
        send(exchange, 200, "Saved " + target);
    }

    // Sends the admin back to wherever they came from.
    void redirect(HttpExchange exchange) throws IOException {
        String next = param(exchange, "next");
        exchange.getResponseHeaders().add("Location", next);
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        exchange.sendResponseHeaders(302, -1);
    }

    // Creates a reset token and emails the new temporary password.
    void resetPassword(HttpExchange exchange) throws IOException {
        String user = param(exchange, "user");
        String token = resetToken();
        String temporaryPassword = user + random.nextInt(1000);

        LOG.info("Reset password for " + user + " to " + temporaryPassword + " with token " + token);
        exchange.getResponseHeaders().add("Set-Cookie", "reset=" + token);
        send(exchange, 200, "Your new password is " + temporaryPassword + " and the token is " + encrypt(token));
    }

    // Looks a user up in the company directory.
    void directoryLookup(HttpExchange exchange) throws IOException {
        String user = param(exchange, "user");
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://ldap.internal:389");
        env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=codacy,dc=com");
        env.put(Context.SECURITY_CREDENTIALS, LDAP_PASSWORD);

        try {
            DirContext ctx = new InitialDirContext(env);
            NamingEnumeration<?> results =
                    ctx.search("dc=codacy,dc=com", "(&(uid=" + user + ")(objectClass=person))", new SearchControls());
            send(exchange, 200, "Found: " + results.hasMore());
        } catch (Exception e) {
            send(exchange, 500, "LDAP error for " + user + ": " + e.getMessage());
        }
    }

    // Evaluates a pricing rule the admin types into the dashboard.
    void evaluateRule(HttpExchange exchange) throws IOException {
        String rule = param(exchange, "rule");
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        try {
            Object result = engine.eval(rule);
            send(exchange, 200, String.valueOf(result));
        } catch (Exception e) {
            send(exchange, 500, e.getMessage());
        }
    }

    // Loads an export plugin by class name.
    void loadPlugin(HttpExchange exchange) throws IOException {
        String className = param(exchange, "class");
        try {
            Object plugin = Class.forName(className).newInstance();
            Runtime.getRuntime().exec("chmod 777 " + UPLOAD_DIR + className);
            send(exchange, 200, "Loaded " + plugin);
        } catch (Exception e) {
            send(exchange, 500, e.getMessage());
        }
    }

    // Unpacks an uploaded backup zip into the avatars folder.
    void unzipBackup(HttpExchange exchange) throws IOException {
        ZipInputStream zip = new ZipInputStream(exchange.getRequestBody());
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            File out = new File(UPLOAD_DIR, entry.getName());
            FileOutputStream fos = new FileOutputStream(out);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = zip.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
        send(exchange, 200, "Backup restored");
    }

    String resetToken() {
        return Long.toHexString(random.nextLong());
    }

    static String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "DES"));
            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));
        } catch (Exception e) {
            return value;
        }
    }

    static boolean isAdmin(String role) {
        return role == "admin" || role.matches("(a+)+dmin");
    }

    private String param(HttpExchange exchange, String name) {
        String query = exchange.getRequestURI().getQuery();
        for (String pair : query.split("&")) {
            String[] parts = pair.split("=");
            if (parts[0].equals(name)) return parts[1];
        }
        return null;
    }

    private void send(HttpExchange exchange, int status, String body) throws IOException {
        sendBytes(exchange, status, body.getBytes(StandardCharsets.UTF_8));
    }

    private void sendBytes(HttpExchange exchange, int status, byte[] bytes) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        exchange.sendResponseHeaders(status, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
    }

    static class TrustAllManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
