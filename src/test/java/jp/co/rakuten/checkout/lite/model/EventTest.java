package jp.co.rakuten.checkout.lite.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import jp.co.rakuten.checkout.lite.RpayLite;
import jp.co.rakuten.checkout.lite.RpayLiteTest;
import jp.co.rakuten.checkout.lite.exception.UnexpectedValueException;
import jp.co.rakuten.checkout.lite.net.Webhook;
import jp.co.rakuten.checkout.lite.net.Webhook.SignatureVerificationException;

public class EventTest extends RpayLiteTest {

    String payload = "";

    @Before
    public void setUpAll() {
        payload += "{\"object\": \"event\",\"id\": \"evt_ace3a9e65ad548a8b5c8de7965efa160\",\"livemode\": false,\"type\": \"charge.check\",\"synchronous\": true,\"data\": {\"object\": {\"object\": \"charge\",";
        payload += "\"open_id\": \"https://myid.rakuten.co.jp/openid/user/h65MxxxxxxxQxn0wJENoHHsalseDD==\", \"id\": null,\"cipher\": null,\"livemode\": false,\"currency\": \"jpy\",\"amount\": 5000,\"point\": 1000,";
        payload += "\"cart_id\": \"cart_id1\",\"paid\": false,\"captured\": false,\"status\": null,\"refunded\": false,";
        payload += "\"items\": [{\"id\": \"item_id1\",\"name\": \"item1\",\"quantity\": 10,\"unit_price\": 1000},{\"id\": \"item_id2\",\"name\": \"item2\",\"quantity\": 20,\"unit_price\": 2000}],";
        payload += "\"address\": null,\"created\": null,\"updated\": null}},\"pending_webhooks\":0,\"created\":1433862000}";
    }

    @Test
    public void testConstruct() throws UnexpectedValueException, SignatureVerificationException {
        RpayLite.setWebhookSignature("123");
        Event ev = Webhook.constructEvent(payload, "123", "123");
        assertEquals(ev.getId(), "evt_ace3a9e65ad548a8b5c8de7965efa160");
        assertEquals(ev.getData().getObject().getPoint(), 1000);
    }

    @Test(expected = SignatureVerificationException.class)
    public void testNullSigHeader() throws SignatureVerificationException, UnexpectedValueException {
        Webhook.constructEvent(payload, null, "123");
    }

    @Test(expected = SignatureVerificationException.class)
    public void testSignatureNotEqual() throws SignatureVerificationException, UnexpectedValueException {
        RpayLite.setWebhookSignature("123");
        Webhook.constructEvent(payload, "188", "123");

    }

    @Test(expected = UnexpectedValueException.class)
    public void testInvalidJson() throws SignatureVerificationException, UnexpectedValueException {
        String payloadError = "{\"object\" \"event\",\"id\": \"evt_0a28558a912043d7bb82ba0702afda7f\",\"livemode\": false,\"type\": \"ping\",\"synchronous\": true,\"data\": null,\"pending_webhooks\": 0,\"created\": 1499068723}";
        Webhook.constructEvent(payloadError, "188", "123");
    }

    @Test(expected = SignatureVerificationException.class)
    public void testNullSignature() throws SignatureVerificationException, UnexpectedValueException {
        Webhook.constructEvent(payload, "188", null);
    }

    @Test
    public void testExceptionSigHeader() throws UnexpectedValueException {
        try {
            Webhook.constructEvent(payload, "188", "123");
        } catch (SignatureVerificationException e) {
            assertEquals("188", e.getSigHeader());
        }
    }
}
