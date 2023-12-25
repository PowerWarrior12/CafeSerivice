package com.simbirsoft.services;

import com.simbirsoft.dto.basket.AddProductIntoBasketRequest;
import com.simbirsoft.dto.basket.BasketResponse;
import com.simbirsoft.dto.basket.UpdateBasketRequest;

public interface BasketService {
    int addNewBasketItem(AddProductIntoBasketRequest request, String login);
    /**
     * @param login of user, who want to add product into basket
     * @param request if count in request = 0, then product will be removed from basket,
     * otherwise it will be updated
     */
    String updateBasket(String login, UpdateBasketRequest request);
    BasketResponse getBasketByUsersLogin(String login);
}
