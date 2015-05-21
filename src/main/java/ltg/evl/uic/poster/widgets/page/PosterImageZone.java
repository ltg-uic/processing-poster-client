package ltg.evl.uic.poster.widgets.page;

import processing.core.PImage;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 5/13/15.
 */
public class PosterImageZone extends Zone {
    /** The PImage that contains the image passed by the user **/
    private PImage image;
    /** The tint colour of the image zone **/
    private int tint = 0xFFFFFFFF;


    /**
     * PosterImageZone constructor, creates a rectangular zone and draws a PImage to it.
     *
     * @param name String The name of the zone
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     * @param x int X-coordinate of the upper left corner of the zone
     * @param y int Y-coordinate of the upper left corner of the zone
     * @param width int Width of the zone
     * @param height int Height of the zone
     **/
    public PosterImageZone(String name, PImage image,
                      int x, int y, int width, int height) {
        super( name, x, y, width, height);
        this.image = image;
    }

    /**
     * PosterImageZone constructor, creates a rectangular zone and draws a PImage to it.
     *
     * @param name String The name of the zone
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     * @param x int X-coordinate of the upper left corner of the zone
     * @param y int Y-coordinate of the upper left corner of the zone
     * @param width int Width of the zone
     * @param height int Height of the zone
     * @param tint int The image will be tinted by this colour
     **/
    public PosterImageZone(String name, PImage image,
                      int x, int y, int width, int height, int tint) {
        this( name, image, x, y, width, height);
        this.tint = tint;
    }

    /**
     * PosterImageZone constructor, creates a rectangular zone and draws a PImage to it.
     *
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     * @param x int X-coordinate of the upper left corner of the zone
     * @param y int Y-coordinate of the upper left corner of the zone
     * @param width int Width of the zone
     * @param height int Height of the zone
     **/
    public PosterImageZone( PImage image, int x, int y, int width, int height) {
        this( null, image, x, y, width, height);
    }

    /**
     * PosterImageZone constructor, creates a rectangular zone and draws a PImage to it.
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     **/
    public PosterImageZone( PImage image) {
        this( image, 0, 0, image.width, image.height);
    }

    /**
     * PosterImageZone constructor, creates a rectangular zone and draws a PImage to it.
     *
     * @param name String The name of the zone
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     **/
    public PosterImageZone( String name, PImage image) {
        this( name, image, 0, 0, image.width, image.height);
    }

    /**
     * PosterImageZone constructor. Creates a rectangular zone and draws a PImage to
     * it. The width and height of the zone is set to the PImage's width and
     * height.
     *
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     * @param x int X-coordinate of the upper left corner of the zone
     * @param y int Y-coordinate of the upper left corner of the zone
     **/
    public PosterImageZone( PImage image, int x, int y) {
        this( image, x, y, image.width, image.height);
    }

    /**
     * PosterImageZone constructor. Creates a rectangular zone and draws a PImage to it. The width and height of the zone is set to the PImage's width and height.
     *
     * @param name String The name of the zone
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     * @param x int X-coordinate of the upper left corner of the zone
     * @param y int Y-coordinate of the upper left corner of the zone
     **/
    public PosterImageZone( String name, PImage image, int x, int y) {
        this( name, image, x, y, image.width, image.height);
    }

    /**
     * Pass a created PosterImageZone to 'clone' it.
     * @param PosterImageZone PosterImageZone The PosterImageZone that is copied.
     **/
    public PosterImageZone( PosterImageZone PosterImageZone) {
        this( PosterImageZone.name, PosterImageZone.image,
              PosterImageZone.x, PosterImageZone.y, PosterImageZone.width, PosterImageZone.height);
    }

    /**
     * This creates an PosterImageZone from a URL
     * @param url The URL of an image
     **/
    public PosterImageZone( String url) {
        this( SMT.getApplet().loadImage( url));
    }

    /** Used to override what is drawn into the zone **/
    @Override
    public void draw() {
        tint( tint);
        image( this.getZoneImage(), 0, 0, this.getWidth(), this.getHeight());
    }

    //get accessor methods
    /**
     * Get the image being drawn by this zone
     * @return the image being drawn by this zone
     **/
    public PImage getZoneImage(){
        return image;
    }
    /**
     * Get the tint of the image drawn by this zone
     * @return the tint being used by this zone
     **/
    public int getTint(){
        return tint;
    }

    //set accessor methods
    /**
     * Set the image being drawn by this zone
     * @param image the image currently that should be drawn by this zone
     **/
    public void setZoneImage( PImage image){
        this.image = image;
    }
    /**
     * Set the tint of the image drawn by this zone
     * @param tint the tint used by this zone
     **/
    public void setTint( int tint){
        this.tint = tint;
    }

    //disabled warnings
    boolean warnDraw() {
        return false;
    }
}

