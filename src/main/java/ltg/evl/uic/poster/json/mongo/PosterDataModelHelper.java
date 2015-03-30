package ltg.evl.uic.poster.json.mongo;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.*;
import ltg.evl.util.RESTHelper;
import vialab.SMT.Zone;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by aperritano on 3/18/15.
 */
public class PosterDataModelHelper {

    private static PosterDataModelHelper ourInstance = new PosterDataModelHelper();
    private static Logger logger;
    public final EventBus eventBus;
    public List<User> allUsers = Lists.newArrayList();
    public List<PosterItem> allPosterItems = Lists.newArrayList();
    public List<Poster> allPosters = Lists.newArrayList();

    private Boolean hasNotFinishedFetchingAll = true;
    //  private List<User> allConfigurations = Lists.newArrayList();


    private PosterDataModelHelper() {
        eventBus = new EventBus();
        logger = Logger.getLogger(PosterDataModelHelper.class.getName());
        logger.setLevel(Level.ALL);
        logger.addHandler(new Handler() {
            @Override
            public void close() throws SecurityException {
            }

            @Override
            public void flush() {
            }

            @Override
            public void publish(LogRecord record) {
                // default ConsoleHandler will print >= INFO to System.err
                if (record.getLevel().intValue() < Level.INFO.intValue()) {
                    System.out.println(record.getMessage());
                }
            }
        });

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

    public void sendEvent(ObjectEvent objectEvent) {
        eventBus.post(objectEvent);
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


    public Poster findPosterWithPosterItemUuid(final String posterUuid) {

        ImmutableList<Poster> imPosters = ImmutableList.copyOf(allPosters);
        ImmutableList<PosterItem> imPosterItems = ImmutableList.copyOf(allPosterItems);


        Predicate<Poster> filterPoster = new Predicate<Poster>() {
            @Override
            public boolean apply(Poster poster) {

                for (String posterUuid : poster.getPosterItems()) {
                    if (posterUuid.equals(posterUuid)) {
                        return true;
                    }
                }
                return false;
            }

        };


        Collection<Poster> result = Collections2.filter(imPosters, filterPoster);


        if (!result.isEmpty()) {
            return result.iterator().next();
        }

        return null;
    }

    public PosterItem findPosterItemWithPosterItemUuid(final String posterItemUuid) {
        ImmutableList<PosterItem> imPosterItems = ImmutableList.copyOf(allPosterItems);

        Predicate<PosterItem> filterPosterItem = new Predicate<PosterItem>() {
            @Override
            public boolean apply(PosterItem posterItem) {
                return posterItem.getUuid().equals(posterItemUuid);
            }
        };

        Collection<PosterItem> result = Collections2.filter(imPosterItems, filterPosterItem);

        if (!result.isEmpty()) {
            return result.iterator().next();
        }

        return null;
    }

    public void removePosterItem(final String posterItemUuid) {

        logger.log(Level.INFO, "REMOVING POSTER_ITEM");


        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        ListenableFuture<Poster> deleteFutre = service.submit(new Callable<Poster>() {
            public Poster call() throws InterruptedException, GeneralSecurityException, ExecutionException, IOException {
                Poster foundPoster = findPosterWithPosterItemUuid(posterItemUuid);
                PosterItem foundPosterItem = findPosterItemWithPosterItemUuid(posterItemUuid);

                foundPoster.getPosterItems().remove(posterItemUuid);

                RESTHelper.getInstance()
                          .postCollection(null, RESTHelper.PosterUrl.updateDeletePosterItem(
                                  String.valueOf(foundPosterItem.get_id().get("$oid")),
                                  RESTHelper.PosterUrl.REQUEST_TYPE.DELETE), PosterItem.class);

                RESTHelper.getInstance()
                          .postCollection(foundPoster, RESTHelper.PosterUrl.updateDeletePoster(
                                                  String.valueOf(foundPoster.get_id().get("$oid")),
                                                  RESTHelper.PosterUrl.REQUEST_TYPE.UPDATE),
                                          Poster.class);


                return foundPoster;
            }
        });

        Futures.addCallback(deleteFutre, new FutureCallback<Poster>() {
            public void onSuccess(Poster poster) {


                Iterator<PosterItem> lirNew = allPosterItems.iterator();
                while (lirNew.hasNext()) {
                    PosterItem pi = lirNew.next();
                    if (pi.getUuid().equals(posterItemUuid)) {
                        lirNew.remove();
                    }
                }


                logger.log(Level.SEVERE, "DELETING POSTERITEM " + posterItemUuid + " SUCCESS!");
                PosterDataModelHelper.getInstance()
                                     .sendEvent(
                                             new ObjectEvent(ObjectEvent.OBJ_TYPES.DELETE_POSTER_ITEM, posterItemUuid));
            }

            public void onFailure(Throwable thrown) {
                logger.log(Level.SEVERE, "DELETING POSTERITEM " + posterItemUuid + " FAILED!");
                thrown.printStackTrace();
            }
        });
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

    public void removeZone(Zone zone) {
        eventBus.post(new ObjectEvent(ObjectEvent.OBJ_TYPES.DELETE_POSTER_ITEM, zone.getName()));
    }

    public void initializationDone() {
        eventBus.post(new ObjectEvent(ObjectEvent.OBJ_TYPES.INIT_ALL));
    }

    public void updatePosterItemCollection(PosterItem posterItem) {

//        ce.setPosterItems(Lists.newArrayList(posterItem));
        allPosterItems.add(posterItem);
        // eventBus.post(new ObjectEvent(ObjectEvent.OBJ_TYPES.POST_ITEM, posterItem));
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
