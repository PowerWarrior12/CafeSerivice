package com.simbirsoft.services;

import com.simbirsoft.dto.order.CustomerOrderResponse;

public interface NotificationService {
    public void notifyAboutOrderCreation(CustomerOrderResponse order);
    public void notifyAboutOrderStatusChanged(CustomerOrderResponse order);
}
