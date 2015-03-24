package ltg.evl.uic.poster.json.mongo;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import java.util.List;

/**
 * Created by aperritano on 3/18/15.
 */
public class PosterDataModelHelper {

    private static PosterDataModelHelper ourInstance = new PosterDataModelHelper();
    public final EventBus eventBus;

    public List<User> allUsers = Lists.newArrayList();
    public List<PosterItem> allPosterItems = Lists.newArrayList();
    public List<Poster> allPosters = Lists.newArrayList();

    private Boolean hasNotFinishedFetchingAll = true;
    //  private List<User> allConfigurations = Lists.newArrayList();


    private PosterDataModelHelper() {
        eventBus = new EventBus();
    }

    public static PosterDataModelHelper getInstance() {
        return ourInstance;
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
                if (poster.getUuid().equals(posterId)) {
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
                if (posterItem.getUuid().equals(posterItemId)) {
                    filtered.add(posterItem);
                }
            }
        }
        return filtered;
    }

    public void initializationDone() {
        eventBus.post(new ObjectEvent(ObjectEvent.OBJ_TYPES.INIT_ALL, null));

    }

    public void updatePosterItemCollection(PosterItem posterItem) {

//        ce.setPosterItems(Lists.newArrayList(posterItem));
        allPosterItems.add(posterItem);
        eventBus.post(new ObjectEvent(ObjectEvent.OBJ_TYPES.POST_ITEM, posterItem));
        //eventBus.post(ce);
    }

    public void addPosterItemUUIDWithPosterId(String posterItemUuuid, String posterId) {
        for (Poster poster : allPosters) {
            if (poster.getUuid().equals(posterId)) {
                poster.addPosterItems(posterItemUuuid);
            }
        }
    }
}
