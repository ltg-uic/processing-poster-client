package ltg.evl.uic.poster.json.mongo;

import java.util.List;

/**
 * Created by aperritano on 3/18/15.
 */
public class CollectionEvent {

    private final List<User> users;
    private final EVENT_TYPES eventType;

    public CollectionEvent(EVENT_TYPES eventType, List<User> users) {
        this.eventType = eventType;
        this.users = users;
    }


    public EVENT_TYPES getEventType() {
        return eventType;
    }

    public List<User> getUsers() {
        return users;
    }

    public enum EVENT_TYPES {ADD_ALL}
}
