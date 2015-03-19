package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.json.GenericJson;

/**
 * Created by aperritano on 3/18/15.
 */
public class ObjectEvent {

    private final GenericJson jsonObject;
    private final OBJ_TYPES eventType;

    public ObjectEvent(OBJ_TYPES eventType, GenericJson jsonObject) {
        this.eventType = eventType;
        this.jsonObject = jsonObject;
    }


    public OBJ_TYPES getEventType() {
        return eventType;
    }

    public GenericJson getJsonObject() {
        return jsonObject;
    }


    public enum OBJ_TYPES {USER, POST_ITEM, POSTER}
}
