package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw PermissionException if HTTP error code is 403.
 * 
 * @author rpayonline
 *
 */
public class PermissionException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public PermissionException(Integer statusCode, String message) {
        this(statusCode, message, null);
    }

    public PermissionException(Integer statusCode, String message, Throwable e) {
        super(statusCode, message, e);
    }

}
