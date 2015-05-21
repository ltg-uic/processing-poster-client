package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class PosterGrab extends GenericJson {


//    {
//        "action": "process_grabbed_poster_item",
//            "class_name": "ben | michael | test",
//            "grabbing_user_name": "aman",
//            "type": "text | media",
//            "content": "Text or URL https://pikachu.coati.encorelab.org/delynb5kw0.jpg",
//            "poster_title": "Timeline of Space Discoveries and Exploration ",
//            "poster_uuid": "552c055509a8d33947000000-poster",
//            "grabbed_poster_item_uuid": "UUID string of grabbed_poster_item"
//    }


    @Key
    private String type; //"text || media"

    @Key
    private String poster_title;


    public PosterGrab() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster_title() {
        return poster_title;
    }

    public void setPoster_title(String poster_title) {
        this.poster_title = poster_title;
    }
}
