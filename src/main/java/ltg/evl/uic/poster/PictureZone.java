package ltg.evl.uic.poster;

import processing.core.PImage;
import vialab.SMT.ImageZone;

/**
 * Created by aperritano on 2/13/15.
 */
public class PictureZone extends ImageZone {

    private String uuid;

    public PictureZone(PImage image) {
        super(image);
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
}
