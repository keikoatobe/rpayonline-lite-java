package jp.co.rakuten.checkout.lite.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.Ignore;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ChargeTest extends RpayLiteTest {

    @ClassRule
    public static WireMockRule rpayLite = new WireMockRule(8091);

    public String normalChargeCollectionString;
    public ChargeCollection normalChargeCollectionObject;
    public Charge normalChargeObject;
    public Charge capturedChargeObject;
    public String capturedChargeString;
    public Charge refundedChargeObject;
    public String refundedChargeString;
    public Charge updatedChargeObject;
    public String updatedChargeString;

    @Before
    public void setUpAll() throws InvalidApiKeyException, UnsupportedEncodingException, FileNotFoundException {
        RpayLite.setApiKey(TEST_KEY);

        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("chargeCollectionExample.json"), "UTF-8");
        @SuppressWarnings("resource")
        Scanner s = new Scanner(reader).useDelimiter("\\A");
        normalChargeCollectionString = s.hasNext() ? s.next() : "";
        normalChargeCollectionObject = APIResource.GSON.fromJson(normalChargeCollectionString, ChargeCollection.class);
        normalChargeObject = normalChargeCollectionObject.getData().get(0);
        capturedChargeObject = normalChargeCollectionObject.getData().get(1);
        refundedChargeObject = normalChargeCollectionObject.getData().get(2);
        updatedChargeObject = normalChargeCollectionObject.getData().get(3);
        capturedChargeString = APIResource.GSON.toJson(capturedChargeObject);
        refundedChargeString = APIResource.GSON.toJson(refundedChargeObject);
        updatedChargeString = APIResource.GSON.toJson(updatedChargeObject);

        rpayLite.stubFor(get(urlEqualTo("/charges?id=" + normalChargeObject.getId())).willReturn(aResponse().withBody(normalChargeCollectionString)));
        rpayLite.stubFor(
                post(urlEqualTo("/charges/" + normalChargeObject.getId() + "/capture")).willReturn(aResponse().withBody(capturedChargeString)));
        rpayLite.stubFor(
                post(urlEqualTo("/charges/" + normalChargeObject.getId() + "/refund")).willReturn(aResponse().withBody(refundedChargeString)));
        rpayLite.stubFor(
                post(urlEqualTo("/update/charges/" + normalChargeObject.getId() + "/refund")).willReturn(aResponse().withBody(updatedChargeString)));
        rpayLite.stubFor(get(urlEqualTo("/charges?limit=3")).willReturn(aResponse().withBody(normalChargeCollectionString)));
    }

    @Ignore
    @Test
    public void testRetrive() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge ch = Charge.retrieve(TEST_ID_NORMAL);
        System.out.println(ch);
        assertEquals(TEST_ID_NORMAL, ch.getId());
    }

    @Test
    public void testRetrieveMock() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RpayLite.overrideApiBase("http://localhost:8091");
        Charge ch = Charge.retrieve(normalChargeObject.getId());
        assertEquals(normalChargeObject.getId(), ch.getId());
    }

    @Test(expected = APIException.class)
    public void testOrderNotFound() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge.retrieve("1800005627-20170623-1000003160");
    }

    @Test(expected = AuthenticationException.class)
    public void testIncorrectKey()
            throws InvalidApiKeyException, APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RpayLite.setApiKey(TEST_KEY + "0");
        Charge.retrieve(TEST_ID_NORMAL);
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveEmptyId() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge.retrieve("");
    }

    @Test(expected = InvalidRequestException.class)
    public void testRetrieveNullId() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge.retrieve(null);
    }

    @Ignore
    @Test
    public void testCapture() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge ch = Charge.retrieve(TEST_ID_NORMAL_1);
        System.out.println("ID:" + ch.getId() + "\n" + "Amount:" + ch.getAmount() + "\n" + "captured:" + ch.getCaptured() + "\n" + "refunded:"
                + ch.getRefunded());
        assertFalse(ch.getCaptured());
        ch = ch.capture();
        System.out.println("captured:\nID:" + ch.getId() + "\n" + "Amount:" + ch.getAmount() + "\n" + "captured:" + ch.getCaptured() + "\n"
                + "refunded:" + ch.getRefunded());
        assertTrue(ch.getCaptured());
    }

    @Test
    public void testCaptureMock() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RpayLite.overrideApiBase("http://localhost:8091");
        Charge ch = Charge.retrieve(normalChargeObject.getId());
        assertEquals(normalChargeObject.getId(), ch.getId());
        ch = ch.capture();
        assertTrue(ch.getCaptured());
    }

    @Test(expected = InvalidRequestException.class)
    public void testAlreadyCaptured() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge ch = Charge.retrieve(TEST_ID_CAPTURED);
        ch = ch.capture();
    }

    @Ignore
    @Test
    public void testUpdate() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge ch = Charge.retrieve(TEST_ID_NORMAL_1);
        System.out.println("ID:" + ch.getId() + "\n" + "Amount:" + ch.getAmount() + "\n" + "captured:" + ch.getCaptured() + "\n" + "refunded:"
                + ch.getRefunded());

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("item_id_1", "itemId-001");
        params.put("item_name_1", "item1");
        params.put("item_quantity_1", "8");
        params.put("item_unit_price_1", "500");

        ch = ch.update(params);
        System.out.println("refunded:\nID:" + ch.getId() + "\n" + "Amount:" + ch.getAmount() + "\n" + "captured:" + ch.getCaptured() + "\n"
                + "refunded:" + ch.getRefunded());
        assertEquals(4000, ch.getAmount());
    }

    @Test
    public void testUpdateMock() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RpayLite.overrideApiBase("http://localhost:8091/update");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("item_id_1", "itemId-001");
        params.put("item_name_1", "item1");
        params.put("item_quantity_1", "8");
        params.put("item_unit_price_1", "500");
        Charge ch = normalChargeObject.update(params);
        assertEquals(4000, ch.getAmount());
    }

    @Test(expected = InvalidRequestException.class)
    public void testAlreadyUpdated() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge ch = Charge.retrieve(TEST_ID_CAPTURED);
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("item_id_1", "itemId-001");
        params.put("item_name_1", "item1");
        params.put("item_quantity_1", "8");
        params.put("item_unit_price_1", "500");

        ch = ch.update(params);
    }

    @Test(expected = InvalidRequestException.class)
    public void testUpdateEmptyParams() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge ch = Charge.retrieve(TEST_ID_NORMAL);
        Map<String, Object> params = new HashMap<String, Object>();
        ch = ch.update(params);
    }

    @Test(expected = InvalidRequestException.class)
    public void testUpdateNullParams() throws InvalidRequestException, APIConnectionException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge ch = Charge.retrieve(TEST_ID_NORMAL);
        ch = ch.update(null);
    }

    @Ignore
    @Test
    public void testRefund() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge ch = Charge.retrieve(TEST_ID_NORMAL_2);
        System.out.println("ID:" + ch.getId() + "\n" + "Amount:" + ch.getAmount() + "\n" + "captured:" + ch.getCaptured() + "\n" + "refunded:"
                + ch.getRefunded());
        assertFalse(ch.getRefunded());
        ch = ch.refund();
        System.out.println("refunded:\nID:" + ch.getId() + "\n" + "Amount:" + ch.getAmount() + "\n" + "captured:" + ch.getCaptured() + "\n"
                + "refunded:" + ch.getRefunded());
        assertTrue(ch.getRefunded());
    }

    @Test
    public void testRefundeMock() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RpayLite.overrideApiBase("http://localhost:8091");
        Charge ch = Charge.retrieve(normalChargeObject.getId());
        assertEquals(normalChargeObject.getId(), ch.getId());
        ch = ch.refund();
        assertTrue(ch.getRefunded());
    }

    @Test(expected = InvalidRequestException.class)
    public void testAlreadyRefunded() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Charge ch = Charge.retrieve(TEST_ID_REFUNDED);
        ch = ch.refund();
    }

    @Ignore
    @Test
    public void testList() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("limit", 3);
        params.put("offset", 2);
        // params.put("payment[captured]", false);
        // params.put("payment[refunded]", true);
        // params.put("payment[paid]", false);
        ChargeCollection chargeList = Charge.list(params);
        System.out.println(chargeList);
        assertEquals(1301, chargeList.getTotal());
    }

    @Test
    public void testListMock() throws APIConnectionException, InvalidRequestException, AuthenticationException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RpayLite.overrideApiBase("http://localhost:8091");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("limit", 3);
        ChargeCollection chargeList = Charge.list(params);
        System.out.println(chargeList);
        assertEquals(normalChargeCollectionObject.getTotal(), chargeList.getTotal());
    }

    @Test
    public void testAddress() throws UnsupportedEncodingException {
        String address = "{\"country\": \"JP\",\"first_name\": \"tairou\",\"first_name_kana\": \"tairou\",\"last_name\":\"rakuten\",\"last_name_kana\": \"rakuten\",\"address_zip\": \"158-0094\",\"address_state\": \"Tokyo\",\"address_city\":\"setagayaku\",\"address_line\": \"tamagawa1-14-1\",\"tel\": \"010-0123-4567\"}";
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        Address a = gson.fromJson(address, Address.class);
        System.out.println(a);
        assertEquals("158-0094", a.getAddressZip());
    }
  
}
