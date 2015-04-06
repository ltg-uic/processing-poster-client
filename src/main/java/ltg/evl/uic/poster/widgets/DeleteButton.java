package ltg.evl.uic.poster.widgets;

import processing.core.PImage;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class DeleteButton extends PictureZone implements DeleteButtonListener {

    public static String DELETE_NAME = "deleteButton";
    private DeleteButtonListener deleteButtonListener;
    private boolean isDrawingOutline;
    private int redColor = ZoneHelper.getInstance().redOutline;
    private int whiteOutline = ZoneHelper.getInstance().whiteOutline;
    private int outlineColor = redColor;


    public DeleteButton(PImage image, String UUID, int x, int y, int width, int height) {
        super(image, UUID, x, y, width, height);
    }

    public void addDeleteListener(DeleteButtonListener deleteButtonListener) {
        this.deleteButtonListener = deleteButtonListener;
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

//    @Override
//    public void press(Touch touch) {
//        outlineColor = redColor;
//        super.press(touch);
//    }

    @Override
    public void touchDown(Touch touch) {
        outlineColor = whiteOutline;
        System.out.println("Delete Touched Down");

    }

    @Override
    public void touchUp(Touch touch) {
        outlineColor = redColor;
        System.out.println("Delete Touched up");
        deleteButtonListener.deleteZone(this);
    }

    @Override
    public void drawImpl() {
        if (isDrawingOutline) {
            outlineColor = whiteOutline;
        } else {
            outlineColor = redColor;
        }
        smooth();
        fill(outlineColor);
        ellipse(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() - 3, this.getHeight() - 3);
        image(this.getZoneImage(), 0, 0, this.getWidth(), this.getHeight());
    }


    @Override
    public void deleteZone(Zone zone) {
        deleteButtonListener.deleteZone(zone);
    }

    public void setDrawingOutline(boolean isDrawingOutline) {
        this.isDrawingOutline = isDrawingOutline;

    }
}
