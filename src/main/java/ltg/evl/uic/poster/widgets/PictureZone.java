package ltg.evl.uic.poster.widgets;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import de.looksgood.ani.Ani;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.util.ImageLoader;
import processing.core.PImage;
import processing.core.PVector;
import vialab.SMT.ImageZone;
import vialab.SMT.SMT;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class PictureZone extends ImageZone implements DeleteButtonListener, ScaleButtonListener {


    public static int padding = 6;
    public PVector point = new PVector(0, 0);
    public PVector target = new PVector(0, 0);
    int paddingOffset = (2 * padding);
    int offScreenPadding = 10;
    private boolean isEditing;
    private String zoneName;
    private int initialX = 0;
    private int initialY = 0;
    private DeleteButton deleteButton;
    private ScaleButton scaleButton;
    private boolean isDrawingOutline;
    private int selectedOutline = ZoneHelper.blueOutline;
    private int unselectedOutline = ZoneHelper.greyOutline;
    private DeleteButtonListener deleteButtonListener;
    private String type;
    private boolean isAnimating;

    public PictureZone(PImage image, String uuid, int x, int y, int width, int height, String type, String zoneName) {
        super(uuid, image, x, SMT.getApplet().getHeight(), width, height);

        this.initialX = x;
        this.initialY = SMT.getApplet().getHeight() + offScreenPadding;
        this.target.y = y;
        this.setX(x);
        this.setY(SMT.getApplet().getHeight() + offScreenPadding);
        this.setHeight(height);
        this.setWidth(width);
        this.type = type;
        this.zoneName = zoneName;
        this.isEditing = false;
        this.isAnimating = false;

    }

    public PictureZone(PosterItem posterItem) {
        super(posterItem.getUuid(), ImageLoader.toPImage(posterItem.getImageBytes()), posterItem.getX(),
              posterItem.getY(), posterItem.getWidth(),
              posterItem.getHeight());
        this.initialX = posterItem.getX();
        this.initialY = SMT.getApplet().getHeight() + offScreenPadding;
        this.type = posterItem.getType();
        this.zoneName = posterItem.getName();
        this.isEditing = false;
        this.isAnimating = false;
    }

    @Override
    public boolean add(Zone zone) {
        if (Optional.fromNullable(zone).isPresent()) {
            if (zone instanceof DeleteButton) {
                deleteButton = (DeleteButton) zone;
            } else if (zone instanceof ScaleButton) {
                scaleButton = (ScaleButton) zone;
            }
        }
        return super.add(zone);
    }


    public void startAni(float speed, float delay) {
        isAnimating = true;
        this.setIsEditing(true);
        Ani diameterAni = new Ani(this, speed, 0.5f, "initialY", target.y, Ani.EXPO_IN_OUT, "onEnd:done");
        diameterAni.start();
    }

    public void done() {
        // this.setIsEditing(true);
        isAnimating = false;
        setY(initialY);
        System.out.println("we are done");

        if (Optional.fromNullable(deleteButton).isPresent()) {
            if (deleteButton.getVisible() == false) {
                deleteButton.setVisible(true);
            }
        }

    }

    @Override
    public void deleteZone(Zone zone) {
        PosterDataModel.helper().removePosterItem(this.getName());
    }

    @Override
    public void scaleZone(Zone zone) {

    }



    @Override
    public void drawImpl() {
        if (isAnimating)
            setY(initialY);

        if (isEditing) {
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
            image(this.getZoneImage(), padding, padding, this.getWidth() - paddingOffset,
                  this.getHeight() - paddingOffset);
        } else {
            image(this.getZoneImage(), 0, 0, this.getWidth(),
                  this.getHeight());
        }
    }

    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
        if (Optional.fromNullable(deleteButton).isPresent())
            deleteButton.setVisible(isEditing);
    }

    @Override
    public void touch() {
        if (isEditing) {
            super.touch();
            SMT.putZoneOnTop(this);
            rst(true, true, true);
        } else {
            rst(false, false, false);
        }
    }

    @Override
    public void touchDown(Touch touch) {
        if (isEditing) {
            this.isDrawingOutline = true;

            if (Optional.fromNullable(deleteButton).isPresent()) {
                deleteButton.setVisible(false);
                deleteButton.setDrawingOutline(isDrawingOutline);
            }
//        scaleButton.setVisible(false);
//        scaleButton.setDrawingOutline(isDrawingOutline);
        } else {
            this.scale(1.5f);
        }
    }

    @Override
    public void touchUp(Touch touch) {

        if (isEditing) {
            this.isDrawingOutline = false;

            if (Optional.fromNullable(deleteButton).isPresent()) {
                deleteButton.setVisible(true);
                deleteButton.setDrawingOutline(isDrawingOutline);
            } else if (Optional.fromNullable(scaleButton).isPresent()) {
                scaleButton.setVisible(true);
                scaleButton.setDrawingOutline(isDrawingOutline);
            }
        } else {

        }
    }

    public void addDeleteListener(DeleteButtonListener deleteButtonListener) {
        deleteButtonListener = deleteButtonListener;
    }


    public PictureZone getPresentationZone() {
        return new PictureZoneBuilder().setZoneName(this.zoneName)
                                       .setType(this.getType())
                                       .setY(this.getY())
                                       .setX(this.getX())
                                       .setHeight(this.getHeight())
                                       .setWidth(this.getWidth())
                                       .setUuid(this.getName() + "-p")
                                       .createPictureZone();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public boolean isEditing() {
        return isEditing;
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
                      .toString();

    }

    public void scaleToFit(int screenWidth, int screenHeight) {


        float zoneImageWidth = getZoneImage().width;
        float zoneImageHeight = getZoneImage().height;

        float widthRatio = (zoneImageWidth / new Float(screenWidth).floatValue());

        float heightRatio = (zoneImageHeight / new Float(screenHeight).floatValue());

        if (widthRatio > heightRatio) {

            if (widthRatio > 1.0) {
                getZoneImage().resize(screenWidth - 15, 0);
            } else {
                float diff = 1.0f - widthRatio;
                float add = zoneImageWidth * diff;
                // getZoneImage().resize(zoneImageWidth+add,0);
            }

        }


//            getZoneImage().resize(0, height-10);
//            setAnimationWidth(getZoneImage().width);
//            setAnimationHeight(getZoneImage().height);
//
//        int x = (this.getWidth() / 2) - (width / 2);
//        int y = (this.getHeight() / 2) - (height / 2);
//
//        this.setX(x);
//        this.setY(y);
    }
    //endregion

}
