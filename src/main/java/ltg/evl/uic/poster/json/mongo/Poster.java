package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.base.Optional;
import ltg.evl.util.ModelHelper;

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
    private long lastEdited;

    @Key
    private List<String> posterItems = new ArrayList<>();

    public Poster() {

    }

    public Poster(String uuid, int height, int width, String name) {
        this.height = height;
        this.width = width;
        this.name = name;
        this.uuid = uuid;
        this.lastEdited = ModelHelper.getTimestampMilli();
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Poster)) {
            return false;
        }

        if (Optional.fromNullable(o).isPresent()) {
            Poster poster = (Poster) o;

            if (this.uuid != null && poster.getUuid() != null) {
                return this.uuid.equals(poster.getUuid());
            }
        }

        return false;
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

    public void addAllPosterItems(List<String> posterItems) {
        this.posterItems.addAll(posterItems);
    }

    public long getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(long lastEdited) {
        this.lastEdited = lastEdited;
    }
}
