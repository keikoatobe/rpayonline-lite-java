package jp.co.rakuten.checkout.lite.model;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
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
import jp.co.rakuten.checkout.lite.net.APIResource;

public class NotificationTest extends RpayLiteTest {

    @ClassRule
    public static WireMockRule rpayLite = new WireMockRule(8091);

    public String notificationCollectionString;
    public NotificationCollection notificationCollectionObject;
    public String notificationString;
    public Notification notificationObject;

    @Before
    public void setUpAll() throws InvalidApiKeyException, UnsupportedEncodingException {
        RpayLite.setApiKey(TEST_KEY);
        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("notificationExample.json"), "UTF-8");
        @SuppressWarnings("resource")
        Scanner s = new Scanner(reader).useDelimiter("\\A");
        notificationCollectionString = s.hasNext() ? s.next() : "";
        notificationCollectionObject = APIResource.GSON.fromJson(notificationCollectionString, NotificationCollection.class);
        notificationObject = notificationCollectionObject.getData().get(0);
        notificationString = APIResource.GSON.toJson(notificationObject);
        rpayLite.stubFor(get(urlEqualTo("/notifications?charge=" + notificationObject.getCharge() + "&id=" + notificationObject.getId()))
                .willReturn(aResponse().withBody(notificationCollectionString)));
        rpayLite.stubFor(post(urlEqualTo("/notifications/mail")).willReturn(aResponse().withBody(notificationString)));
        rpayLite.stubFor(get(urlEqualTo("/notifications?charge=" + notificationObject.getCharge()))
                .willReturn(aResponse().withBody(notificationCollectionString)));
    }

    @Ignore
    @Test
    public void testRetrieve() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Notification n = Notification.retrieve(TEST_CHARGE, TEST_EMAIL);
        assertEquals(TEST_EMAIL, n.getId());
        assertEquals(TEST_CHARGE, n.getCharge());
    }

    @Test
    public void testRetrieveMock() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RpayLite.overrideApiBase("http://localhost:8091");
        Notification n = Notification.retrieve(notificationObject.getCharge(), notificationObject.getId());
        System.out.print(n);
        assertEquals(notificationObject.getId(), n.getId());
        assertEquals(notificationObject.getCharge(), n.getCharge());
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveNullCharge() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Notification.retrieve(null, TEST_EMAIL);
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveEmptyCharge() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Notification.retrieve("", TEST_EMAIL);
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveNullId() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Notification.retrieve(TEST_CHARGE, null);
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveEmptyId() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Notification.retrieve(TEST_CHARGE, "");
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveNotFound() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Notification.retrieve(TEST_CHARGE, "ntfn_1de1c222e9224716a26f9d5e8bb0cb4f");
    }

    @Ignore
    @Test
    public void testSend() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("charge", TEST_ID_NORMAL_2);
        params.put("subject", "test subject");
        params.put("body", "商品を発送しました。\\n#CUSTOMER_NAME# 様のまたのご利用をお待ちしております。");
        Notification n = Notification.send(params);
        System.out.println(n.getBody());
        assertEquals("test subject", n.getSubject());
        assertEquals("商品を発送しました。\\n#CUSTOMER_NAME# 様のまたのご利用をお待ちしております。", n.getBody());
    }

    @Test
    public void testSendMock() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RpayLite.overrideApiBase("http://localhost:8091");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("charge", "1250000255-20150623-0000168715");
        params.put("subject", "通知件名");
        params.put("body", "通知本文");
        Notification n = Notification.send(params);
        assertEquals(notificationObject.getBody(), n.getBody());
    }

    @Test(expected = InvalidRequestException.class)
    public void testSendEmptyParams() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Map<String, Object> params = new HashMap<String, Object>();
        Notification.send(params);
    }

    @Test(expected = InvalidRequestException.class)
    public void testSendNullParams() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Notification.send(null);
    }

    @Ignore
    @Test
    public void testList() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("charge", TEST_CHARGE);
        NotificationCollection nc = Notification.list(params);
        assertEquals(1, nc.getTotal());
    }
    
    @Test
    public void testListMock() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException{
        RpayLite.overrideApiBase("http://localhost:8091");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("charge", "1250000255-20150623-0000168715");
        NotificationCollection nc = Notification.list(params);
        assertEquals(notificationCollectionObject.getTotal(), nc.getTotal());
    }

    @Test(expected = InvalidRequestException.class)
    public void testListEmptyParams() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Map<String, Object> params = new HashMap<String, Object>();
        Notification.list(params);
    }

    @Test(expected = InvalidRequestException.class)
    public void testListNullParams() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Notification.list(null);
    }

}
