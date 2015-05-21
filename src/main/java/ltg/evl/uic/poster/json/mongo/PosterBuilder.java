package ltg.evl.uic.poster.json.mongo;

public class PosterBuilder {
    private int height;
    private int width;
    private String name;
    private String uuid;

    public PosterBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public PosterBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public PosterBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PosterBuilder setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Poster createPoster() {
        return new Poster(uuid, height, width, name);
    }
}