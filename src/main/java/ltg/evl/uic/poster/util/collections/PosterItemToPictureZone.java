package ltg.evl.uic.poster.util.collections;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.util.ImageLoader;
import ltg.evl.uic.poster.widgets.DialogZoneController;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.PictureZoneBuilder;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import processing.core.PImage;
import vialab.SMT.SMT;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static ltg.evl.uic.poster.json.mongo.PosterItem.IMAGE;
import static ltg.evl.uic.poster.json.mongo.PosterItem.TEXT;

/**
 * Created by aperritano on 2/20/15.
 */
public class PosterItemToPictureZone implements Function<PosterItem, PictureZone> {

    final static Logger logger = Logger.getLogger(PosterItemToPictureZone.class);
    private final boolean isEditing;

    public PosterItemToPictureZone(
            boolean lastIsEditToggle) {
        this.isEditing = lastIsEditToggle;
    }

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

                if (posterItem.getY() < 0) {
                    posterItem.setY(ZoneHelper.random(200, 500));
                }

                if (posterItem.getX() < 0) {
                    posterItem.setX(ZoneHelper.random(200, 500));
                }

                //convert legacy items
                if (posterItem.getXn() < 0) {
                    ZoneHelper.computeCoefficient(posterItem);
                }

                int new_x = (int) ((posterItem.getXn() * SMT.getApplet().displayWidth));
                int new_y = (int) ((posterItem.getYn() * SMT.getApplet().displayHeight));
                int new_w = (int) ((posterItem.getWn() * SMT.getApplet().displayWidth));
                int new_h = (int) ((posterItem.getHn() * SMT.getApplet().displayHeight));


                PosterDataModel.helper().replacePosterItem(posterItem);

                // Print information to track whats going on
                //printDebugInfo(posterItem, new_x, new_y, new_w, new_h);

//                pictureZone = new PictureZoneBuilder().setImage(pImage)
//                                                      .setUuid(Strings.nullToEmpty(posterItem.getUuid()))
////                                                      .setX(new_x)
////                                                      .setY(new_y)
////                                                      .setWidth(new_w)
////                                                      .setHeight(new_h)
////                                                      .setRotation(posterItem.getRotation())
////                                                      .setScale(posterItem.getScale())
////                                                      .setZoneName(Strings.nullToEmpty(posterItem.getName()))
//                                                      .setType(posterItem.getType())
//                                                      .createPictureZone();

                pictureZone = new PictureZone(pImage, Strings.nullToEmpty(posterItem.getUuid()), new_x, new_y, new_w,
                                              new_h);

                if (SMT.add(pictureZone)) {
                    pictureZone.setLocation(new_x, new_y);
                    pictureZone.setSize(new_w, new_h);
                }


            } else {

                String uuid = UUID.randomUUID().toString();


                pictureZone = new PictureZoneBuilder().setUuid(uuid)
                                                      .setX(ZoneHelper.random(0, 500))
                                                      .setY(ZoneHelper.random(0, 500))
                                                      .setWidth(200)
                                                      .setHeight(200)
                                                      .setRotation("0")
                                                      .setScale("0")
                                                      .setZoneName(uuid)
                                                      .setType("txt")
                                                      .createPictureZone();

            }


//            System.out.println("PI ID: " + posterItem.getCited_from_poster_item_uuid());
//            System.out.println("POSTER ID: " + posterItem.getCited_from_poster_uuid());
//            System.out.println("USER ID: " + posterItem.getCited_from_user_uuid());
//            System.out.println("ACTUCAL" + posterItem.getUuid());

            //green cite


            if (posterItem.getCited_from_user_uuid() != null || StringUtils.stripToNull(
                    posterItem.getCited_from_poster_item_uuid()) != null) {
                pictureZone.setHasBeenCited(true);
            }

            pictureZone.setIsEditing(true);

            DialogZoneController.dialog().putControlPageOnTop();


            return pictureZone;

        }
        return null;
    }

    private void printDebugInfo(PosterItem posterItem, int nX, int nY, int nW, int nH){
        System.out.println("\nIn PosterItemToPictureZone");
        System.out.println("===================");
        System.out.println("X Values!!");
        System.out.println("new_x: " + nX);
        System.out.println("posterItem X: " + posterItem.getX());
        System.out.println("posterItem this.X: " + posterItem.getThisX());
        System.out.println("posterItem Xn: " + posterItem.getXn());
        System.out.println("\n===================");
        System.out.println("Y Values!!");
        System.out.println("new_y: " + nY);
        System.out.println("posterItem Y: " + posterItem.getY());
        System.out.println("posterItem Yn: " + posterItem.getYn());
        System.out.println("\n===================");
        System.out.println("Width Values!!");
        System.out.println("new_w: " + nW);
        System.out.println("posterItem W: " + posterItem.getWidth());
        System.out.println("posterItem Wn: " + posterItem.getWn());
        System.out.println("\n===================");
        System.out.println("Height Values!!");
        System.out.println("new_h: " + nH);
        System.out.println("posterItem H: " + posterItem.getHeight());
        System.out.println("posterItem Hn: " + posterItem.getHn());
        System.out.println("===================\n");
    }
}
