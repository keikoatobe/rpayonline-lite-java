package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw RateLimitException if HTTP error code is 429.
 * 
 * @author rpayonline
 *
 */
public class RateLimitException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public RateLimitException(Integer statusCode, String message) {
        this(statusCode, message, null);
    }

    public RateLimitException(Integer statusCode, String message, Throwable e) {
        super(statusCode, message, e);
    }

}
