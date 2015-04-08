package tmp;

import ltg.commons.SimpleMQTTClient;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.json.mongo.PosterItemBuilder;
import ltg.evl.uic.poster.widgets.*;
import ltg.evl.util.MQTTPipe;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import vialab.SMT.*;

import java.awt.*;


public class UIControlTest extends PApplet {


    public static PFont helveticaFont = null;
    private static SimpleMQTTClient sc;
    private static Logger logger;
    int buttonWidth = 100;
    int buttonHeight = buttonWidth;
    int buttonStartX = 20;
    int buttonStartY = buttonStartX;
    int whiteColor = color(255);
    int greenButtonColor = color(35, 147, 70);
    int yellowButtonColor = color(244, 208, 63);
    private UserButton editButton;
    private UserButton presentButton;
    private NewTextButton newTextButton;

    public static void main(String args[]) {

        logger = Logger.getLogger(UIControlTest.class.getName());
        logger.setLevel(org.apache.log4j.Level.ALL);

//        sc = new SimpleMQTTClient("ltg.evl.uic.edu", "test-bot");
//        sc.subscribe("quakes", new MessageListener() {
//            @Override
//            public void processMessage(String message) {
//
//
//                SMT.add(new TextBoxZone("quake", ColorHelper.randInt(0,500), ColorHelper.randInt(0,500) ,300,35, message));
//                System.out.println("Received " + message );
//            }
//        });

        PApplet.main(new String[]{"tmp.UIControlTest"});
    }


    public void doInit() {
//        helveticaFont = loadFont(Resources.getResource("Roboto-Light-48.vlw").getPath());
        MQTTPipe.getInstance();
    }

    public void setup() {

        thread("doInit");
        int screen_width = 1200;
        int screen_height = 800;
        size(screen_width, screen_height, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);
        SMT.debug = true;
        SMT.setTouchDraw(TouchDraw.NONE);


        PosterButton posterButton = new PosterButton("Hey", "The fox jumped of the the big advance", 200, 200, 250, 75);
        // SMT.add(posterButton);


        //SMT.add(buttonZone);


        ButtonZone buttonZone = new ButtonZone("b", 400, 400, 100, 100) {

            @Override
            public void touchUp(Touch touch) {

                String text = "The friendly lawyer is now managing a bowl. A fierce duck leased a treehouse. The fierce table spoke to the stable building.";


                javaxt.io.Image jxt2 = new javaxt.io.Image(ZoneHelper.renderTextToImage(
                        ZoneHelper.helveticaNeue18JavaFont, new Color(0, 0, 0), text,
                        600));

                //jxt2.saveAs("/Users/aperritano/dev/research/poster/processing-poster-client/src/main/java/tmp/NEW_TEXT.png");

                PImage pImage = new PImage(jxt2.getBufferedImage());

                pImage.save(
                        "/Users/aperritano/dev/research/poster/processing-poster-client/src/main/java/tmp/NEW_TEXT.png");
                PosterItem pi = new PosterItemBuilder().setY(200)
                                                       .setX(200)
                                                       .setHeight(jxt2.getHeight())
                                                       .setWidth(jxt2.getWidth())
                                                       .setName("test")
                                                       .setContent(text)
                                                       .createPosterItem();

                PictureZone pz = new PictureZoneBuilder().setX(pi.getX())
                                                         .setY(pi.getY())
                                                         .setUuid("fuck you")
                                                         .setWidth(pi.getWidth())
                                                         .setHeight(pi.getHeight())
                                                         .setImage(pImage)
                                                         .createPictureZone();
                SMT.add(pz);
//                TextBoxZone textBoxZone =
//                        new TextBoxZone("uuid", "Too many short sentences can hurt an essay. of the the big advanceToo many short sentences can hurt an essay. of the the big advanceToo many short sentences can hurt an essay. of the the big advanceToo many short sentences can hurt an essay. of the the big advanceToo many short sentences can hurt an essay. of the the big advanceToo many short sentences can hurt an essay. of the the big advanceToo many short sentences can hurt an essay. of the the big advanceToo many short sentences can hurt an essay. of the the big advanceToo many short sentences can hurt an essay. of the the big advanceToo many short sentences can hurt an essay. of the the big advanceToo many short sentences can hurt an essay. of the the big advance", 150,150, 500, 200);
//
//                SMT.add(textBoxZone);


            }
        };

        SMT.add(buttonZone);

        ButtonZone mqttZone = new ButtonZone("b", 400, 400, 50, 50) {

            @Override
            public void touchUp(Touch touch) {
                MQTTPipe.getInstance().publishMessage("HELLLOO POSTER");
            }
        };

        SMT.add(mqttZone);


        VideoZone videoZone = new VideoZone("vid", 200, 200, 400, 400);

        SMT.add(videoZone);
    }


    public void draw() {
        background(255);
        fill(0);
        text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, 10);
    }


}
