package ltg.evl.uic.poster.json.parse;

import org.parse4j.ParseClassName;
import org.parse4j.ParseFile;
import org.parse4j.ParseObject;

/**
 * Created by aperritano on 2/13/15.
 */
@ParseClassName("PImageFile")
public class PImageFile extends ParseObject {

    public PImageFile() {
    }

    public String getImageFileId() {
        return getString("imageFileId");
    }

    public void setImageFileId(String imageId) {
        put("imageFileId", imageId);
    }

    public ParseFile getImageFile() {
        return getParseFile("imageFile");
    }

    public void setImageFile(ParseFile parseFile) {
        put("imageFile", parseFile);
    }
}
