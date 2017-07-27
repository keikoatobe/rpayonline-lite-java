package jp.co.rakuten.checkout.lite.exception;

/**
 * Throw PaymentRequestException if HTTP error code is 402 (error related to payment method).
 * 
 * @author rpayonline
 *
 */
public class PaymentRequestException extends RpayLiteException {

    private static final long serialVersionUID = 1L;

    public PaymentRequestException(Integer statusCode, String message, String type, String code, Throwable e) {
        super(statusCode, message, e);
        this.type = type;
        this.code = code;
    }

    public PaymentRequestException(Integer statusCode, String message, String type, String code) {
        this(statusCode, message, type, code, null);
    }

}
