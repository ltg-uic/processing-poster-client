package ltg.evl.uic.poster;

import processing.core.PImage;
import vialab.SMT.ImageZone;

/**
 * Created by aperritano on 2/13/15.
 */
public class PictureZone extends ImageZone {

    private String UUID;

    public PictureZone(PImage image, String UUID, int x, int y, int width, int height) {
        super(image, x, y, width, height);
        this.UUID = UUID;
    }

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
}
