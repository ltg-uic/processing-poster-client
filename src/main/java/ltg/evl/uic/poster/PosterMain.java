package ltg.evl.uic.poster;

import ltg.evl.JSON.Poster;
import ltg.evl.JSON.PosterItem;
import ltg.evl.util.DBHelper;
import ltg.evl.util.DownloadHelper;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import vialab.SMT.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * Created by aperritano on 9/24/14.
 */
public class PosterMain extends PApplet implements DownloadHelper.NewFileAddedEventListener {

    private static CompositeConfiguration config;
    private static DownloadHelper downloadHelper;
    private PFont face_font;
    private String[] mainBackground = config.getStringArray("color.mainBackground");
    private static String backpackPath;
    private Poster poster;
    private ArrayList<PosterItem> posterItems = new ArrayList<>();
    
    @Override
    public void setup() {
        //size(config.getInt("window.width"), config.getInt("window.height"), SMT.RENDERER);
        
        Poster poster = new Poster();
        poster.setResolution(new Dimension(displayWidth,displayHeight));
        
        size(displayWidth, displayHeight, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);

        downloadHelper.addFileWatchEventListener(this);

        //load fonts
        face_font = createFont(config.getString("button.font.name"), config.getInt("button.font.size"));


        //create an anonymous zone
        Zone newTextButtonZone = new Zone( "NewTextZone", 50, 50, 150, 150){


            int buttonBackground = createColor(config.getStringArray("button.color.background"));
            int buttonOutline = createColor(config.getStringArray("button.color.outline"));
            int buttonHighlight = createColor(config.getStringArray("button.color.highlight"));
            int buttonColor = buttonBackground;

            //draw method
            @Override
            public void draw(){
                fill(buttonColor);
                //fill( 220, 140, 160, 140);
                stroke(buttonOutline);
                strokeWeight( 3);

                rect( 0, 0, this.getWidth(), this.getHeight());

                textAlign( CENTER, CENTER);
                textFont( face_font);
                //textMode( TEXT);
                fill( 0, 0, 0);
                text("NEW TEXT", this.getWidth()/2, this.getHeight()/2 );
            }



            //touch method
            @Override
            public void touch(){
                //rst();
            }

            @Override
            public void touchDown(Touch touch){
                buttonColor = buttonHighlight;
                DownloadHelper.downloadFileToBackpack("http://www.google.com/images/logos/ps_logo2.png",backpackPath.concat("/google.png"));
                System.out.println("touch down");
            }


            @Override
            public void touchUp(Touch touch){
                if( this.getNumTouches() == 0) {
                    buttonColor = buttonBackground;
                    System.out.println("touch up");
                }
            }

        };
        
        
        
        
        SMT.add(newTextButtonZone);




//        int keyboardBGColor = createColor(config.getStringArray("color.keyboard.background"));

//        SwipeKeyboard keyboard = new PosterKeyboard(keyboardBGColor);
//        keyboard.setCornerRounding(0);
//        keyboard.setBackgroundEnabled(true);
//        keyboard.setLocation( 45, 300);
//        keyboard.addKeyListener(this);

          ParagraphZone texty = new ParagraphZone( "Texty", 300, 100, 1000, 200);
        //  SMT.add( texty);
//        keyboard.addKeyListener( texty);
//        keyboard.addSwipeKeyboardListener( texty);

        //add our zones to the sketch

        //SMT.add(keyboard);

        //get all the images in the backpack
//        File[] files = downloadHelper.getAllBackpackFiles();

//        for(File file : files) {
//            if(file.getName().toLowerCase().contains("png") || file.getName().toLowerCase().contains("jpeg") || file.getName().toLowerCase().contains("jpg")) {
//                SMT.add(new ImageZone(loadImage(file.getPath() + file.getName())));
//            }
//        }

        
    }

    @Override
    public void draw() {

        background(createColor(mainBackground));

        fill( 0, 0, 0, 255);
        text(round(frameRate)+"fps, # of zones: "+SMT.getZones().length, width/2, height/2);


    }

    public int createColor(String[] c) {
        return color(Float.parseFloat(c[0]), Float.parseFloat(c[1]), Float.parseFloat(c[2]), Float.parseFloat(c[3]));
    }




    @Override
    public void newFileAdded(DownloadHelper.FileWatchEvent evt) {
        ImageZone imageZone = new ImageZone(loadImage(evt.getFilePath()));
        SMT.add(imageZone);
    }
    
    public void save() {
        Zone[] zones = SMT.getZones();
        for (Zone zone : zones) {
            PosterItem pi = new PosterItem();
            pi.setSize(pi.getSize());
            pi.setLocation(pi.getLocation());
            
            if( zone instanceof ImageZone ) {
                BufferedImage bi = (BufferedImage) zone.getImage();
                //DBHelper.getSingleton().saveAttachment(pi,bi);
            }

        }

    }

    public static void main(String args[])  {
        config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        

        
        try {
            config.addConfiguration(new PropertiesConfiguration("system.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        //get the back path

        backpackPath = config.getString("backpack.path");
        downloadHelper = new DownloadHelper(backpackPath);


        PApplet.main(new String[]{"ltg.evl.uic.poster.PosterMain"});
    }
}
