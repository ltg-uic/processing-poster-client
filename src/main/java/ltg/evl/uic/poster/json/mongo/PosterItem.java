package ltg.evl.uic.poster.json.mongo;

import com.google.common.base.Objects;
import de.caluga.morphium.annotations.Entity;
import de.caluga.morphium.annotations.Id;
import de.caluga.morphium.annotations.caching.NoCache;
import org.bson.types.ObjectId;

@NoCache
@Entity(translateCamelCase = true)
public class PosterItem {

    @Id
    private ObjectId id;

    private int x;
    private int y;
    private int width;
    private int height;
    private String name;
    private String imageId;
    private String imageBytes;

    public PosterItem() {
    }

    public PosterItem(int x, int y, int width, int height, String name, String imageId, String imageBytes) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.imageId = imageId;
        this.imageBytes = imageBytes;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(String imageBytes) {
        this.imageBytes = imageBytes;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String toString() {

        return Objects.toStringHelper(this)
                      .omitNullValues()
                      .add("id", id)
                      .add("x", getX())
                      .add("y", getY())
                      .add("name", getName())
                      .add("imageId", getImageId())
                      .add("height", getHeight())
                      .add("width", getWidth())
                      .toString();

    }
}
