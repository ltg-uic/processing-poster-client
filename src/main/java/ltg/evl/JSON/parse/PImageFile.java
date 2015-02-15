package ltg.evl.json.parse;

import org.parse4j.ParseClassName;
import org.parse4j.ParseFile;
import org.parse4j.ParseObject;

/**
 * Created by aperritano on 2/13/15.
 */
@ParseClassName("PImageFile")
public class PImageFile extends ParseObject {
    
    public PImageFile() {}
    
    public void setImageFileId(String imageId) {
        put("imageFileId", imageId);
    }
    
    public String getImageFileId() {
        return getString("imageFileId");
    }
    
    public void setImageFile(ParseFile parseFile) {
        put("imageFile",parseFile);
    }
    
    public ParseFile getImageFile() {
        return getParseFile("imageFile");
    }
}
