package ltg.evl.uic.poster.json.mongo;

public class PosterItemBuilder {

    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    private String type;
    private String imageBytes;
    private String uuid;
    private String color;
    private int rotation;


    public PosterItemBuilder setColor(String color) {
        this.color = color;
        return this;
    }

    public PosterItemBuilder setRotation(int rotation) {
        this.rotation = rotation;
        return this;
    }

    public PosterItemBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public PosterItemBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public PosterItemBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public PosterItemBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public PosterItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PosterItemBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public PosterItemBuilder setImageBytes(String imageBytes) {
        this.imageBytes = imageBytes;
        return this;
    }

    public PosterItemBuilder setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
    public PosterItem createPosterItem() {
        return new PosterItem(uuid, x, y, width, height, name, type, imageBytes, color, rotation);
    }
}