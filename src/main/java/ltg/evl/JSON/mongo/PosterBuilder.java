package ltg.evl.json.mongo;

public class PosterBuilder {
    private int height;
    private int width;
    private String name;

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

    public Poster createPoster() {
        return new Poster(height, width, name);
    }
}