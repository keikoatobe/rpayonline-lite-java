package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw InvalidRequestException if HTTP error code is 400 (bad request) or 401 (invalid request), or the resource URL does not exist.
 * 
 * @author rpayonline
 *
 */
public class InvalidRequestException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public InvalidRequestException(Integer statusCode, String message, Throwable e) {
        super(statusCode, message, e);

    }

    public InvalidRequestException(Integer statusCode, String message) {
        this(statusCode, message, null);
    }

    public InvalidRequestException(Integer statusCode, String message, String type, String code, Throwable e) {
        super(statusCode, message, e);
        this.type = type;
        this.code = code;
    }

    public InvalidRequestException(Integer statusCode, String message, String type, String code) {
        this(statusCode, message, type, code, null);
    }
}
