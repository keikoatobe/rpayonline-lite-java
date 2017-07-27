package jp.co.rakuten.checkout.lite.net;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import jp.co.rakuten.checkout.lite.RpayLite;
import jp.co.rakuten.checkout.lite.RpayLiteTest;
import jp.co.rakuten.checkout.lite.exception.APIConnectionException;
import jp.co.rakuten.checkout.lite.exception.APIException;
import jp.co.rakuten.checkout.lite.exception.AuthenticationException;
import jp.co.rakuten.checkout.lite.exception.InternalServerException;
import jp.co.rakuten.checkout.lite.exception.InvalidApiKeyException;
import jp.co.rakuten.checkout.lite.exception.InvalidRequestException;
import jp.co.rakuten.checkout.lite.exception.MethodNotAllowedException;
import jp.co.rakuten.checkout.lite.exception.PaymentRequestException;
import jp.co.rakuten.checkout.lite.exception.PermissionException;
import jp.co.rakuten.checkout.lite.exception.RateLimitException;
import jp.co.rakuten.checkout.lite.exception.ServiceException;
import jp.co.rakuten.checkout.lite.net.APIResource.RequestMethod;
import jp.co.rakuten.checkout.lite.net.APIResource.RequestType;

public class ExceptionTest extends RpayLiteTest {
    
    @ClassRule
    public static WireMockRule rpayLite = new WireMockRule(8091);

    RpayLiteResponseGetter responseGetter = new LiveRpayLiteResponseGetter();
    String paymentError = "{\"errors\": [{\"type\": \"payment_error\",\"code\": \"invalid_payment\",\"message\": \"invalid_payment_status\"}]}";
    String permissionError = "{\"errors\": [{\"type\": \"permission_error\",\"code\": \"invalid_format\",\"message\": \"Permission is denied\"}]}";
    String requestError = "{\"errors\": [{\"type\": \"invalid_request_error\",\"code\": \"invalid_format\",\"message\": \"page not found\"}]}";
    String requestMethodError = "{\"errors\": [{\"type\": \"invalid_method\",\"code\": \"invalid_method\",\"message\": \"method not allowed\"}]}";
    String rateLimitError = "{\"errors\": [{\"type\": \"rate_limit\",\"code\": \"rate_limit\",\"message\": \"limit_exceeded\"}]}";
    String internalServerError = "{\"errors\": [{\"type\": \"server_error\",\"code\": \"server_error\",\"message\": \"cannot connect to server\"}]}";
    String serviceError = "{\"errors\": [{\"type\": \"service_error\",\"code\": \"service_error\",\"message\": \"service unavailable\"}]}";
    String apiError = "{\"errors\": [{\"type\": \"api_error\",\"code\": \"invalid_request\",\"message\": \"api error\"}]}";
    

    @Before
    public void setUpAll() throws InvalidApiKeyException, UnsupportedEncodingException, FileNotFoundException {
        RpayLite.setApiKey(TEST_KEY);
        RpayLite.overrideApiBase("http://localhost:8091");
        rpayLite.stubFor(get(urlEqualTo("/exception/payment")).willReturn(aResponse().withStatus(402).withBody(paymentError)));
        rpayLite.stubFor(get(urlEqualTo("/exception/permission")).willReturn(aResponse().withStatus(403).withBody(permissionError)));
        rpayLite.stubFor(get(urlEqualTo("/exception/request404")).willReturn(aResponse().withStatus(404).withBody(requestError)));
        rpayLite.stubFor(get(urlEqualTo("/exception/method")).willReturn(aResponse().withStatus(405).withBody(requestMethodError)));
        rpayLite.stubFor(get(urlEqualTo("/exception/rate")).willReturn(aResponse().withStatus(429).withBody(rateLimitError)));
        rpayLite.stubFor(get(urlEqualTo("/exception/server")).willReturn(aResponse().withStatus(500).withBody(internalServerError)));
        rpayLite.stubFor(get(urlEqualTo("/exception/service")).willReturn(aResponse().withStatus(503).withBody(serviceError)));
        rpayLite.stubFor(get(urlEqualTo("/exception/api")).willReturn(aResponse().withStatus(666).withBody(apiError)));
        rpayLite.stubFor(get(urlEqualTo("/exception/connection")).willReturn(aResponse().withStatus(777).withBody(apiError)));
    }

    @SuppressWarnings("unchecked")
    public <T> T request(String url) throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RequestOptions options = RequestOptions.builder().setApiKey(RpayLite.getApiKey()).build();
        return (T) responseGetter.request(RequestMethod.GET, url, null, this.getClass(), RequestType.NORMAL, options);
    }

    @Test(expected = PaymentRequestException.class)
    public void test402() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        request(RpayLite.getApiBase() + "/exception/payment");
    }
    
    @Test(expected = PermissionException.class)
    public void test403() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        request(RpayLite.getApiBase() + "/exception/permission");
    }
    
    @Test(expected = InvalidRequestException.class)
    public void test404() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        request(RpayLite.getApiBase() + "/exception/request404");
    }
    
    @Test
    public void InvalidRequestTest() throws AuthenticationException, APIConnectionException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        try{
            request(RpayLite.getApiBase() + "/exception/request404");
        }catch(InvalidRequestException e){
            InvalidRequestException ire = new InvalidRequestException(null, e.getMessage(), e.getType(), e.getCode());
            assertEquals(e.getType(), ire.getType());
            assertEquals(e.getCode(), ire.getCode());
            ire.toString();
        }
    }

    @Test(expected = MethodNotAllowedException.class)
    public void test405() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        request(RpayLite.getApiBase() + "/exception/method");
    }
    
    @Test(expected = RateLimitException.class)
    public void test429() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        request(RpayLite.getApiBase() + "/exception/rate");
    }
    
    @Test(expected = InternalServerException.class)
    public void test500() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        request(RpayLite.getApiBase() + "/exception/server");
    }
    
    @Test(expected = ServiceException.class)
    public void test503() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        request(RpayLite.getApiBase() + "/exception/service");
    }
    
    @Test(expected = APIException.class)
    public void test666() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        request(RpayLite.getApiBase() + "/exception/api");
    }
    
    @Test
    public void test777() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        try{
            request(RpayLite.getApiBase() + "/exception/connection");
        }catch(APIException e){
            APIConnectionException ace = new APIConnectionException(e.getMessage());
            assertEquals(0, (int) ace.getStatusCode());
        }
    }
    
}





