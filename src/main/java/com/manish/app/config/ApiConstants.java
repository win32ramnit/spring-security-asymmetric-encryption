package com.manish.app.config;

import org.aspectj.apache.bcel.classfile.Code;

public final class ApiConstants {

    // API versioning
    public static final String API_VERSION = "v1";
    public static final String API_PREFIX = "/api/" + API_VERSION;

    // Auth endpoints
    public static final String AUTH_BASE = API_PREFIX + "/auth";
    public static final String AUTH_LOGIN = "/login";
    public static final String AUTH_REGISTER = "/register";
    public static final String AUTH_REFRESH = "/refresh";

    // User endpoints
    public static final String USERS_BASE = API_PREFIX + "/users";
    public static final String USERS_ME = "/me";
    public static final String USERS_ME_PASSWORD = USERS_ME + "/password";
    public static final String USERS_ME_DEACTIVATE = USERS_ME +"/deactivate";
    public static final String USERS_ME_REACTIVATE = USERS_ME +"/reactivate";
    public static final String USERS_ME_DELETE = USERS_ME + "/delete";

    // File endpoints
    public static final String FILES_BASE = API_PREFIX + "/files";
    public static final String FILES_UPLOAD = "/upload";

    // Response messages
    public static final String SUCCESS_PROFILE_UPDATED = "Profile updated successfully";
    public static final String SUCCESS_PASSWORD_CHANGED = "Password changed successfully";
    public static final String SUCCESS_ACCOUNT_DEACTIVATED = "Account deactivated successfully";
    public static final String SUCCESS_ACCOUNT_REACTIVATED = "Account reactivated successfully";
    public static final String SUCCESS_ACCOUNT_DELETED = "Account deleted successfully";
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_INVALID_FILE = "Invalid file uploaded";
    public static final String ERROR_INVALID_REQUEST = "Invalid request data";
    public static final String ERROR_UNAUTHORIZED = "Unauthorized";
    public static final String ERROR_INTERNAL_SERVER = "Internal server error";
    public static final String ERROR_EMAIL_EXISTS = "Email already exists";
    public static final String ERROR_PASSWORD_MISMATCH = "Passwords do not match";
    public static final String ERROR_EMAIL_NOT_FOUND = "Email not found";
    public static final String ERROR_PASSWORD_TOO_SHORT = "Password must be at least 8 characters long";
    public static final String ERROR_PASSWORD_TOO_SIMPLE = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character";
    public static final String ERROR_TOKEN_INVALID = "Invalid token";
    public static final String ERROR_TOKEN_EXPIRED = "Token expired";
    public static final String ERROR_FORBIDDEN_ACCESS = "Access to this resource is forbidden";



    // HTTP status codes
    // 2xx Success
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_ACCEPTED = 202;         // optional
    public static final int HTTP_NO_CONTENT = 204;

    // 3xx Redirection
    public static final int HTTP_MOVED_PERMANENTLY = 301;
    public static final int HTTP_FOUND = 302;
    public static final int HTTP_TEMPORARY_REDIRECT = 307;
    public static final int HTTP_PERMANENT_REDIRECT = 308;

    // 4xx Client Errors
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_CONFLICT = 409;         // especially useful in REST
    public static final int HTTP_UNPROCESSABLE_ENTITY = 422; // for validation failures
    public static final int HTTP_TOO_MANY_REQUESTS = 429;    // rate limiting

    // 5xx Server Errors
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    public static final int HTTP_NOT_IMPLEMENTED = 501;       // optional
    public static final int HTTP_BAD_GATEWAY = 502;
    public static final int HTTP_SERVICE_UNAVAILABLE = 503;
    public static final int HTTP_GATEWAY_TIMEOUT = 504;


    private ApiConstants() {
        throw new AssertionError("Utility class, cannot be instantiated");
    }
}
