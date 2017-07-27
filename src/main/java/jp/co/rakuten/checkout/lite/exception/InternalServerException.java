package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw InternalServerException if HTTP error code is 500.
 * 
 * @author rpayonline
 *
 */
public class InternalServerException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public InternalServerException(Integer statusCode, String message) {
        this(statusCode, message, null);
    }

    public InternalServerException(Integer statusCode, String message, Throwable e) {
        super(statusCode, message, e);
    }

}
