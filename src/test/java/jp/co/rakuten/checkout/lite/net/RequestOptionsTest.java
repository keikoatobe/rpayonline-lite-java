package jp.co.rakuten.checkout.lite.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

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
import jp.co.rakuten.checkout.lite.net.RequestOptions.InvalidRequestOptionsException;
import jp.co.rakuten.checkout.lite.net.RequestOptions.RequestOptionsBuilder;

public class RequestOptionsTest extends RpayLiteTest {

    @Before
    public void setUpAll() throws InvalidApiKeyException, UnsupportedEncodingException, FileNotFoundException {
        RpayLite.setApiKey(TEST_KEY);
    }

    @Test(expected = InvalidRequestOptionsException.class)
    public void testEmptyKey() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException, APIException,
            PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException {
        RequestOptions.builder().setApiKey("  ").build();
    }

    @Test
    public void testSetVersion() {
        RequestOptions options = RequestOptions.builder().setRpayLiteVersion(null).build();
        assertEquals(null, options.getRpayLiteVersion());
    }

    @Test(expected = InvalidRequestOptionsException.class)
    public void testEmptyVersion() {
        RequestOptions.builder().setRpayLiteVersion("  ").build();
    }

    @Test(expected = InvalidRequestOptionsException.class)
    public void testEmptyIKey() {
        RequestOptions.builder().setIdempotencyKey("  ").build();
    }

    @Test(expected = InvalidRequestOptionsException.class)
    public void testEmptyAccout() {
        RequestOptions.builder().setRpayLiteAccount("  ").build();
    }

    @Test
    public void trivialOptionTest() {
        RequestOptions options = RequestOptions.getDefault();
        assertEquals(TEST_KEY, options.getApiKey());
        assertEquals(RpayLite.VERSION, options.getRpayLiteVersion());
        assertEquals(null, options.getIdempotencyKey());
        assertEquals(null, options.getRpayLiteAccount());
        System.out.println(options.hashCode());
    }

    @Test
    public void trivialBuilderTest() {
        RequestOptionsBuilder builder = new RequestOptionsBuilder();
        assertEquals(TEST_KEY, builder.getApiKey());
        builder.setRpayLiteVersion("1.0");
        builder.setIdempotencyKey("idempotencyKey");
        builder.setRpayLiteAccount("rpayLiteAccount");
        assertEquals("idempotencyKey", builder.getIdempotencyKey());
        assertEquals("rpayLiteAccount", builder.getRpayLiteAccount());
        builder.build();
    }

    @Test
    public void builderClearTest() {
        RequestOptionsBuilder builder = new RequestOptionsBuilder();
        builder.clearApiKey();
        builder.clearIdempotencyKey();
        builder.clearRltieVersion();
        builder.clearRpayLiteAccount();
    }

    @Test(expected = InvalidRequestOptionsException.class)
    public void testLongKey() {
        String key = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        RequestOptions.builder().setIdempotencyKey(key).build();
    }
    
    @Test
    public void testEqual(){
        RequestOptions options = RequestOptions.getDefault();
        RequestOptions options2 = RequestOptions.getDefault();
        RequestOptions otherKey = RequestOptions.builder().setApiKey("something").build();
        RequestOptions nullKey = RequestOptions.builder().setApiKey(null).build();
        RequestOptions otherIKey = RequestOptions.builder().setIdempotencyKey("something").build();
        RequestOptions someIKey = RequestOptions.builder().setIdempotencyKey("something_else").build();
        RequestOptions otherVersion = RequestOptions.builder().setRpayLiteVersion("something").build();
        RequestOptions nullVersion = RequestOptions.builder().setRpayLiteVersion(null).build();
        
        assertTrue(options.equals(options));
        assertFalse(options.equals(null));
        assertFalse(options.equals("abc"));
        
        assertFalse(options.equals(otherKey));
        assertFalse(nullKey.equals(options));
        assertFalse(otherIKey.equals(someIKey));
        assertFalse(options.equals(otherIKey));
        assertFalse(options.equals(otherVersion));
        assertFalse(nullVersion.equals(options));
        assertTrue(options.equals(options2));
    }

}
