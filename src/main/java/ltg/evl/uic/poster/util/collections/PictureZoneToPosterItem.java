package ltg.evl.uic.poster.util.collections;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.util.ModelHelper;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import vialab.SMT.SMT;

import java.awt.*;
import java.util.concurrent.Executors;

public class PictureZoneToPosterItem implements Function<PictureZone, PosterItem> {
    final static Logger logger = Logger.getLogger(PictureZoneToPosterItem.class);
    ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    @Override
    public PosterItem apply(final PictureZone pictureZone) {


        Optional<PictureZone> pictureZoneOptional = Optional.fromNullable(pictureZone);

        PosterItem modPosteritem = null;
        if (pictureZoneOptional.isPresent()) {

            Optional<PosterItem> posterItemOptional = Optional.fromNullable(
                    PosterDataModel.helper().findPosterItemWithPosterItemUuid(pictureZone.getName()));

            if (posterItemOptional.isPresent()) {
                modPosteritem = posterItemOptional.get();

                logger.log(Level.INFO, "MOD PosterItem: " + modPosteritem);


                Dimension screenSize = pictureZone.getScreenSize();
                System.out.println("screenSize " + screenSize.toString());

                Dimension size = pictureZone.getSize();
                System.out.println("Size " + size.toString());


                ZoneHelper.computeCoefficient(modPosteritem);

                modPosteritem.setLastEdited(ModelHelper.getTimestampMilli());
                modPosteritem.setXn((pictureZone.getX() * 1.0) / SMT.getApplet().displayWidth);
                modPosteritem.setYn((pictureZone.getY() * 1.0) / SMT.getApplet().displayHeight);
                modPosteritem.setWn((pictureZone.getWidth() * 1.0) / SMT.getApplet().displayWidth);
                modPosteritem.setHn((pictureZone.getHeight() * 1.0) / SMT.getApplet().displayHeight);

                System.out.println("\nIn PictureZoneToPosterItem.java");
                System.out.println("===================================================");
                System.out.println("pictureZone.getX(): " + pictureZone.getX() * 1.0);
                System.out.println("SMT.getApplet().displayWidth: " + SMT.getApplet().displayWidth);
                System.out.println("Combined: " + (pictureZone.getX() * 1.0) / SMT.getApplet().displayWidth);

                System.out.println("\npictureZone.getY(): " + pictureZone.getY() * 1.0);
                System.out.println("SMT.getApplet().displayHeight: " + SMT.getApplet().displayHeight);
                System.out.println("Combined: " + (pictureZone.getY() * 1.0) / SMT.getApplet().displayHeight);

                System.out.println("\npictureZone.getWidth(): " + pictureZone.getWidth() * 1.0);
                System.out.println("SMT.getApplet().displayWidth: " + SMT.getApplet().displayWidth);
                System.out.println("Combined: " + (pictureZone.getWidth() * 1.0) / SMT.getApplet().displayWidth);

                System.out.println("\npictureZone.getHeight(): " + pictureZone.getHeight() * 1.0);
                System.out.println("SMT.getApplet().displayHeight: " + SMT.getApplet().displayHeight);
                System.out.println("Combined: " + (pictureZone.getHeight() * 1.0) / SMT.getApplet().displayHeight);
                System.out.println("===================================================");

                return modPosteritem;
            }


        }
        return null;
    }
}
