package ltg.evl.uic.poster;

import de.looksgood.ani.Ani;
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
    
    private int mainBackgroundColor;
    private int from;
    private int to;
    private Circle[] circles;


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
        // Ani.init() must be called always first!
        Ani.init(this);
    }

    @Override
    public void setup() {

        doInit();
        circles = new Circle[5];

        from = color(255, 8, 8);
        to = color(8, 187, 255);

        for(int i=0; i<circles.length; i++) {
            circles[i] = new Circle();
        }
        
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


            background(mainBackgroundColor);
            fill(0, 0, 0, 255);
            // text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, height / 2);
        for(int i=0; i< circles.length; i++) circles[i].draw();


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

    
    class Circle {
        float x = random(0,width);
        float y = random(0,height);
        int diameter = 5;
        Ani diameterAni;
        int c = color(0);

        Circle() {
            // diameter animation
            

            
            diameterAni = new Ani(this, random(1,5), 0.5f, "diameter", 50.0f, Ani.EXPO_IN_OUT, "onEnd:randomize");
            // repeat yoyo style (go up and down)
            diameterAni.setPlayMode(Ani.YOYO);
            // repeat 3 times
            diameterAni.repeat(3);
        }

        void draw() {
            fill(c);
            ellipse(x,y,diameter,diameter);
            fill(0);
            text(diameterAni.getRepeatNumber()+" / "+diameterAni.getRepeatCount(), x, y+diameter);
        }

        void randomize(Ani _ani) {
            c = lerpColor(from, to, random(1));

            // new repeat count
            int newCount = 1+2*round(random(4));
            diameterAni.repeat(newCount);
            // restart
            diameterAni.start();

            // move to new position
            

            
            Ani.to(this, 1.5f, "x", random(0,width), Ani.EXPO_IN_OUT);
            Ani.to(this, 1.5f, "y", random(0,height), Ani.EXPO_IN_OUT);
        }
    }
    
}
