package com.simbirsoft.services.reports.xlsx.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DailyTableData {
    private LocalDateTime date;
    /**
     * List of parameters (quantity, total revenue, etc.)
     */
    private List<List<Object>> params;
}
