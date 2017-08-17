package jp.co.rakuten.checkout.lite.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import jp.co.rakuten.checkout.lite.RpayLiteTest;
import jp.co.rakuten.checkout.lite.exception.APIException;
import jp.co.rakuten.checkout.lite.exception.InvalidApiKeyException;
import jp.co.rakuten.checkout.lite.model.Button.ImageId;

public class ButtonTest extends RpayLiteTest {

    @Test
    public void testPublicKey() {
        Button b = new Button();
        b.setPublicKey("public_key");
        assertEquals("public_key", b.getPublicKey());
    }

    @Test
    public void testGetItems() {
        Button b = new Button();
        b.addItem("item-01", "item1", 100, 1);
        Item item = b.getItems().get(0);
        assertEquals(item.getUnitPrice(), 100);
    }

    @Test
    public void testCartId() {
        Button b = new Button();
        b.setCartId("cart_id");
        assertEquals("cart_id", b.getCartId());
    }

    @Test
    public void testImage() throws APIException {
        Button b = new Button();
        b.setImage(ImageId._3);
        assertEquals("3", b.getImage());
    }
    
    @Test(expected = APIException.class)
    public void testNullImage() throws APIException{
        Button b = new Button();
        b.setImage(null);
    }

    @Test
    public void testCallback() {
        Button b = new Button();
        b.setCallback("callback");
        assertEquals("callback", b.getCallback());
    }

    @Test
    public void testSig() {
        Button b = new Button();
        b.setSignature(true);
        assertTrue(b.getSignature());
    }

    @Test
    public void testBuild() throws APIException, InvalidApiKeyException {
        Button b = new Button(); 
        b.setPublicKey(TEST_PUBLIC);
        b.addItem("item-01", "item-name", 1122, 3);
        b.setCartId("cart-id-1");
        b.setCallback("callback");
        b.setSignature(true);
        System.out.println(b.build());
        assertEquals(
                "<script src=\"https://lite.checkout.rakuten.co.jp/s/js/checkout-lite-v1.js\" class=\"checkout-lite-button\" data-key=\"sandbox_public_888a3f17ab713c77c63ae105a44c46ee8f796c5fceb9bc3f78aea14b94b054c8ebd0fafd81b7595afe466226b787bbe3\" data-cart-id=\"cart-id-1\" data-callback=\"callback\" data-item-id-1=\"item-01\" data-item-name-1=\"item-name\" data-item-unit-price-1=\"1122\" data-item-quantity-1=\"3\" data-sig=\"07592988ea2a7ea44a26921cd1de6508cb588ebc\"></script>",
                b.build());
    }

    @Test
    public void testBuildNoSig() throws APIException, InvalidApiKeyException{
        Button b = new Button();
        b.setPublicKey(TEST_PUBLIC);
        b.addItem("item-01", "item_name", 1122, 3);
        b.setCartId("cart-id-1");
        b.setCallback("callback");
        b.setSignature(false);
        assertEquals(
                "<script src=\"https://lite.checkout.rakuten.co.jp/s/js/checkout-lite-v1.js\" class=\"checkout-lite-button\" data-key=\"sandbox_public_888a3f17ab713c77c63ae105a44c46ee8f796c5fceb9bc3f78aea14b94b054c8ebd0fafd81b7595afe466226b787bbe3\" data-cart-id=\"cart-id-1\" data-callback=\"callback\" data-item-id-1=\"item-01\" data-item-name-1=\"item_name\" data-item-unit-price-1=\"1122\" data-item-quantity-1=\"3\"></script>",
                b.build());
    }

    @Test
    public void testBuildNoCart() throws APIException, InvalidApiKeyException {
        Button b = new Button();
        b.setPublicKey(TEST_PUBLIC);
        b.addItem("item-01", "item_name", 1122, 3);
        b.setCallback("callback");
        b.setSignature(true);
        assertEquals(
                "<script src=\"https://lite.checkout.rakuten.co.jp/s/js/checkout-lite-v1.js\" class=\"checkout-lite-button\" data-key=\"sandbox_public_888a3f17ab713c77c63ae105a44c46ee8f796c5fceb9bc3f78aea14b94b054c8ebd0fafd81b7595afe466226b787bbe3\" data-callback=\"callback\" data-item-id-1=\"item-01\" data-item-name-1=\"item_name\" data-item-unit-price-1=\"1122\" data-item-quantity-1=\"3\" data-sig=\"0570e821c68b6a42baf1fcf01020fe0284e2fac1\"></script>",
                b.build());
    }

    @Test(expected = APIException.class)
    public void testEmptyKey() throws APIException, InvalidApiKeyException {
        Button b = new Button();
        b.setPublicKey("");
        b.setImage(ImageId._2);
        b.build();
    }

    @Test(expected = APIException.class)
    public void testNullKey() throws APIException, InvalidApiKeyException {
        Button b = new Button();
        b.setImage(ImageId._0);
        b.build();
    }

    @Test(expected = APIException.class)
    public void testEmptyItem() throws APIException, InvalidApiKeyException {
        Button b = new Button();
        b.setPublicKey("public_key");
        b.setImage(ImageId._1);
        b.build();
    }

}
