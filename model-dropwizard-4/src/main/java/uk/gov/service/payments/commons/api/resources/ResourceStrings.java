package uk.gov.service.payments.commons.api.resources;

public class ResourceStrings {
    private static final String API_OPERATION_NOTES_AUTH = "The Authorisation token needs to be specified in the 'authorization' header ";
    private static final String API_OPERATION_NOTES_BEARER = "as 'authorization: Bearer YOUR_API_KEY_HERE'";
    public static final String API_OPERATION_NOTES_AUTH_BEARER = API_OPERATION_NOTES_AUTH + API_OPERATION_NOTES_BEARER;
    public static final String API_RESPONSE_ERROR_MSG_CREDENTIALS_REQ = "Credentials are required to access this resource";
    public static final String API_RESPONSE_OK = "OK";
    public static final String API_RESPONSE_CREATED = "Created";
    public static final String API_RESPONSE_ERROR_MSG_BAD_REQUEST = "Bad request";
    public static final String API_RESPONSE_ERROR_MSG_TOO_MANY_REQUESTS = "Too many requests";
    public static final String API_OPERATION_ERROR_MSG_NOT_FOUND = "Not found";
    public static final String API_OPERATION_ERROR_MSG_DOWNSTREAM_SYS_ERROR = "Downstream system error";
}
