package ltg.evl.uic.poster.json.mongo;

import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Level;

/**
 * Created by aperritano on 4/1/15.
 */
public class PosterLoadEvent {
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public PosterLoadEvent() {
        logger.setLevel(Level.ALL);
    }

    @Subscribe
    public void handlePosterLoadEvent(String posterUuid) {
        logger.log(Level.INFO, "poster event with user uuid: " + posterUuid);
    }

}
