package com.simbirsoft.services.impl;

import com.simbirsoft.domain.BasketItem;
import com.simbirsoft.domain.User;
import com.simbirsoft.dto.basket.AddProductIntoBasketRequest;
import com.simbirsoft.dto.basket.BasketResponse;
import com.simbirsoft.dto.basket.UpdateBasketRequest;
import com.simbirsoft.exceptions.ApiRequestException;
import com.simbirsoft.repositories.BasketItemRepository;
import com.simbirsoft.repositories.UserRepository;
import com.simbirsoft.services.BasketService;
import com.simbirsoft.services.mappers.basket.BasketMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.simbirsoft.constants.ErrorMessages.BASKET_ITEM_NOT_FOUND;
import static com.simbirsoft.constants.ErrorMessages.LOGIN_NOT_FOUND;
import static com.simbirsoft.constants.OkMessages.BASKET_UPDATED_OK;
import static com.simbirsoft.constants.OkMessages.PRODUCT_REMOVED_FROM_BASKET_OK;

@Service
@RequiredArgsConstructor
@Log4j2
public class BasketServiceImpl implements BasketService {
    private final BasketItemRepository basketItemRepository;
    private final UserRepository userRepository;
    private final BasketMapper basketMapper;

    @Override
    @Transactional
    public int addNewBasketItem(AddProductIntoBasketRequest request, String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> {
                    log.atDebug().log(String.format("Update basket process: user with login %s not exists", login));
                    return new ApiRequestException(LOGIN_NOT_FOUND, HttpStatus.NOT_FOUND);
                });
        log.atDebug().log(String.format("Create new basket item process: start to user with id %d", user.getId()));
        BasketItem item = basketMapper.addProductIntoBasketRequestToBasketItem(request, user.getId());
        BasketItem savedItem = basketItemRepository.saveAndFlush(item);
        return savedItem.getId();
    }

    @Override
    @Transactional
    public String updateBasket(String login, UpdateBasketRequest request) {
        log.atDebug().log(String.format("Update basket process: start for user with login %s and request: %s", login, request));
        if (request.getCount() == 0) {
            deleteBasketItem(request.getBasketItemId());
            return PRODUCT_REMOVED_FROM_BASKET_OK;
        } else {
            updateBasketItem(request);
            return BASKET_UPDATED_OK;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BasketResponse getBasketByUsersLogin(String login) {
        log.atDebug().log(String.format("Get basket by users login process: start for user with login %s", login));
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> {
                    log.atDebug().log(String.format("Get basket by users login process: user with login %s not exists", login));
                    return new ApiRequestException(LOGIN_NOT_FOUND, HttpStatus.NOT_FOUND);
                });
        List<BasketItem> basketItemList = basketItemRepository.findByUserId(user.getId());
        return basketMapper.basketItemsToBasketResponse(basketItemList);
    }

    private void updateBasketItem(UpdateBasketRequest request) {
        log.atDebug().log(String.format("Update basket item process: start to basket item with id %d", request.getBasketItemId()));
        BasketItem item = basketItemRepository.findById(request.getBasketItemId())
                .orElseThrow(() -> {
                    log.atDebug().log(String.format("Update basket item process: basket item with id %d did not found",
                            request.getBasketItemId()));
                    return new ApiRequestException(BASKET_ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
                });
        item.setCount(request.getCount());
        basketItemRepository.flush();
    }

    private void deleteBasketItem(int basketItemId) {
        log.atDebug().log(String.format("Delete basket item process: start to basket item with id %d", basketItemId));
        basketItemRepository.deleteById(basketItemId);
    }
}
