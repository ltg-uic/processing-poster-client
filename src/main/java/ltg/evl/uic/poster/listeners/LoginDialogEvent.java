package ltg.evl.uic.poster.listeners;

import ltg.evl.uic.poster.json.mongo.User;

public class LoginDialogEvent {

    private int buttonColor;
    private User user;
    private EVENT_TYPES eventType;
    private String uuid;

    public LoginDialogEvent(EVENT_TYPES eventType, String uuid, int buttonColor) {
        this.eventType = eventType;
        this.uuid = uuid;
        this.buttonColor = buttonColor;
    }

    public LoginDialogEvent(EVENT_TYPES eventType, String uuid) {
        this.eventType = eventType;
        this.uuid = uuid;
    }

    public LoginDialogEvent(EVENT_TYPES eventType, User user) {
        this.uuid = user.getUuid();
        this.eventType = eventType;
        this.user = user;

    }

    public LoginDialogEvent(EVENT_TYPES eventType) {
        this.eventType = eventType;
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

    public int getButtonColor() {
        return buttonColor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum EVENT_TYPES {USER, POST_ITEM, CLASS_NAME, POSTER, LOGOUT, CLASS_NAME_SHARE, USER_SHARE, LOGOUT_DONE}


}
