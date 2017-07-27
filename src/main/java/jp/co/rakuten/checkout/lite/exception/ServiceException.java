package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw ServiceException if HTTP error code is 503 (error related to Rpay Lite server)
 * 
 * @author rpayonline
 *
 */
public class ServiceException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public ServiceException(Integer statusCode, String message) {
        this(statusCode, message, null);
    }

    public ServiceException(Integer statusCode, String message, Throwable e) {
        super(statusCode, message, e);
    }

}
