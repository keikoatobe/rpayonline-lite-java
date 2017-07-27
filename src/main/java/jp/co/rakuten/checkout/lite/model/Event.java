package jp.co.rakuten.checkout.lite.model;

import jp.co.rakuten.checkout.lite.net.APIResource;

/**
 * This class is for Event Object received from Rpay Lite through post method. Each JSON code for Event object can be parsed into an instance of this
 * class. One can retrieve parameters of charge object through get method, e.g. event.getType(). See {@link jp.co.rakuten.checkout.lite.net.Webhook#constructEvent(String, String, String) Webhook.constructEvent(payload, sigHeader, expectedSignature)} for construction of event object.
 * 
 * @author rpayonline
 *
 */
public class Event extends APIResource {

    String object;
    String id;
    boolean livemode;
    String type;
    boolean synchronous;
    EventData data;
    int pendingWebhooks;
    long created;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSynchronous() {
        return synchronous;
    }

    public void setSynchronous(boolean synchronous) {
        this.synchronous = synchronous;
    }

    public int getPendingWebhooks() {
        return pendingWebhooks;
    }

    public void setPendingWebhooks(int pendingWebhooks) {
        this.pendingWebhooks = pendingWebhooks;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public EventData getData() {
        return data;
    }

    public void setData(EventData data) {
        this.data = data;
    }

}
