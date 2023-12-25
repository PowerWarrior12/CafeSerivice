package com.simbirsoft.services.impl;

import com.simbirsoft.dto.order.CustomerOrderResponse;
import com.simbirsoft.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    @Value("${websocket.admin.secret}")
    private String websocketAdminSecret;
    @Override
    public void notifyAboutOrderCreation(CustomerOrderResponse order) {
        messagingTemplate.convertAndSendToUser(
                websocketAdminSecret, "/order/new",
                order
        );
    }

    @Override
    public void notifyAboutOrderStatusChanged(CustomerOrderResponse order) {
        messagingTemplate.convertAndSendToUser(
                order.getCode().toString(), "order/status-changed",
                order
        );
    }
}
