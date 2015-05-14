package ltg.evl.uic.poster.widgets;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.widgets.button.DeleteButton;
import ltg.evl.uic.poster.widgets.button.DeleteButtonListener;
import processing.core.PImage;
import vialab.SMT.ImageZone;
import vialab.SMT.SMT;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class PictureZone extends ImageZone implements DeleteButtonListener {


    public static int padding = 6;

    public static int paddingOffset = (2 * padding);
    private boolean isEditing;
    private DeleteButton deleteButton;
    private boolean isDrawingOutline;
    private int selectedOutline = ZoneHelper.blueOutline;
    private int unselectedOutline = ZoneHelper.greyOutline;
    private DeleteButtonListener deleteButtonListener;
    private String type;
    private boolean isAnimating;
    private boolean isDeleteMode;
    private String zoneRotation;
    private String zoneScale;
    private int initX;
    private int initY;
    private int initWidth;
    private int initHeight;

    public PictureZone(PImage image, String uuid, int x, int y, int width, int height) {
        super(uuid, image, x, y, width, height);
        this.isEditing = true;
        this.isAnimating = false;
        this.isDeleteMode = false;
        this.initX = x;
        this.initY = y;
        this.initHeight = height;
        this.initWidth = width;
    }

    public PictureZone(PictureZone pictureZone) {
        this(pictureZone.getZoneImage(), pictureZone.getName(), pictureZone.getX(), pictureZone.getY(),
             pictureZone.getWidth(), pictureZone.getHeight());
    }


    public void resetToInitPos() {
        this.setX(this.initX);
        this.setY(this.initY);
        this.setWidth(this.initWidth);
        this.setHeight(this.initHeight);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        System.out.println("height: " + height);
        System.out.println("image height: " + getZoneImage().height);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        System.out.println("width: " + width);
        System.out.println("image width: " + getZoneImage().width);
    }


    @Override
    public void scale() {
        super.scale();
    }

    @Override
    protected void scaleImpl(float sx, float sy, float sz) {
        super.scaleImpl(sx, sy, sz);
        System.out.println("Scale " + " " + sx + " " + sy + " " + sz);
    }

    @Override
    public void scale(float x, float y) {
        super.scale(x, y);
        System.out.println("Scale " + x + " y" + y);
    }

    @Override
    public void scale(float s) {
        super.scale(s);
        System.out.println("scale " + s);
    }


    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        System.out.println("width: " + width + " " + "height: " + height);
    }

    @Override
    public void scale(float x, float y, float z) {
        // System.out.println("x: " + x + "y: " + y + "z: " + z);
        super.scale(x, y, z);
    }



    public void setIsDeleteMode(boolean isDeleteMode) {
        this.isDeleteMode = isDeleteMode;
        if (isDeleteMode) {


//            double buttonSize = SMT.getApplet().getHeight() * .03;
//            double adjustedButtonSize = (buttonSize / 2.0);
//            int x = (int) (this.getWidth() - adjustedButtonSize) - 2;
//            int y = (int) (2 - adjustedButtonSize);
//
////        System.out.println(
////                "delete button: " + buttonSize + " adjust button size: " + adjustedButtonSize + " x: " + x + " y: " + y);
//
//            deleteButton = new DeleteButtonBuilder().setUUID(DeleteButton.DELETE_NAME)
//                                                    .setImage(ZoneHelper.deleteImage)
//                                                    .setX(x)
//                                                    .setY(y)
//                                                    .setWidth((int) buttonSize)
//                                                    .setHeight((int) buttonSize).createDeleteButton();
//            deleteButton.addDeleteListener(this);
//            deleteButton.rotate(this.getRotationAngle());
//            this.add(deleteButton);


        } else {

            if (Optional.fromNullable(deleteButton).isPresent()) {
                this.remove(deleteButton);
            }
        }


    }

    @Override
    public void deleteZone(Zone zone) {
        PosterDataModel.helper().removePosterItem(this.getName());
    }


    @Override
    public void drawImpl() {
        super.drawImpl();
//        if (isEditing) {
//            fill(255, 255, 255);
//            if (isDrawingOutline) {
//                stroke(selectedOutline);
//                strokeWeight(2);
//                smooth();
//                rect(0, 0, this.getWidth(), this.getHeight(), 5);
//            } else {
//                stroke(unselectedOutline);
//                strokeWeight(2);
//                smooth();
//                rect(0, 0, this.getWidth(), this.getHeight(), 5);
//            }
//
//
//            if (this.getZoneImage() != null) {
////                System.out.println(this.getRotationAngle()*180/PI);
//////                System.out.println(
//////                        "PictureZone.drawImpl w:" + getWidth() + " h: " + getHeight() + " scale: " + getZoneScale() + " rotation: " +);
//                image(this.getZoneImage(), padding, padding, this.getWidth() - paddingOffset,
//                      this.getHeight() - paddingOffset);
//            } else {
//                //TODO put text problem with img server.
//                fill(255, 255, 255);
//                rect(padding, padding, this.getWidth() - paddingOffset, this.getHeight() - paddingOffset);
//            }
//            //fill(255,255,255);
//            //rect(padding, padding, this.getWidth()-paddingOffset, this.getHeight()-paddingOffset);
//
//
//        } else {
//            image(this.getZoneImage(), 0, 0, this.getWidth(),
//                  this.getHeight());
//        }
    }

    @Override
    public void touch() {
        super.touch();

        printThisShit();




        if (isEditing) {
            SMT.putZoneOnTop(this);
            rst(false, true, true);
        } else if (isDeleteMode) {
            rst(false, false, false);
        }
    }

    public void printThisShit() {
        System.out.println("width: " + getWidth());
        System.out.println("height: " + getHeight());
        System.out.println("get screen size: " + getScreenSize());
        System.out.println("get size: " + getSize());
        System.out.println("get rotationradius: " + getRntRadius());
        System.out.println("NAME: " + getName());
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
            presentationZone.fade(1f, 0f, 255, false);
            presentationZone.presentImageZone(this.getZoneImage());
        }
    }

    @Override
    public void touchUp(Touch touch) {
        if (isEditing) {
            this.isDrawingOutline = false;
        }
    }


    @Override
    public Zone clone() {
        return super.clone();
    }

    //region getset

    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
        if (!isEditing) {
            this.setIsDeleteMode(false);
        }
    }

    public boolean isDeleteMode() {
        return isDeleteMode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public String getZoneRotation() {
        return zoneRotation;
    }

    public void setZoneRotation(String zoneRotation) {
        this.zoneRotation = zoneRotation;
    }

    public String getZoneScale() {
        return zoneScale;
    }

    public void setZoneScale(String zoneScale) {
        this.zoneScale = zoneScale;
    }

    public void applyScaleRotation() {
        super.rotate(new Float(getZoneRotation()) * 180 / PI);
    }


    //endregion getset

}
