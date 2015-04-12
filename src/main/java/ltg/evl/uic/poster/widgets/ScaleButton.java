package ltg.evl.uic.poster.widgets;

import processing.core.PImage;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class ScaleButton extends PictureZone implements ScaleButtonListener {

    public static String SCALE_NAME = "scaleButton";
    private ScaleButtonListener scaleButtonListener;
    private boolean isDrawingOutline;
    private int redColor = ZoneHelper.redOutline;
    private int whiteOutline = ZoneHelper.whiteOutline;
    private int greyOutline = ZoneHelper.greyOutline;

    private int outlineColor = whiteOutline;

    public ScaleButton(PImage image, String UUID, int x, int y, int width, int height) {
        super(image, UUID, x, y, width, height, "img", "scaleZone");
    }

    public void addScaleListener(ScaleButtonListener scaleButtonListener) {
        this.scaleButtonListener = scaleButtonListener;
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    @Override
    public void scaleZone(Zone zone) {
        scaleButtonListener.scaleZone(zone);
    }

    public void setDrawingOutline(boolean isDrawingOutline) {
        this.isDrawingOutline = isDrawingOutline;
    }

    @Override
    public void drawImpl() {
//        if (isDrawingOutline) {
//            outlineColor = whiteOutline;
//        } else {
//            outlineColor = whiteOutline;
//        }
        smooth();
        fill(outlineColor);
        stroke(255);
        rect(0, 0, this.getWidth() - 3, this.getHeight() - 3);
        image(this.getZoneImage(), 0, 0, this.getWidth(), this.getHeight());
    }


    @Override
    public void touchDown(Touch touch) {
        outlineColor = greyOutline;
        System.out.println("SCALE Touched Down");

    }

    @Override
    public void touchUp(Touch touch) {
        outlineColor = whiteOutline;
        System.out.println("SCALE Touched up");
        //scaleButtonListener.deleteZone(this);
    }
}
