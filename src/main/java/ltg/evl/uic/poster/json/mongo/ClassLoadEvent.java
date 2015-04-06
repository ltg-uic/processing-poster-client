package ltg.evl.uic.poster.json.mongo;

import com.google.common.eventbus.Subscribe;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by aperritano on 3/30/15.
 */
public class ClassLoadEvent {


    private final Logger logger;

    public ClassLoadEvent() {
        logger = Logger.getLogger(ClassLoadEvent.class.getName());
        logger.setLevel(Level.ALL);
    }

    @Subscribe
    public void handleClassLoadEvent(Collection<User> users) {
        logger.log(Level.INFO, "class event with user uuid: " + users);
    }

}
