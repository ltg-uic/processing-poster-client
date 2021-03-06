package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.json.mongo.PosterItem;

public class TextBoxZoneBuilder {
    private String uuid;
    private String textString;
    private int x;
    private int y;
    private int width;
    private int height;
    private PosterItem posterItem;

    public TextBoxZoneBuilder setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public TextBoxZoneBuilder setTextString(String textString) {
        this.textString = textString;
        return this;
    }

    public TextBoxZoneBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public TextBoxZoneBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public TextBoxZoneBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public TextBoxZoneBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public TextBoxZoneBuilder setPosterItem(PosterItem posterItem) {
        this.posterItem = posterItem;
        return this;
    }

    public TextBoxZone createTextBoxZone() {
        return new TextBoxZone(uuid, textString, x, y, width, height);
    }

    public TextBoxZone createTextZoneWithPosterItem() {
        return new TextBoxZone(posterItem);
    }
}