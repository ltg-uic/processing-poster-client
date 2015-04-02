package ltg.evl.uic.poster.json.mongo;

import com.google.common.eventbus.Subscribe;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by aperritano on 4/1/15.
 */
public class PosterLoadEvent {
    private final Logger logger;

    public PosterLoadEvent() {
        logger = Logger.getLogger(UserLoadEvent.class.getName());
        logger.setLevel(Level.ALL);
    }

    @Subscribe
    public void handlePosterLoadEvent(String posterUuid) {
        logger.log(Level.INFO, "poster event with user uuid: " + posterUuid);
    }

}
