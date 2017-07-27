package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw AuthenticationException if HTTP error code is 401 (incorrect private key specified).
 * 
 * @author rpayonline
 *
 */
public class AuthenticationException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException(Integer statusCode, String message) {
        super(statusCode, message);
    }

}
