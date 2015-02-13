package ltg.evl.JSON;

import org.ektorp.Attachment;
import org.ektorp.support.CouchDbDocument;

/**
 * Created by aperritano on 2/11/15.
 */
public class PosterItem extends CouchDbDocument {

    private String itemType;
    private Size size;
    private Location location;



    
    public PosterItem(){}

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void addInlineAttachment(Attachment a) {
        super.addInlineAttachment(a);
    }

    
    @Override
    public String toString() {
        return "posterItem";
    }

}
