package jp.co.rakuten.checkout.lite.exception;

/**
 * This abstract class is the base for all Exceptions related to Rpay Lite API. Developers will not use this class directly.
 * 
 * @author rpayonline
 *
 */
public abstract class RpayLiteException extends Exception {

    private static final long serialVersionUID = 1L;

    Integer statusCode;
    String type;
    String code;

    public RpayLiteException(Integer statusCode, String message) {
        super(message, null);
        this.statusCode = statusCode;
    }

    public RpayLiteException(Integer statusCode, String message, Throwable e) {
        super(message, e);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        String str = "";
        // for debugging
        if (statusCode != null) {
            str += " (" + statusCode + ")";
        }
        return super.toString() + str;
    }

}
