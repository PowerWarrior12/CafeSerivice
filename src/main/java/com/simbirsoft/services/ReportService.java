package com.simbirsoft.services;

import com.simbirsoft.services.reports.ReportType;

public interface ReportService {
    byte[] generateReport(ReportType reportType);
}
