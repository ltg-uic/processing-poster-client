package tmp;

import de.looksgood.ani.Ani;
import ltg.commons.SimpleMQTTClient;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.json.mongo.PosterItemBuilder;
import ltg.evl.uic.poster.widgets.*;
import ltg.evl.util.MQTTPipe;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
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
    private LogoutButton newZone;
    private EditColorZone colorZone;
    private PVector target1;
    private PVector point1;
    private PVector target2;
    private PVector point2;

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
        final int screen_width = 1200;
        int screen_height = 800;
        size(screen_width, screen_height, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);
        SMT.debug = true;
        SMT.setTouchDraw(TouchDraw.NONE);


        PosterButton posterButton = new PosterButton("Hey", "The fox jumped of the the big advance", 200, 200, 250, 75,
                                                     ZoneHelper.blueOutline);
        // SMT.add(posterButton);


        //SMT.add(buttonZone);


        ButtonZone buttonZone = new ButtonZone("b", "Group 1", ZoneHelper.helveticaNeue18Font) {

            @Override
            public void touchUp(Touch touch) {

                String text = "The friendly lawyer is now managing a bowl. A fierce duck leased a treehouse. The fierce table spoke to the stable building.";


                javaxt.io.Image jxt2 = new javaxt.io.Image(ZoneHelper.renderTextToImage(
                        ZoneHelper.helveticaNeue20JavaFont, new Color(0, 0, 0), text,
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

        buttonZone.setHeight(50);
        buttonZone.setWidth(200);
        //SMT.add(buttonZone);


        Ani.init(this);

        point1 = new PVector(50, 50);


        colorZone = new EditColorZone("lol", 40, 40, 50) {
            @Override
            public void draw() {
                fill(130);
                rect(point1.x, point1.y, this.getWidth(), this.getHeight());
            }
        };

        newZone = new LogoutButton("b", "Group 1", colorZone.getWidth() + (colorZone.getX() / 2) + 5,
                                   (colorZone.getY() / 2) - 5, 100, 50, ZoneHelper.redOutline);


        //ideoZone videoZone = new VideoZone("vid", 200, 200, 400, 400);


        SMT.add(colorZone);


        ButtonZone mqttZone = new ButtonZone("b", 400, 400, 50, 50) {


            @Override
            public void touchUp(Touch touch) {


                PresentationZone z = new PresentationZone("bg", 0, 0, screen_width, screen_width) {

                };

                String text = "The friendly lawyer is now managing a bowl. A fierce duck leased a treehouse. The fierce table spoke to the stable building.";


                javaxt.io.Image jxt2 = new javaxt.io.Image(ZoneHelper.renderTextToImage(
                        ZoneHelper.helveticaNeue20JavaFont, new Color(0, 0, 0), text,
                        600));

                //jxt2.saveAs("/Users/aperritano/dev/research/poster/processing-poster-client/src/main/java/tmp/NEW_TEXT.png");

                PImage pImage = loadImage("0.jpg");


                PosterItem pi = new PosterItemBuilder().setY(200)
                                                       .setX(200)
                                                       .setHeight(pImage.height)
                                                       .setWidth(pImage.width)
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


//                  SMT.add(z);
//
//                  z.add(pz);
//                  pz.startAni(0f, 1f);


//                target1 = new PVector(colorZone.getX() + 250, colorZone.getY() + 250);
//
//                Ani.to(point1, 1.0f, "x", target1.x);
//                Ani.to(point1, 1.0f, "y", target1.y);


                // MQTTPipe.getInstance().publishMessage("HELLLOO POSTER");
            }
        };
        //  SMT.add(mqttZone);

        PresentationZone presentationZone = new PresentationZone("s", 0, 0, SMT.getApplet().getWidth(),
                                                                 SMT.getApplet().getHeight());

        SMT.add(presentationZone);
        presentationZone.showDialog("Do you want to logout?", 200);
        // SMT.add(newZone);
        //newZone.toString();
    }


    public void draw() {
        background(255);
        fill(0);
        text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, 10);
        text("color zone: " + colorZone.toString() + " newZone " + newZone.toString(), 250, 20);
    }


}
