package ltg.evl.uic.poster;

import com.google.common.base.Function;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import tmp.TextBoxZone;

/**
 * Created by aperritano on 3/22/15.
 */
public class PosterItemToTextZone implements Function<PosterItem, TextBoxZone> {
    @Override
    public TextBoxZone apply(PosterItem posterItem) {


        TextBoxZone textZone = new TextBoxZone(posterItem.getContent(), posterItem.getUuid(), posterItem.getX(),
                                               posterItem.getY(), 200, 50);
        return textZone;
    }
}