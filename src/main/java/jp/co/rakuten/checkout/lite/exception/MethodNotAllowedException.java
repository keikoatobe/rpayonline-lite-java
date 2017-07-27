package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw MethodNotAllowedException if HTTP error code is 405 (wrong HTTP request method used).
 * 
 * @author rpayonline
 *
 */
public class MethodNotAllowedException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public MethodNotAllowedException(Integer statusCode, String message) {
        this(statusCode, message, null);
    }

    public MethodNotAllowedException(Integer statusCode, String message, Throwable e) {
        super(statusCode, message, e);
    }

}
