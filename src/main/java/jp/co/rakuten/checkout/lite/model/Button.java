package jp.co.rakuten.checkout.lite.model;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import jp.co.rakuten.checkout.lite.RpayLite;
import jp.co.rakuten.checkout.lite.exception.APIException;
import jp.co.rakuten.checkout.lite.exception.InvalidApiKeyException;

/**
 * This class is used for building the HTML code for generating the Rpay Lite button. By inputing necessary information and calling build() method,
 * the HTML code for the button will be returned.
 * <p>
 * Example code for button:<br>
 * 
 * <pre>
 * {@code<script
 *   src="https://lite.checkout.rakuten.co.jp/s/js/checkout-lite-v1.js"
 *   data-key="sandbox_public_xxxxxx"
 *   class="checkout-lite-button"
 *   data-item-id-1="itemId-001"
 *   data-item-name-1="商品名"
 *   data-item-unit-price-1="2000"
 *   data-item-quantity-1="1">
 * </script>}
 * </pre>
 * 
 * @author rpayonline
 *
 */
public class Button extends RpayLiteObject {

    public enum ImageId {
        _0, _1, _2, _3
    }

    /******* Compulsory *******/
    static final String SRC = "https://lite.checkout.rakuten.co.jp/s/js/checkout-lite-v1.js";
    static final String CLASS = "checkout-lite-button";

    String publicKey;
    ArrayList<Item> items;

    /******* Optional *******/
    String cartId;
    String image;
    String callback;
    boolean signature;

    public Button() {
        publicKey = null;
        items = new ArrayList<Item>();
        cartId = null;
        image = null;
        callback = null;
        signature = false;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Add an item with id, name, unit price and quantity
     * 
     * @param id
     *            item id
     * @param name
     *            item name
     * @param unitPrice
     *            item unit price
     * @param quantity
     *            item quantity
     */
    public void addItem(String id, String name, int unitPrice, int quantity) {
        Item item = new Item(id, name, unitPrice, quantity);
        items.add(item);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getImage() {
        return image;
    }

    /**
     * set data-image
     * 
     * @param imageId
     *            Enum, choose from _0, _1, _2, _3
     * @throws APIException
     *             if imageId is null or unsupported value
     */
    public void setImage(ImageId imageId) throws APIException {
        if (imageId == null) {
            throw new APIException(null, "Image number cannot be null.", null);
        }
        switch (imageId) {
        case _0:
            image = "0";
            break;
        case _1:
            image = "1";
            break;
        case _2:
            image = "2";
            break;
        case _3:
            image = "3";
            break;
        default:
            throw new APIException(null, "Invalid image number.", null);
        }
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public void setSignature(boolean b) {
        signature = b;
    }

    public boolean getSignature() {
        return signature;
    }

    /**
     * build html string for creating a button
     * 
     * @return html code for creating button
     * @throws APIException
     *             if public key is empty/null or item is empty
     * @throws InvalidApiKeyException
     *             if private key is empty or null
     */
    public String build() throws APIException, InvalidApiKeyException {
        StringBuilder html = new StringBuilder("<script");
        html.append(" src=\"" + SRC + "\"");
        html.append(" class=\"" + CLASS + "\"");

        if (StringUtils.isEmpty(publicKey)) {
            throw new APIException(null, "Public key is empty.", null);
        }
        html.append(" data-key=\"" + publicKey + "\"");

        if (cartId != null) {
            html.append(" data-cart-id=\"" + cartId + "\"");
        }
        if (image != null) {
            html.append(" data-image=\"" + image + "\"");
        }
        if (callback != null) {
            html.append(" data-callback=\"" + callback + "\"");
        }
        if (items.isEmpty()) {
            throw new APIException(null, "No item selected.", null);
        }
        for (int i = 1; i <= items.size(); i++) {
            html.append(" data-item-id-" + i + "=\"" + items.get(i - 1).getId() + "\"");
            html.append(" data-item-name-" + i + "=\"" + items.get(i - 1).getName() + "\"");
            html.append(" data-item-unit-price-" + i + "=\"" + items.get(i - 1).getUnitPrice() + "\"");
            html.append(" data-item-quantity-" + i + "=\"" + items.get(i - 1).getQuantity() + "\"");
        }
        if (signature) {
            html.append(" data-sig=\"" + buildSig() + "\"");
        }
        html.append(">");
        html.append("</script>");
        return html.toString();
    }

    /**
     * Create signature based on key values, items and cart id. Developers will not call this function directly.
     * 
     * @return signature
     * @throws APIException
     * @throws InvalidApiKeyException
     */
    private String buildSig() throws APIException, InvalidApiKeyException {
        if (StringUtils.isEmpty(publicKey)) {
            throw new APIException(null, "Public key is empty.", null);
        }
        StringBuilder data = new StringBuilder();
        data.append(publicKey + ";");
        for (int i = 0; i < items.size(); i++) {
            data.append(items.get(i).getId() + ";");
            data.append(items.get(i).getName() + ";");
            data.append(items.get(i).getUnitPrice() + ";");
            data.append(items.get(i).getQuantity() + ";");
        }
        if (StringUtils.isEmpty(cartId)) {
            data.append(";");
        } else {
            data.append(cartId + ";");
        }

        String key = RpayLite.getApiKey();
        if (StringUtils.isEmpty(key)) {
            throw new InvalidApiKeyException(null, "Secret key is empty.");
        }
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
        } catch (NoSuchAlgorithmException e) {
            // This should not happen
        } catch (InvalidKeyException e) {
            // This should not happen
        }
        return toHexString(mac.doFinal(data.toString().getBytes()));
    }

    private static String toHexString(byte[] bytes) {
        @SuppressWarnings("resource")
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

}
