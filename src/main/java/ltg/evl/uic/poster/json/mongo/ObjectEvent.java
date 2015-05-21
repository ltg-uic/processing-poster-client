package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.json.GenericJson;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 3/18/15.
 */
public class ObjectEvent {

    private final OBJ_TYPES eventType;
    private String itemId;
    private GenericJson genericJson = null;
    private Zone zone = null;

    public ObjectEvent(OBJ_TYPES eventType, GenericJson genericJson) {
        this.eventType = eventType;
        this.genericJson = genericJson;
    }

    public ObjectEvent(OBJ_TYPES eventType, String itemId) {
        this.eventType = eventType;
        this.itemId = itemId;
    }

    public ObjectEvent(OBJ_TYPES eventType) {
        this.eventType = eventType;
    }

    public OBJ_TYPES getEventType() {
        return eventType;
    }

    public GenericJson getGenericJson() {
        return genericJson;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public enum OBJ_TYPES {USER, POST_ITEM, POSTER, DELETE_POSTER_ITEM, INIT_ALL}
}
