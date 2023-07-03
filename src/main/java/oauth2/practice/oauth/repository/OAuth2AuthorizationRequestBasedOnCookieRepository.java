package oauth2.practice.oauth.repository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oauth2.practice.utils.CookieUtil;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

//https://jyami.tistory.com/121
//
public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public final static String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public final static String REFRESH_TOKEN = "refresh_token";
    private final static int cookieExpireSeconds = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtil.getCookies(request, "oauth2_auth_request")
                .map(cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }


    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        //// 쿠키에
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
//            CookieUtil.deleteCookie(request, response, "oauth2_auth_request");
//            CookieUtil.deleteCookie(request, response, "redirect_uri");
//            CookieUtil.deleteCookie(request, response, "refresh_token");
            return;
        }
        CookieUtil.addCookie(response, "oauth2_auth_request", CookieUtil.serialize(authorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin = request.getParameter("redirect_uri");
        if (redirectUriAfterLogin != null && !redirectUriAfterLogin.equals("")) {
            CookieUtil.addCookie(response, "redirect_uri", redirectUriAfterLogin, cookieExpireSeconds);
        }
    }


    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
    }
}
