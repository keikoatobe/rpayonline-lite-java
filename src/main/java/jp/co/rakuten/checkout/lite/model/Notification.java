package jp.co.rakuten.checkout.lite.model;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;

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
import jp.co.rakuten.checkout.lite.net.APIResource;
import jp.co.rakuten.checkout.lite.net.RequestOptions;

/**
 * This class is for the Notification Object retrieved through API request. Each part of JSON code for notification object (email) can be parsed into
 * an instance of this class. One can retrieve parameters of notification object through get method, e.g. notification.getId(). The class also
 * contains methods for retrieving and sending single notification (email), as well as retrieving notification list.
 * 
 * @author rpayonline
 *
 */
public class Notification extends APIResource {
    String object;
    String id;
    String charge;
    String type;
    String subject;
    String body;
    long created;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * Retrieve single Notification object by charge id and notification id, redirect to NotificationCollection.retrieve(charge, id)
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
    public static Notification retrieve(String charge, String id)
            throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        return NotificationCollection.retrieve(charge, id);
    }

    /**
     * Send email to customer, redirect to send(params, options)
     * 
     * @param params
     *            charge id & subject & body
     * @return email sent
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
    public static Notification send(Map<String, Object> params)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        if (MapUtils.isEmpty(params)) {
            throw new InvalidRequestException(null, "Parameter is empty.");
        }
        RequestOptions options = RequestOptions.builder().setApiKey(RpayLite.getApiKey()).build();
        return send(params, options);
    }

    /**
     * Send email to customer
     * 
     * @param params
     *            charge id & subject & body
     * @param options
     *            RequestOptions
     * @return email sent
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
    public static Notification send(Map<String, Object> params, RequestOptions options)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        String url_string = String.format("%s/%s", classURL(Notification.class), RpayLite.MAIL_API);

        return request(RequestMethod.POST, url_string, params, Notification.class, options);

    }

    /**
     * return email list with specified charge id, redirect to NotificationCollection.list(params)
     * 
     * @param params
     *            charge id & notification id(optional)
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
    public static NotificationCollection list(Map<String, Object> params)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        return NotificationCollection.list(params);

    }

}
