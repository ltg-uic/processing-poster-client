package ltg.evl.uic.poster.json.mongo;

import com.fasterxml.jackson.jr.ob.JSON;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.base.Objects;
import org.bson.types.ObjectId;

import java.io.IOException;

public class PosterItem extends GenericJson {


    public static String IMAGE = "img";
    public static String TEXT = "txt";
    @Key("_id")
    private ObjectId id;
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



    public PosterItem() {
    }

    public PosterItem(String uuid, int x, int y, int width, int height, String name, String type, String imageBytes,
                      String color, int rotation) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.type = type;
        this.imageBytes = imageBytes;
    }

    public static PosterItem toObj(String json) {
        try {
            PosterItem bean = JSON.std.beanFrom(PosterItem.class, json);
            return bean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

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

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String toString() {

        return Objects.toStringHelper(this)
                      .omitNullValues()
                      .add("id", id)
                      .add("uuid", uuid)
                      .add("x", getX())
                      .add("y", getY())
                      .add("rotation", getRotation())
                      .add("name", getName())
                      .add("type", getType())
                      .add("height", getHeight())
                      .add("width", getWidth())
                      .add("Color", getColor())
                      .add("content", getContent())
                      .toString();

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

    public String toJSON() {

        String json = null;
        try {
            json = JSON.std.asString(this);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
