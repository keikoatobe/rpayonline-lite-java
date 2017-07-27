package jp.co.rakuten.checkout.lite.net;

import jp.co.rakuten.checkout.lite.RpayLite;
import jp.co.rakuten.checkout.lite.exception.RpayLiteException;
import jp.co.rakuten.checkout.lite.exception.UnexpectedValueException;
import jp.co.rakuten.checkout.lite.model.Event;
import jp.co.rakuten.checkout.lite.model.RpayLiteObject;

/**
 * This class is used to construct Event object if signature header is verified.
 * 
 * @author rpayonline
 *
 */
public final class Webhook {

    /**
     * Construct event object from JSON payload
     * 
     * @param payload
     *            JSON string
     * @param sigHeader
     *            signature header sent by Rakuten
     * @param expectedSignature
     *            webhook signature
     * @return Event object if signature header is verified
     * @throws SignatureVerificationException
     *             if signature header does not match with expected signature
     * @throws UnexpectedValueException
     *             if JSON payload format is not valid
     */
    public static Event constructEvent(String payload, String sigHeader, String expectedSignature)
            throws SignatureVerificationException, UnexpectedValueException {
        try {
            Event event = RpayLiteObject.PRETTY_PRINT_GSON.fromJson(payload, Event.class);
            Signature.verifyHeader(sigHeader, expectedSignature);
            return event;
        } catch (com.google.gson.JsonSyntaxException e) {
            throw new UnexpectedValueException(null, "Not valid JSON, " + e.getMessage());
        }
    }

    /**
     * This class contains method for verifying signature header.
     * 
     * @author rpayonline
     *
     */
    public static final class Signature {

        /**
         * Verify the validity of signature header
         * 
         * @param sigHeader
         *            signature header sent by Rakuten
         * @param expectedSignature
         *            webhook signature
         * @return true if signature is valid
         * @throws SignatureVerificationException
         *             if signature verification fails
         */
        public static boolean verifyHeader(String sigHeader, String expectedSignature) throws SignatureVerificationException {
            String headerKey = RpayLite.SIGNATURE_HEADER;
            if (sigHeader == null) {
                throw new SignatureVerificationException(String.format("%s header is not requested.", headerKey), sigHeader);
            }
            if (expectedSignature == null && RpayLite.getWebhookSignature() == null) {
                throw new SignatureVerificationException("Webhook Signature is not defined.", sigHeader);
            }
            String expected = expectedSignature != null ? expectedSignature : RpayLite.getWebhookSignature();
            if (!sigHeader.equals(expected)) {
                throw new SignatureVerificationException(String.format("Expected Webhook Signature is %s, but %s.", expected, sigHeader), sigHeader);
            }
            return true;
        }

    }

    /**
     * Throw SignatureVerificationException if webhook signature is empty or does not match.
     * 
     * @author rpayonline
     *
     */
    public static class SignatureVerificationException extends RpayLiteException {

        private static final long serialVersionUID = 1L;

        private final String sigHeader;

        public SignatureVerificationException(String message, String sigHeader) {
            super(0, message, null);
            this.sigHeader = sigHeader;
        }

        public String getSigHeader() {
            return sigHeader;
        }

    }

}
