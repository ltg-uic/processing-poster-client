package ltg.evl.uic.poster.json.mongo;

import com.fasterxml.jackson.jr.ob.JSON;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class User extends GenericJson {

    @Key
    public Map _id;

    @Key
    public String uuid;

    @Key
    public String name;

    @Key
    public List<String> nameTags = new ArrayList<>();

    @Key
    public List<String> posters = new ArrayList<>();


    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, String uuid, List<String> nameTags) {
        this.name = name;
        this.uuid = uuid;
        this.nameTags = nameTags;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map get_id() {
        return _id;
    }

    public void set_id(Map _id) {
        this._id = _id;
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


    @Override
    public String toString() {
        try {
            return toPrettyString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public List<String> getNameTags() {
        return nameTags;
    }

    public void setNameTags(List<String> nameTags) {
        this.nameTags = nameTags;
    }
}
