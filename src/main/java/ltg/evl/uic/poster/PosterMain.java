package ltg.evl.uic.poster;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import ltg.commons.SimpleMQTTClient;
import ltg.evl.uic.poster.json.mongo.*;
import ltg.evl.uic.poster.listeners.LoadUserListerner;
import ltg.evl.uic.poster.listeners.SaveUserListerner;
import ltg.evl.uic.poster.widgets.*;
import ltg.evl.util.DownloadHelper;
import ltg.evl.util.RESTHelper;
import ltg.evl.util.StyleHelper;
import ltg.evl.util.collections.PictureZoneToPosterItem;
import ltg.evl.util.collections.PosterItemToPictureZone;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import processing.core.PFont;
import vialab.SMT.*;

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
    private static SimpleMQTTClient sc;
    private static DownloadHelper downloadHelper;

    private static String backpackPath;
    final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    private int mainBackgroundColor;
    private int from;
    private int to;
    private TopBarZone topBarZone;
    private tmp.ControlButtonZone presentButton;
    private tmp.ControlButtonZone editButton;

    public static void main(String args[]) {
        logger = Logger.getLogger(PosterMain.class.getName());
        logger.setLevel(Level.ALL);
        //backpackPath = StyleHelper.helper().getConfiguration().getString("backpack.path");


        StyleHelper.helper();

        PApplet.main(new String[]{"ltg.evl.uic.poster.PosterMain"});

        //get the back path


        //downloadHelper = new DownloadHelper(backpackPath);


    }

    public void doInit() {
        System.out.println("Setup started");
        StyleHelper.helper().setGraphicsContext(this);

        logger.debug("POSTER MQTT CLIENT STARTED");

        // MQTTPipe.getInstance();
    }

    @Override
    public void setup() {

        thread("doInit");

        int w = displayWidth - 100;
        int h = displayHeight - 100;

        int bar_h = (int) (h * .07);

        size(w, h, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);

        setupControlButtons();

        ObjectSubscriber objectSubscriber = new ObjectSubscriber() {
            @Override
            public void handleUpdatedObject(ObjectEvent objectEvent) {
                super.handleUpdatedObject(objectEvent);
                if (objectEvent.getEventType().equals(ObjectEvent.OBJ_TYPES.POST_ITEM)) {
                    PosterItem newPosterItem = (PosterItem) objectEvent.getGenericJson();
                    loadPosterItems(Lists.newArrayList(newPosterItem), false);
                } else if (objectEvent.getEventType().equals(ObjectEvent.OBJ_TYPES.INIT_ALL)) {
                    List<User> allUsers = PosterDataModelHelper.getInstance().allUsers;

                    if (allUsers != null && !allUsers.isEmpty()) {
                        loadPosterForUser(allUsers.get(0));
                    }

                } else if (objectEvent.getEventType().equals(ObjectEvent.OBJ_TYPES.DELETE_POSTER_ITEM)) {

                    String posterItemId = objectEvent.getItemId();
//                    for(PosterItem pi : PosterDataModelHelper.getInstance().allPosterItems) {
//                        if(pi.getUuid().equals(posterItemId)) {
//                            PosterDataModelHelper.getInstance().allPosterItems.remove(pi);
//                        }
//                    }


                    SMT.remove(posterItemId);

                }

            }
        };


        logger.debug("POSTER MODELER STARTED");
        // PosterDataModelHelper.getInstance().addUserSubscriber(userSubscriber);
        PosterDataModelHelper.getInstance().addObjectSubscriber(objectSubscriber);

        logger.debug("POSTER MODELER INIT ALL COLLECTIONS");
        RESTHelper.getInstance().initAllCollections();
    }

    private void loadPosterForUser(User user) {

        List<Poster> allPostersForUser = PosterDataModelHelper.getInstance().getAllPostersForUser(user);

        if (!allPostersForUser.isEmpty()) {
            Poster poster = allPostersForUser.get(0);
            List<PosterItem> allPostersItemsForPoster = PosterDataModelHelper.getInstance()
                                                                             .getAllPostersItemsForPoster(poster);
            loadPosterItems(allPostersItemsForPoster, true);
        }


    }

    public void loadPosterItems(List<PosterItem> posterItems, boolean shouldRemove) {

        if (shouldRemove)
            removeAlZones();

        List<PosterItem> textItems = Lists.newArrayList();
        List<PosterItem> pictureItems = Lists.newArrayList();

        for (PosterItem posterItem : posterItems) {
            if (posterItem.getType().equals(PosterItem.TEXT)) {
                textItems.add(posterItem);
            } else {
                pictureItems.add(posterItem);
            }
        }

        FluentIterable<PictureZone> pictureZones = FluentIterable.from(pictureItems)
                                                                 .transform(new PosterItemToPictureZone());

        FluentIterable<TextBoxZone> textZones = FluentIterable.from(textItems)
                                                              .transform(new PosterItemToTextZone());


        for (PictureZone pictureZone : pictureZones) {
            if (pictureZone != null) {

                boolean hasAdded = SMT.add(pictureZone);
                if (hasAdded) {
                    // topBarZone.stopTimer();
                    pictureZone.startAnimation(true);
                }
            }
        }

        for (TextBoxZone textZone : textZones) {
            if (textZone != null) {
                boolean hasAdded = SMT.add(textZone);
//                if (hasAdded) {
//                    // topBarZone.stopTimer();
//                    pictureZone.startAnimation(true);
//                }
            }
        }

        // DBHelper.getInstance().shutdownThreads();
    }

    private void setupControlButtons() {


        int buttonWidth = 100;
        int buttonHeight = buttonWidth;

        int buttonStartX = 20;
        int buttonStartY = buttonStartX;

        int whiteColor = color(255);
        int greenButtonColor = color(35, 147, 70);


        int yellowButtonColor = color(38, 166, 243, 255);

        PFont controlButtonFont = createFont("HelveticaNeue-Bold", 23);

        //create mode buttons
        presentButton = new tmp.ControlButtonZone("PresentButton", buttonStartX, buttonStartY, buttonWidth,
                                                  buttonHeight, "PRESENT", greenButtonColor, yellowButtonColor,
                                                  controlButtonFont);
        editButton = new tmp.ControlButtonZone("EditButton", buttonStartX,
                                               presentButton.getY() + presentButton.getHeight() + presentButton.getY(),
                                               buttonWidth, buttonHeight, "EDIT", greenButtonColor, yellowButtonColor,
                                               controlButtonFont);



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

        saveButton.addSaveListener(new SaveUserListerner() {
            @Override
            public void saveUser(String userName) {

                saveUserLayout("");
                // MQTTPipe.getInstance().publishMessage("HELLO");
            }
        });

        loadButton.addSaveListener(new SaveUserListerner() {
            @Override
            public void saveUser(String userName) {

            }
        });


        ButtonZone testButton = new ButtonZone("TestButton", 100, 100, 200, 200, "EXIT Button") {

            @Override
            public void press(Touch touch) {
                System.exit(0);
            }
        };

        SMT.add(loadButton, testButton);
        //SMT.add(saveButton);
        //loadButton.addLoadUserListener(this);
        //saveButton.addSaveListener(this);
    }

    @Override
    public void draw() {
        background(255, 255, 255, 255);
        fill(0, 0, 0, 255);
        text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, height / 2);
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
                                               .setUuid(UUID.randomUUID().toString())
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
