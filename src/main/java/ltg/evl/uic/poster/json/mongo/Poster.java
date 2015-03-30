package ltg.evl.uic.poster.json.mongo;

import com.fasterxml.jackson.jr.ob.JSON;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Poster extends GenericJson {

    @Key
    private Map _id;
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
        try {
            return toPrettyString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    @Override
//    public String toString() {
//
//        return Objects.toStringHelper(this)
//                      .omitNullValues()
//                      .add("_id", _id)
//                      .add("uuid", uuid)
//                      .add("name", getName())
//                      .add("height", getHeight())
//                      .add("width", getWidth())
//                      .add("Color", getColor())
//                      .toString();
//
//    }

    public Map get_id() {
        return _id;
    }

    public void set_id(Map _id) {
        this._id = _id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
