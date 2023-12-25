package com.simbirsoft.services.mappers.order;

import com.simbirsoft.domain.Order;
import com.simbirsoft.domain.enums.OrderStatus;
import com.simbirsoft.dto.order.CreateOrderRequest;
import com.simbirsoft.dto.order.CustomerOrderResponse;
import com.simbirsoft.dto.order.PageOrderResponse;
import com.simbirsoft.repositories.projection.DailyOrdersReportProjection;
import com.simbirsoft.services.reports.xlsx.data.DailyTableData;
import com.simbirsoft.services.reports.xlsx.data.XlsxTableReportData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;
    public CustomerOrderResponse orderToCustomerOrderResponse(Order order) {
        return new CustomerOrderResponse(
                order.getCode(),
                order.getDate(),
                order.getStatus(),
                order.getComment(),
                order.getOrderItemSet()
                        .stream()
                        .map(orderItemMapper::orderItemToOrderItemResponse)
                        .toList()
        );
    }

    public Order createOrderRequestToOrder(CreateOrderRequest request, int customerId) {
        Order order = new Order();
        UUID orderCode = UUID.randomUUID();
        order.setCode(orderCode);
        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.RECEIVED);
        order.setCustomerId(customerId);
        return order;
    }
    public PageOrderResponse pageOrderToPageOrderResponse(Page<Order> page) {
        return new PageOrderResponse(
                page.getTotalPages(),
                page.hasNext(),
                page.stream()
                        .map(this::orderToCustomerOrderResponse)
                        .toList()
        );
    }

    public XlsxTableReportData dailyOrdersReportToXlsxTableReport(List<DailyOrdersReportProjection> dailyOrdersReport, List<Object> headerValues) {
        Map<LocalDateTime, List<DailyOrdersReportProjection>> groupReportResult =  dailyOrdersReport.stream()
                .collect(Collectors.groupingBy(DailyOrdersReportProjection::getDate));

        List<DailyTableData> dailyTableDataList = groupReportResult.entrySet().stream()
                .map((entry) -> new DailyTableData(
                        entry.getKey(),
                        entry.getValue().stream().map(this::dailyOrdersReportProjectionToPropertyList).toList()
                ))
                .toList();

        return new XlsxTableReportData(
                headerValues,
                dailyTableDataList
        );
    }

    private List<Object> dailyOrdersReportProjectionToPropertyList(DailyOrdersReportProjection projection) {
        return new ArrayList() {{
            add(projection.getProductTitle());
            add(projection.getCount());
            add(projection.getTotalPrice());
        }};
    }
}
