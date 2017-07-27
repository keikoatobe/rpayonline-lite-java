package jp.co.rakuten.checkout.lite.model;

import jp.co.rakuten.checkout.lite.net.APIResource;

/**
 * Class for holding Event Data. Only contains charge object at this stage. One can use eventData.getObject() to retrieve charge object.
 * 
 * @author rpayonline
 *
 */
public class EventData extends APIResource {

    Charge object;

    public Charge getObject() {
        return object;
    }
}
