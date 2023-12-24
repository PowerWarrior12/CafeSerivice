package com.simbirsoft.services.reports.xlsx.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class XlsxTableReportData {
    private List<Object> titles;
    private List<DailyTableData> data;
}
