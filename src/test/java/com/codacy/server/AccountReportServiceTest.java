package com.codacy.server;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountReportServiceTest {
    @Test
    public void shouldBuildStableWeakEtags() {
        String etag = AccountReportService.etagFor("report".getBytes(StandardCharsets.UTF_8));
        assertEquals(etag, AccountReportService.etagFor("report".getBytes(StandardCharsets.UTF_8)));
        assertTrue(etag.startsWith("W/\""));
    }

    @Test
    public void shouldReadBundledTemplateColumns() throws Exception {
        assertEquals(Arrays.asList("name", "email", "balance"),
                AccountReportService.templateColumns(AccountReportService.ReportTable.ACCOUNTS));
        assertTrue(AccountReportService.templateColumns(AccountReportService.ReportTable.AUDIT_LOG).isEmpty());
    }

    @Test
    public void shouldCreateWarehouseTlsContext() throws Exception {
        assertEquals("TLS", AccountReportService.warehouseTlsContext().getProtocol());
    }
}
