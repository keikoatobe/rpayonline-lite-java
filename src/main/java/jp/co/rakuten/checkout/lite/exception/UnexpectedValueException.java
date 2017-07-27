package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw UnexpectedValueException if the JSON string for {@link jp.co.rakuten.checkout.lite.net.Webhook Webhook} object to parse into
 * {@link jp.co.rakuten.checkout.lite.model.Event Event} object is in invalid format.
 * 
 * @author rpayonline
 *
 */
public class UnexpectedValueException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public UnexpectedValueException(Integer statusCode, String message) {
        this(statusCode, message, null);
    }

    public UnexpectedValueException(Integer statusCode, String message, Throwable e) {
        super(statusCode, message, e);
    }

}
