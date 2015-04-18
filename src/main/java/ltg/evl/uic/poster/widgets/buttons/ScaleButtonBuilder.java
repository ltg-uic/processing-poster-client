package ltg.evl.uic.poster.widgets.buttons;

import processing.core.PImage;

public class ScaleButtonBuilder {
    private PImage image;
    private String uuid;
    private int x;
    private int y;
    private int width;
    private int height;

    public ScaleButtonBuilder setImage(PImage image) {
        this.image = image;
        return this;
    }

    public ScaleButtonBuilder setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public ScaleButtonBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public ScaleButtonBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public ScaleButtonBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public ScaleButtonBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public ScaleButton createScaleButton() {
        return new ScaleButton(image, uuid, x, y, width, height);
    }
}