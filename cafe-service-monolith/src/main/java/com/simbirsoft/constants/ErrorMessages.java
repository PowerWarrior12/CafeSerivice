package com.simbirsoft.constants;

public class ErrorMessages {
    // region Auth
    public static final String EMPTY_FIRST_NAME = "Your first name can't be empty";
    public static final String EMPTY_LAST_NAME = "Your last name can't be empty";
    public static final String EMPTY_LOGIN = "Your login can't be empty";
    public static final String INCORRECT_LOGIN = "Your login must be more correct";
    public static final String ACCOUNT_DO_NOT_ACTIVATED = "Your account don't activated";
    public static final String INCORRECT_PASSWORD = "Your password must be more correct";
    public static final String INCORRECT_AUTHENTICATION_PASSWORD = "Incorrect password";
    public static final String INCORRECT_AUTHENTICATION_LOGIN = "Incorrect login";
    public static final String ALREADY_EXISTS_LOGIN = "This login already exists";
    public static final String PASSWORDS_DO_NOT_MATCH = "Your input passwords don't match";
    public static final String INCORRECT_ACTIVATION_PROCESS = "Your activation code is not correct";
    public static final String LOGIN_NOT_FOUND = "This login don't exists";
    public static final String INCORRECT_RESET_PASSWORD_PROCESS = "Your reset password key is not correct";

    // endregion
    // region Product
    public static final String PRODUCT_NOT_FOUND_BY_ID = "Product with such id did not exists";
    // endregion
    // region Image
    public static final String FAILED_SAVING_IMAGE = "Failed saving the image";
    public static final String FAILED_LOADING_IMAGE = "Failed loading the image";
    // endregion
    // region Order
    public static final String ORDER_NOT_FOUNT = "This order don't exists";
    // endregion
    // region Basket
    public static final String BASKET_ITEM_NOT_FOUND = "Basket item did not found";
    // endregion
    // region Report
    public static final String REPORT_EXCEPTION = "Internal exception with report generation";
    public static final String REPORT_HANDLER_EXCEPTION = "Can't find generator for report type";
    // endregion
}
