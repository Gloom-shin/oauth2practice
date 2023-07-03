package oauth2.practice.oauth.exception;

public class OAuthProviderMissMatchException extends RuntimeException{

    public OAuthProviderMissMatchException(String message) {
        super(message);
    }
}
