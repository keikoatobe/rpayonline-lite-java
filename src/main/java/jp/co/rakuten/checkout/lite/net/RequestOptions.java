package jp.co.rakuten.checkout.lite.net;

import jp.co.rakuten.checkout.lite.RpayLite;

/**
 * Class for holding request options when making API request. Use {@link jp.co.rakuten.checkout.lite.net.RequestOptions.RequestOptionsBuilder
 * RequestOptionsBuilder} to generate request options.
 * 
 * @author rpayonline
 *
 */
public class RequestOptions {

    private final String apiKey;
    private final String rpayLiteVersion;
    private final String idempotencyKey;
    private final String rpayLiteAccount;

    private RequestOptions(String apiKey, String rpayLiteVersion, String idempotencyKey, String rpayLiteAccount) {
        this.apiKey = apiKey;
        this.rpayLiteVersion = rpayLiteVersion;
        this.idempotencyKey = idempotencyKey;
        this.rpayLiteAccount = rpayLiteAccount;
    }

    public static RequestOptions getDefault() {
        return new RequestOptions(RpayLite.getApiKey(), RpayLite.VERSION, null, null);
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getRpayLiteVersion() {
        return rpayLiteVersion;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getRpayLiteAccount() {
        return rpayLiteAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestOptions that = (RequestOptions) o;

        if (apiKey != null ? !apiKey.equals(that.apiKey) : that.apiKey != null) {
            return false;
        }
        if (idempotencyKey != null ? !idempotencyKey.equals(that.idempotencyKey) : that.idempotencyKey != null) {
            return false;
        }
        if (rpayLiteVersion != null ? !rpayLiteVersion.equals(that.rpayLiteVersion) : that.rpayLiteVersion != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = apiKey != null ? apiKey.hashCode() : 0;
        result = 31 * result + (rpayLiteVersion != null ? rpayLiteVersion.hashCode() : 0);
        result = 31 * result + (idempotencyKey != null ? idempotencyKey.hashCode() : 0);
        return result;
    }

    /**
     * Start a request options builder
     * 
     * @return new RequestOptionsBuilder
     */
    public static RequestOptionsBuilder builder() {
        return new RequestOptionsBuilder();
    }

    private static String normalizeApiKey(String apiKey) {
        // null apiKeys are considered "valid"
        if (apiKey == null) {
            return null;
        }
        String normalized = apiKey.trim();
        if (normalized.isEmpty()) {
            throw new InvalidRequestOptionsException("Empty API key specified!");
        }
        return normalized;
    }

    private static String normalizeRpayLiteVersion(String rpayLiteVersion) {
        // null RpayLiteVersions are considered "valid" and use RpayLite.VERSION
        if (rpayLiteVersion == null) {
            return null;
        }
        String normalized = rpayLiteVersion.trim();
        if (normalized.isEmpty()) {
            throw new InvalidRequestOptionsException("Empty RpayLite version specified!");
        }
        return normalized;
    }

    private static String normalizeIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null) {
            return null;
        }
        String normalized = idempotencyKey.trim();
        if (normalized.isEmpty()) {
            throw new InvalidRequestOptionsException("Empty Idempotency Key Specified!");
        }
        if (normalized.length() > 255) {
            throw new InvalidRequestOptionsException(
                    String.format("Idempotency Key length was %d, which is larger than the 255 character maximum!", normalized.length()));
        }
        return normalized;
    }

    private static String normalizeRpayLiteAccount(String rpayLiteAccount) {
        if (rpayLiteAccount == null) {
            return null;
        }
        String normalized = rpayLiteAccount.trim();
        if (normalized.isEmpty()) {
            throw new InvalidRequestOptionsException("Empty RpayLite account specified!");
        }
        return normalized;
    }

    /**
     * Class for build RequestOptions.
     * 
     * @author rpayonline
     *
     */
    public static final class RequestOptionsBuilder {
        private String apiKey;
        private String rpayLiteVersion;
        private String idempotencyKey;
        private String rpayLiteAccount;

        public RequestOptionsBuilder() {
            apiKey = RpayLite.getApiKey();
            rpayLiteVersion = RpayLite.VERSION;
        }

        public String getApiKey() {
            return apiKey;
        }

        public RequestOptionsBuilder setApiKey(String apiKey) {
            this.apiKey = normalizeApiKey(apiKey);
            return this;
        }

        public RequestOptionsBuilder clearApiKey() {
            apiKey = null;
            return this;
        }

        public RequestOptionsBuilder setRpayLiteVersion(String rpayLiteVersion) {
            this.rpayLiteVersion = normalizeRpayLiteVersion(rpayLiteVersion);
            return this;
        }

        public RequestOptionsBuilder clearRltieVersion() {
            rpayLiteVersion = null;
            return this;
        }

        public RequestOptionsBuilder setIdempotencyKey(String idempotencyKey) {
            this.idempotencyKey = idempotencyKey;
            return this;
        }

        public RequestOptionsBuilder clearIdempotencyKey() {
            idempotencyKey = null;
            return this;
        }

        public String getIdempotencyKey() {
            return idempotencyKey;
        }

        public String getRpayLiteAccount() {
            return rpayLiteAccount;
        }

        public RequestOptionsBuilder setRpayLiteAccount(String rpayLiteAccount) {
            this.rpayLiteAccount = rpayLiteAccount;
            return this;
        }

        public RequestOptionsBuilder clearRpayLiteAccount() {
            return setRpayLiteAccount(null);
        }

        /**
         * build a RequestOptions object
         * 
         * @return RequestOptions object
         */
        public RequestOptions build() {
            return new RequestOptions(normalizeApiKey(apiKey), normalizeRpayLiteVersion(rpayLiteVersion), normalizeIdempotencyKey(idempotencyKey),
                    normalizeRpayLiteAccount(rpayLiteAccount));
        }
    }

    /**
     * Throw InvalidRequestOptionsException if required request option is empty.
     * 
     * @author rpayonline
     *
     */
    public static class InvalidRequestOptionsException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public InvalidRequestOptionsException(String message) {
            super(message);
        }
    }

}
