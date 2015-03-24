package ltg.evl.util.collections;

import com.google.common.base.Function;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.util.ImageLoader;
import processing.core.PImage;

/**
 * Created by aperritano on 2/20/15.
 */
public class PosterItemToPictureZone implements Function<PosterItem, PictureZone> {
    @Override
    public PictureZone apply(PosterItem posterItem) {
        PImage pImage = ImageLoader.toPImage(posterItem.getImageBytes());

        PictureZone pictureZone = new PictureZone(pImage, posterItem.getUuid().toString(),
                                                  posterItem.getX(),
                                                  posterItem.getY(), posterItem.getWidth(),
                                                  posterItem.getHeight());
        return pictureZone;

    }
}
