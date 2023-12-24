package com.simbirsoft.configurations;

import com.simbirsoft.services.reports.ReportGenerator;
import com.simbirsoft.services.reports.xlsx.ProductReportDataProvider;
import com.simbirsoft.services.reports.xlsx.XlsxHelper;
import com.simbirsoft.services.reports.xlsx.XlsxTableReportGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Configuration
public class ReportConfiguration {
    @Value("${report.xlsx.products.dateFormat}")
    private String dateFormat;
    @Value("${report.xlsx.products.header.isBold}")
    private boolean headerIsBold;
    @Value("${report.xlsx.products.header.fontHeight}")
    private int headerFontHeight;
    @Value("${report.xlsx.products.header.isBorder}")
    private boolean headerIsBorder;
    @Value("${report.xlsx.products.data.isBold}")
    private boolean dataIsBold;
    @Value("${report.xlsx.products.data.fontHeight}")
    private int dataFontHeight;
    @Value("${report.xlsx.products.data.isBorder}")
    private boolean dataIsBorder;
    @Bean
    public ReportGenerator<?> xlsxProductsReportGenerator(XlsxHelper xlsxHelper, ProductReportDataProvider provider) {
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendPattern(dateFormat)
                .toFormatter();
        XlsxTableReportGenerator xlsxTableReportGenerator = new XlsxTableReportGenerator(xlsxHelper, dateTimeFormatter);
        xlsxTableReportGenerator.configHeaderCellStyle(headerIsBold, headerFontHeight, headerIsBorder);
        xlsxTableReportGenerator.configDataCellStyle(dataIsBold, dataFontHeight, dataIsBorder);
        xlsxTableReportGenerator.setReportDataProvider(provider);
        return xlsxTableReportGenerator;
    }
}
