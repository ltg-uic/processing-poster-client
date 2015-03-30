package ltg.evl.util.collections;

import com.google.common.base.Function;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.widgets.PictureZone;

/**
 * Created by aperritano on 2/20/15.
 */
public class PictureZoneToPosterItem implements Function<PictureZone, PosterItem> {
    @Override
    public PosterItem apply(PictureZone pictureZone) {
//        String uuid = pictureZone.getUUID();
//        for (PosterItem posterItem : RESTHelper.getInstance().users.get(0).getPosters().get(0).getPosterItems()) {
//            if (posterItem.get_id().toString().equals(uuid)) {
//                posterItem.setWidth(pictureZone.getWidth());
//                posterItem.setHeight(pictureZone.getHeight());
//                posterItem.setY(pictureZone.getY());
//                posterItem.setX(pictureZone.getX());
//                //DBHelper.getInstance().dbClient().store(posterItem);
//               // System.out.println("POSTERITEM UPDATED: " + posterItem.toString());
//
//                pictureZone.startAnimation(false);
//
//                return posterItem;
//            }
//        }


        return null;
    }
}
