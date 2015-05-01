package ltg.evl.uic.poster.util.collections;

import com.google.common.base.Function;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.widgets.TextBoxZone;
import ltg.evl.uic.poster.widgets.TextBoxZoneBuilder;

/**
 * Created by aperritano on 3/22/15.
 */
public class PosterItemToTextZone implements Function<PosterItem, TextBoxZone> {
    @Override
    public TextBoxZone apply(PosterItem posterItem) {
        return new TextBoxZoneBuilder().setPosterItem(posterItem).createTextZoneWithPosterItem();
    }
}