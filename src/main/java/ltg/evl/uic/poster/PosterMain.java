package ltg.evl.uic.poster;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import processing.core.PApplet;
import processing.core.PFont;
import vialab.SMT.ButtonZone;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 9/24/14.
 */
public class PosterMain extends PApplet {

    private static CompositeConfiguration config;
    private PFont face_font;

    @Override
    public void setup() {
        size(1200, 800, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);

        //create an anonymous zone
        Zone anonyzone = new Zone( "AnonyZone", 50, 50, 125, 125){


            String[] background = config.getStringArray("colors.riverblue");


            //draw method
            @Override
            public void draw(){
                fill(Float.parseFloat(background[0]),Float.parseFloat(background[1]),Float.parseFloat(background[2]));
                //fill( 220, 140, 160, 140);
                stroke( 240, 180);
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
        };

        //add our zones to the sketch
        SMT.add( anonyzone);

        //load fonts
        face_font = createFont("HelveticaNeue-Bold", 23);       
        
        
        
        
    }

    @Override
    public void draw() {
        background(0, 0, 0);
    }




    public static void main(String args[])  {


        config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        try {
            config.addConfiguration(new PropertiesConfiguration("colors.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        PApplet.main(new String[]{"ltg.evl.uic.poster.PosterMain"});
    }
}
