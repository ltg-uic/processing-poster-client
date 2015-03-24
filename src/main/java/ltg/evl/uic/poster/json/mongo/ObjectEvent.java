package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.json.GenericJson;

/**
 * Created by aperritano on 3/18/15.
 */
public class ObjectEvent {

    private final GenericJson genericJson;
    private final OBJ_TYPES eventType;

    public ObjectEvent(OBJ_TYPES eventType, GenericJson genericJson) {
        this.eventType = eventType;
        this.genericJson = genericJson;
    }


    public OBJ_TYPES getEventType() {
        return eventType;
    }

    public GenericJson getGenericJson() {
        return genericJson;
    }


    public enum OBJ_TYPES {USER, POST_ITEM, POSTER, INIT_ALL}
}
