package ltg.evl.uic.poster.widgets;

import com.google.common.base.Objects;
import de.looksgood.ani.Ani;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.util.ImageLoader;
import processing.core.PImage;
import vialab.SMT.ImageZone;
import vialab.SMT.SMT;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class PictureZone extends ImageZone implements DeleteButtonListener, ScaleButtonListener {


    public static int padding = 6;
    int paddingOffset = (2 * padding);
    private Ani widthAni;
    private Ani heightAni;
    private int animationWidth = 0;
    private int animationHeight = 0;
    private int initialX = 0;
    private int initialY = 0;
    private DeleteButton deleteButton;
    private ScaleButton scaleButton;
    private boolean isDrawingOutline;
    private int selectedOutline = ZoneHelper.getInstance().blueOutline;
    private int unselectedOutline = ZoneHelper.getInstance().greyOutline;
    private DeleteButtonListener deleteButtonListener;

    public PictureZone(PImage image, String uuid, int x, int y, int width, int height) {
        super(uuid, image, x, y, width, height);

        this.initialX = x;
        this.initialY = y;
        this.setAnimationHeight(height);
        this.setAnimationWidth(width);


//        Ani.init(this.applet);
    }

    public PictureZone(PosterItem posterItem) {
        super(posterItem.getUuid(), ImageLoader.toPImage(posterItem.getImageBytes()), posterItem.getX(),
              posterItem.getY(), posterItem.getWidth(),
              posterItem.getHeight());
        this.setAnimationHeight(posterItem.getHeight());
        this.setAnimationWidth(posterItem.getWidth());
        this.initialX = posterItem.getX();
        this.initialY = posterItem.getY();
        // Ani.init() must be called always first!
        // Ani.init(this.applet);
    }

    @Override
    public boolean add(Zone zone) {

        if (zone instanceof DeleteButton) {
            deleteButton = (DeleteButton) zone;
        } else if (zone instanceof ScaleButton) {
            scaleButton = (ScaleButton) zone;
        }

        return super.add(zone);
    }

    public void addDeleteListener(DeleteButtonListener deleteButtonListener) {
        deleteButtonListener = deleteButtonListener;
    }

    @Override
    public void deleteZone(Zone zone) {
        PosterDataModel.helper().removePosterItem(this.getName());
    }

    @Override
    public void scaleZone(Zone zone) {

    }

    @Override
    public String toString() {

        return Objects.toStringHelper(this)
                      .omitNullValues()
                      .add("UUID", getName())
                      .add("x", getX())
                      .add("y", getY())
                      .add("initialX", initialX)
                      .add("initialY", initialY)
                      .add("height", getHeight())
                      .add("width", getWidth())
                      .add("animationHeight", getAnimationHeight())
                      .add("animationWidth", getAnimationWidth())
                      .toString();

    }

    @Override
    public void drawImpl() {
        fill(255, 255, 255);
        if (isDrawingOutline) {
            stroke(selectedOutline);
            strokeWeight(2);
            smooth();
            rect(0, 0, this.getWidth(), this.getHeight(), 5);
        } else {
            stroke(unselectedOutline);
            strokeWeight(2);
            smooth();
            rect(0, 0, this.getWidth(), this.getHeight(), 5);
        }


        //fill(255,255,255);
        //rect(padding, padding, this.getWidth()-paddingOffset, this.getHeight()-paddingOffset);
        image(this.getZoneImage(), padding, padding, this.getWidth() - paddingOffset, this.getHeight() - paddingOffset);

    }

    @Override
    public void touch() {
        super.touch();
        SMT.putZoneOnTop(this);
    }

    @Override
    public void touchDown(Touch touch) {
        this.isDrawingOutline = true;
        deleteButton.setVisible(false);
        deleteButton.setDrawingOutline(isDrawingOutline);
        scaleButton.setVisible(false);
        scaleButton.setDrawingOutline(isDrawingOutline);
    }

    @Override
    public void touchUp(Touch touch) {
        this.isDrawingOutline = false;
        deleteButton.setVisible(true);
        deleteButton.setDrawingOutline(isDrawingOutline);
        scaleButton.setVisible(true);
        scaleButton.setDrawingOutline(isDrawingOutline);
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

    public void startAnimation(boolean isOUT) {
        System.out.println("STARTED");
    }

    public void itsStarted() {
    }


}
