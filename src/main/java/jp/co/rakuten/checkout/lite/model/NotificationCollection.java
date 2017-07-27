package jp.co.rakuten.checkout.lite.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import jp.co.rakuten.checkout.lite.RpayLite;
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
import jp.co.rakuten.checkout.lite.net.APIResource;
import jp.co.rakuten.checkout.lite.net.RequestOptions;

/**
 * This class is for the Notification List Object retrieved through API request. Each part of JSON code for notification list object can be parsed
 * into an instance of this class. One can retrieve each notification object contains in the list through getData() method.
 * 
 * @author rpayonline
 *
 */
public class NotificationCollection extends APIResource {

    String object;
    String url;
    int total;
    String charge;
    String id;
    List<Notification> data;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Notification> getData() {
        return data;
    }

    public void setData(List<Notification> data) {
        this.data = data;
    }

    /**
     * Retrieve single Notification object by charge id and notification id, redirect to NotificationCollection.retrieve(charge, id, options)
     * 
     * @param charge
     *            charge id
     * @param id
     *            notification id
     * @return Notification object
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws AuthenticationException
     * @throws PaymentRequestException
     * @throws APIException
     * @throws PermissionException
     * @throws RateLimitException
     * @throws ServiceException
     * @throws InternalServerException
     * @throws MethodNotAllowedException
     */
    protected static Notification retrieve(String charge, String id)
            throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        if (StringUtils.isEmpty(charge) || StringUtils.isEmpty(id)) {
            throw new InvalidRequestException(null, "Invalid input parameters.");
        }
        RequestOptions options = RequestOptions.builder().setApiKey(RpayLite.getApiKey()).build();
        return retrieve(charge, id, options);
    }

    /**
     * return email list, redirect to NotificationCollection.list(params, options)
     * 
     * @param params
     *            parameters
     * @return email list
     * @throws APIConnectionException
     * @throws RateLimitException
     * @throws PermissionException
     * @throws APIException
     * @throws PaymentRequestException
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws ServiceException
     * @throws InternalServerException
     * @throws MethodNotAllowedException
     */
    protected static NotificationCollection list(Map<String, Object> params)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        if (MapUtils.isEmpty(params)) {
            throw new InvalidRequestException(null, "Parameter is empty.");
        }

        RequestOptions options = RequestOptions.builder().setApiKey(RpayLite.getApiKey()).build();
        return list(params, options);
    }

    /**
     * Retrieve single Notification object by charge id and notification id
     * 
     * @param charge
     *            charge id
     * @param id
     *            notification id
     * @param options
     *            RequestOptions
     * @return Notification object
     * @throws APIConnectionException
     * @throws InvalidRequestException
     * @throws AuthenticationException
     * @throws PaymentRequestException
     * @throws APIException
     * @throws PermissionException
     * @throws RateLimitException
     * @throws ServiceException
     * @throws InternalServerException
     * @throws MethodNotAllowedException
     */
    private static Notification retrieve(String charge, String id, RequestOptions options)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("charge", charge);
        params.put("id", id);

        NotificationCollection nc = (NotificationCollection) request(RequestMethod.GET, formatURL(classURL(Notification.class), params), null,
                NotificationCollection.class, options);
        if (nc.getData().isEmpty()) {
            throw new InvalidRequestException(null, "Notification was not found.");
        }
        Notification n = nc.getData().get(0);

        return n;
    }

    /**
     * return email list
     * 
     * @param params
     *            parameters
     * @param options
     *            RequestOptions
     * @return email list
     * @throws APIConnectionException
     * @throws RateLimitException
     * @throws PermissionException
     * @throws APIException
     * @throws PaymentRequestException
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws ServiceException
     * @throws InternalServerException
     * @throws MethodNotAllowedException
     */
    private static NotificationCollection list(Map<String, Object> params, RequestOptions options)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {

        return request(RequestMethod.GET, formatURL(classURL(Notification.class), params), null, NotificationCollection.class, options);

    }

}
