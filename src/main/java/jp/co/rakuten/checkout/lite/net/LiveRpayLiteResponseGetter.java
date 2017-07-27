package jp.co.rakuten.checkout.lite.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.DatatypeConverter;

import jp.co.rakuten.checkout.lite.RpayLite;
import jp.co.rakuten.checkout.lite.exception.APIConnectionException;
import jp.co.rakuten.checkout.lite.exception.APIException;
import jp.co.rakuten.checkout.lite.exception.AuthenticationException;
import jp.co.rakuten.checkout.lite.exception.InternalServerException;
import jp.co.rakuten.checkout.lite.exception.InvalidRequestException;
import jp.co.rakuten.checkout.lite.exception.MethodNotAllowedException;
import jp.co.rakuten.checkout.lite.exception.PaymentRequestException;
import jp.co.rakuten.checkout.lite.exception.PermissionException;
import jp.co.rakuten.checkout.lite.exception.RateLimitException;
import jp.co.rakuten.checkout.lite.exception.ServiceException;
import jp.co.rakuten.checkout.lite.net.APIResource.RequestMethod;
import jp.co.rakuten.checkout.lite.net.APIResource.RequestType;

/**
 * Class for making API request through HttpURLConnection. Developers will not use this class directly.
 * 
 * @author rpayonline
 *
 */
public class LiveRpayLiteResponseGetter implements RpayLiteResponseGetter {

    private static final String DNS_CACHE_TTL_PROPERTY_NAME = "networkaddress.cache.ttl";

    private static final SSLSocketFactory socketFactory = new RpayLiteSSLSocketFactory();

    /*
     * (non-Javadoc)
     * 
     * @see jp.co.rakuten.checkout.lite.net.RpayLiteResponseGetter#request(jp.co.rakuten.checkout.lite.net.APIResource.RequestMethod,
     * java.lang.String,
     * java.util.Map, java.lang.Class, jp.co.rakuten.checkout.lite.net.APIResource.RequestType, jp.co.rakuten.checkout.lite.net.RequestOptions)
     */
    @Override
    public <T> T request(RequestMethod method, String url, Map<String, Object> params, Class<T> clazz, RequestType type, RequestOptions options)
            throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {

        if (options == null) {
            options = RequestOptions.getDefault();
        }
        if (options.getApiKey() == null || options.getApiKey().trim().isEmpty()) {
            throw new AuthenticationException(0, "No API key provided. (HINT: set your private API key using 'RpayLite.setApiKey(<API-KEY>)'. "
                    + "You can generate API keys from the Rpay lite dashBoard. " + "See https://lite.checkout.rakuten.co.jp/manual for details.");
        }
        String originalDNSCacheTTL = null;
        Boolean allowedToSetTTL = true;
        try {
            originalDNSCacheTTL = java.security.Security.getProperty(DNS_CACHE_TTL_PROPERTY_NAME);
            // disable DNS cache
            java.security.Security.setProperty(DNS_CACHE_TTL_PROPERTY_NAME, "0");
        } catch (SecurityException se) {
            allowedToSetTTL = false;
        }

        try {
            String response = getContent(url, options, params, method);
            T resource = APIResource.GSON.fromJson(response, clazz);

            return resource;
        } finally {
            if (allowedToSetTTL) {
                if (originalDNSCacheTTL == null) {
                    // value unspecified by implementation
                    // DNS_CACHE_TTL_PROPERTY_NAME of -1 = cache forever
                    java.security.Security.setProperty(DNS_CACHE_TTL_PROPERTY_NAME, "-1");
                } else {
                    java.security.Security.setProperty(DNS_CACHE_TTL_PROPERTY_NAME, originalDNSCacheTTL);
                }
            }
        }
    }

    /**
     * Get response body of Http url connection request
     * 
     * @param url_string
     *            url string
     * @param options
     *            RequestOptions
     * @param params
     *            parameters
     * @param method
     *            RequestMethod
     * @return string which is response body (json)
     * @throws APIConnectionException
     * @throws InvalidRequestException
     * @throws AuthenticationException
     * @throws PaymentRequestException
     * @throws APIException
     * @throws PermissionException
     * @throws RateLimitException
     * @throws MethodNotAllowedException
     * @throws ServiceException
     * @throws InternalServerException
     */
    private static String getContent(String url_string, RequestOptions options, Map<String, Object> params, RequestMethod method)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {

        RpayLiteResponse response;

        response = getRpayLiteResponse(url_string, options, params, method);

        int rCode = response.getResponseCode();
        String rBody = response.getResponseBody();

        if (rCode < 200 || rCode >= 300) {
            //rCode refers to HTTP response status code. In case response code is not 2xx, error will be handled
            handleAPIError(rBody, rCode);
        }
        return rBody;

    }

    /**
     * Get RpayLiteResponse through HttpURLConnection, RpayLiteResponse contains response body and response code
     * 
     * @param url_string
     *            url
     * @param options
     *            RequestOptions
     * @param params
     *            parameters
     * @param method
     *            RequestMethod
     * @return RpayLiteResponse
     * @throws APIConnectionException
     *             if HTTP method is null or other than GET/POST
     * @throws RateLimitException
     * @throws PermissionException
     * @throws APIException
     * @throws PaymentRequestException
     * @throws AuthenticationException
     * @throws InvalidRequestException
     */
    private static RpayLiteResponse getRpayLiteResponse(String url_string, RequestOptions options, Map<String, Object> params, RequestMethod method)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException {
            HttpURLConnection connection = null;
        try {
            URL url = new URL(url_string);
            if (method == null) {
                throw new APIConnectionException("Null HTTP method specified.");
            }
            switch (method) {
            case GET:
                connection = createGetConnection(url, options);
                break;
            case POST:
                connection = createPostConnection(url, options, params);
                break;
            default:
                throw new APIConnectionException(
                        String.format("Unrecognized HTTP method %s. " + "This indicates a bug in the Rpay Lite bindings. Please contact us at "
                                + "https://checkout.faq.rakuten.ne.jp for assistance.", method));
            }
            connection.connect();
            int rCode = connection.getResponseCode();
            String rBody;
            Map<String, List<String>> headers;
            if (rCode >= 200 && rCode < 300) {
                rBody = getResponseBody(connection.getInputStream());
            } else {
                rBody = getResponseBody(connection.getErrorStream());
            }
            headers = connection.getHeaderFields();
            
            return new RpayLiteResponse(rCode, rBody, headers);

        } catch (IOException e) {
            throw new APIConnectionException(String.format(
                    "IOException during API request to Rpay lite (%s): %s "
                            + "Please check your internet connection and try again. If this problem persists,"
                            + "you should check Rpay Lite's service status," + " or let us know at https://checkout.faq.rakuten.ne.jp.",
                    RpayLite.getApiBase(), e.getMessage()), e);
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Get connection object
     * 
     * @param url
     *            url
     * @param options
     *            RequestOptions
     * @return connection object
     * @throws IOException
     */
    private static HttpURLConnection createRpayLiteConnection(URL url, RequestOptions options) throws IOException {
        String basic = String.format("Basic %s", DatatypeConverter.printBase64Binary((options.getApiKey() + ":").getBytes()));
        HttpURLConnection connection;
        if (RpayLite.getConnectionProxy() != null) {
            connection = (HttpURLConnection) url.openConnection(RpayLite.getConnectionProxy());
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return RpayLite.getProxyCredential();
                }
            });
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }

        
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", basic);
        connection.setRequestProperty("User-Agent", String.format("%s/%s", RpayLite.PRODUCT_NAME, RpayLite.VERSION));
        connection.setRequestProperty("charset", APIResource.CHARSET);
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);
        }
        return connection;
    }

    /**
     * Get connection object with GET method
     * 
     * @param url
     *            url
     * @param options
     *            RequestOptions
     * @return connection object
     * @throws IOException
     */
    private static HttpURLConnection createGetConnection(URL url, RequestOptions options) throws IOException {
        HttpURLConnection connection = createRpayLiteConnection(url, options);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("GET");
        return connection;
    }

    /**
     * Get connection object with POST method
     * 
     * @param url
     *            url
     * @param options
     *            RequestOptions
     * @param params
     *            parameters
     * @return connection object
     * @throws IOException
     */
    private static HttpURLConnection createPostConnection(URL url, RequestOptions options, Map<String, Object> params) throws IOException {
        StringBuilder postData = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(APIResource.urlEncode(param.getKey()));
                postData.append('=');
                postData.append(APIResource.urlEncode(String.valueOf(param.getValue())));
            }
        }
        byte[] postDataBytes = postData.toString().getBytes(APIResource.CHARSET);
        HttpURLConnection connection = createRpayLiteConnection(url, options);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        connection.getOutputStream().write(postDataBytes);
        return connection;
    }

    /**
     * Get response body from connection inputStream
     * 
     * @param responseStream
     *            connection inputStream
     * @return response body
     * @throws IOException
     */
    private static String getResponseBody(InputStream responseStream) throws IOException {
        // \A is the beginning of the stream boundary
        @SuppressWarnings("resource")
        String rBody = new Scanner(responseStream, APIResource.CHARSET).useDelimiter("\\A").next();
        responseStream.close();
        return rBody;
    }

    /**
     * Method for handling errors, throw exceptions according to response code
     * 
     * @param rBody
     *            error message
     * @param rCode
     *            error code
     * @throws InvalidRequestException
     * @throws AuthenticationException
     * @throws PaymentRequestException
     * @throws APIException
     * @throws PermissionException
     * @throws RateLimitException
     * @throws MethodNotAllowedException
     * @throws InternalServerException
     * @throws ServiceException
     */
    private static void handleAPIError(String rBody, int rCode) throws InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        List<Error> error = APIResource.GSON.fromJson(rBody, LiveRpayLiteResponseGetter.ErrorContainer.class).errors;
        for (Error e : error) {
            switch (rCode) {
            case 400:
                throw new InvalidRequestException(rCode, e.message);
            case 401:
                throw new AuthenticationException(rCode, e.message);
            case 402:
                throw new PaymentRequestException(rCode, e.message, e.type, e.code);
            case 403:
                throw new PermissionException(rCode, e.message);
            case 404:
                throw new InvalidRequestException(rCode, e.message);
            case 405:
                throw new MethodNotAllowedException(rCode, e.message);
            case 429:
                throw new RateLimitException(rCode, e.message);
            case 500:
                throw new InternalServerException(rCode, e.message);
            case 503:
                throw new ServiceException(rCode, e.message);
            default:
                throw new APIException(rCode, e.message, null);
            }
        }
    }

}
