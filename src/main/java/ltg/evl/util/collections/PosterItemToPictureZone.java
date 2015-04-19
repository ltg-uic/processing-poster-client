package ltg.evl.util.collections;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.PictureZoneBuilder;
import ltg.evl.util.ImageLoader;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PImage;

import static ltg.evl.uic.poster.json.mongo.PosterItem.IMAGE;
import static ltg.evl.uic.poster.json.mongo.PosterItem.TEXT;

/**
 * Created by aperritano on 2/20/15.
 */
public class PosterItemToPictureZone implements Function<PosterItem, PictureZone> {

    final static Logger logger = Logger.getLogger(PosterItemToPictureZone.class);

    @Override
    public PictureZone apply(final PosterItem posterItem) {
        logger.log(Level.INFO, "New PosterItem: " + posterItem);

        PictureZone pictureZone = null;

        PImage pImage = null;

        if (Optional.fromNullable(posterItem).isPresent()) {
            if (!Strings.isNullOrEmpty(posterItem.getType())) {

                if (Strings.isNullOrEmpty(posterItem.getContent())) {
                    return null;
                }

                switch (posterItem.getType()) {


                    case IMAGE:


                        pImage = ImageLoader.downloadImage(posterItem.getContent());

                        break;
                    case TEXT:


                        pImage = ImageLoader.textToImage(posterItem.getContent());

                        break;
                }

                int width = 0;
                int height = 0;
                if (posterItem.getHeight() <= 0 || posterItem.getWidth() <= 0) {
                    width = pImage.width;
                    height = pImage.height;
                } else {
                    width = posterItem.getWidth();
                    height = posterItem.getHeight();
                }


                pictureZone = new PictureZoneBuilder().setImage(pImage)
                                                      .setUuid(Strings.nullToEmpty(posterItem.getUuid()))
                                                      .setX(posterItem.getX())
                                                      .setY(posterItem.getY())
                                                      .setWidth(width)
                                                      .setHeight(height)
                                                      .setZoneName(
                                                              Strings.nullToEmpty(posterItem.getName()))
                                                      .setType(posterItem.getType())
                                                      .createPictureZone();
                return pictureZone;
            }
        }
        return null;
    }


}
