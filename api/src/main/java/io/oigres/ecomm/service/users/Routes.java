package io.oigres.ecomm.service.users;

public class Routes {
    public static final String USERS_CONTROLLER_PATH = "/v1/users";
    public static final String PROFILE_ADMIN_USER = "/profile/admin";
    public static final String PROFILE_CONSUMER_USER = "/profile/consumer";
    public static final String PROFILE_DISPENSARY_USER = "/profile/dispensary";
    public static final String SEARCH_USERS = "/search";
    public static final String GET_USER = "/{userId}";
    public static final String GET_PROFILE_ADMIN_USER = "/profile/admins/{userId}";
    public static final String GET_PROFILE_CONSUMER_USER = "/profile/consumers/{userId}";
    public static final String GET_PROFILE_DISPENSARY_USER = "/profile/dispensaries/{userId}";
    public static final String GET_PROFILE_DISPENSARY_USER_BY_DISPENSARY_ID = "/profile/dispensaries/bydispensaryid/{dispensaryId}";
    public static final String GET_ALL_PROFILE_ADMIN_USER = "/profile/admins";
    public static final String GET_ALL_PROFILE_CONSUMER_USER = "/profile/consumers";
    public static final String GET_ALL_PROFILE_DISPENSARY_USER = "/profile/dispensaries";
    public static final String UPDATE_ADMIN_USER = "/profile/admins/{userId}";
    public static final String ACTIVATE_ADMIN_USER = "/profile/admins/{userId}/activate";
    public static final String DEACTIVATE_ADMIN_USER = "/profile/admins/{userId}/deactivate";
    public static final String ACTIVATE_CONSUMER_USER = "/profile/consumers/{userId}/activate";
    public static final String DEACTIVATE_CONSUMER_USER = "/profile/consumers/{userId}/deactivate";
    public static final String ACTIVATE_DISPENSARY_USER = "/profile/dispensaries/{userId}/activate";
    public static final String DEACTIVATE_DISPENSARY_USER = "/profile/dispensaries/{userId}/deactivate";
    public static final String UPDATE_CONSUMER_USER = "/profile/consumers/{userId}";
    public static final String UPDATE_DISPENSARY_USER = "/profile/dispensaries/{userId}";
    public static final String UPLOADS_CARDS = "/profile/uploads/mmjcard";
    public static final String DELETE_USER = "/{userId}";

    public static final String DELETE_ADMIN_USER = "/admin/{userId}";
    public static final String DELETE_CONSUMER_USER = "/consumer/{userId}";
    public static final String DELETE_DISPENSARY_USER = "/dispensary/{dispensaryId}";
    public static final String VALIDATE_USER = "/validate";
    public static final String SEND_CODE = "/send-code";
    public static final String VERIFY_CODE = "/verify-code";
    public static final String REGION_PATH = "/v1/region";
    public static final String GET_STATES = "/states";
    public static final String GET_STATE_BY_ID = "/states/{stateId}";
    public static final String GET_ALL_GENDERS = "/genders";
    public static final String GET_GENDER_BY_ID = "/genders/{genderId}";
    public static final String ZIP_CODES = "/states/{stateId}/zipcodes";
    public static final String GET_ZIPCODE_BY_ID = "/states/zipcodes/{zipCodeId}";
    public static final String GET_USER_STATE = "/{userId}/state";
    public static final String GET_ALL_USERTYPES = "/usertypes";
    public static final String GET_USERTYPE_BY_ID = "/usertypes/{userTypeId}";
    public static final String GET_AVATAR_IMAGE_UPLOAD_LOCATION = "/uploads/avatar";
    public static final String EDIT_PROFILE_IMAGE_STATUS = "/uploads/avatar";
    public static final String GET_MMJCARD_IMAGE_UPLOAD_LOCATION = "/profile/consumers/uploads/mmjcard";
}
