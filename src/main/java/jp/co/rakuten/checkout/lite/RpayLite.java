package jp.co.rakuten.checkout.lite;

import java.net.PasswordAuthentication;
import java.net.Proxy;

import org.apache.commons.lang3.StringUtils;

import jp.co.rakuten.checkout.lite.exception.InvalidApiKeyException;

/**
 * This class stores basic information for calling the API, including private key, API endpoint, webhook signature, etc. Before calling methods of
 * other classes, set private key through {@link #setApiKey(String) RpayLite.setApiKey(key)} method.
 * 
 * @author rpayonline
 *
 */
public abstract class RpayLite {

    private static volatile String apiKey = "";
    private static volatile String apiBase;
    private static String webhookSignature;
    private static volatile Proxy connectionProxy = null;
    private static volatile PasswordAuthentication proxyCredential = null;

    public static final String API_ENDPOINT = "https://api.lite.checkout.rakuten.co.jp";
    public static final String LIVE_API_BASE = "/v1";
    public static final String SANDBOX_API_BASE = "/sandbox/v1";
    public static final String CHARGE_API = "/charges";
    public static final String NOTIFICATION_API = "/notifications";
    public static final String CAPTURE_API = "capture";
    public static final String REFUND_API = "refund";
    public static final String MAIL_API = "mail";
    public static final String SIGNATURE_HEADER = "X-Lite-Webhook-Signature";
    public static final String VERSION = "1.0.0";
    public static final String PRODUCT_NAME = "Rpay Online LITE API Client for Java";
    
    public static boolean isSandbox = false;

    /**
     * get api base for live/sandbox
     * 
     * @return api base
     */
    public static String getApiBase() {
        return apiBase;
    }

    /**
     * Set private key, live/sandbox environment is determined according to inputed key automatically
     * 
     * @param key
     *            private key
     * @throws InvalidApiKeyException
     *             if private key is not following the specification
     */
    public static void setApiKey(String key) throws InvalidApiKeyException {
        if(StringUtils.isEmpty(key)){
            throw new InvalidApiKeyException(null, "Secret key is empty.");
        }
        if (key.startsWith("live_private_") || key.startsWith("test_private_")) {
            isSandbox = false;
            apiBase = API_ENDPOINT + LIVE_API_BASE;
        } else if (key.startsWith("sandbox_private_")) {
            isSandbox = true;
            apiBase = API_ENDPOINT + SANDBOX_API_BASE;
        } else {
            throw new InvalidApiKeyException(null, "Secret key is not correct.");
        }
        apiKey = key;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getWebhookSignature() {
        return webhookSignature;
    }

    public static void setWebhookSignature(String webSig) {
        webhookSignature = webSig;
    }

    /**
     * For testing purpose
     * 
     * @param overriddenApiBase
     *            desired api url
     */
    public static void overrideApiBase(final String overriddenApiBase) {
        apiBase = overriddenApiBase;
    }

    public static Proxy getConnectionProxy() {
        return connectionProxy;
    }

    public static void setConnectionProxy(Proxy connectionProxy) {
        RpayLite.connectionProxy = connectionProxy;
    }

    public static PasswordAuthentication getProxyCredential() {
        return proxyCredential;
    }

    public static void setProxyCredential(PasswordAuthentication proxyCredential) {
        RpayLite.proxyCredential = proxyCredential;
    }

}
