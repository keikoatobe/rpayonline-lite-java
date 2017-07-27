package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw APIConnectionException if HTTP request method other than GET or POST is used.
 * 
 * @author rpayonline
 *
 */
public class APIConnectionException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public APIConnectionException(String message) {
        this(message, null);
    }

    public APIConnectionException(String message, Throwable e) {
        super(0, message, e);
    }

}
