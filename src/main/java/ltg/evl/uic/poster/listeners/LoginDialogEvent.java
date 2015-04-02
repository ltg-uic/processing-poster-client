package ltg.evl.uic.poster.listeners;

public class LoginDialogEvent {

    private EVENT_TYPES eventType;
    private String uuid;

    public LoginDialogEvent(EVENT_TYPES eventType, String uuid) {
        this.eventType = eventType;
        this.uuid = uuid;
    }

    public EVENT_TYPES getEventType() {
        return eventType;
    }

    public void setEventType(EVENT_TYPES eventType) {
        this.eventType = eventType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public enum EVENT_TYPES {USER, POST_ITEM, POSTER}


}
