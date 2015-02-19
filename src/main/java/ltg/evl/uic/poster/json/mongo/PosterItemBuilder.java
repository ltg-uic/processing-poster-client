package ltg.evl.uic.poster.json.mongo;

public class PosterItemBuilder {
    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    private String imageId;
    private String imageBytes;

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

    public PosterItemBuilder setImageId(String imageId) {
        this.imageId = imageId;
        return this;
    }

    public PosterItemBuilder setImageBytes(String imageBytes) {
        this.imageBytes = imageBytes;
        return this;
    }

    public PosterItem createPosterItem() {
        return new PosterItem(x, y, width, height, name, imageId, imageBytes);
    }
}