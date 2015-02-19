package ltg.evl.uic.poster.json.parse;

import org.parse4j.*;

import java.util.List;

/**
 * Created by aperritano on 2/13/15.
 */
@ParseClassName("PUser")
public class PUser extends ParseObject {

    public PUser() {


    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public void addPoster(PPoster pposter) {
        ParseRelation<ParseObject> pItems = getRelation("PPosters");
        pItems.add(pposter);
    }

    public List<PPoster> getPosters() {
        ParseRelation<PPoster> pPosters = getRelation("PPosters");
        ParseQuery<PPoster> fetchQuery = pPosters.getQuery();

        List<PPoster> allPosters = null;
        try {


            allPosters = fetchQuery.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return allPosters;

    }

}
