package ltg.evl.util;

import ltg.evl.json.mongo.PosterItem;

import java.util.List;

/**
 * Created by aperritano on 2/17/15.
 */
public interface DBListener {


    void posterItemsUpdated(List<PosterItem> pictureZones);

}
