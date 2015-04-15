package ltg.evl.uic.poster.widgets;

import processing.core.PImage;
import vialab.SMT.ImageZone;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class DeleteButton extends ImageZone implements DeleteButtonListener {

    public static String DELETE_NAME = "deleteButton";
    private DeleteButtonListener deleteButtonListener;
    private boolean isDrawingOutline;
    private int redColor = ZoneHelper.redOutline;
    private int whiteOutline = ZoneHelper.whiteOutline;
    private int outlineColor = redColor;


    public DeleteButton(PImage image, String uuid, int x, int y, int width, int height) {
        super(uuid, image, x, y, width, height);
    }

    public void addDeleteListener(DeleteButtonListener deleteButtonListener) {
        this.deleteButtonListener = deleteButtonListener;
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    @Override
    public void touchDown(Touch touch) {
        outlineColor = whiteOutline;

    }

    @Override
    public void touchUp(Touch touch) {
        outlineColor = redColor;
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
