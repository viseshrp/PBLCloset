package com.dbfall16.pblcloset.utils;

/**
 * Created by viseshprasad on 11/4/16.
 */
/*All constants related to the app go here*/
public class AppConstants {

    public static final String IS_LOGGED_IN = "is_logged_in";

    /*  Constants for user input validation */
    public static int DATA_TYPE_EMAIL = 0;
    public static int DATA_TYPE_PASSWORD = 1;
    public static int DATA_TYPE_GENERAL_TEXT = 2;
    public static int PASSWORD_MIN_CHARACTER_COUNT = 6;
    public static int DATA_TYPE_PHONE_NUMBER = 3;
    public static int DATA_TYPE_OTP = 4;
    public static int PHONE_NUMBER_MIN_COUNT = 10;


    /* General User Constants */
    public static final String USER_ID = "user_id";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String EMAIL = "email";

    /* Network response generic Constants */
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    public static final String USER_TYPE = "user_type";
    public static final String USER_TYPE_BUYER = "cust";
    public static final String USER_TYPE_DONOR = "donor";
    public static final String USER_TYPE_UNREG = "unreg";

    public static final String PROCESSED = "Processed";
    public static final String PENDING = "Pending";


}
