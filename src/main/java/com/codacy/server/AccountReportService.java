package com.codacy.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.net.ssl.SSLContext;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AccountReportService {

    /** Tables a report can be built from. SQL only ever sees these fixed names. */
    enum ReportTable {
        ACCOUNTS("accounts"),
        TRANSFERS("transfers"),
        AUDIT_LOG("audit_log");

        private final String tableName;

        ReportTable(String tableName) {
            this.tableName = tableName;
        }

        String tableName() {
            return tableName;
        }
    }

    // Cached for tables we already counted and found empty, so we don't ask the database again.
    private static final Object NO_REPORT = new Object();

    private static final int MAX_LATEST = 500;

    private final Connection connection;
    private final ConcurrentMap<ReportTable, Object> cache = new ConcurrentHashMap<>();

    public AccountReportService(Connection connection) {
        this.connection = connection;
    }

    public long countRows(ReportTable table) throws SQLException {
        // Table names can't be bound as parameters; the enum is the allow-list.
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM " + table.tableName())) {
            rs.next();
            return rs.getLong(1);
        }
    }

    public List<String> latestNames(int limit) throws SQLException {
        int safeLimit = Math.max(1, Math.min(limit, MAX_LATEST));
        List<String> names = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(
                     String.format("SELECT name FROM accounts ORDER BY created_at DESC LIMIT %d", safeLimit))) {
            while (rs.next()) {
                names.add(rs.getString(1));
            }
        }
        return names;
    }

    public String cachedSummary(ReportTable table) throws SQLException {
        Object cached = cache.get(table);
        if (cached == NO_REPORT) {
            return "";
        }
        if (cached != null) {
            return (String) cached;
        }

        long rows = countRows(table);
        if (rows == 0) {
            cache.put(table, NO_REPORT);
            return "";
        }
        String summary = table.tableName() + ": " + rows + " rows";
        cache.put(table, summary);
        return summary;
    }

    /** Weak HTTP ETag for an exported report, so browsers can revalidate with If-None-Match. */
    static String etagFor(byte[] body) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(body);
            StringBuilder hex = new StringBuilder("W/\"");
            for (byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            return hex.append('"').toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Every JVM ships MD5", e);
        }
    }

    /** Column layout for a report, read from the template bundled in the jar. */
    static List<String> templateColumns(ReportTable table) throws Exception {
        try (InputStream in = AccountReportService.class.getResourceAsStream(
                "/report-templates/" + table.tableName() + ".xml")) {
            if (in == null) {
                return Collections.emptyList();
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(in);

            NodeList nodes = doc.getElementsByTagName("column");
            List<String> columns = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                columns.add(((Element) nodes.item(i)).getAttribute("name"));
            }
            return columns;
        }
    }

    /** TLS for pushing reports to the warehouse. Default key and trust stores, so certificates are verified. */
    static SSLContext warehouseTlsContext() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);
        return context;
    }
}
