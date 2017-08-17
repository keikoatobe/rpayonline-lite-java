package jp.co.rakuten.checkout.lite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

import org.junit.BeforeClass;
import org.junit.Test;

import jp.co.rakuten.checkout.lite.exception.InvalidApiKeyException;

public class RpayLiteTest {

    protected static final String TEST_KEY = "sandbox_private_41ab62a7db0ee44526d1f0f62fc494878f796c5fceb9bc3f78aea14b94b054c8ebd0fafd81b7595afe466226b787bbe3";
    protected static final String TEST_PUBLIC = "sandbox_public_888a3f17ab713c77c63ae105a44c46ee8f796c5fceb9bc3f78aea14b94b054c8ebd0fafd81b7595afe466226b787bbe3";
    protected static final String TEST_ID_NORMAL = "1250000255-20170720-0000003567";
    protected static final String TEST_ID_NORMAL_1 = "1250000255-20170726-0000003614";
    protected static final String TEST_ID_NORMAL_2 = "1250000255-20170726-0000003615";
    protected static final String TEST_ID_REFUNDED = "1250000255-20170726-0000003617";
    protected static final String TEST_ID_CAPTURED = "1250000255-20170726-0000003616";
    protected static final String TEST_CHARGE = "1250000255-20170718-0000003555";
    protected static final String TEST_EMAIL = "ntfn_e916da337cde47db90a78ca53d58ce90";

    @Test
    public void testSetKey() throws InvalidApiKeyException {
        RpayLite.setApiKey(TEST_KEY);
        assertEquals(TEST_KEY, RpayLite.getApiKey());
        assertTrue(RpayLite.isSandbox);
    }
    
    @Test 
    public void testLive() throws InvalidApiKeyException{
        RpayLite.setApiKey("live_private_XXXX");
        assertEquals("live_private_XXXX", RpayLite.getApiKey());
        assertFalse(RpayLite.isSandbox);
    }
    
    @Test 
    public void testTestLive() throws InvalidApiKeyException{
        RpayLite.setApiKey("test_private_XXXX");
        assertEquals("test_private_XXXX", RpayLite.getApiKey());
        assertFalse(RpayLite.isSandbox);
    }
    
    @Test
    public void testApiEndpoint() {
        assertEquals("https://api.lite.checkout.rakuten.co.jp", RpayLite.API_ENDPOINT);
    }
    
    @Test
    public void testApiBaseSandbox() throws InvalidApiKeyException{
        RpayLite.setApiKey(TEST_KEY);
        assertEquals("https://api.lite.checkout.rakuten.co.jp/sandbox/v1", RpayLite.getApiBase());
    }
    
    @Test(expected = InvalidApiKeyException.class)
    public void testInvalidApiKeyException() throws InvalidApiKeyException{
        RpayLite.setApiKey("some_key");
    }
    
    @Test(expected = InvalidApiKeyException.class)
    public void testSetNullKey() throws InvalidApiKeyException{
        RpayLite.setApiKey(null);
    }
    
    @Test
    public void testWebhookSignature(){
        RpayLite.setWebhookSignature("some_signature");
        assertEquals(RpayLite.getWebhookSignature(), "some_signature");
    }

    @Test 
    public void testConnectionProxy(){
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("http://localhost", 8080));
        RpayLite.setConnectionProxy(proxy);
        assertEquals(proxy, RpayLite.getConnectionProxy());
        PasswordAuthentication credential = new PasswordAuthentication("username", "password".toCharArray());
        RpayLite.setProxyCredential(credential);
        assertEquals(credential, RpayLite.getProxyCredential());
    }
    
    @Test
    public void testOverrideApiBase(){
        RpayLite.overrideApiBase("override_base");
        assertEquals(RpayLite.getApiBase(), "override_base");
    }            

    @BeforeClass
    public static void setUp() throws InvalidApiKeyException {
        RpayLite.setApiKey(TEST_KEY);
        RpayLite.setConnectionProxy(null);
    }
}