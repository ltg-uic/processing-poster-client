package ltg.evl.uic.poster.listeners;

import ltg.evl.uic.poster.json.mongo.User;

/**
 * Created by aperritano on 2/15/15.
 */
public interface LoadUserListener {
    void loadUser(String userUuid, int buttonColor);

    void loadUser(User user);
}
