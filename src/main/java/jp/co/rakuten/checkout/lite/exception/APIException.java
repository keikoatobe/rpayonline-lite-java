package jp.co.rakuten.checkout.lite.exception;

/**
 * This class is for all exceptions related to Rpay Lite API but has not been defined specifically.
 * 
 * @author rpayonline
 *
 */
public class APIException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public APIException(Integer statusCode, String message, Throwable e) {
        super(statusCode, message, e);
    }

}
