package jp.co.rakuten.checkout.lite.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class BaseTest {

    @Test
    public void chargeTrivialTest() {
        Charge ch = new Charge();
        ch.setId("some_id");
        assertEquals("some_id", ch.getId());
        ch.setCurrency("jpy");
        assertEquals("jpy", ch.getCurrency());
        ch.setAmount(1000);
        assertEquals(1000, ch.getAmount());
        ch.setPoint(100);
        assertEquals(100, ch.getPoint());
        ch.setCartId("some_cart");
        assertEquals("some_cart", ch.getCartId());
        ch.setPaid(true);
        assertTrue(ch.getPaid());
        ch.setCaptured(false);
        assertFalse(ch.getCaptured());
        ch.setStatus("some_status");
        assertEquals("some_status", ch.getStatus());
        ch.setRefunded(true);
        assertTrue(ch.getRefunded());
        ch.setCreated(12345678);
        assertEquals(12345678, ch.getCreated());
        ch.setUpdated(87654321);
        assertEquals(87654321, ch.getUpdated());

        List<Charge> chs = new ArrayList<Charge>();
        chs.add(ch);
        ChargeCollection cc = new ChargeCollection();
        cc.setObject("list");
        assertEquals("list", cc.getObject());
        cc.setUrl("url");
        assertEquals("url", cc.getUrl());
        cc.setId("some_id");
        assertEquals("some_id", cc.getId());
        cc.setLimit(5);
        assertEquals(5, cc.getLimit());
        cc.setOffset(10);
        assertEquals(10, cc.getOffset());
        Map<String, Boolean> payment = new HashMap<String, Boolean>();
        payment.put("paid", false);
        cc.setPayment(payment);
        assertFalse(cc.getPayment().get("paid"));
        cc.setStartingAfter("after");
        assertEquals("after", cc.getStartingAfter());
        cc.setTotal(20);
        assertEquals(20, cc.getTotal());
        Map<String, Long> created = new HashMap<String, Long>();
        created.put("lte", (long) 1234);
        cc.setCreated(created);
        assertEquals((long) 1234, (long) cc.getCreated().get("lte"));
        cc.setData(chs);
        assertEquals(ch, cc.getData().get(0));
    }

    @Test
    public void itemTrivialTest() {
        Charge ch = new Charge();
        Item item = new Item();
        item.setId("item_id");
        assertEquals("item_id", item.getId());
        item.setName("item_name");
        assertEquals("item_name", item.getName());
        item.setQuantity(5);
        assertEquals(5, item.getQuantity());
        item.setUnitPrice(100);
        assertEquals(100, item.getUnitPrice());
        List<Item> items = new ArrayList<Item>();
        items.add(item);
        ch.setItem(items);
        assertEquals(item, ch.getItems().get(0));
    }

    @Test
    public void addressTrivialTest() {
        Charge ch = new Charge();
        Address ad = new Address();
        ad.setCountry("Japan");
        assertEquals("Japan", ad.getCountry());
        ad.setFirstName("taro");
        assertEquals("taro", ad.getFirstName());
        ad.setFirstNameKana("taro");
        assertEquals("taro", ad.getFirstNameKana());
        ad.setLastName("rakuten");
        assertEquals("rakuten", ad.getLastName());
        ad.setLastNameKana("rakuten");
        assertEquals("rakuten", ad.getLastNameKana());
        ad.setAddressZip("123456");
        assertEquals("123456", ad.getAddressZip());
        ad.setAddressState("state");
        assertEquals("state", ad.getAddressState());
        ad.setAddressCity("city");
        assertEquals("city", ad.getAddressCity());
        ad.setAddressLine("line");
        assertEquals("line", ad.getAddressLine());
        ad.setTel("123123123");
        assertEquals("123123123", ad.getTel());
        ch.setAddress(ad);
        assertEquals(ad, ch.getAddress());
    }

    @Test
    public void notificationTrivialTest() {
        Notification n = new Notification();
        n.setObject("notification");
        assertEquals("notification", n.getObject());
        n.setId("some_id");
        assertEquals("some_id", n.getId());
        n.setCharge("some_charge");
        assertEquals("some_charge", n.getCharge());
        n.setType("mail");
        assertEquals("mail", n.getType());
        n.setSubject("subject");
        assertEquals("subject", n.getSubject());
        n.setBody("body");
        assertEquals("body", n.getBody());
        n.setCreated(12345678);
        assertEquals(12345678, n.getCreated());

        NotificationCollection nc = new NotificationCollection();
        nc.setObject("notification");
        assertEquals("notification", nc.getObject());
        nc.setId("some_id");
        assertEquals("some_id", nc.getId());
        nc.setCharge("some_charge");
        assertEquals("some_charge", nc.getCharge());
        List<Notification> ns = new ArrayList<Notification>();
        ns.add(n);
        nc.setData(ns);
        assertEquals(n, nc.getData().get(0));
        nc.setUrl("url");
        assertEquals("url", nc.getUrl());
        nc.setTotal(10);
        assertEquals(10, nc.getTotal());
    }
    
    @Test
    public void eventTrivialTest(){
        Event ev = new Event();
        ev.setObject("event");
        assertEquals("event", ev.getObject());
        ev.setId("event_id");
        assertEquals("event_id", ev.getId());
        ev.setLivemode(true);
        assertTrue(ev.isLivemode());
        ev.setType("type");
        assertEquals("type", ev.getType());
        ev.setSynchronous(false);
        assertFalse(ev.isSynchronous());
        ev.setPendingWebhooks(123);
        assertEquals(123, ev.getPendingWebhooks());
        ev.setCreated(12345678);
        assertEquals(12345678, ev.getCreated());
        
        Charge ch = new Charge();
        ch.setId("666");
        EventData data = new EventData();
        data.object = ch;
        ev.setData(data);
        assertEquals(ch, ev.getData().getObject());
    }
    
    @Test
    public void RpayLiteObjectTest(){
        Event object = new Event();
        System.out.println(object.toJson());
        System.out.println(object.toString());        
        assertTrue(RpayLiteObject.equals(null, null));
        assertTrue(RpayLiteObject.equals(object, object));
        assertFalse(RpayLiteObject.equals(object, null));
        assertFalse(RpayLiteObject.equals(null, object));
    }

}
