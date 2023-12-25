package com.simbirsoft.common;

import com.simbirsoft.domain.enums.OrderStatus;

public class TestConstants {
    // region User
    public static final int USER_ID = 2;
    public static final String SIMPLE_USER_LOGIN = "test3@mail.ru";
    // Email of user with admin role
    public static final String USER_LOGIN = "businessmail1710@mail.ru";
    public static final String USER_FIRST_NAME = "Peter";
    public static final String USER_LAST_NAME = "Ivanov";
    public static final String USER_PHONE_NUMBER = "+79031254212";
    public static final String USER_ADDRESS = "My address";
    public static final String USER_ACTIVATION_KEY = "7c1a2288-869a-4df7-be26-0c5007e5c704";
    public static final String USER_RESET_PASSWORD_KEY = "7c1a2288-869a-4df7-be26-0c5007e5c704";
    public static final String USER_PASSWORD_DECODED = "Password12!";
    public static final String USER_PASSWORD_ENCODED = "$2y$10$gAbcJefjbgNyxHNXCgYE9uFbksYKe4Jg2ZLJT9l8C8xzF8Tx0OCXO";
    public static final String USER_NEW_PASSWORD_DECODED = "password_new";
    public static final String USER_NEW_PASSWORD_ENCODED = "$2y$10$qWN93K4UWUdzGfzWymcRb.ZCk6wpfcqBrKJZxhMN3y/oRlWXov3dS";
    // endregion
    // region Product
    public static int PRODUCT_ID = 1;
    public static String PRODUCT_TITLE = "product1";
    public static String PRODUCT_DESCRIPTION = "description";
    public static float PRODUCT_PRICE = 100;
    public static String PRODUCT_CATEGORY = "category1";
    public static String PRODUCT_PRODUCER = "producer2";
    public static boolean PRODUCT_AVAILABILITY = true;
    public static String PRODUCT_IMAGE_PATH = "image_path.jpg";
    // endregion
    // region Order
    public static String ORDER_CODE = "bbb73c60-c43f-41e8-bf13-e07d363e6c0c";
    public static int ORDER_ITEMS_COUNT = 5;
    public static OrderStatus ORDER_STATUS = OrderStatus.RECEIVED;
    public static String ORDER_COMMENT = "comment";
    public static int ORDER_USER_ID = 1;
    // endregion
    // region Basket
    public static int BASKET_ID = 1;
    public static int BASKET_COUNT = 5;
    public static int BASKET_PRODUCT_ID = PRODUCT_ID;
    // endregion
}
