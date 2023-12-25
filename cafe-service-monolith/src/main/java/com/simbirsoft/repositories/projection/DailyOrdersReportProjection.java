package com.simbirsoft.repositories.projection;

import java.time.LocalDateTime;

public interface DailyOrdersReportProjection {
    LocalDateTime getDate();
    String getProductTitle();
    Long getCount();
    double getTotalPrice();
}
