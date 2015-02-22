package ltg.evl.uic.poster;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.listeners.LoadUserListerner;
import ltg.evl.uic.poster.listeners.SaveUserListerner;
import ltg.evl.uic.poster.widgets.ControlButtonZone;
import ltg.evl.uic.poster.widgets.ControlButtonZoneBuilder;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.TopBarZone;
import ltg.evl.util.DBHelper;
import ltg.evl.util.DownloadHelper;
import ltg.evl.util.StyleHelper;
import ltg.evl.util.collections.PictureZoneToPosterItem;
import ltg.evl.util.collections.PosterItemToPictureZone;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;
import vialab.SMT.Zone;

import java.util.List;
import java.util.concurrent.Callable;
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

        int w = displayWidth;
        int h = displayHeight;

        int bar_h = (int) (h * .07);

        size(w, h, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);

        topBarZone = new TopBarZone(0, 0, w, bar_h, color(44, 153, 241, 255));

        SMT.add(topBarZone);


        setupControlButtons();


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

    public void remoteAlZones() {
        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone) {
                SMT.remove(zone);
            }
        }
    }

    public void loadUserLayout(final String userName) {

        remoteAlZones();

        int MILLI_SECS = 10000;
        topBarZone.setTotalTime(MILLI_SECS);
        topBarZone.startTimer();


        ListenableFuture<Boolean> explosion = service.submit(new Callable<Boolean>() {

            public Boolean call() {

                List<PosterItem> pis = DBHelper.helper().getPosterItemsForUser(userName);
                FluentIterable<PictureZone> pictureZones = FluentIterable.from(pis)
                                                                         .transform(new PosterItemToPictureZone());

                for (PictureZone pictureZone : pictureZones) {
//                    boolean hasAdded = SMT.add(pictureZone);
//                    if (hasAdded) {
//                        // topBarZone.stopTimer();
//                        pictureZone.startAnimation(true);
//                    }
                }

                return new Boolean(true);
            }
        });
        Futures.addCallback(explosion, new FutureCallback<Boolean>() {
            // we want this handler to run immediately after we push the big red button!
            public void onSuccess(Boolean explosion) {
                topBarZone.stopTimer();
            }

            public void onFailure(Throwable thrown) {
            }
        });


        // DBHelper.helper().shutdownThreads();
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

        int MILLI_SECS = 10000;
        topBarZone.setTotalTime(MILLI_SECS);
        topBarZone.startTimer();

        final List<PictureZone> pictureZoneList = Lists.newArrayList();

        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone)
                pictureZoneList.add((PictureZone) zone);
        }


        final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(2));
        ListenableFuture<Boolean> explosion = service.submit(new Callable<Boolean>() {

            public Boolean call() {


                FluentIterable<PosterItem> posterItems = FluentIterable.from(pictureZoneList)
                                                                       .transform(new PictureZoneToPosterItem());

                for (PosterItem posterItem : posterItems) {
                    System.out.println(posterItem.toString());
                }


                return new Boolean(true);
            }
        });
        Futures.addCallback(explosion, new FutureCallback<Boolean>() {
            // we want this handler to run immediately after we push the big red button!
            public void onSuccess(Boolean explosion) {
                topBarZone.stopTimer();
            }

            public void onFailure(Throwable thrown) {
            }
        });


    }


}
