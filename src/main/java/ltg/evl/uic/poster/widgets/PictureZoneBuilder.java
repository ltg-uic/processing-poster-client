package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.json.mongo.PosterItem;
import processing.core.PImage;

public class PictureZoneBuilder {
    private PImage image;
    private String uuid;
    private int x;
    private int y;
    private int width;
    private int height;
    private PosterItem posterItem;
    private String type;
    private String zoneName;
    private String rotation;
    private String scale;

    public PictureZoneBuilder setImage(PImage image) {
        this.image = image;
        return this;
    }

    public PictureZoneBuilder setUuid(String uuid) {
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

    public PictureZoneBuilder setPosterItem(PosterItem posterItem) {
        this.posterItem = posterItem;
        return this;
    }

    public PictureZoneBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public PictureZoneBuilder setZoneName(String zoneName) {
        this.zoneName = zoneName;
        return this;
    }

    public PictureZoneBuilder setRotation(String rotation) {
        this.rotation = rotation;
        return this;
    }


    public PictureZone createPictureZone() {


        return new PictureZone(image, uuid, x, y, width, height);
    }

    public PictureZoneBuilder setScale(String scale) {
        this.scale = scale;
        return this;
    }
}