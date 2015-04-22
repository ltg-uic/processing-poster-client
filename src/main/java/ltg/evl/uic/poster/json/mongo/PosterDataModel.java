package ltg.evl.uic.poster.json.mongo;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import ltg.evl.uic.poster.listeners.LoginCollectionListener;
import ltg.evl.uic.poster.listeners.LoginDialogEvent;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.util.RESTHelper;
import ltg.evl.util.collections.PictureZoneToPosterItem;
import org.apache.commons.lang.StringUtils;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
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
    //public final EventBus eventBus;
    public List<User> allUsers = Lists.newArrayList();
    public List<PosterItem> allPosterItems = Lists.newArrayList();
    public List<Poster> allPosters = Lists.newArrayList();
    private User currentUser;
    private Poster currentPoster;
    private String currentClassName;
    private Collection<User> currentClassUsers;
    private Collection<Poster> currentUserPosters;
    private LoginCollectionListener loginCollectionListener;

    private PosterDataModel() {
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




    public static PosterDataModel helper() {
        return ourInstance;
    }

    public void addLoginCollectionListener(LoginCollectionListener loginCollectionListener) {
        this.loginCollectionListener = loginCollectionListener;
    }

    public void logout() {
        PosterDataModel.helper().savePosterItemsForCurrentUser();
    }

    public void startInitialization() {
        RESTHelper.getInstance().initAllCollections();
    }

    public void initializationDone() {
        this.loginCollectionListener.initializationDone();
    }


    //region Loading User

    /**
     * Loading Users in main screen
     *
     * @param userUuid
     * @param buttonColor
     */
    public void loadUser(String userUuid, int buttonColor) {
        ImmutableList<User> imAllUsers = ImmutableList.copyOf(PosterDataModel.helper().allUsers);
        if (!imAllUsers.isEmpty()) {
            for (User user : imAllUsers) {
                if (user.getUuid().equals(userUuid)) {
                    this.currentUser = user;
                    this.loginCollectionListener.loadUserEvent(
                            new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.USER, userUuid, buttonColor));
                }
            }
        }
    }



    public void loadUser(User user) {
        if (Optional.fromNullable(user).isPresent()) {
            this.currentUser = user;
            fetchAllPostersForCurrentUser();
            this.loginCollectionListener.loadUserEvent(new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.USER, user));
        }


    }
    //endregion

    //region load poster

    /**
     * Loading Posters in the main screen
     *
     * @param posterUuid
     */
    public void loadPoster(String posterUuid) {
        if (Strings.isNullOrEmpty(posterUuid))
            return;

        Collection<Poster> imAllPosters = PosterDataModel.helper().getAllPostersForUser(currentUser);

        if (!imAllPosters.isEmpty()) {
            for (Poster poster : imAllPosters) {
                if (poster.getUuid().equals(posterUuid)) {
                    this.currentPoster = poster;
                    this.loginCollectionListener.loadPosterEvent(
                            new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.POSTER, posterUuid));
                }
            }
        }
    }

    //endregion loading poster

    public void loadClass(final String classname) {
        ImmutableList<User> imAllUsers = ImmutableList.copyOf(PosterDataModel.helper().allUsers);

        Predicate<User> predicateClass = new Predicate<User>() {
            @Override
            public boolean apply(User input) {
                return StringUtils.lowerCase(input.getClassname()).equals(classname);
            }
        };

        setCurrentClassName(classname);

        this.loginCollectionListener.loadClassEvent(
                new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.CLASS_NAME, classname));
    }

    public void fetchAllUsersForCurrentClass(String className) {

        setCurrentClassName(className);
        Optional<String> classNameOptional = Optional.fromNullable(getCurrentClassName());

        if (classNameOptional.isPresent()) {
            final String cname = StringUtils.lowerCase(classNameOptional.get());

            final ImmutableList<User> imAllUsers = ImmutableList.copyOf(PosterDataModel.helper().allUsers);

            final Predicate<User> predicateClass = new Predicate<User>() {
                @Override
                public boolean apply(User input) {

                    String userClassName = StringUtils.lowerCase(input.getClassname());

                    return userClassName.equals(cname);
                }
            };

            ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
            ListenableFuture<Collection<User>> explosion = service.submit(new Callable<Collection<User>>() {
                public Collection<User> call() {
                    return Collections2.filter(imAllUsers, predicateClass);
                }
            });

            Futures.addCallback(explosion, new FutureCallback<Collection<User>>() {


                @Override
                public void onSuccess(Collection<User> result) {
                    currentClassUsers = result;
                    loginCollectionListener.loadClassEvent(
                            new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.CLASS_NAME, cname));
                }

                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
    }


    public void savePosterItemsForCurrentUser() {
        final List<PictureZone> pictureZoneList = Lists.newArrayList();
        ImmutableList<Zone> zones = ImmutableList.copyOf(SMT.getZones());

        if (zones.isEmpty()) {
            this.loginCollectionListener.logoutDoneEvent();
        } else {

            for (Zone zone : zones) {
                Optional<Zone> zoneOptional = Optional.fromNullable(zone);
                if (zoneOptional.isPresent()) {
                    if (zone instanceof PictureZone) {
                        pictureZoneList.add((PictureZone) zone);
                    }
                }
            }

            if (!pictureZoneList.isEmpty()) {


                FluentIterable<PosterItem> posterItems = FluentIterable.from(pictureZoneList).transform(
                        new PictureZoneToPosterItem());


                try {
                    RESTHelper.getInstance().updatePosterItems(posterItems);
                } catch (InterruptedException | IOException | ExecutionException | GeneralSecurityException e) {
                    e.printStackTrace();
                }


            }

            this.loginCollectionListener.logoutDoneEvent();
        }
    }

    public void updatePosterItem(
            PosterItem posterItem) throws InterruptedException, GeneralSecurityException, ExecutionException, IOException {
        RESTHelper.getInstance()
                  .postCollection(posterItem, RESTHelper.PosterUrl.updateDeletePosterItem(
                                          posterItem.get_id().get("$oid").toString(),
                                          RESTHelper.PosterUrl.REQUEST_TYPE.UPDATE),
                                  PosterItem.class, false);

    }

    public void fetchAllPostersForCurrentUser() {
        this.currentUserPosters = getAllPostersForUser(this.currentUser);
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

        return Collections2.filter(imPosters, filterPosters);
    }

    public Poster findPosterWithPosterItemUuid(final String posterUuid) {

        ImmutableList<Poster> imPosters = ImmutableList.copyOf(allPosters);

        Predicate<Poster> filterPoster = new Predicate<Poster>() {
            @Override
            public boolean apply(Poster poster) {

                for (String aPosterUuid : poster.getPosterItems()) {
                    if (aPosterUuid.equals(posterUuid)) {
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

    public Collection<PosterItem> getAllPostersItemsForPoster(final String posterUuid) {

        ImmutableList<Poster> imPosters = ImmutableList.copyOf(allPosters);
        Predicate<Poster> filterPosters = new Predicate<Poster>() {
            @Override
            public boolean apply(Poster poster) {
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

        return Collections2.filter(imPosterItems, filterPosterItem);
    }

    public PosterItem findPosterItemWithPosterItemUuid(final String posterItemUuid) {
        ImmutableList<PosterItem> imPosterItems = ImmutableList.copyOf(allPosterItems);

        Predicate<PosterItem> filterPosterItem = new Predicate<PosterItem>() {
            @Override
            public boolean apply(PosterItem posterItem) {

                if (Optional.fromNullable(posterItemUuid).isPresent()) {
                    if (Optional.fromNullable(posterItem.getUuid()).isPresent()) {
                        return posterItem.getUuid().equals(posterItemUuid);
                    }
                    return false;
                }

                return false;
            }
        };

        if (!imPosterItems.isEmpty()) {

            Collection<PosterItem> result = Collections2.filter(imPosterItems, filterPosterItem);

            if (!result.isEmpty()) {
                return result.iterator().next();
            }
        }

        return null;
    }

    public void removePosterItem(final String posterItemUuid) {

        logger.log(Level.INFO, "REMOVING POSTER_ITEM");

        final Poster foundPoster = findPosterWithPosterItemUuid(posterItemUuid);
        final PosterItem foundPosterItem = findPosterItemWithPosterItemUuid(posterItemUuid);

        foundPoster.getPosterItems().remove(posterItemUuid);

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        ListenableFuture<Poster> deleteFuture = service.submit(new Callable<Poster>() {
            public Poster call() throws InterruptedException, GeneralSecurityException, ExecutionException, IOException {


                RESTHelper.getInstance()
                          .postCollection(null, RESTHelper.PosterUrl.updateDeletePosterItem(
                                  String.valueOf(foundPosterItem.get_id().get("$oid")),
                                  RESTHelper.PosterUrl.REQUEST_TYPE.DELETE), PosterItem.class, false);

                RESTHelper.getInstance()
                          .postCollection(foundPoster, RESTHelper.PosterUrl.updateDeletePoster(
                                                  String.valueOf(foundPoster.get_id().get("$oid")),
                                                  RESTHelper.PosterUrl.REQUEST_TYPE.UPDATE),
                                          Poster.class, false);

                return foundPoster;
            }
        });

        Futures.addCallback(deleteFuture, new FutureCallback<Poster>() {

            @Override
            public void onSuccess(Poster poster) {
                logger.log(Level.SEVERE, "DELETED POSTERITEM " + posterItemUuid + " SUCCESS!");
                loginCollectionListener.deletePosterItem(
                        new ObjectEvent(ObjectEvent.OBJ_TYPES.DELETE_POSTER_ITEM, posterItemUuid));
            }

            @Override
            public void onFailure(Throwable thrown) {
                logger.log(Level.SEVERE, "DELETED POSTERITEM " + posterItemUuid + " FAILED!");
                //loginCollectionListener.(new ObjectEvent(ObjectEvent.OBJ_TYPES.DELETE_POSTER_ITEM, posterItemUuid));
                thrown.printStackTrace();
            }
        });
    }

    public void updatePosterItemCollection(PosterItem posterItem) {
        if (Optional.fromNullable(posterItem).isPresent()) {

            ArrayList<PosterItem> one = Lists.newArrayList(posterItem);
            ImmutableList<PosterItem> two = ImmutableList.copyOf(allPosterItems);
            allPosterItems = Lists.newArrayList(Iterables.concat(one, two));
            if (this.loginCollectionListener != null) {
                this.loginCollectionListener.updatePosterItem(
                        new ObjectEvent(ObjectEvent.OBJ_TYPES.POST_ITEM, posterItem));
            }
        }
    }

    public void addPosterItemUUIDWithPosterId(final String posterItemUuuid, final String posterId) {

        ImmutableList<Poster> posterImmutableList = ImmutableList.copyOf(allPosters);

        List<Poster> allPosters = FluentIterable.from(posterImmutableList).transform(new Function<Poster, Poster>() {
            @Override
            public Poster apply(Poster poster) {

                if (poster.getUuid().equals(posterId)) {
                    poster.addPosterItems(posterItemUuuid);
                }

                return poster;
            }
        }).toList();
    }

    public void updateAllUserCollection(List<User> users) {
        this.allUsers = Lists.newArrayList();
        allUsers.addAll(users);
    }

    public void updateAllPosterItemsCollection(List<PosterItem> posterItems) {
        this.allPosterItems = Lists.newArrayList();
        allPosterItems.addAll(posterItems);
    }

    public void updateAllPosterCollection(List<Poster> posters) {
        this.allPosters = Lists.newArrayList();
        allPosters.addAll(posters);
    }

    public void updateUserCollection(User user) {
        allUsers.add(user);
    }

    public void updatePosterCollection(Poster poster) {
        allPosters.add(poster);
    }


    public void resetData() {
        allUsers = Lists.newArrayList();
        allPosterItems = Lists.newArrayList();
        allPosters = Lists.newArrayList();
        currentUserPosters = Lists.newArrayList();
        currentClassUsers = Lists.newArrayList();
        currentPoster = null;
        currentUser = null;
        currentClassName = null;
    }


    //region mutators
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


    public String getCurrentClassName() {
        return currentClassName;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    public Collection<User> getCurrentClassUsers() {
        return currentClassUsers;
    }

    public void setCurrentClassUsers(Collection<User> currentClassUsers) {
        this.currentClassUsers = currentClassUsers;
    }

    public Collection<Poster> getCurrentUserPosters() {
        return currentUserPosters;
    }

    public void setCurrentUserPosters(Collection<Poster> currentUserPosters) {
        this.currentUserPosters = currentUserPosters;
    }
    //end region


}