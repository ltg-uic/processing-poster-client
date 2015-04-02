package ltg.evl.uic.poster.json.mongo;

import com.google.common.eventbus.Subscribe;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by aperritano on 3/30/15.
 */
public class UserLoadEvent {


    private final Logger logger;

    public UserLoadEvent() {
        logger = Logger.getLogger(UserLoadEvent.class.getName());
        logger.setLevel(Level.ALL);
    }

    @Subscribe
    public void handleUserLoadEvent(String userUuid) {
        logger.log(Level.INFO, "user event with user uuid: " + userUuid);
    }

}
