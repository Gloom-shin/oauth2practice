package oauth2.practice.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private final static int SUCCESS_CODE = 200;
    private final static int NOT_FOUND = 400;
    private final static int FAILED = 500;
    private final static String SUCCESS_MESSAGE = "SUCCESS";
    private final static String NOT_FOUND_MESSAGE = "NOT FOUND";
    private final static String FAILED_MESSAGE = "서버에서 오류가 발생하였습니다.";
    private final static String INVALID_ACCESS_TOKEN = "Invalid access token.";
    private final static String INVALID_REFRESH_TOKEN = "Invalid refresh token.";
    private final static String NOT_EXPIRED_TOKEN_YET = "Not expired token yet.";

    private final ApiResponseHeader header;
    private final Map<String, T> body;


    public static <T> ApiResponse<T> success(String name, T data) {
        return new ApiResponse<>(new ApiResponseHeader(SUCCESS_CODE, SUCCESS_MESSAGE), Map.of(name, data));
    }

    public static <T> ApiResponse<T> fail() {
        return new ApiResponse<>(new ApiResponseHeader(FAILED, FAILED_MESSAGE), new HashMap<>());
    }

    public static <T> ApiResponse<T> invalidAccessToken() {
        return new ApiResponse<>(new ApiResponseHeader(FAILED, INVALID_ACCESS_TOKEN), new HashMap<>());
    }
    public static <T> ApiResponse<T> invalidRefreshToken() {
        return new ApiResponse<>(new ApiResponseHeader(FAILED, INVALID_REFRESH_TOKEN), new HashMap<>());
    }
    public static <T> ApiResponse<T> notExpiredTokenYet() {
        return new ApiResponse<>(new ApiResponseHeader(FAILED, NOT_EXPIRED_TOKEN_YET), new HashMap<>());
    }
}
