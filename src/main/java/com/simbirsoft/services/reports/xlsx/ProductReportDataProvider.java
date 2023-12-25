package com.simbirsoft.services.reports.xlsx;

import com.simbirsoft.repositories.OrderRepository;
import com.simbirsoft.services.mappers.order.OrderMapper;
import com.simbirsoft.services.reports.ReportDataProvider;
import com.simbirsoft.services.reports.xlsx.data.XlsxTableReportData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductReportDataProvider implements ReportDataProvider<XlsxTableReportData> {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    @Value("${report.xlsx.products.header.values}")
    private List<Object> headerValues;

    @Override
    @Transactional
    public XlsxTableReportData provide() {
        log.info("Start provide product report data process");
        return orderMapper.dailyOrdersReportToXlsxTableReport(
                orderRepository.getDailyOrdersReport(),
                headerValues
        );
    }
}
