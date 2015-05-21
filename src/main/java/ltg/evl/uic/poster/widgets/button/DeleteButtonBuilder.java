package ltg.evl.uic.poster.widgets.button;

import processing.core.PImage;

public class DeleteButtonBuilder {
    private PImage image;
    private String uuid;
    private int x;
    private int y;
    private int width;
    private int height;

    public DeleteButtonBuilder setImage(PImage image) {
        this.image = image;
        return this;
    }

    public DeleteButtonBuilder setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public DeleteButtonBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public DeleteButtonBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public DeleteButtonBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public DeleteButtonBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public DeleteButton createDeleteButton() {
        return new DeleteButton(image, uuid, x, y, width, height);
    }
}