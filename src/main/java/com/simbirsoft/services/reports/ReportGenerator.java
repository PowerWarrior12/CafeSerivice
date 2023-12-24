package com.simbirsoft.services.reports;

import java.io.IOException;

public interface ReportGenerator<T> {
    byte[] generate() throws IOException;
    boolean match(ReportType type);
    void setReportDataProvider(ReportDataProvider<T> provider);
}
