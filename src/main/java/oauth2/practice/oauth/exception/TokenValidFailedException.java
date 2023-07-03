package oauth2.practice.oauth.exception;

public class TokenValidFailedException extends RuntimeException{

    public TokenValidFailedException() {
        super("Failed to validate token");
    }
    private TokenValidFailedException(String message) {
        super(message);
    }
}
