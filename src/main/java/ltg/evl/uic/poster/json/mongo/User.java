package ltg.evl.uic.poster.json.mongo;

import com.fasterxml.jackson.jr.ob.JSON;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class User extends GenericJson {
    @Key
    private String id;
    @Key
    private String name;

    //private List<Poster> posters = new ArrayList<>();

    @Key
    private List<String> posters = new ArrayList<>();


    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public static User toObj(String json) {
        try {
            User bean = JSON.std.beanFrom(User.class, json);
            return bean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static User[] toArray(String json) {
        try {
            User[] bean = JSON.std.arrayOfFrom(User.class, json);
            return bean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public List<Poster> getPosters() {
//        return posters;
//    }
//
//    public void setPosters(List<Poster> posters) {
//        this.posters = posters;
//    }
//
//    public void addPoster(Poster poster) {
//        posters.add(poster);
//    }
//
//    public void removePoster(Poster poster) {
//        posters.remove(poster);
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPosters() {
        return posters;
    }

    public void setPosters(List<String> posters) {
        this.posters = posters;
    }

    public void addPoster(String poster) {
        posters.add(poster);
    }

    public void removePoster(String poster) {
        posters.remove(poster);
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
