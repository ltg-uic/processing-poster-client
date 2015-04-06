package ltg.evl.uic.poster.json.mongo;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.*;
import ltg.evl.uic.poster.listeners.LoginDialogEvent;
import ltg.evl.util.RESTHelper;
import org.apache.commons.lang.StringUtils;
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
public class PosterDataModel {

    private static PosterDataModel ourInstance = new PosterDataModel();
    private static Logger logger;
    public final EventBus eventBus;
    public List<User> allUsers = Lists.newArrayList();
    public List<PosterItem> allPosterItems = Lists.newArrayList();
    public List<Poster> allPosters = Lists.newArrayList();
    private User currentUser;
    private Poster currentPoster;

    private Boolean hasNotFinishedFetchingAll = true;

    private PosterDataModel() {
        eventBus = new EventBus();
        logger = Logger.getLogger(PosterDataModel.class.getName());
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


    //region Loading User and Posters for the main screen

    public static PosterDataModel helper() {
        return ourInstance;
    }

    /**
     * Loading Users in main screen
     *
     * @param userUuid
     */
    public void loadUser(String userUuid) {
        ImmutableList<User> imAllUsers = ImmutableList.copyOf(PosterDataModel.helper().allUsers);

        if (!imAllUsers.isEmpty()) {
            for (User user : imAllUsers) {
                if (user.getUuid().equals(userUuid)) {
                    this.currentUser = user;
                    this.postObject(
                            new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.USER, userUuid));
                }
            }
        }
    }
    //endregion

    /**
     * Loading Posters in the main screen
     *
     * @param posterUuid
     */
    public void loadPoster(String posterUuid) {
        Collection<Poster> imAllPosters = PosterDataModel.helper().getAllPostersForUser(currentUser);

        if (!imAllPosters.isEmpty()) {
            for (Poster poster : imAllPosters) {
                if (poster.getUuid().equals(posterUuid)) {
                    this.currentPoster = poster;
                    this.postObject(new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.POSTER, posterUuid));
                }
            }
        }
    }

    public void loadClass(final String classname) {
        ImmutableList<User> imAllUsers = ImmutableList.copyOf(PosterDataModel.helper().allUsers);

        Predicate<User> predicateClass = new Predicate<User>() {
            @Override
            public boolean apply(User input) {
                return StringUtils.lowerCase(input.getClassname()).equals(classname);
            }
        };

        Collection<User> resultClass = Collections2.filter(imAllUsers, predicateClass);

        this.postObject(new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.CLASS_NAME, classname));

    }

    public Collection<Poster> getAllPostersForCurrentUser() {
        return getAllPostersForUser(this.currentUser);
    }

    public Collection<Poster> getAllPostersForUser(final User user) {
        ImmutableList<Poster> imPosters = ImmutableList.copyOf(allPosters);
        Predicate<Poster> filterPosters = new Predicate<Poster>() {
            @Override
            public boolean apply(Poster poster) {
                String posterUuid = poster.getUuid();
                return user.getPosters().contains(posterUuid);
                }
        };

        Collection<Poster> result = Collections2.filter(imPosters, filterPosters);
        return result;
    }

    public Poster findPosterWithPosterItemUuid(final String posterUuid) {

        ImmutableList<Poster> imPosters = ImmutableList.copyOf(allPosters);
        ImmutableList<PosterItem> imPosterItems = ImmutableList.copyOf(allPosterItems);


        Predicate<Poster> filterPoster = new Predicate<Poster>() {
            @Override
            public boolean apply(Poster poster) {

                for (String aPosterUuid : poster.getPosterItems()) {
                    if (aPosterUuid.equals(aPosterUuid)) {
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

    public Collection<PosterItem> getAllPostersItemsForPoster(String posterUuid) {

        ImmutableList<Poster> imPosters = ImmutableList.copyOf(allPosters);
        Predicate<Poster> filterPosters = new Predicate<Poster>() {
            @Override
            public boolean apply(Poster poster) {
                String posterUuid = poster.getUuid();
                return poster.getUuid().equals(posterUuid);
            }
        };

        Collection<Poster> result = Collections2.filter(imPosters, filterPosters);
        return getAllPostersItemsForPoster(result.iterator().next());
    }

    public Collection<PosterItem> getAllPostersItemsForPoster(final Poster poster) {

        ImmutableList<PosterItem> imPosterItems = ImmutableList.copyOf(allPosterItems);

        Predicate<PosterItem> filterPosterItem = new Predicate<PosterItem>() {
            @Override
            public boolean apply(PosterItem posterItem) {
                return poster.getPosterItems().contains(posterItem.getUuid());
            }
        };

        Collection<PosterItem> result = Collections2.filter(imPosterItems, filterPosterItem);


        return result;
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

    public void saveZoneState(Zone zone) {
        Iterator<PosterItem> it = allPosterItems.iterator();
        while (it.hasNext()) {
            PosterItem pi = it.next();
            if (pi.getUuid().equals(zone.getName())) {
                pi.setX(zone.getX());
                pi.setY(zone.getY());
                pi.setWidth(zone.getWidth());
                pi.setHeight(zone.getHeight());
                //don't forget rotation
                //save posteritem to db
                updatePosterItemState(pi);
            }
        }

    }

    public void updatePosterItemState(final PosterItem posterItem) {

        logger.log(Level.INFO, "UPDATING POSTER_ITEM");


        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        ListenableFuture<PosterItem> updatedPosterItemFuture = service.submit(new Callable<PosterItem>() {
            public PosterItem call() throws InterruptedException, GeneralSecurityException, ExecutionException, IOException {
                RESTHelper.getInstance()
                          .postCollection(posterItem, RESTHelper.PosterUrl.updateDeletePosterItem(
                                  String.valueOf(posterItem.get_id().get("$oid")),
                                  RESTHelper.PosterUrl.REQUEST_TYPE.UPDATE), PosterItem.class);


                return posterItem;
            }
        });

        Futures.addCallback(updatedPosterItemFuture, new FutureCallback<PosterItem>() {
            public void onSuccess(PosterItem updateDatedPosterItem) {


                logger.log(Level.SEVERE, "UPDATE POSTERITEM " + updateDatedPosterItem.getUuid() + " SUCCESS!");
//                PosterDataModel.helper()
//                                     .sendEvent(
//                                             new ObjectEvent(ObjectEvent.OBJ_TYPES.DELETE_POSTER_ITEM, posterItemUuid));
            }

            public void onFailure(Throwable thrown) {
                logger.log(Level.SEVERE, "DELETING POSTERITEM " + posterItem.getUuid() + " FAILED!");
                thrown.printStackTrace();
            }
        });




    }

    public void removePosterItem(final String posterItemUuid) {

        logger.log(Level.INFO, "REMOVING POSTER_ITEM");


        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        ListenableFuture<Poster> deleteFuture = service.submit(new Callable<Poster>() {
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

        Futures.addCallback(deleteFuture, new FutureCallback<Poster>() {
            public void onSuccess(Poster poster) {


                Iterator<PosterItem> lirNew = allPosterItems.iterator();
                while (lirNew.hasNext()) {
                    PosterItem pi = lirNew.next();
                    if (pi.getUuid().equals(posterItemUuid)) {
                        lirNew.remove();
                    }
                }


                logger.log(Level.SEVERE, "DELETING POSTERITEM " + posterItemUuid + " SUCCESS!");
                PosterDataModel.helper()
                               .sendEvent(
                                       new ObjectEvent(ObjectEvent.OBJ_TYPES.DELETE_POSTER_ITEM, posterItemUuid));
            }

            public void onFailure(Throwable thrown) {
                logger.log(Level.SEVERE, "DELETING POSTERITEM " + posterItemUuid + " FAILED!");
                thrown.printStackTrace();
            }
        });
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

    public void registerObject(Object obj) {
        eventBus.register(obj);
    }

    public void postObject(Object obj) {
        eventBus.post(obj);
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

    public Poster getCurrentPoster() {
        return currentPoster;
    }

    public void setCurrentPoster(Poster currentPoster) {
        this.currentPoster = currentPoster;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}