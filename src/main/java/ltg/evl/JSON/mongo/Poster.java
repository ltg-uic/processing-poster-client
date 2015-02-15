package ltg.evl.json.mongo;

import de.caluga.morphium.annotations.Embedded;
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
    private List<PosterItem> posterItems = new ArrayList<>();
    public Poster() {}


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

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
