package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.util.ModelHelper;

import java.io.IOException;
import java.util.Map;

public class PosterItem extends GenericJson {

    public static final String IMAGE = "img";
    public static final String TEXT = "txt";
    public static final String VIDEO = "vid";
    @Key
    private Map _id;
    @Key
    private String uuid;
    @Key
    private int x;
    @Key
    private int y;
    @Key
    private int width;
    @Key
    private int height;
    @Key
    private String rotation;
    @Key
    private String scale;
    @Key
    private String name;
    @Key
    private String type;
    @Key
    private String imageBytes;
    @Key
    private String color;
    @Key
    private String originator;
    @Key
    private String content;
    @Key
    private long lastEdited;
    @Key
    private double xn;
    @Key
    private double yn;
    @Key
    private double wn;
    @Key
    private double hn;

    @Key
    private String cited_from_poster_item_uuid;
    @Key
    private String cited_from_poster_uuid;
    @Key
    private String cited_from_user_uuid;

    public PosterItem() {
        this.setXn(-1.0);
        this.setWn(-1.0);
        this.setYn(-1.0);
        this.setHn(-1.0);
    }

    public PosterItem(String uuid, int x, int y, int width, int height, String name, String type, String imageBytes,
                      String color, int rotation, String content) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.type = type;
        this.imageBytes = imageBytes;
        this.content = content;
        this.lastEdited = ModelHelper.getTimestampMilli();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getThisX() {
        return this.x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {

        this.width = width;
    }

    public int getHeight() {

        return height;
    }

    public void setHeight(int height) {

        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(String imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Map get_id() {
        return _id;
    }

    public void set_id(Map _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        try {
            return this.toPrettyString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PosterItem)) {
            return false;
        }

        if (Optional.fromNullable(o).isPresent()) {
            PosterItem posterItem = (PosterItem) o;

            if (this.uuid != null && posterItem.getUuid() != null) {
                return this.uuid.equals(posterItem.getUuid());
            }
        }

        return false;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(long lastEdited) {
        this.lastEdited = lastEdited;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public double getXn() {
        return xn;
    }

    public void setXn(double xn) {
        this.xn = xn;
    }

    public double getYn() {
        return yn;
    }

    public void setYn(double yn) {
        this.yn = yn;
    }

    public double getWn() {
        return wn;
    }

    public void setWn(double wn) {
        this.wn = wn;
    }

    public double getHn() {
        return hn;
    }

    public void setHn(double hn) {
        this.hn = hn;
    }

    public String getCited_from_poster_item_uuid() {
        return cited_from_poster_item_uuid;
    }

    public void setCited_from_poster_item_uuid(String cited_from_poster_item_uuid) {
        this.cited_from_poster_item_uuid = cited_from_poster_item_uuid;
    }

    public String getCited_from_poster_uuid() {
        return cited_from_poster_uuid;
    }

    public void setCited_from_poster_uuid(String cited_from_poster_uuid) {
        this.cited_from_poster_uuid = cited_from_poster_uuid;
    }

    public String getCited_from_user_uuid() {
        return cited_from_user_uuid;
    }

    public void setCited_from_user_uuid(String cited_from_user_uuid) {
        this.cited_from_user_uuid = cited_from_user_uuid;
    }
}
