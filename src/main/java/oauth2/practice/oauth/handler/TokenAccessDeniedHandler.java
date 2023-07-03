package oauth2.practice.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAccessDeniedHandler implements AccessDeniedHandler {

    //MVC패턴 controller 밖으로 예외가 던져진 경우 예외를 해결
        // 예외 상태 코드 변환
        // 뷰 템플릿 변환
        // API 응답 처리를 해줄 수 있다.
    private final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException); // AccessDeniedException 을 발생시킨다.
    }
}
