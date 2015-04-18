package ltg.evl.uic.poster.widgets;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.widgets.buttons.DeleteButton;
import ltg.evl.uic.poster.widgets.buttons.DeleteButtonBuilder;
import ltg.evl.uic.poster.widgets.buttons.DeleteButtonListener;
import ltg.evl.uic.poster.widgets.buttons.ScaleButtonListener;
import ltg.evl.util.ImageLoader;
import ltg.evl.util.de.looksgood.ani.Ani;
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
    private boolean isDrawingOutline;
    private int selectedOutline = ZoneHelper.blueOutline;
    private int unselectedOutline = ZoneHelper.greyOutline;
    private DeleteButtonListener deleteButtonListener;
    private String type;
    private boolean isAnimating;
    private boolean isDeleteMode;

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
        this.isEditing = true;
        this.isAnimating = false;
        this.isDeleteMode = false;
    }

    public PictureZone(PosterItem posterItem) {
        super(posterItem.getUuid(), ImageLoader.toPImage(posterItem.getImageBytes()), posterItem.getX(),
              posterItem.getY(), posterItem.getWidth(),
              posterItem.getHeight());
        this.initialX = posterItem.getX();
        this.initialY = SMT.getApplet().getHeight() + offScreenPadding;
        this.type = posterItem.getType();
        this.zoneName = posterItem.getName();
        this.isEditing = true;
        this.isAnimating = false;
        this.isDeleteMode = false;
    }



    public void startAni(float speed, float delay) {
        isAnimating = true;
        this.setIsEditing(true);
        Ani diameterAni = new Ani(this, speed, delay, "initialY", target.y, Ani.EXPO_IN_OUT, "onEnd:done");
        diameterAni.start();
    }


    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

    public boolean isDeleteMode() {
        return isDeleteMode;
    }

    public void setIsDeleteMode(boolean isDeleteMode) {
        this.isDeleteMode = isDeleteMode;
        if (isDeleteMode) {


            double buttonSize = SMT.getApplet().getHeight() * .03;
            double adjustedButtonSize = (buttonSize / 2.0);
            int x = (int) (this.getWidth() - adjustedButtonSize) - 2;
            int y = (int) (2 - adjustedButtonSize);

//        System.out.println(
//                "delete button: " + buttonSize + " adjust button size: " + adjustedButtonSize + " x: " + x + " y: " + y);

            deleteButton = new DeleteButtonBuilder().setName(DeleteButton.DELETE_NAME)
                                                    .setImage(ZoneHelper.deleteImage)
                                                    .setX(x)
                                                    .setY(y)
                                                    .setWidth((int) buttonSize)
                                                    .setHeight((int) buttonSize).createDeleteButton();
            deleteButton.addDeleteListener(this);
            deleteButton.rotate(this.getRotationAngle());
            this.add(deleteButton);


        } else {

            if (Optional.fromNullable(deleteButton).isPresent()) {
                this.remove(deleteButton);
            }
        }


    }

    public void done() {
        // this.setIsEditing(true);
        isAnimating = false;
        setY(initialY);
        System.out.println("PictureZone.done " + getName());
    }

    @Override
    public void deleteZone(Zone zone) {
        PosterDataModel.helper().removePosterItem(this.getName());
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

    @Override
    public void touch() {
        if (isEditing) {
            super.touch();
            SMT.putZoneOnTop(this);
            rst(true, true, true);
            super.touch();
        } else if (isDeleteMode) {
            rst(false, false, false);
        }
    }

    @Override
    public void touchDown(Touch touch) {
        super.touchDown(touch);

        System.out.println("PictureZone.touchDown name: " + getName());

        if (isEditing) {
            this.isDrawingOutline = true;
        } else {
            PresentationZone presentationZone = new PresentationZone(this.getName() + "-P");
            presentationZone.setBgAlpha(0);
            //
            SMT.add(presentationZone);
            presentationZone.fade(1f, 0f, 200, false);
            presentationZone.presentImageZone(this.getZoneImage());
        }
    }

    @Override
    public void touchUp(Touch touch) {
        if (isEditing) {
            this.isDrawingOutline = false;
        }
    }

    public void addDeleteListener(DeleteButtonListener deleteButtonListener) {
        deleteButtonListener = deleteButtonListener;
    }


    @Override
    public Zone clone() {
        Zone z = new PictureZoneBuilder().setZoneName(this.zoneName)
                                         .setType(this.getType())
                                         .setY(this.getY())
                                         .setX(this.getX())
                                         .setHeight(this.getHeight())
                                         .setWidth(this.getWidth())
                                         .setUuid(this.getName() + "-p")
                                         .createPictureZone();
        return z;
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

    @Override
    public void scaleZone(Zone zone) {

    }


    //endregion

}
