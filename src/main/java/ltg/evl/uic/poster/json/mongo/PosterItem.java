package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import ltg.evl.util.ModelHelper;

import java.io.IOException;
import java.util.Map;

public class PosterItem extends GenericJson {

    public static final String IMAGE = "img";
    public static final String TEXT = "txt";
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
    private int rotation;
    @Key
    private String name;
    @Key
    private String type;
    @Key
    private String imageBytes;
    @Key
    private String color;
    @Key
    private String content;
    @Key
    private long lastEdited;

    public PosterItem() {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
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
}
