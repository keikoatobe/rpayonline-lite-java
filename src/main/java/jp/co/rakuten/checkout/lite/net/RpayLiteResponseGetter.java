package jp.co.rakuten.checkout.lite.net;

import java.util.List;
import java.util.Map;

import jp.co.rakuten.checkout.lite.exception.APIConnectionException;
import jp.co.rakuten.checkout.lite.exception.APIException;
import jp.co.rakuten.checkout.lite.exception.AuthenticationException;
import jp.co.rakuten.checkout.lite.exception.PaymentRequestException;
import jp.co.rakuten.checkout.lite.exception.InternalServerException;
import jp.co.rakuten.checkout.lite.exception.InvalidRequestException;
import jp.co.rakuten.checkout.lite.exception.MethodNotAllowedException;
import jp.co.rakuten.checkout.lite.exception.PermissionException;
import jp.co.rakuten.checkout.lite.exception.RateLimitException;
import jp.co.rakuten.checkout.lite.exception.ServiceException;

/**
 * This interface defines method
 * {@link #request(jp.co.rakuten.checkout.lite.net.APIResource.RequestMethod, String, Map, Class, jp.co.rakuten.checkout.lite.net.APIResource.RequestType, RequestOptions)
 * request(requestMethod, url, parameters, clazzObject, requestType, requestOptions)}, and class
 * {@link jp.co.rakuten.checkout.lite.net.RpayLiteResponseGetter.Error Error} for holding errors.
 * 
 * @author rpayonline
 *
 */
public interface RpayLiteResponseGetter {

    /**
     * return the requested object regarding class name
     * 
     * @param method
     *            RequestMethod
     * @param url
     *            connection url
     * @param params
     *            parameters
     * @param clazz
     *            class object to be returned
     * @param type
     *            RequestType, default NORMAL
     * @param options
     *            RequestOptions
     * @return requested object
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
    public <T> T request(APIResource.RequestMethod method, String url, Map<String, Object> params, Class<T> clazz, APIResource.RequestType type,
            RequestOptions options) throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException;

    /**
     * Class for holding single error, error type, code and message are stored.
     * 
     * @author rpayonline
     *
     */
    public static class Error {
        public String type;
        public String code;
        public String message;
    }

    /**
     * Class for containing errors.
     * 
     * @author rpayonline
     *
     */
    static class ErrorContainer {
        List<Error> errors;
    }

}
