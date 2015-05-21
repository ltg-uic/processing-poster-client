package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.util.ModelHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class User extends GenericJson {

    @Key
    private Map _id;

    @Key
    private String uuid;

    @Key
    private String name;

    @Key
    private List<String> nameTags = new ArrayList<>();

    @Key
    private List<String> posters = new ArrayList<>();

    @Key
    private String classname;

    @Key
    private int color;

    @Key
    private long lastEdited;

    public User() {

    }

    public User(String name, String uuid, String classname, List<String> nameTags, int color, List<String> posters) {
        this.name = name;
        this.uuid = uuid;
        this.classname = classname;
        this.nameTags = nameTags;
        this.color = color;
        this.posters = posters;
        this.lastEdited = ModelHelper.getTimestampMilli();
    }

    public User(String name) {
        this.name = name;
        this.lastEdited = ModelHelper.getTimestampMilli();
    }

    public User(String name, String uuid, List<String> nameTags) {
        this.name = name;
        this.uuid = uuid;
        this.nameTags = nameTags;
        this.lastEdited = ModelHelper.getTimestampMilli();
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }

        if (Optional.fromNullable(o).isPresent()) {
            User user = (User) o;

            if (this.uuid != null && user.getUuid() != null) {
                return this.uuid.equals(user.getUuid());
            }
        }

        return false;
    }

    public List<String> getNameTags() {
        return nameTags;
    }

    public void setNameTags(List<String> nameTags) {
        this.nameTags = nameTags;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void addAllPosters(List<String> uuidPosterIds) {
        posters.addAll(uuidPosterIds);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(long lastEdited) {
        this.lastEdited = lastEdited;
    }
}
