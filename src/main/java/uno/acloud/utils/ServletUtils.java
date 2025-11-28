package uno.acloud.utils;
import jakarta.servlet.http.HttpServletRequest;

public class ServletUtils {
    public static String getToken(HttpServletRequest request) {
        return request.getHeader("token");   // 或 "Authorization"
    }
}