package ltg.evl.JSON;

import com.google.gson.annotations.SerializedName;
import org.ektorp.support.CouchDbDocument;
import org.lightcouch.Document;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.util.List;
import java.util.UUID;

/**
 * Created by aperritano on 2/11/15.
 */

public class Poster extends CouchDbDocument {


    @SerializedName("resolution") private Dimension resolution;
    @SerializedName("posterItems") private String[] posterItems;
    
    public Poster(String id) {
        this.setId(id);
    }
    
    public Poster() { this.setId(UUID.randomUUID().toString());}
    
    public Dimension getResolution() {
        return resolution;
    }

    public void setResolution(Dimension resolution) {
        this.resolution = resolution;
    }

    public String[] getPosterItems() {
        return posterItems;
    }

    public void setPosterItems(String[] posterItems) {
        this.posterItems = posterItems;
    }

    @Override
    public String toString() {
        return "poster";
    }

}
