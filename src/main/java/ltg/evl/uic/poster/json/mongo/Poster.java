package ltg.evl.uic.poster.json.mongo;

import com.fasterxml.jackson.jr.ob.JSON;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Poster extends GenericJson {

    @Key("_id")
    private ObjectId id;
    @Key
    private int height;
    @Key
    private int width;
    @Key
    private String name;
    @Key
    private String color;
    @Key
    private String uuid;

    @Key
    private List<String> posterItems = new ArrayList<>();

    public Poster() {
    }

    public Poster(String uuid, int height, int width, String name) {
        this.height = height;
        this.width = width;
        this.name = name;
        this.uuid = uuid;
    }

    public static Poster toObj(String json) {
        try {
            Poster bean = JSON.std.beanFrom(Poster.class, json);
            return bean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public String toString() {
        return "poster";
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public Poster setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public Poster setWidth(int width) {
        this.width = width;
        return this;
    }

    public String getName() {
        return name;
    }

    public Poster setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getPosterItems() {
        return posterItems;
    }

    public void setPosterItems(List<String> posterItems) {
        this.posterItems = posterItems;
    }

    public void addPosterItems(String posterItem) {
        posterItems.add(posterItem);

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
