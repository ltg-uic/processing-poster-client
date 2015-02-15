package ltg.evl.uic.poster;

import processing.core.PImage;

public class PictureZoneBuilder {
    private PImage image;
    private String uuid;

    public PictureZoneBuilder setImage(PImage image) {
        this.image = image;
        return this;
    }

    public PictureZone createPictureZone() {
        return new PictureZone(image);
    }

    public PictureZoneBuilder setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }
}