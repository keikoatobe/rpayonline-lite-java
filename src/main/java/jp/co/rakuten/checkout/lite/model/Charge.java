package jp.co.rakuten.checkout.lite.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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

import org.apache.commons.collections4.MapUtils;

/**
 * This class is for the Charge Object retrieved through API request. Each part of JSON code for charge object can be parsed into an instance of this
 * class. One can retrieve parameters of charge object through get method, e.g. charge.getId(). The class also contains methods for retrieving,
 * refunding, capturing, updating single charge, as well as retrieving charge list.
 * 
 * @author rpayonline
 *
 */
public class Charge extends APIResource {

    String id;
    String currency;
    int amount;
    int point;
    String cart_id;
    Boolean paid;
    Boolean captured;
    String status;
    Boolean refunded;
    List<Item> items;
    Address address;
    long created;
    long updated;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getCartId() {
        return cart_id;
    }

    public void setCartId(String cart_id) {
        this.cart_id = cart_id;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getCaptured() {
        return captured;
    }

    public void setCaptured(Boolean captured) {
        this.captured = captured;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getRefunded() {
        return refunded;
    }

    public void setRefunded(Boolean refunded) {
        this.refunded = refunded;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItem(List<Item> items) {
        this.items = items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    /**
     * Retrieve Charge object by id, redirect to ChargeCollection.retrieve(id)
     * 
     * @param id
     *            Charge object id
     * @return Charge object
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
    public static Charge retrieve(String id)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        if (StringUtils.isEmpty(id)) {
            throw new InvalidRequestException(400, "Charge id is empty.");
        }
        return ChargeCollection.retrieve(id);
    }

    /**
     * Cancel a charge
     * 
     * @return charge that canceled
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws RateLimitException
     * @throws PermissionException
     * @throws APIException
     * @throws PaymentRequestException
     * @throws AuthenticationException
     * @throws ServiceException
     * @throws InternalServerException
     * @throws MethodNotAllowedException
     */
    public Charge refund() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RequestOptions options = RequestOptions.builder().setApiKey(RpayLite.getApiKey()).build();
        return refund(options);
    }

    /**
     * Capture a charge
     * 
     * @return charge that captured
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws RateLimitException
     * @throws PermissionException
     * @throws APIException
     * @throws PaymentRequestException
     * @throws AuthenticationException
     * @throws ServiceException
     * @throws InternalServerException
     * @throws MethodNotAllowedException
     */
    public Charge capture() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RequestOptions options = RequestOptions.builder().setApiKey(RpayLite.getApiKey()).build();
        return capture(options);
    }

    /**
     * update charge items, redirect to update(params, options).
     * updated object need to differ from old object
     * 
     * @param params
     *            parameters
     * @return updated charge object
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws RateLimitException
     * @throws PermissionException
     * @throws APIException
     * @throws PaymentRequestException
     * @throws AuthenticationException
     * @throws ServiceException
     * @throws InternalServerException
     * @throws MethodNotAllowedException
     */
    public Charge update(Map<String, Object> params)
            throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        if (MapUtils.isEmpty(params)) {
            throw new InvalidRequestException(null, "Parameter is empty.");
        }

        RequestOptions options = RequestOptions.builder().setApiKey(RpayLite.getApiKey()).build();
        return update(params, options);

    }

    /**
     * Get the list of charge objects, redirect to ChargeCollection.list(params)
     * 
     * @param params
     *            parameters
     * @return list of charge objects
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
    public static ChargeCollection list(Map<String, Object> params)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        return ChargeCollection.list(params);
    }

    /**
     * Cancel a charge
     * 
     * @param options
     *            RequestOptions
     * @return Charge that canceled
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws RateLimitException
     * @throws PermissionException
     * @throws APIException
     * @throws PaymentRequestException
     * @throws AuthenticationException
     * @throws ServiceException
     * @throws InternalServerException
     * @throws MethodNotAllowedException
     */
    protected Charge refund(RequestOptions options)
            throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        String url_string = String.format("%s/%s", instanceURL(Charge.class, getId()), RpayLite.REFUND_API);
        return request(RequestMethod.POST, url_string, null, Charge.class, options);
    }

    /**
     * Capture a charge
     * 
     * @param options
     *            RequestOptions
     * @return charge that captured
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws RateLimitException
     * @throws PermissionException
     * @throws APIException
     * @throws PaymentRequestException
     * @throws AuthenticationException
     * @throws ServiceException
     * @throws InternalServerException
     * @throws MethodNotAllowedException
     */
    protected Charge capture(RequestOptions options)
            throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {

        String url_string = String.format("%s/%s", instanceURL(Charge.class, getId()), RpayLite.CAPTURE_API);
        return request(RequestMethod.POST, url_string, null, Charge.class, options);

    }

    /**
     * update charge items
     * 
     * @param params
     *            parameters
     * @param options
     *            RequestOptions
     * @return updated charge object
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws RateLimitException
     * @throws PermissionException
     * @throws APIException
     * @throws PaymentRequestException
     * @throws AuthenticationException
     * @throws ServiceException
     * @throws InternalServerException
     * @throws MethodNotAllowedException
     */
    protected Charge update(Map<String, Object> params, RequestOptions options)
            throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        String url_string = String.format("%s/%s", instanceURL(Charge.class, getId()), RpayLite.REFUND_API);
        return request(RequestMethod.POST, url_string, params, Charge.class, options);

    }

}
