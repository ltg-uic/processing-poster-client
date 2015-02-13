package ltg.evl.JSON;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbDocument;

/**
 * Created by aperritano on 2/12/15.
 */
public class Location extends CouchDbDocument {
    
    private int x;
    private int y;
    
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
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
}
