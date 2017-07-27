package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw InvalidApiKeyException if the key to call API is not following the specification.
 * 
 * @author rpayonline
 *
 */
public class InvalidApiKeyException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public InvalidApiKeyException(Integer statusCode, String message, Throwable e) {
        super(statusCode, message, e);
    }

    public InvalidApiKeyException(Integer statusCode, String message) {
        this(statusCode, message, null);
    }

}
