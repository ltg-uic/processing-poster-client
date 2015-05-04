package ltg.evl.uic.poster.util.collections;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.util.ImageLoader;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.PictureZoneBuilder;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import processing.core.PImage;
import vialab.SMT.SMT;

import java.util.concurrent.ExecutionException;

import static ltg.evl.uic.poster.json.mongo.PosterItem.IMAGE;
import static ltg.evl.uic.poster.json.mongo.PosterItem.TEXT;

/**
 * Created by aperritano on 2/20/15.
 */
public class PosterItemToPictureZone implements Function<PosterItem, PictureZone> {

    final static Logger logger = Logger.getLogger(PosterItemToPictureZone.class);

    @Override
    public PictureZone apply(PosterItem posterItem) {
        //logger.log(Level.INFO, "New PosterItem: " + posterItem);

        PictureZone pictureZone;
        PImage pImage = null;

        if (Optional.fromNullable(posterItem).isPresent()) {
            if (!Strings.isNullOrEmpty(posterItem.getType())) {
                switch (posterItem.getType()) {
                    case IMAGE:
                        if (posterItem.getContent() != null) {
                            try {

                                if (posterItem.getContent().contains("https")) {
                                    String newUrl = StringUtils.replace(posterItem.getContent(), "https", "http");
                                    pImage = ImageLoader.downloadImage(newUrl);
                                } else {
                                    pImage = ImageLoader.downloadImage(posterItem.getContent());
                                }
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case TEXT:
                        if (posterItem.getContent() != null) {
                            pImage = ImageLoader.textToImage(posterItem.getContent());
                        }
                        break;
                }


                if (posterItem.getHeight() == 0 && posterItem.getWidth() == 0) {
                    if (pImage != null) {
                        posterItem.setWidth(pImage.width);
                        posterItem.setHeight(pImage.height);
                    }
                }

                if (posterItem.getX() == 0 && posterItem.getY() == 0) {
                    posterItem.setY(ZoneHelper.random(200, 500));
                    posterItem.setX(ZoneHelper.random(200, 500));
                } else if (posterItem.getX() >= SMT.getApplet().displayWidth) {
                    posterItem.setX(ZoneHelper.random(200, SMT.getApplet().displayWidth - 50));
                } else if (posterItem.getY() >= SMT.getApplet().displayHeight) {
                    posterItem.setY(ZoneHelper.random(200, SMT.getApplet().displayHeight - 50));
                }
            }


            pictureZone = new PictureZoneBuilder().setImage(pImage)
                                                      .setUuid(Strings.nullToEmpty(posterItem.getUuid()))
                                                      .setX(posterItem.getX())
                                                      .setY(posterItem.getY())
                                                      .setWidth(posterItem.getWidth())
                                                      .setHeight(posterItem.getHeight())
                                                      .setRotation(posterItem.getRotation())
                                                      .setScale(posterItem.getScale())
                                                      .setZoneName(Strings.nullToEmpty(posterItem.getName()))
                                                      .setType(posterItem.getType())
                                                      .createPictureZone();

                return pictureZone;

        }
        return null;
    }


}
