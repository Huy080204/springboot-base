package com.base.auth.constant;


import io.swagger.models.auth.In;

public class UserBaseConstant {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";


    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_MANAGER = 2;
    public static final Integer USER_KIND_USER = 3;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    public static final Integer NATION_KIND_PROVINCE = 1;
    public static final Integer NATION_KIND_DISTRICT = 2;
    public static final Integer NATION_KIND_COMMUNE = 3;

    public static final Integer GROUP_KIND_ADMIN = 1;
    public static final Integer GROUP_KIND_MANAGER = 2;
    public static final Integer GROUP_KIND_USER=4;

    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;
    public static final int MAX_TIME_FORGET_PWD = 5 * 60 * 1000; //5 minutes
    public static final Integer MAX_ATTEMPT_LOGIN = 5;

    public static final Integer CATEGORY_KIND_NEWS = 1;

    public static final Integer ORDER_STATE_BOOKING = 1;
    public static final Integer ORDER_STATE_APPROVED = 2;
    public static final Integer ORDER_STATE_DELIVERY = 3;
    public static final Integer ORDER_STATE_DONE = 4;
    public static final Integer ORDER_STATE_CANCEL = 5;

    public static final Integer CUSTOMER_ADDRESS_TYPE_HOME = 1;
    public static final Integer CUSTOMER_ADDRESS_TYPE_OFFICE = 2;


    private UserBaseConstant(){
        throw new IllegalStateException("Utility class");
    }
}
