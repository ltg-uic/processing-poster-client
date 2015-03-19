package ltg.evl.uic.poster.json.mongo;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.util.List;

/**
 * Created by aperritano on 3/18/15.
 */
public class PosterDataModelHelper {

    private static PosterDataModelHelper ourInstance = new PosterDataModelHelper();
    private final EventBus eventBus;

    private List<User> allUsers = Lists.newArrayList();
    private List<PosterItem> allPosterItems = Lists.newArrayList();
    private List<Poster> allPosters = Lists.newArrayList();

    private Boolean hasNotFinishedFetchingAll = true;
    //  private List<User> allConfigurations = Lists.newArrayList();


    private PosterDataModelHelper() {


        eventBus = new EventBus();

    }

    public static PosterDataModelHelper getInstance() {
        return ourInstance;
    }

    public void addUserSubscriber(UserSubscriber userSubscriber) {
        eventBus.register(userSubscriber);
    }

    public void addObjectSubscriber(ObjectSubscriber objectSubscriber) {
        eventBus.register(objectSubscriber);
    }

    public void updateAllUserCollection(List<User> users) {
        allUsers.addAll(users);
    }

    public void updateAllPosterItemsCollection(List<PosterItem> posterItems) {
        allPosterItems.addAll(posterItems);
    }

    public void updateAllPosterCollection(List<Poster> posters) {
        allPosters.addAll(posters);
    }

    public void updatePosterItemsCollection(PosterItem posterItem) {
        allPosterItems.add(posterItem);
        //  allPosters.get(0).addPosterItems(posterItem.getId());
        // eventBus.post(new ObjectEvent(ObjectEvent.OBJ_TYPES.POST_ITEM, posterItem));
    }

    public void updateUserCollection(User user) {
        allUsers.add(user);
    }

    public void updatePosterCollection(Poster poster) {
        allPosters.add(poster);
    }

    public List<Poster> getAllPostersForUser(User user) {
        List<Poster> filtered = Lists.newArrayList();
        for (String posterId : user.getPosters()) {
            for (Poster poster : allPosters) {
                if (poster.getId().equals(posterId)) {
                    filtered.add(poster);
                }
            }
        }
        return filtered;
    }

    public List<PosterItem> getAllPostersItemsForPoster(Poster poster) {
        List<PosterItem> filtered = Lists.newArrayList();
        for (String posterItemId : poster.getPosterItems()) {
            for (PosterItem posterItem : allPosterItems) {
                if (posterItem.getId().equals(posterItemId)) {
                    filtered.add(posterItem);
                }
            }
        }
        return filtered;
    }

    public void initializationDone() {
        eventBus.post(new CollectionEvent(CollectionEvent.EVENT_TYPES.ADD_ALL, allUsers));

    }
}
