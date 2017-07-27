package jp.co.rakuten.checkout.lite.net;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RpayLiteResponseTest {

    @Test
    public void trivialTest(){
        RpayLiteResponse response = new RpayLiteResponse(200, "body");
        response.setResponseCode(400);
        assertEquals(400, response.getResponseCode());
        response.setResponseBody("bodybody");
        assertEquals("bodybody", response.getResponseBody());
        assertEquals(null, response.getResponseHeaders());
    }
}
