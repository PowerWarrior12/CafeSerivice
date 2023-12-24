package com.simbirsoft.services.reports.xlsx;

import com.simbirsoft.services.reports.AbstractReportGenerator;
import com.simbirsoft.services.reports.ReportType;
import com.simbirsoft.services.reports.xlsx.data.DailyTableData;
import com.simbirsoft.services.reports.xlsx.data.XlsxTableReportData;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Setter
@Log4j2
public class XlsxTableReportGenerator extends AbstractReportGenerator<XlsxTableReportData> {
    private final XlsxHelper xlsxHelper;
    private final DateTimeFormatter dateTimeFormatter;
    private CellStyle headerCellStyle;
    private CellStyle dataCellStyle;
    private XSSFWorkbook workbook;

    public XlsxTableReportGenerator(XlsxHelper xlsxHelper, DateTimeFormatter dateTimeFormatter) {
        this.xlsxHelper = xlsxHelper;
        workbook = new XSSFWorkbook();
        headerCellStyle = workbook.createCellStyle();
        dataCellStyle = workbook.createCellStyle();
        this.dateTimeFormatter = dateTimeFormatter;
    }
    @Override
    public byte[] generate() throws IOException {
        XlsxTableReportData reportData = reportDataProvider.provide();
        List<DailyTableData> dailyDataList = reportData.getData();

        for (int sheetIndex = 0; sheetIndex < dailyDataList.size(); sheetIndex++) {
            DailyTableData dailyData = dailyDataList.get(sheetIndex);
            Sheet sheet = workbook.createSheet(dailyData.getDate().format(dateTimeFormatter));
            xlsxHelper.createRow(sheet, reportData.getTitles(), headerCellStyle, 0);
            for (int rowIndex = 0; rowIndex < dailyData.getParams().size(); rowIndex++) {
                // rowIndex + 1 because 0 row is header
                xlsxHelper.createRow(sheet, dailyData.getParams().get(rowIndex), dataCellStyle, rowIndex + 1);
            }
        }

        return convertWorkbookToByteArray();
    }

    @Override
    public boolean match(ReportType type) {
        return type == ReportType.PRODUCTS_BY_DAY_XLSX;
    }

    public void configHeaderCellStyle(boolean isBold, int fontHeight, boolean isBorder) {
        configCellStyle(headerCellStyle, isBold, fontHeight, isBorder);
    }

    public void configDataCellStyle(boolean isBold, int fontHeight, boolean isBorder) {
        configCellStyle(dataCellStyle, isBold, fontHeight, isBorder);
    }

    private void configCellStyle(CellStyle style, boolean isBold, int fontHeight, boolean isBorder) {
        XSSFFont font = workbook.createFont();
        font.setBold(isBold);
        font.setFontHeight(fontHeight);
        BorderStyle borderStyle = isBorder ? BorderStyle.MEDIUM : null;

        if (borderStyle != null) {
            style.setBorderBottom(borderStyle);
            style.setBorderLeft(borderStyle);
            style.setBorderRight(borderStyle);
            style.setBorderTop(borderStyle);
        }

        style.setFont(font);
    }

    private byte[] convertWorkbookToByteArray() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        clearWorkbook();
        return byteArrayOutputStream.toByteArray();
    }

    private void clearWorkbook() {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = numberOfSheets - 1; i >= 0; i--) {
            workbook.removeSheetAt(i);
        }
    }
}
