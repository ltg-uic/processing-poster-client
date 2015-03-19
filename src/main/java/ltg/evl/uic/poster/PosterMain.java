package ltg.evl.uic.poster;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import ltg.evl.uic.poster.json.mongo.*;
import ltg.evl.uic.poster.listeners.LoadUserListerner;
import ltg.evl.uic.poster.listeners.SaveUserListerner;
import ltg.evl.uic.poster.widgets.ControlButtonZone;
import ltg.evl.uic.poster.widgets.ControlButtonZoneBuilder;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.TopBarZone;
import ltg.evl.util.DownloadHelper;
import ltg.evl.util.RESTHelper;
import ltg.evl.util.StyleHelper;
import ltg.evl.util.collections.PictureZoneToPosterItem;
import ltg.evl.util.collections.PosterItemToPictureZone;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;
import vialab.SMT.Zone;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


/**
 * Created by aperritano on 9/24/14.
 */
public class PosterMain extends PApplet implements LoadUserListerner, SaveUserListerner {


    public static final int DIVISOR = 10000;
    protected static org.apache.log4j.Logger logger;

    private static DownloadHelper downloadHelper;

    private static String backpackPath;
    final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    private int mainBackgroundColor;
    private int from;
    private int to;
    private TopBarZone topBarZone;

    public static void main(String args[]) {
        logger = Logger.getLogger(PosterMain.class.getName());
        logger.setLevel(Level.INFO);

        PApplet.main(new String[]{"ltg.evl.uic.poster.PosterMain"});

        //get the back path

        backpackPath = StyleHelper.helper().getConfiguration().getString("backpack.path");
        //downloadHelper = new DownloadHelper(backpackPath);


    }

    public void doInit() {
        System.out.println("Setup started");
        StyleHelper.helper().setGraphicsContext(this);
        mainBackgroundColor = StyleHelper.createColor("color.mainBackground");


    }

    @Override
    public void setup() {

        thread("doInit");

        int w = displayWidth - 500;
        int h = displayHeight - 500;

        int bar_h = (int) (h * .07);

        size(w, h, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);

        topBarZone = new TopBarZone(0, 0, w, bar_h, color(44, 153, 241, 255));

        //SMT.add(topBarZone);

        setupControlButtons();

        //topBarZone.setTotalNumberOfTicks(2);

        UserSubscriber userSubscriber = new UserSubscriber() {

            @Override
            public void handleUpdatedAllUsers(CollectionEvent userEvent) {
                super.handleUpdatedAllUsers(userEvent);
                if (CollectionEvent.EVENT_TYPES.ADD_ALL.equals(CollectionEvent.EVENT_TYPES.ADD_ALL)) {
                    System.out.println("GO all users " + userEvent.getUsers());

                    List<User> users = userEvent.getUsers();

//                    for(User user :  users) {
//                        System.out.println(user.getPosters().get(0));
//                    }
                    loadPosterForUser(users.get(0));

                }
            }
        };

        ObjectSubscriber objectSubscriber = new ObjectSubscriber() {
            @Override
            public void handleUpdatedObject(ObjectEvent objectEvent) {
                super.handleUpdatedObject(objectEvent);
                if (objectEvent.getEventType().equals(ObjectEvent.OBJ_TYPES.POST_ITEM)) {
                    PosterItem newPosterItem = (PosterItem) objectEvent.getJsonObject();
                    loadPosterItems(Lists.newArrayList(newPosterItem), false);
                }

            }
        };

        PosterDataModelHelper.getInstance().addUserSubscriber(userSubscriber);
        PosterDataModelHelper.getInstance().addObjectSubscriber(objectSubscriber);

        RESTHelper.getInstance().initAllCollections();
    }

    private void loadPosterForUser(User user) {

        List<Poster> allPostersForUser = PosterDataModelHelper.getInstance().getAllPostersForUser(user);
        Poster poster = allPostersForUser.get(0);

        List<PosterItem> allPostersItemsForPoster = PosterDataModelHelper.getInstance()
                                                                         .getAllPostersItemsForPoster(poster);

        loadPosterItems(allPostersItemsForPoster, true);

    }

    public void loadPosterItems(List<PosterItem> posterItems, boolean shouldRemove) {

        if (shouldRemove)
            removeAlZones();


        FluentIterable<PictureZone> pictureZones = FluentIterable.from(posterItems)
                                                                 .transform(new PosterItemToPictureZone());

        for (PictureZone pictureZone : pictureZones) {
            boolean hasAdded = SMT.add(pictureZone);
            if (hasAdded) {
                // topBarZone.stopTimer();
                pictureZone.startAnimation(true);
            }
        }

        // DBHelper.getInstance().shutdownThreads();
    }

    private void setupControlButtons() {
        ControlButtonZone saveButton = new ControlButtonZoneBuilder().setButtonText("SAVE")
                                                                     .setName("save_button")
                                                                     .setHeight(200)
                                                                     .setWidth(200)
                                                                     .setX(50)
                                                                     .setY(200)
                                                                     .createControlButtonZone();

        ControlButtonZone loadButton = new ControlButtonZoneBuilder().setButtonText("LOAD")
                                                                     .setName("load_button")
                                                                     .setHeight(200)
                                                                     .setWidth(200)
                                                                     .setX(50)
                                                                     .setY(500)
                                                                     .createControlButtonZone();

        SMT.add(saveButton, loadButton);

        loadButton.addLoadUserListener(this);
        saveButton.addSaveListener(this);
    }

    @Override
    public void draw() {
        background(mainBackgroundColor);
        fill(0, 0, 0, 255);
        // text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, height / 2);
    }

    @Override
    public void loadUser(final String userName) {
        Thread t = new Thread() {
            public void run() {
                loadUserLayout(userName);
            }
        };


        t.start();
    }

    public void removeAlZones() {
        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone) {
                SMT.remove(zone);
            }
        }
    }


    public void loadUserLayout(final String userName) {

        javaxt.io.Image image = new javaxt.io.Image(Resources.getResource("1.jpg").getPath());


        PosterItem pi = new PosterItemBuilder().setName("posteritem-test")
                                               .setId(UUID.randomUUID().toString())
                                               .setWidth(400)
                                               .setHeight(500)
                                               .setX(150)
                                               .setY(600)
                                               .setColor("#34567")
                                               .setRotation(0)
                                               .setType("jpg").setImageBytes(
                        Base64.encodeBase64String(image.getByteArray()))
                                               .createPosterItem();


        try {
            RESTHelper.getInstance().postCollection(pi, RESTHelper.PosterUrl.addPosterItem(), PosterItem.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(final String userName) {

        Thread later = new Thread() {
            @Override
            public void run() {
                saveUserLayout(userName);
            }
        };
        later.start();
    }

    private void saveUserLayout(String userName) {



        final List<PictureZone> pictureZoneList = Lists.newArrayList();

        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone)
                pictureZoneList.add((PictureZone) zone);
        }


        FluentIterable<PosterItem> posterItems = FluentIterable.from(pictureZoneList)
                                                               .transform(new PictureZoneToPosterItem());

        User user = null;

//            user = RESTHelper.getInstance().users.get(0);
//        try {
//            RESTHelper.getInstance().saveUser(user);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }


}
