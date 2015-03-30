package ltg.evl.uic.poster;

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


        TextBoxZone textZone = new TextBoxZoneBuilder().setPosterItem(posterItem).createTextZoneWithPosterItem();

        return textZone;
    }
}