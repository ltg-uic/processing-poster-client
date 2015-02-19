package ltg.evl.uic.poster;

import de.looksgood.ani.Ani;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import processing.core.PImage;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 2/13/15.
 */
public class PictureZone extends Zone {

    //-- from image zone

    int diameter = 50;
    Ani widthAni, heightAni;
    /**
     * The PImage that contains the image passed by the user *
     */
    private PImage image;
    /**
     * The tint colour of the image zone *
     */
    private int tint = 0xFFFFFFFF;
    private String UUID;
    private int animationWidth = 0;
    private int animationHeight = 0;
    private int initialX = 0;
    private int initialY = 0;
    
    public PictureZone(PImage image, String UUID, int x, int y, int width, int height) {
        this(image, x, y, width, height);
        this.UUID = UUID;
    }

    public PictureZone(PosterItem posterItem, PImage image) {
        this.image = image;
        this.setHeight(0);
        this.setWidth(0);
        this.setUUID(posterItem.getId().toString());
        this.setAnimationHeight(posterItem.getHeight());
        this.setAnimationWidth(posterItem.getWidth());
        this.setX(posterItem.getX());
        this.setY(posterItem.getY());
        this.initialX = posterItem.getX();
        this.initialY = posterItem.getY();
    }

    /**
     * PictureZone constructor, creates a rectangular zone and draws a PImage to it.
     *
     * @param name   String The name of the zone
     * @param image  PImage The PImage that will be drawn to the zone's coordinates.
     * @param x      int X-coordinate of the upper left corner of the zone
     * @param y      int Y-coordinate of the upper left corner of the zone
     * @param width  int Width of the zone
     * @param height int Height of the zone
     */
    public PictureZone(String name, PImage image,
                       int x, int y, int width, int height) {
        super(name, x, y, width, height);
        this.image = image;
    }

    /**
     * PictureZone constructor, creates a rectangular zone and draws a PImage to it.
     *
     * @param name   String The name of the zone
     * @param image  PImage The PImage that will be drawn to the zone's coordinates.
     * @param x      int X-coordinate of the upper left corner of the zone
     * @param y      int Y-coordinate of the upper left corner of the zone
     * @param width  int Width of the zone
     * @param height int Height of the zone
     * @param tint   int The image will be tinted by this colour
     */
    public PictureZone(String name, PImage image,
                       int x, int y, int width, int height, int tint) {
        this(name, image, x, y, width, height);
        this.tint = tint;
    }

    /**
     * PictureZone constructor, creates a rectangular zone and draws a PImage to it.
     *
     * @param image  PImage The PImage that will be drawn to the zone's coordinates.
     * @param x      int X-coordinate of the upper left corner of the zone
     * @param y      int Y-coordinate of the upper left corner of the zone
     * @param width  int Width of the zone
     * @param height int Height of the zone
     */
    public PictureZone(PImage image, int x, int y, int width, int height) {
        this(null, image, x, y, width, height);
    }

    /**
     * PictureZone constructor, creates a rectangular zone and draws a PImage to it.
     *
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     */
    public PictureZone(PImage image) {
        this(image, 0, 0, image.width, image.height);
    }

    /**
     * PictureZone constructor, creates a rectangular zone and draws a PImage to it.
     *
     * @param name  String The name of the zone
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     */
    public PictureZone(String name, PImage image) {
        this(name, image, 0, 0, image.width, image.height);
    }

    /**
     * PictureZone constructor. Creates a rectangular zone and draws a PImage to
     * it. The width and height of the zone is set to the PImage's width and
     * height.
     *
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     * @param x     int X-coordinate of the upper left corner of the zone
     * @param y     int Y-coordinate of the upper left corner of the zone
     */
    public PictureZone(PImage image, int x, int y) {
        this(image, x, y, image.width, image.height);
    }

    /**
     * PictureZone constructor. Creates a rectangular zone and draws a PImage to it. The width and height of the zone is set to the PImage's width and height.
     *
     * @param name  String The name of the zone
     * @param image PImage The PImage that will be drawn to the zone's coordinates.
     * @param x     int X-coordinate of the upper left corner of the zone
     * @param y     int Y-coordinate of the upper left corner of the zone
     */
    public PictureZone(String name, PImage image, int x, int y) {
        this(name, image, x, y, image.width, image.height);
    }

    /**
     * Pass a created PictureZone to 'clone' it.
     *
     * @param PictureZone PictureZone The PictureZone that is copied.
     */
    public PictureZone(PictureZone PictureZone) {
        this(PictureZone.name, PictureZone.image,
             PictureZone.x, PictureZone.y, PictureZone.width, PictureZone.height);
    }

    /**
     * This creates an PictureZone from a URL
     *
     * @param url The URL of an image
     */
    public PictureZone(String url) {
        this(SMT.getApplet().loadImage(url));
    }

    @Override
    public void draw() {
        tint(getTint());
        image(this.image, 0, 0, this.width, this.height);
    }

    //-- from image zone

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public String toString() {
        return "id:" + getUUID() + " W: " + getWidth() + " H: " + getHeight() + " X: " + getX() + " Y:" + getY();
    }

    public int getAnimationHeight() {
        return animationHeight;
    }

    public void setAnimationHeight(int animationHeight) {
        this.animationHeight = animationHeight;
    }

    public int getAnimationWidth() {
        return animationWidth;
    }

    public void setAnimationWidth(int animationWidth) {
        this.animationWidth = animationWidth;
    }

    public void startAnimation() {
        // Ani.init() must be called always first!
        Ani.init(this.applet);
        setX(initialX);
        setY(initialY);
        // define a Ani with callbacks, specify the method name after the keywords: onStart, onEnd, onDelayEnd and onUpdate
        widthAni = new Ani(this, 1.5f, "width", animationWidth, Ani.EXPO_IN_OUT, "onStart:itsStarted");
        heightAni = new Ani(this, 1.5f, "height", animationHeight, Ani.EXPO_IN_OUT, "onStart:itsStarted");
        widthAni.start();
        heightAni.start();
    }

    public void itsStarted() {
        System.out.println("STARTED");
    }

    //get accessor methods

    /**
     * Get the image being drawn by this zone
     *
     * @return the image being drawn by this zone
     */
    public PImage getZoneImage() {
        return image;
    }

    /**
     * Set the image being drawn by this zone
     *
     * @param image the image currently that should be drawn by this zone
     */
    public void setZoneImage(PImage image) {
        this.image = image;
    }

    //set accessor methods

    /**
     * Get the tint of the image drawn by this zone
     *
     * @return the tint being used by this zone
     */
    public int getTint() {
        return tint;
    }

    /**
     * Set the tint of the image drawn by this zone
     *
     * @param tint the tint used by this zone
     */
    public void setTint(int tint) {
        this.tint = tint;
    }

    //disabled warnings
    boolean warnDraw() {
        return false;
    }
}
