package com.simbirsoft.services.reports.xlsx;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XlsxHelper {
    public Row createRow(Sheet sheet, List<?> dataList, CellStyle style, int rowCount) {
        Row row = sheet.createRow(rowCount);
        for (int i = 0; i < dataList.size(); i++) {
            createCell(sheet, i, dataList.get(i), style, row);
        }
        return row;
    }
    public Cell createCell(Sheet sheet, int columnCount, Object value, CellStyle style, Row row) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
        return cell;
    }
}
