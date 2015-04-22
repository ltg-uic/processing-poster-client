package ltg.evl.util.collections;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.util.ModelHelper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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

                modPosteritem.setHeight(pictureZone.getHeight());
                modPosteritem.setWidth(pictureZone.getWidth());
                modPosteritem.setRotation(pictureZone.getZoneRotation());
                modPosteritem.setScale(pictureZone.getZoneScale());
                modPosteritem.setY(pictureZone.getY());
                modPosteritem.setX(pictureZone.getX());
                modPosteritem.setLastEdited(ModelHelper.getTimestampMilli());
            }

            return modPosteritem;
        }
        return null;
    }
}
