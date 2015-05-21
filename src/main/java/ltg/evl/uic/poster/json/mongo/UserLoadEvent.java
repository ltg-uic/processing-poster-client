package ltg.evl.uic.poster.json.mongo;

import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Created by aperritano on 3/30/15.
 */
public class UserLoadEvent {

    private final Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public UserLoadEvent() {
        logger.setLevel(Level.ALL);
    }

    @Subscribe
    public void handleUserLoadEvent(String userUuid) {
        logger.log(Level.INFO, "user event with user uuid: " + userUuid);
    }

}
