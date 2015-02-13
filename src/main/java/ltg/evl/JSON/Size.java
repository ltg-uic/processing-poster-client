package ltg.evl.JSON;

import org.ektorp.support.CouchDbDocument;

/**
 * Created by aperritano on 2/12/15.
 */
public class Size extends CouchDbDocument {
    
    private int height;
    private int width;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
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
}
