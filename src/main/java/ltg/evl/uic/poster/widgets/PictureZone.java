package ltg.evl.uic.poster.widgets;

import com.google.common.base.Objects;
import de.looksgood.ani.Ani;
import ltg.evl.uic.poster.json.mongo.PosterDataModelHelper;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.util.ImageLoader;
import processing.core.PImage;
import vialab.SMT.ImageZone;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class PictureZone extends ImageZone implements DeleteButtonListener {


    private Ani widthAni;
    private Ani heightAni;

    private int animationWidth = 0;
    private int animationHeight = 0;
    private int initialX = 0;
    private int initialY = 0;
    private DeleteButton deleteButton;
    private boolean isDrawingOutline;
    private int outline = ZoneHelper.getInstance().blueOutline;
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
        }

        return super.add(zone);
    }

    public void addListener(DeleteButtonListener deleteButtonListener) {
        this.deleteButtonListener = deleteButtonListener;
    }

    @Override
    public void deleteZone(Zone zone) {
        PosterDataModelHelper.getInstance().removePosterItem(this.getName());
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
        if (isDrawingOutline) {
            stroke(outline);
            strokeWeight(4);
            rect(0, 0, this.getWidth(), this.getHeight());
        }
        image(this.getZoneImage(), 0, 0, this.getWidth(), this.getHeight());

    }

    @Override
    public void touchDown(Touch touch) {
        this.isDrawingOutline = true;
        deleteButton.setDrawingOutline(isDrawingOutline);
    }

    @Override
    public void touchUp(Touch touch) {
        this.isDrawingOutline = false;
        deleteButton.setDrawingOutline(isDrawingOutline);
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
