package ltg.evl.json.mongo;

import de.caluga.morphium.annotations.Entity;
import de.caluga.morphium.annotations.Id;
import de.caluga.morphium.annotations.Reference;
import de.caluga.morphium.annotations.caching.NoCache;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@NoCache
@Entity(translateCamelCase = true)
public class Poster {

    @Id
    private ObjectId id;
    private int height;
    private int width;
    private String name;
    
    @Reference
    private List<ltg.evl.json.mongo.PosterItem> posterItems = new ArrayList<>();
    public Poster() {}
    
    public Poster(int height, int width, String name) {
        this.height = height;
        this.width = width;
        this.name = name;
    }

    @Override
    public String toString() {
        return "poster";
    }

    public ObjectId getId() {
        return id;
    }

    public Poster setId(ObjectId id) {
        this.id = id;
        return this;
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

    public List<PosterItem> getPosterItems() {
        return posterItems;
    }

    public void setPosterItems(List<PosterItem> posterItems) {
        this.posterItems = posterItems;
    }
    
    public void addPosterItems(PosterItem posterItem) {
        posterItems.add(posterItem);

    }
}
