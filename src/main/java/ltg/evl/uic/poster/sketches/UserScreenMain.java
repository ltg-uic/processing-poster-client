package ltg.evl.uic.poster.sketches;

import ltg.evl.uic.poster.listeners.SaveUserListerner;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.util.DownloadHelper;
import ltg.evl.util.StyleHelper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;
import vialab.SMT.Zone;


/**
 * Created by aperritano on 9/24/14.
 */
public class UserScreenMain extends PApplet implements SaveUserListerner {


    protected static Logger logger;

    private static DownloadHelper downloadHelper;

    private static String backpackPath;

    public static void main(String args[]) {
        logger = Logger.getLogger(UserScreenMain.class.getName());
        logger.setLevel(Level.INFO);

        // DBHelper.helper().fetchUsers();

        PApplet.main(new String[]{"ltg.evl.uic.poster.sketches.UserScreenMain"});


    }

    public void doInit() {
        System.out.println("Setup started");
        StyleHelper.helper().setGraphicsContext(this);
        // int defaultBarColor = StyleHelper.helper().createColor("color.keyboard.background");
    }

    @Override
    public void setup() {

        //thread("doInit");

        // size(displayWidth, displayHeight, SMT.RENDERER);

        int w = displayWidth;
        int h = displayHeight;

        size(w, h, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);

//        int bar_h = (int) (h * .07);



        //setupControlButtons();


    }

    private void setupControlButtons() {
//        ControlButtonZone saveButton = new ControlButtonZoneBuilder().setButtonText("SAVE")
//                                                                     .setName("save_button")
//                                                                     .setHeight(200)
//                                                                     .setWidth(200)
//                                                                     .setX(50)
//                                                                     .setY(50)
//                                                                     .createControlButtonZone();
//
//        ControlButtonZone loadButton = new ControlButtonZoneBuilder().setButtonText("LOAD")
//                                                                     .setName("load_button")
//                                                                     .setHeight(200)
//                                                                     .setWidth(200)
//                                                                     .setX(50)
//                                                                     .setY(300)
//                                                                     .createControlButtonZone();

        // SMT.add(saveButton, loadButton);


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


//        loadButton.addLoadUserListener(this);
//        saveButton.addSaveListener(this);
    }

    @Override
    public void draw() {


        background(255, 255, 255, 255);
        //fill(0, 0, 0, 255);
        // text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, height / 2);


    }


    public void loadUserLayout(String userName) {

        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone) {
                SMT.remove(zone);
            }
        }


        // User user = DBHelper.helper().fetchUser(userName);

//        for (PosterItem posterItem : user.getPosters().get(0).getPosterItems()) {
//            Image awtImage = null;
//            try {
//                awtImage = StyleHelper.helper().getImage(posterItem);
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            PImage pixelImage = loadImageMT(awtImage);


//            PictureZone pz = new PictureZoneBuilder().setImage(pixelImage).setPosterItem(posterItem).toPictureZone();
//
//
//            boolean hasAdded = SMT.add(pz);
//
//            if (hasAdded) {
//
//
//                pz.startAnimation();
//            }

//        }

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
        //     User user = DBHelper.helper().fetchUser(userName);

        //java.util.List<PictureZone> pictureZones = new ArrayList<>();

//        for (Zone zone : SMT.getZones()) {
//            if (zone instanceof PictureZone) {
//                PictureZone pz = (PictureZone) zone;
//
//                String uuid = pz.getUUID();
//
//                PosterItem posterItem = DBHelper.helper().getPosterItem(uuid);
//
//                System.out.println("Zone: " + pz.toString());
//                System.out.println("posteritem " + posterItem.toString());
//                System.out.println(" ");
//
//                posterItem.setWidth(pz.getWidth());
//                posterItem.setHeight(pz.getHeight());
//                posterItem.setY(pz.getY());
//                posterItem.setX(pz.getX());
//
//                //  DBHelper.helper().replacePosterItem(posterItem);
//
//                //DBHelper.helper().dbClient().store(posterItem);
//
//            }
//
//        }

    }


}
