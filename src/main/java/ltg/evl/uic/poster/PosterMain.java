package ltg.evl.uic.poster;

import ltg.evl.json.mongo.PosterItem;
import ltg.evl.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadUserListerner;
import ltg.evl.uic.poster.listeners.SaveUserListerner;
import ltg.evl.util.DBHelper;
import ltg.evl.util.DownloadHelper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import processing.core.PImage;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;
import vialab.SMT.Zone;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by aperritano on 9/24/14.
 */
public class PosterMain extends PApplet implements LoadUserListerner, SaveUserListerner {


    protected static org.apache.log4j.Logger logger;
    
    private static DownloadHelper downloadHelper;

    private static String backpackPath;
    String[] urls = {
            "http://processing.org",
            "http://www.processing.org/exhibition/",
            "http://www.processing.org/reference/",
            "http://www.processing.org/reference/libraries",
            "http://www.processing.org/reference/tools",
            "http://www.processing.org/reference/environment",
            "http://www.processing.org/learning/",
            "http://www.processing.org/learning/basics/",
            "http://www.processing.org/learning/topics/",
            "http://www.processing.org/learning/gettingstarted/",
            "http://www.processing.org/download/",
            "http://www.processing.org/shop/",
            "http://www.processing.org/about/",
            "http://www.processing.org/about/people"
    };
    // This will keep track of whether the thread is finished
    boolean finished = true;
    // And how far along
    float percent = 0;
    // A variable to keep all the data loaded
    String allData;
    private int mainBackgroundColor;
    private boolean mode;

    public static void main(String args[]) {
        logger = Logger.getLogger(PosterMain.class.getName());
        logger.setLevel(Level.INFO);

        DBHelper.helper().fetchUsers();

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

        size(displayWidth, displayHeight, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);

        thread("doInit");



        ControlButtonZone saveButton = new ControlButtonZoneBuilder().setButtonText("SAVE")
                                                                     .setName("save_button")
                                                                     .setHeight(200)
                                                                     .setWidth(200)
                                                                     .setX(50)
                                                                     .setY(50)
                                                                     .createControlButtonZone();

        ControlButtonZone loadButton = new ControlButtonZoneBuilder().setButtonText("LOAD")
                                                                     .setName("load_button")
                                                                     .setHeight(200)
                                                                     .setWidth(200)
                                                                     .setX(50)
                                                                     .setY(300)
                                                                     .createControlButtonZone();

        SMT.add(saveButton, loadButton);


//        DBHelper.helper().addDBListener(new DBListener() {
//            @Override
//            public void posterItemsUpdated(List<PosterItem> posterItems) {
//                for (PosterItem posterItem : posterItems) {
//                    Image awtImage = null;
//                    try {
//                        awtImage = StyleHelper.helper().getImage(posterItem);
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    PImage pixelImage = loadImageMT(awtImage);
//
//                    PictureZone pz = new PictureZoneBuilder().setImage(pixelImage)
//                                                             .setHeight(posterItem.getHeight())
//                                                             .setWidth(posterItem.getWidth())
//                                                             .setUUID(posterItem.getName())
//                                                             .setX(posterItem.getX())
//                                                             .setY(posterItem.getY()).createPictureZone();
//
//                    SMT.add(pz);
//                }
//
//
//            }
//        });


        loadButton.addLoadUserListener(this);
        saveButton.addSaveListener(this);


    }

    @Override
    public void draw() {

        if (finished) {
            background(mainBackgroundColor);
            fill(0, 0, 0, 255);
            // text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, height / 2);
        } else if (finished == false) {
            progressbar();
        }

    }

    public void progressbar() {
        if (!finished) {
            stroke(0);
            noFill();
            rect(width / 2 - 150, height / 2, 300, 10);
            fill(0);
            // The size of the rectangle is mapped to the percentage completed
            float w = map(percent, 0, 1, 0, 300);
            rect(width / 2 - 150, height / 2, w, 10);
            textSize(14);
            textAlign(CENTER);
            fill(0);
            text("Loading", width / 2, height / 2 + 30);
        }

    }

    @Override
    public void mouseClicked() {
        thread("loadData");
        mode = true;
    }


    public void loadData() {
        // The thread is not completed
        finished = false;
        // Reset the data to empty
        allData = "";

        // Look at each URL
        // This example is doing some highly arbitrary things just to make it take longer
        // If you had a lot of data parsing you needed to do, this can all happen in the background
        for (int i = 0; i < urls.length; i++) {
            String[] lines = loadStrings(urls[i]);
            // Demonstrating some arbitrary text splitting, joining, and sorting to make the thread take longer
            String allTxt = join(lines, " ");
            String[] words = splitTokens(allTxt, "\t+\n <>=\\-!@#$%^&*(),.;:/?\"\'");
            for (int j = 0; j < words.length; j++) {
                words[j] = words[j].trim();
                words[j] = words[j].toLowerCase();
            }
            words = sort(words);
            allData += join(words, " ");
            percent = PApplet.parseFloat(i) / urls.length;
        }

        String[] words = split(allData, " ");
        words = sort(words);
        allData = join(words, " ");

        // The thread is completed!
        finished = true;
        mode = false;
    }
    
    @Override
    public void loadUser(final String userName) {
        Thread later = new Thread() {
            @Override
            public void run() {
                loadUserLayout(userName);
            }
        };
        later.start();
    }

    public void loadUserLayout(String userName) {

        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone) {
                SMT.remove(zone);
            }
        }


        User user = DBHelper.helper().fetchUser(userName);

        for (PosterItem posterItem : user.getPosters().get(0).getPosterItems()) {
            Image awtImage = null;
            try {
                awtImage = StyleHelper.helper().getImage(posterItem);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            PImage pixelImage = loadImageMT(awtImage);

            PictureZone pz = new PictureZoneBuilder().setImage(pixelImage)
                                                     .setHeight(posterItem.getHeight())
                                                     .setWidth(posterItem.getWidth())
                                                     .setUUID(posterItem.getId().toString())
                                                     .setX(posterItem.getX())
                                                     .setY(posterItem.getY()).createPictureZone();

            SMT.add(pz);
            pz.setX(posterItem.getX());
            pz.setY(posterItem.getY());
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
        User user = DBHelper.helper().fetchUser(userName);

        java.util.List<PictureZone> pictureZones = new ArrayList<>();

        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone) {
                PictureZone pz = (PictureZone) zone;

                String uuid = pz.getUUID();

                PosterItem posterItem = DBHelper.helper().getPosterItem(uuid);

                System.out.println("Zone: " + pz.toString());
                System.out.println("posteritem " + posterItem.toString());
                System.out.println(" ");

                posterItem.setWidth(pz.getWidth());
                posterItem.setHeight(pz.getHeight());
                posterItem.setY(pz.getY());
                posterItem.setX(pz.getX());

                //  DBHelper.helper().replacePosterItem(posterItem);

                DBHelper.helper().dbClient().store(posterItem);

            }

        }

    }
}
