package ltg.evl.uic.poster.json.mongo;

import com.google.common.eventbus.Subscribe;

/**
 * Created by aperritano on 3/18/15.
 */
public class UserSubscriber {

    @Subscribe
    public void handleUpdatedAllUsers(CollectionEvent userEvent) {
        System.out.println("ALLLLL USERS UPDATED");
    }
}
