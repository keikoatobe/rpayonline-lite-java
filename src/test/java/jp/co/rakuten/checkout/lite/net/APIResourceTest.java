package jp.co.rakuten.checkout.lite.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import jp.co.rakuten.checkout.lite.RpayLite;
import jp.co.rakuten.checkout.lite.RpayLiteTest;
import jp.co.rakuten.checkout.lite.exception.InvalidApiKeyException;
import jp.co.rakuten.checkout.lite.model.Charge;

public class APIResourceTest extends RpayLiteTest {

    @Before
    public void setUpAll() throws InvalidApiKeyException {
        RpayLite.setApiKey(TEST_KEY);
    }

    @Test
    public void testSetter() {
        RpayLiteResponseGetter srg = new LiveRpayLiteResponseGetter();
        APIResource.setRpayLiteResponseGetter(srg);

    }

    @Test
    public void testEncode() throws UnsupportedEncodingException {
        assertNull(APIResource.urlEncode(null));

    }

    @Test
    public void testSingleURL() {
        assertEquals(RpayLite.getApiBase() + "/charge", APIResource.singleClassURL(Charge.class));
    }

    @Test
    public void testFormatURL() {
        Map<String, Object> params = new HashMap<String, Object>();
        assertEquals(RpayLite.getApiBase() + "?", APIResource.formatURL(RpayLite.getApiBase(), params));
        assertEquals(RpayLite.getApiBase() + "?", APIResource.formatURL(RpayLite.getApiBase(), null));
    }

}
