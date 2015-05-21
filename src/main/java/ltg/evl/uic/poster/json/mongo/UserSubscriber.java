package ltg.evl.uic.poster.json.mongo;

import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class UserSubscriber {

    private final Logger logger = Logger.getLogger(this.getClass());


    @Subscribe
    public void handleUpdatedAllUsers(CollectionEvent userEvent) {
        logger.log(Level.INFO, "ALL USERS UPDATED");
    }
}
