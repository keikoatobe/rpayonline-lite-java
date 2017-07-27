package jp.co.rakuten.checkout.lite.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import jp.co.rakuten.checkout.lite.model.RpayLiteObject;

/**
 * This class contains resources that are useful when make API request. This class is extended by all model classes except
 * {@link jp.co.rakuten.checkout.lite.model.Button Button}.
 * 
 * @author rpayonline
 *
 */
public abstract class APIResource extends RpayLiteObject {

    public enum RequestMethod {
        GET, POST
    }

    public enum RequestType {
        NORMAL, MULTIPART
    }

    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    public static final String CHARSET = "UTF-8";

    private static RpayLiteResponseGetter rpayLiteResponseGetter = new LiveRpayLiteResponseGetter();

    public static void setRpayLiteResponseGetter(RpayLiteResponseGetter srg) {
        APIResource.rpayLiteResponseGetter = srg;
    }

    /**
     * return encoded string
     * 
     * @param str
     *            input string
     * @return encoded string
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String str) throws UnsupportedEncodingException {

        if (str == null) {
            return null;
        } else {
            return URLEncoder.encode(str, CHARSET);
        }
    }

    /**
     * return url without 's'
     * 
     * @param clazz
     *            Class
     * @return url without 's'
     */
    protected static String singleClassURL(Class<?> clazz) {
        return singleClassURL(clazz, RpayLite.getApiBase());
    }

    /**
     * return url without 's'
     * 
     * @param clazz
     *            Class
     * @param apiBase
     *            API base
     * @return url without 's'
     */
    protected static String singleClassURL(Class<?> clazz, String apiBase) {
        return String.format("%s/%s", apiBase, className(clazz));
    }

    /**
     * return url with 's' at the end
     * 
     * @param clazz
     *            Class
     * @return url with 's' at the end
     */
    protected static String classURL(Class<?> clazz) {
        return classURL(clazz, RpayLite.getApiBase());
    }

    /**
     * return url with 's' at the end
     * 
     * @param clazz
     *            Class
     * @param apiBase
     *            API base
     * @return url with 's' at the end
     */
    protected static String classURL(Class<?> clazz, String apiBase) {
        return String.format("%ss", singleClassURL(clazz, apiBase));
    }

    /**
     * return url with id at the end
     * 
     * @param clazz
     *            class
     * @param id
     *            object id
     * @return url with id at the end
     * @throws InvalidRequestException
     */
    protected static String instanceURL(Class<?> clazz, String id) throws InvalidRequestException {
        return instanceURL(clazz, id, RpayLite.getApiBase());
    }

    /**
     * return url with id at the end
     * 
     * @param clazz
     *            class
     * @param id
     *            object id
     * @param apiBase
     *            api base
     * @return url with id at the end
     * @throws InvalidRequestException
     */
    protected static String instanceURL(Class<?> clazz, String id, String apiBase) throws InvalidRequestException {
        try {
            return String.format("%s/%s", classURL(clazz, apiBase), urlEncode(id));
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException(null, "Unable to encode parameters to " + CHARSET + ". Please contact https://checkout.faq.rakuten.ne.jp for assistance.", e);
        }
    }

    /**
     * Get url string with input parameters appended
     * 
     * @param urlBase
     *            base url
     * @param params
     *            parameters
     * @return url string with input parameters appended
     */
    protected static String formatURL(String urlBase, Map<String, Object> params) {
        StringBuilder url_string = new StringBuilder();
        if ( params!=null && (!params.isEmpty())) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (url_string.length() != 0) {
                    url_string.append('&');
                }
                url_string.append(param.getKey());
                url_string.append('=');
                url_string.append(param.getValue());
            }
        }
        return String.format("%s?%s", urlBase, url_string.toString());
    }

    /**
     * call rpayLiteResponseGetter.request and return request object regarding class name
     * 
     * @param method
     *            Request method
     * @param url
     *            request url
     * @param params
     *            parameters
     * @param clazz
     *            target class
     * @param options
     *            RequestOptions
     * @return request object
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws PaymentRequestException
     * @throws APIException
     * @throws PermissionException
     * @throws RateLimitException
     * @throws MethodNotAllowedException
     * @throws InternalServerException
     * @throws ServiceException
     */
    protected static <T> T request(APIResource.RequestMethod method, String url, Map<String, Object> params, Class<T> clazz, RequestOptions options)
            throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        return APIResource.rpayLiteResponseGetter.request(method, url, params, clazz, APIResource.RequestType.NORMAL, options);
    }

    /**
     * get class name
     * 
     * @param clazz
     *            class
     * @return lower case class simple name
     */
    private static String className(Class<?> clazz) {
        String className = clazz.getSimpleName().toLowerCase().replace("$", " ");

        return className;
    }

}
