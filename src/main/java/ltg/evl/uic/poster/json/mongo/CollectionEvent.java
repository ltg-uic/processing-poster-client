package ltg.evl.uic.poster.json.mongo;

import java.util.List;

/**
 * Created by aperritano on 3/18/15.
 */
public class CollectionEvent {

    private final EVENT_TYPES eventType;
    private List<User> users = null;
    private List<PosterItem> posterItems = null;

    public CollectionEvent(EVENT_TYPES eventType) {
        this.eventType = eventType;
    }


    public EVENT_TYPES getEventType() {
        return eventType;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<PosterItem> getPosterItems() {
        return this.posterItems;
    }

    public void setPosterItems(List<PosterItem> posterItems) {
        this.posterItems = posterItems;
    }

    public enum EVENT_TYPES {ADD_ALL, ADD_POSTER_ITEM, ADD_USER, ADD_POSTER, INIT}
}
