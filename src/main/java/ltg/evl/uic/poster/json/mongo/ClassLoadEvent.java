package ltg.evl.uic.poster.json.mongo;

import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Collection;

public class ClassLoadEvent {

    private final Logger logger = Logger.getLogger(this.getClass());

    public ClassLoadEvent() {
        logger.setLevel(Level.ALL);
    }

    @Subscribe
    public void handleClassLoadEvent(Collection<User> users) {
        logger.log(Level.INFO, "class event with user uuid: " + users);
    }

}
