package ltg.evl.uic.poster.json.mongo;

import de.caluga.morphium.annotations.Entity;
import de.caluga.morphium.annotations.Id;
import de.caluga.morphium.annotations.Reference;
import de.caluga.morphium.annotations.caching.NoCache;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@NoCache
@Entity(translateCamelCase = true)
public class User {
    @Id
    private ObjectId id;
    private String name;

    @Reference
    private List<Poster> posters = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<Poster> getPosters() {
        return posters;
    }

    public void setPosters(List<Poster> posters) {
        this.posters = posters;
    }

    public void addPoster(Poster poster) {
        posters.add(poster);
    }

    public void removePoster(Poster poster) {
        posters.remove(poster);
    }
}
