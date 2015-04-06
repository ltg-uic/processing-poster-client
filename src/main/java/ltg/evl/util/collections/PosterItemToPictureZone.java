package ltg.evl.util.collections;

import com.google.common.base.Function;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.PictureZoneBuilder;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import ltg.evl.util.ImageLoader;
import processing.core.PImage;

import java.awt.*;

/**
 * Created by aperritano on 2/20/15.
 */
public class PosterItemToPictureZone implements Function<PosterItem, PictureZone> {
    @Override
    public PictureZone apply(PosterItem posterItem) {
        PictureZone pictureZone = null;

        PImage pImage = null;
        if (posterItem.getType().equals(PosterItem.TEXT)) {

            // Font helveticaNeue = new Font("HelveticaNeue", Font.PLAIN, 16);


            javaxt.io.Image jxt2 = new javaxt.io.Image(ZoneHelper.renderTextToImage(
                    ZoneHelper.helveticaNeue18JavaFont, new Color(0, 0, 0), posterItem.getContent(),
                    600));

            pImage = new PImage(jxt2.getBufferedImage());

            pictureZone = new PictureZoneBuilder().setImage(pImage)
                                                  .setUuid(posterItem.getUuid().toString())
                                                  .setX(posterItem.getX())
                                                  .setY(posterItem.getY())
                                                  .setWidth(jxt2.getWidth())
                                                  .setHeight(jxt2.getHeight())
                                                  .createPictureZone();


        } else {
            pImage = ImageLoader.toPImage(posterItem.getImageBytes());

            pictureZone = new PictureZoneBuilder().setImage(pImage)
                                                  .setUuid(posterItem.getUuid().toString())
                                                  .setX(posterItem.getX())
                                                  .setY(posterItem.getY())
                                                  .setWidth(posterItem.getWidth())
                                                  .setHeight(posterItem.getHeight())
                                                  .createPictureZone();
        }




        return pictureZone;

    }


}
