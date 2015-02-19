package ltg.evl.uic.poster;

import processing.core.PImage;

public class PictureZoneBuilder {
    private PImage image;
    private String uuid;
    private int x;
    private int y;
    private int width;
    private int height;

    public PictureZoneBuilder setImage(PImage image) {
        this.image = image;
        return this;
    }

    public PictureZoneBuilder setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public PictureZoneBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public PictureZoneBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public PictureZoneBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public PictureZoneBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public PictureZone createPictureZone() {
        return new PictureZone(image, uuid, x, y, width, height);
    }
}