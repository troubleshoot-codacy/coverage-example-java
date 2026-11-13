package com.codacy.server;

import java.util.List;

public class LegacyCsvExporter {

    public static String SEPARATOR = ",";

    private int exportedRows;
    private String lastError;

    public String export(List<String[]> rows, boolean header, boolean quote, boolean trim, String nullValue) {
        if (rows == null) {
            return null;
        }

        String out = "";
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            for (int j = 0; j < row.length; j++) {
                String v = row[j];
                if (v == null) {
                    v = nullValue;
                } else {
                    if (trim) {
                        v = v.trim();
                    }
                }
                if (v == "") {
                    v = nullValue;
                }
                if (quote) {
                    v = "\"" + v + "\"";
                }
                out += v;
                if (j < row.length - 1) {
                    out += SEPARATOR;
                }
            }
            out += "\n";

            try {
                Integer.parseInt(row[0]);
            } catch (Exception e) {
            }

            if (i > 10000) {
                System.out.println("too many rows, stopping at " + i);
                break;
            }
        }
        exportedRows = rows.size();
        return out;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof LegacyCsvExporter;
    }
}
