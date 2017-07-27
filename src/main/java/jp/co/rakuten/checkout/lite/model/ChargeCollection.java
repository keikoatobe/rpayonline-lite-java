package jp.co.rakuten.checkout.lite.model;

import java.util.List;
import java.util.Map;

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
 * This class is for the Charge List Object retrieved through API request. Each part of JSON code for charge list object can be parsed into an
 * instance of this
 * class. One can retrieve each charge object contains in the list through getData() method.
 * 
 * @author rpayonline
 *
 */
public class ChargeCollection extends APIResource {

    String object;
    String url;
    int limit;
    int offset;
    Map<String, Boolean> payment;
    String id;
    String startingAfter;
    int total;
    Map<String, Long> created;
    List<Charge> data;

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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Map<String, Boolean> getPayment() {
        return payment;
    }

    public void setPayment(Map<String, Boolean> payment) {
        this.payment = payment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartingAfter() {
        return startingAfter;
    }

    public void setStartingAfter(String startingAfter) {
        this.startingAfter = startingAfter;
    }

    public Map<String, Long> getCreated() {
        return created;
    }

    public void setCreated(Map<String, Long> created) {
        this.created = created;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Charge> getData() {
        return data;
    }

    public void setData(List<Charge> data) {
        this.data = data;
    }

    /**
     * Retrieve Charge object by id, redirect to retrieve(id,options)
     * 
     * @param id
     *            Charge object id
     * @return Charge object
     * @throws APIConnectionException
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
    protected static Charge retrieve(String id)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RequestOptions options = RequestOptions.builder().setApiKey(RpayLite.getApiKey()).build();
        return retrieve(id, options);
    }

    /**
     * Get list of charge objects, redirect to ChargeCollection.list(params,options)
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
    protected static ChargeCollection list(Map<String, Object> params)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RequestOptions options = RequestOptions.builder().setApiKey(RpayLite.getApiKey()).build();
        return list(params, options);
    }

    /**
     * Retrieve single Charge object by id
     * 
     * @param id
     *            Charge object id
     * @param options
     *            RequestOptions
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
    private static Charge retrieve(String id, RequestOptions options)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {

        String url_string = String.format("%s?id=%s", classURL(Charge.class), id);
        ChargeCollection cc = (ChargeCollection) request(RequestMethod.GET, url_string, null, ChargeCollection.class, options);

        if (cc.getData().isEmpty()) {
            throw new APIException(null, "Order was not found.", null);
        } else {
            Charge c = cc.getData().get(0);
            return c;
        }

    }

    /**
     * Get list of charge objects
     * 
     * @param params
     *            parameters
     * @param options
     *            RequestOptions
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
    private static ChargeCollection list(Map<String, Object> params, RequestOptions options)
            throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        return request(RequestMethod.GET, formatURL(classURL(Charge.class), params), null, ChargeCollection.class, options);

    }

}
