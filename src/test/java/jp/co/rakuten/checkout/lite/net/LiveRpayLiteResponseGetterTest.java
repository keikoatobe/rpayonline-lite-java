package jp.co.rakuten.checkout.lite.net;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.UnknownHostException;

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

public class LiveRpayLiteResponseGetterTest extends RpayLiteTest {

    @ClassRule
    public static WireMockRule rpayLite = new WireMockRule(8091);

    RpayLiteResponseGetter responseGetter = new LiveRpayLiteResponseGetter();

    @Before
    public void setUpAll() throws InvalidApiKeyException, UnsupportedEncodingException, FileNotFoundException {
        RpayLite.setApiKey(TEST_KEY);
        RpayLite.setConnectionProxy(null);
        rpayLite.stubFor(get(urlEqualTo("/normal")).withHeader("User-Agent", matching(String.format("%s/%s", RpayLite.PRODUCT_NAME, RpayLite.VERSION))).willReturn(aResponse().withBody("{}")));
        rpayLite.stubFor(get(urlEqualTo("/useproxy")).willReturn(aResponse().proxiedFrom("http://localhost")));
    }

    @Test
    public void testNullOption() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RpayLite.overrideApiBase("http://localhost:8091");
        responseGetter.request(RequestMethod.GET, RpayLite.getApiBase() + "/normal", null, this.getClass(), RequestType.NORMAL, null);
    }

    @Test(expected = APIConnectionException.class)
    public void testNullMethod() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RpayLite.overrideApiBase("http://localhost:8091");
        responseGetter.request(null, RpayLite.getApiBase() + "/normal", null, this.getClass(), RequestType.NORMAL, null);
    }

    @Test(expected = AuthenticationException.class)
    public void testNullKey() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RequestOptions options = RequestOptions.builder().setApiKey(null).build();
        responseGetter.request(RequestMethod.GET, RpayLite.getApiBase() + "/normal", null, this.getClass(), RequestType.NORMAL, options);
    }


    @Test(expected = APIConnectionException.class)
    public void testProxyError() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        RpayLite.overrideApiBase("http://localhost:8091");
        RpayLite.setConnectionProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("8080", 0)));
        RpayLite.setProxyCredential(new PasswordAuthentication("username", "password".toCharArray()));
        responseGetter.request(RequestMethod.GET, RpayLite.getApiBase() + "/normal", null, this.getClass(), RequestType.NORMAL, null);
    }
    
    @Test
    public void SSLtrivialTest() throws UnknownHostException, IOException{
        RpayLiteSSLSocketFactory socket = new RpayLiteSSLSocketFactory();
        socket.getDefaultCipherSuites();
        socket.getSupportedCipherSuites();
    }
   
}
