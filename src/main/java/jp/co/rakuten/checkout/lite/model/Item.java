package jp.co.rakuten.checkout.lite.model;

/**
 * This class is used to store information of items. One can get specific value through get method, e.g. item.getName()
 * 
 * @author rpayonline
 *
 */
public class Item extends RpayLiteObject{

    String id;
    String name;
    int quantity;
    int unitPrice;
    
    public Item(){}

    protected Item(String id, String name, int unitPrice, int quantity) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

}
