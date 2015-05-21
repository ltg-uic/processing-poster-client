package ltg.evl.uic.poster.util.collections;

import com.google.common.base.Predicate;
import ltg.evl.uic.poster.json.mongo.User;

/**
 * Created by aperritano on 2/20/15.
 */
public class UsernamePredicate implements Predicate<User> {

    private final String userName;

    public UsernamePredicate(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean apply(User user) {
        return user.getName().equals(userName);
    }
}
