package ltg.evl.uic.poster.json.parse;

import org.parse4j.*;

import java.util.List;

/**
 * Created by aperritano on 2/13/15.
 */
@ParseClassName("PPoster")
public class PPoster extends ParseObject {

    public PPoster() {

    }

    public void addPosterItems(PItem pitem) {
        ParseRelation<ParseObject> pItems = getRelation("PItems");
        pItems.add(pitem);
    }

    public List<PItem> getPosterItems() {
        ParseRelation<PItem> pItems = getRelation("PItems");
        ParseQuery<PItem> fetchQuery = pItems.getQuery();

        List<PItem> allPItems = null;
        try {
            allPItems = fetchQuery.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return allPItems;
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public int getHeight() {
        return getInt("height");
    }

    public void setHeight(int height) {
        put("height", height);
    }

    public int getWidth() {
        return getInt("width");
    }

    public void setWidth(int width) {
        put("width", width);
    }
}