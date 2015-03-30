package ltg.evl.util.collections;

import com.google.common.base.Function;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.PictureZoneBuilder;
import ltg.evl.util.ImageLoader;
import processing.core.PImage;

/**
 * Created by aperritano on 2/20/15.
 */
public class PosterItemToPictureZone implements Function<PosterItem, PictureZone> {
    @Override
    public PictureZone apply(PosterItem posterItem) {
        PImage pImage = ImageLoader.toPImage(posterItem.getImageBytes());

        PictureZone pictureZone = new PictureZoneBuilder().setImage(pImage)
                                                          .setUuid(posterItem.getUuid().toString())
                                                          .setX(posterItem.getX())
                                                          .setY(posterItem.getY())
                                                          .setWidth(posterItem.getWidth())
                                                          .setHeight(posterItem.getHeight())
                                                          .createPictureZone();
        return pictureZone;

    }
}
