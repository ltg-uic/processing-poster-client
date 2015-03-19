package ltg.evl.uic.poster.json.mongo;

import com.fasterxml.jackson.jr.ob.JSON;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Poster extends GenericJson {

    @Key
    private String id;
    @Key
    private int height;
    @Key
    private int width;
    @Key
    private String name;
    @Key
    private String color;

//    private List<PosterItem> posterItems = new ArrayList<>();

    @Key
    private List<String> posterItems = new ArrayList<>();

    public Poster() {
    }

    public Poster(String id, int height, int width, String name) {
        this.height = height;
        this.width = width;
        this.name = name;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

//    public List<PosterItem> getPosterItems() {
//        return posterItems;
//    }
//
//    public void setPosterItems(List<PosterItem> posterItems) {
//        this.posterItems = posterItems;
//    }
//
//    public void addPosterItems(PosterItem posterItem) {
//        posterItems.add(posterItem);
//
//    }

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
}
