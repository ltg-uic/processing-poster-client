package ltg.evl.util.collections;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.PictureZoneBuilder;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import ltg.evl.util.ImageLoader;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PImage;

import java.awt.*;
import java.util.concurrent.Callable;

import static ltg.evl.uic.poster.json.mongo.PosterItem.IMAGE;
import static ltg.evl.uic.poster.json.mongo.PosterItem.TEXT;

/**
 * Created by aperritano on 2/20/15.
 */
public class PosterItemToPictureZone implements Function<PosterItem, Callable<PictureZone>> {

    final static Logger logger = Logger.getLogger(PosterItemToPictureZone.class);

    @Override
    public Callable<PictureZone> apply(final PosterItem posterItem) {


        logger.log(Level.INFO, "New PosterItem: " + posterItem);


        Callable<PictureZone> callable = new Callable<PictureZone>() {

            @Override
            public PictureZone call() throws Exception {
                PictureZone pictureZone = null;

                PImage pImage = null;

                if (Optional.fromNullable(posterItem).isPresent()) {
                    switch (posterItem.getType()) {
                        case IMAGE:
                            final javaxt.io.Image jxtImage = new javaxt.http.Request(
                                    posterItem.getContent()).getResponse()
                                                            .getImage();


                            pImage = ImageLoader.toPImage(jxtImage);

                            break;
                        case TEXT:
                            javaxt.io.Image jxt2 = new javaxt.io.Image(ZoneHelper.renderTextToImage(
                                    ZoneHelper.helveticaNeue20JavaFont, new Color(0, 0, 0), posterItem.getContent(),
                                    600));

                            pImage = new PImage(jxt2.getBufferedImage());

                            break;
                    }
                }

                pictureZone = new PictureZoneBuilder().setImage(pImage)
                                                      .setUuid(posterItem.getUuid())
                                                      .setX(posterItem.getX())
                                                      .setY(posterItem.getY())
                                                      .setWidth(pImage.width)
                                                      .setHeight(pImage.height)
                                                      .setZoneName(posterItem.getName())
                                                      .setType(posterItem.getType())
                                                      .createPictureZone();

                return pictureZone;
            }
        };
        return callable;
    }
}
