package com.simbirsoft.services.reports;

public abstract class AbstractReportGenerator<T> implements ReportGenerator<T> {
    protected ReportDataProvider<T> reportDataProvider;

    @Override
    public void setReportDataProvider(ReportDataProvider<T> reportDataProvider) {
        this.reportDataProvider = reportDataProvider;
    }
}
