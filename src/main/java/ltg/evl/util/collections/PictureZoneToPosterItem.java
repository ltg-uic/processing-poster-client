package ltg.evl.util.collections;

import com.google.common.base.Function;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.util.DBHelper;

/**
 * Created by aperritano on 2/20/15.
 */
public class PictureZoneToPosterItem implements Function<PictureZone, PosterItem> {
    @Override
    public PosterItem apply(PictureZone pictureZone) {
        String uuid = pictureZone.getUUID();
        for (PosterItem posterItem : DBHelper.helper().getCurrentUser().getPosters().get(0).getPosterItems()) {
            if (posterItem.getId().toString().equals(uuid)) {
                posterItem.setWidth(pictureZone.getWidth());
                posterItem.setHeight(pictureZone.getHeight());
                posterItem.setY(pictureZone.getY());
                posterItem.setX(pictureZone.getX());
                DBHelper.helper().dbClient().store(posterItem);
                System.out.println("POSTERITEM UPDATED: " + posterItem.toString());

                pictureZone.startAnimation(false);

                return posterItem;
            }
        }


        return null;
    }
}
