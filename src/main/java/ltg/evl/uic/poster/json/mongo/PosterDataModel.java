package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.http.HttpResponse;
import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import com.google.gson.GsonBuilder;
import ltg.evl.uic.poster.listeners.LoginCollectionListener;
import ltg.evl.uic.poster.listeners.LoginDialogEvent;
import ltg.evl.uic.poster.util.MQTTPipe;
import ltg.evl.uic.poster.util.RESTHelper;
import ltg.evl.uic.poster.util.collections.PictureZoneToPosterItem;
import ltg.evl.uic.poster.widgets.DialogZoneController;
import ltg.evl.uic.poster.widgets.PictureZone;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class PosterDataModel {

    private static PosterDataModel ourInstance = new PosterDataModel();
    private final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    public List<User> allUsers = Lists.newArrayList();
    public List<PosterItem> allPosterItems = Lists.newArrayList();
    public List<Poster> allPosters = Lists.newArrayList();
    private User currentUser;
    private Poster currentPoster;
    private String currentClassName;
    private Collection<User> currentClassUsers;
    private Collection<Poster> currentUserPosters;
    private LoginCollectionListener loginCollectionListener;
    private Collection<User> currentClassUsersForSharing;
    private String currentSharingClassName;
    private User currentSharingUser;

    private PosterDataModel() {
        logger.setLevel(Level.ALL);
    }


    public static PosterDataModel helper() {
        return ourInstance;
    }

    public void addLoginCollectionListener(LoginCollectionListener loginCollectionListener) {
        this.loginCollectionListener = loginCollectionListener;
    }

    public void logout() {
        PosterDataModel.helper().savePosterItemsForCurrentUser(true);
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
                    logger.log(Level.INFO, "LOADING USER: " + user.getUuid());
                    this.loginCollectionListener.loadUserEvent(
                            new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.USER, userUuid, buttonColor));
                }
            }
        }
    }


    public void loadUser(User user, boolean isShare) {
        if (Optional.fromNullable(user).isPresent()) {

            if (isShare) {
                this.setCurrentSharingUser(user);
                //fetchAllPostersForCurrentUser();
                logger.log(Level.INFO, "LOADING SHARE USER: " + user.getUuid());
                this.loginCollectionListener.loadUserEvent(
                        new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.USER_SHARE, user));
            } else {
                this.setCurrentUser(user);
                fetchAllPostersForCurrentUser();
                logger.log(Level.INFO, "LOADING USER: " + user.getUuid());
                this.loginCollectionListener.loadUserEvent(
                        new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.USER, user));
            }
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
                    logger.log(Level.INFO, "LOADING POSTER: " + poster.getUuid());

                    this.loginCollectionListener.loadPosterEvent(
                            new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.POSTER, posterUuid));
                }
            }
        }
    }

    //endregion loading poster

//    public void loadClass(final String classname) {
//        ImmutableList<User> imAllUsers = ImmutableList.copyOf(PosterDataModel.helper().allUsers);
//
//        Predicate<User> predicateClass = new Predicate<User>() {
//            @Override
//            public boolean apply(User input) {
//                return StringUtils.lowerCase(input.getClassname()).equals(classname);
//            }
//        };
//
//        setCurrentClassName(classname);
//
//        logger.log(Level.INFO, "LOADING CLASS: " + classname);
//
//        this.loginCollectionListener.loadClassEvent(
//                new LoginDialogEvent(LoginDialogEvent.EVENT_TYPES.CLASS_NAME, classname));
//    }

    //    public void fetchAllUsersForCurrentClass(String className, boolean isShare) {
//        //if( isShare ) {
//            //fetchAllUsersForCurrentClass(className, isShare); //LoginDialogEvent.EVENT_TYPES.CLASS_NAME_SHARE, this.currentClassUsersForSharing);
////        } else {
////            fetchAllUsersForCurrentClass(className, LoginDialogEvent.EVENT_TYPES.CLASS_NAME, this.currentClassUsers);
////
////        }
//    }

    public ImmutableMap<String, Collection<User>> getClassMap() {
        ImmutableMap<String, Collection<User>> classMap;
        Iterable<String> splitTest = Splitter.on(',')
                                             .trimResults()
                                             .omitEmptyStrings()
                                             .split("alexander," +
                                                            "aman," +
                                                            "anika," +
                                                            "ayleen," +
                                                            "blaede," +
                                                            "claire," +
                                                            "eli," +
                                                            "erin," +
                                                            "hamish," +
                                                            "harper," +
                                                            "imogen," +
                                                            "jayden," +
                                                            "jessica," +
                                                            "julia," +
                                                            "lindsay," +
                                                            "marten," +
                                                            "morgan," +
                                                            "norah," +
                                                            "rahim," +
                                                            "ruby," +
                                                            "sam," +
                                                            "teadora," +
                                                            "thomas");

        Iterable<String> splitBen = Splitter.on(',')
                                            .trimResults()
                                            .omitEmptyStrings().split("abby," +
                                                                              "adam," +
                                                                              "amelia," +
                                                                              "anna," +
                                                                              "anthony," +
                                                                              "carson," +
                                                                              "chloe," +
                                                                              "cole," +
                                                                              "daeja," +
                                                                              "elliot," +
                                                                              "eva," +
                                                                              "frank," +
                                                                              "kismet," +
                                                                              "liam," +
                                                                              "marshall," +
                                                                              "michael," +
                                                                              "mira," +
                                                                              "mylo," +
                                                                              "niku," +
                                                                              "phoebe," +
                                                                              "teacher," +
                                                                              "trevor," +
                                                                              "vita," +
                                                                              "will");

        Iterable<String> splitMike = Splitter.on(',')
                                             .trimResults()
                                             .omitEmptyStrings().split("alexander," +
                                                                               "aman," +
                                                                               "anika," +
                                                                               "ayleen," +
                                                                               "blaede," +
                                                                               "claire," +
                                                                               "eli," +
                                                                               "erin," +
                                                                               "hamish," +
                                                                               "harper," +
                                                                               "imogen," +
                                                                               "jayden," +
                                                                               "jessica," +
                                                                               "julia," +
                                                                               "lindsay," +
                                                                               "marten," +
                                                                               "morgan," +
                                                                               "norah," +
                                                                               "rahim," +
                                                                               "ruby," +
                                                                               "sam," +
                                                                               "teacher," +
                                                                               "teadora," +
                                                                               "thomas");

        Collection<User> resultBen = com.google.api.client.util.Lists.newArrayList();
        for (String s : splitBen) {
            resultBen.add(new User(s, "Ben"));
        }

        Collection<User> resultMike = com.google.api.client.util.Lists.newArrayList();
        for (String s : splitMike) {
            resultMike.add(new User(s, "Michael"));
        }

        Collection<User> resultTest = com.google.api.client.util.Lists.newArrayList();
        for (String s : splitTest) {
            resultTest.add(new User(s, "Test"));
        }


        ImmutableMap.Builder<String, Collection<User>> builder = new ImmutableMap.Builder<>();

        if (!resultBen.isEmpty())
            builder.put("Ben", resultBen);

        if (!resultMike.isEmpty())
            builder.put("Michael", resultMike);

        if (!resultTest.isEmpty())
            builder.put("Test", resultTest);


        classMap = builder.build();
        return classMap;
    }
    public void fetchAllUsersForCurrentClass(String className, boolean isShare) {


        Optional<String> classNameOptional = Optional.fromNullable(className);

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

            LoginDialogEvent.EVENT_TYPES event_type;
            if (isShare) {
                this.currentClassUsersForSharing = getClassMap().get(className);


                event_type = LoginDialogEvent.EVENT_TYPES.CLASS_NAME_SHARE;
                setCurrentSharingClassName(className);
                loginCollectionListener.loadClassEvent(
                        new LoginDialogEvent(event_type, cname));
            } else {
                this.currentClassUsers = Collections2.filter(imAllUsers, predicateClass);
                event_type = LoginDialogEvent.EVENT_TYPES.CLASS_NAME;
                setCurrentClassName(className);
                loginCollectionListener.loadClassEvent(
                        new LoginDialogEvent(event_type, cname));
            }



        }
    }


    public void savePosterItemsForCurrentUser(boolean shouldLogout) {
        final List<PictureZone> pictureZoneList = Lists.newArrayList();
        ImmutableList<Zone> zones = ImmutableList.copyOf(SMT.getZones());

        if (zones.isEmpty()) {
            if (shouldLogout) {
                this.loginCollectionListener.logoutDoneEvent();
            } else {
                DialogZoneController.dialog().showOKDialog("Saved!");
                DialogZoneController.dialog().hideOKDialog();
            }
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
                    ListenableFuture<List<HttpResponse>> listListenableFutures = RESTHelper.getInstance()
                                                                                           .updatePosterItems(
                                                                                                   posterItems);
                    Futures.addCallback(listListenableFutures, new FutureCallback<List<HttpResponse>>() {
                        // we want this handler to run immediately after we push the big red button!
                        @Override
                        public void onSuccess(List<HttpResponse> listOfReponses) {
                            logger.log(Level.INFO, "PATCHING POSTER ITEMS SUCCESS");

                            if (shouldLogout) {
                                loginCollectionListener.logoutDoneEvent();
                            } else {
                                DialogZoneController.dialog().showOKDialog("Saved!");
                                DialogZoneController.dialog().hideOKDialog();
                            }

                        }

                        @Override
                        public void onFailure(Throwable thrown) {
                            thrown.printStackTrace();

                            if (shouldLogout) {
                                loginCollectionListener.logoutDoneEvent();
                            } else {
                                DialogZoneController.dialog().showOKDialog("Saved!");
                                DialogZoneController.dialog().hideOKDialog();
                            }
                        }
                    });
                } catch (InterruptedException | IOException | ExecutionException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
            } else {

                if (shouldLogout) {
                    loginCollectionListener.logoutDoneEvent();
                } else {
                    DialogZoneController.dialog().showOKDialog("Saved!");
                    DialogZoneController.dialog().hideOKDialog();
                }
            }


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

        Collection<PosterItem> filter = Collections2.filter(imPosterItems, filterPosterItem);
        return filter;
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

        if (posterItemUuid != null) {

            logger.log(Level.INFO, "REMOVING POSTER_ITEM: " + posterItemUuid);
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
                    logger.log(Level.INFO, "DELETED POSTER_ITEM " + posterItemUuid + " SUCCESS!");
                    sendMQTTDeleteMessage(posterItemUuid);
                    loginCollectionListener.deletePosterItem(
                            new ObjectEvent(ObjectEvent.OBJ_TYPES.DELETE_POSTER_ITEM, posterItemUuid));
                }

                @Override
                public void onFailure(Throwable thrown) {
                    logger.log(Level.INFO, "DELETED POSTER_ITEM " + posterItemUuid + " FAILED!");
                    thrown.printStackTrace();
                }
            });
        } else {
            logger.log(Level.INFO, "TRYING TO REMOVE NULL POSTER_ITEM PosterDataModel.removePosterItem");
        }
    }

    public void refreshMessageRecieved() {
        logger.log(Level.INFO, "Refresh Message Recieved");
        loginCollectionListener.refreshEventReceived();
    }

    private void sendMQTTDeleteMessage(String posterItemUuid) {
        if (posterItemUuid != null) {
            PosterMessage posterMessage = new PosterMessage();
            posterMessage.setPosterUuid(currentPoster.getUuid());
            posterMessage.setPosterItemUuid(posterItemUuid);
            posterMessage.setAction(PosterMessage.DELETE);
            com.google.gson.Gson gson = new GsonBuilder().create();
            MQTTPipe.getInstance().publishMessage(gson.toJson(posterMessage));
            logger.log(Level.INFO, "PosterDataModel.sendMQTTDeleteMessage posterUuid: " + posterItemUuid);
        } else {
            logger.log(Level.INFO, "PosterDataModel.sendMQTTDeleteMessage posterUuid Null");
        }
    }

    public void sendMQTTPosterGrabMessage(PosterGrab posterGrab) {
        if (posterGrab != null) {

            com.google.gson.Gson gson = new GsonBuilder().create();
            MQTTPipe.getInstance().publishMessage(gson.toJson(posterGrab));
            try {
                logger.log(Level.INFO,
                           "PosterDataModel.sendMQTTPosterGrabMessage PosterGrab: " + posterGrab.toPrettyString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.log(Level.INFO, "PosterDataModel.sendMQTTPosterGrabMessage PosterGrab Null");
        }
    }
    public void replacePosterItem(PosterItem posterItem) {
        ListIterator<PosterItem> iterator = this.allPosterItems.listIterator();
        while (iterator.hasNext()) {
            PosterItem p = iterator.next();
            if (p.equals(posterItem)) {
                iterator.set(posterItem);
            }
        }

    }

    public Collection<PosterItem> findDuplicatePosterItems(PosterItem posterItem) {

        if (posterItem == null) {
            return Collections.emptyList();
        }

        Predicate<PosterItem> predicate = new Predicate<PosterItem>() {
            @Override
            public boolean apply(PosterItem input) {
                return input.equals(posterItem);
            }
        };

        ImmutableList<PosterItem> posterItemsImmutableList = ImmutableList.copyOf(allPosterItems);
        Collection<PosterItem> result = Collections2.filter(posterItemsImmutableList, predicate);

        return result;
    }

    public Collection<Poster> findDuplicatePosters(Poster poster) {

        if (poster == null) {
            return Collections.emptyList();
        }

        Predicate<Poster> predicate = new Predicate<Poster>() {
            @Override
            public boolean apply(Poster input) {
                return input.equals(poster);
            }
        };

        ImmutableList<Poster> posterImmutableList = ImmutableList.copyOf(allPosters);
        Collection<Poster> result = Collections2.filter(posterImmutableList, predicate);

        return result;
    }

    public Collection<User> findDuplicateUsers(User user) {

        if (user == null) {
            return Collections.emptyList();
        }

        Predicate<User> predicate = new Predicate<User>() {
            @Override
            public boolean apply(User input) {
                return input.equals(user);
            }
        };

        ImmutableList<User> userImmutableList = ImmutableList.copyOf(allUsers);
        Collection<User> result = Collections2.filter(userImmutableList, predicate);

        return result;
    }


    public void updatePosterItemCollection(PosterItem newPosterItem) {
        if (Optional.fromNullable(newPosterItem).isPresent()) {


            Collection<PosterItem> duplicatePosterItems = findDuplicatePosterItems(newPosterItem);

            if (!duplicatePosterItems.isEmpty()) {
                logger.log(Level.INFO,
                           "UPDATE POSTER_ITEM: " + newPosterItem.getUuid() + " DUP x " + duplicatePosterItems.size());
                allPosterItems.removeAll(duplicatePosterItems);

            }

            ImmutableList<PosterItem> two = ImmutableList.copyOf(allPosterItems);
            allPosterItems = Lists.newArrayList(Iterables.concat(Lists.newArrayList(newPosterItem), two));
            if (this.loginCollectionListener != null) {
                logger.log(Level.INFO,
                           "UPDATE POSTER_ITEM: " + newPosterItem.getUuid());
                this.loginCollectionListener.updatePosterItem(
                        new ObjectEvent(ObjectEvent.OBJ_TYPES.POST_ITEM, newPosterItem));
            }
        }
    }

    public void addPosterItemUUIDWithPosterId(final String posterItemUuuid, final String posterId) {

        ImmutableList<Poster> posterImmutableList = ImmutableList.copyOf(allPosters);

        List<Poster> allPosters = FluentIterable.from(posterImmutableList).transform(new Function<Poster, Poster>() {
            @Override
            public Poster apply(Poster poster) {

                if (poster.getUuid().equals(posterId)) {

                    if (poster.getPosterItems() == null || poster.getPosterItems().isEmpty()) {
                        List<String> posterItems = Collections.emptyList();
                        posterItems.add(posterItemUuuid);
                        poster.setPosterItems(posterItems);
                        logger.log(Level.INFO,
                                   "ADDING PosterItem: " + posterItemUuuid);
                    } else if (!poster.getPosterItems().contains(posterItemUuuid)) {
                        poster.getPosterItems().add(posterItemUuuid);
                        logger.log(Level.INFO,
                                   "ADDING PosterItem: " + posterItemUuuid);
                    } else {
                        logger.log(Level.INFO,
                                   "REMOVING and ADDING PosterItem: " + posterItemUuuid + " is a DUP in " + posterId);
                    }
                }

                return poster;
            }
        }).toList();
    }

    public void updateAllUserCollection(List<User> users) {
        this.allUsers = Lists.newArrayList();
        this.allUsers.addAll(users);

        for (User user : users) {
            Collection<User> duplicateUsers = findDuplicateUsers(user);
            if (duplicateUsers.size() > 1) {
                logger.log(Level.INFO, "UPDATING ALL USER found DUP: " + duplicateUsers.iterator().next().getUuid());
            }
        }
    }

    public void updateAllPosterItemsCollection(List<PosterItem> posterItems) {
        this.allPosterItems = Lists.newArrayList();
        this.allPosterItems.addAll(posterItems);
        for (PosterItem posterItem : posterItems) {

            Collection<PosterItem> duplicatePosterItems = findDuplicatePosterItems(posterItem);

            if (duplicatePosterItems.size() > 1) {
                logger.log(Level.INFO,
                           "UPDATING ALL POSTER_ITEMS found DUP: " + duplicatePosterItems.iterator().next()
                                                                                         .getUuid());
            }
        }
    }

    public void updateAllPosterCollection(List<Poster> posters) {
        this.allPosters = Lists.newArrayList();
        this.allPosters.addAll(posters);
        for (Poster poster : posters) {

            Collection<Poster> duplicatePoster = findDuplicatePosters(poster);

            if (duplicatePoster.size() > 1) {
                logger.log(Level.INFO, "UPDATING ALL POSTERS found DUP: " + duplicatePoster.iterator().next()
                                                                                             .getUuid());
            }
        }
    }

    //untested
    public void updateUserCollection(User user) {
        if (allUsers != null) {

            if (allUsers.contains(user)) {
                ListIterator<User> listIterator = allUsers.listIterator();
                while (listIterator.hasNext()) {
                    User nextUser = listIterator.next();
                    if (allUsers.contains(nextUser)) {
                        listIterator.set(user);
                        logger.log(Level.INFO, "UPDATE DUP USER: " + user.getUuid());
                    }
                }
            } else {
                allUsers.add(user);
            }

        }


    }

    //untested
    public void updatePosterCollection(Poster poster) {
        if (allPosters != null) {
            if (allPosters.contains(poster)) {
                ListIterator<Poster> listIterator = allPosters.listIterator();

                while (listIterator.hasNext()) {
                    Poster nextPoster = listIterator.next();
                    if (allPosters.contains(nextPoster)) {
                        listIterator.set(poster);
                        logger.log(Level.INFO, "UPDATE DUP POSTER: " + poster.getUuid());
                    }
                }
            } else {
                allPosters.add(poster);
            }
        }

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

    public String getCurrentSharingClassName() {
        return currentSharingClassName;
    }

    public void setCurrentSharingClassName(String currentSharingClassName) {
        this.currentSharingClassName = currentSharingClassName;
    }

    public User getCurrentSharingUser() {
        return currentSharingUser;
    }

    public void setCurrentSharingUser(User currentSharingUser) {
        this.currentSharingUser = currentSharingUser;
    }

    public Collection<User> getCurrentClassUsersForSharing() {
        return currentClassUsersForSharing;
    }

    public void setCurrentClassUsersForSharing(
            Collection<User> currentClassUsersForSharing) {
        this.currentClassUsersForSharing = currentClassUsersForSharing;
    }


    //end region


}