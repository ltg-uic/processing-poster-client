package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class PosterGrab extends GenericJson {


    @Key
    private String action;
    @Key
    private String class_name;
    @Key
    private String grabbing_user_name;
    @Key
    private String type;    //"text | media",
    @Key
    private String content; //"Text or URL https://pikachu.coati.encorelab.org/delynb5kw0.jpg",
    @Key
    private String poster_from_title; //"Timeline of Space Discoveries and Exploration ",
    @Key
    private String poster_from_uuid; //"552c055509a8d33947000000-poster",
    @Key
    private String poster_from_item_uuid;

    @Key
    private String user_from_uuid;

    @Key
    private String grabbed_poster_item_uuid; //: "UUID string of grabbed_poster_item"

    public PosterGrab(String posterItemUuid) {
        this.setGrabbed_poster_item_uuid(posterItemUuid);
        this.setAction("process_grabbed_poster_item");
    }

    public PosterGrab() {
        this.setAction("process_grabbed_poster_item");
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type != null && !type.equals("txt")) {
            this.type = "media";
        } else {
            this.type = "text";
        }
    }

    public void update(PosterItem posterItem) {
        this.setType(posterItem.getType());
        this.setContent(posterItem.getContent());
        this.setGrabbed_poster_item_uuid(posterItem.getUuid());
    }

    public void update(Poster poster) {
        this.setPoster_from_title(poster.getName());
        this.setPoster_from_uuid(poster.getUuid());
    }

    public void update(User user, boolean originator) {
        if (originator) {
            setUser_from_uuid(user.getUuid());
        } else {
            setGrabbing_user_name(user.getName());
        }
    }

    public String getPoster_from_title() {
        return poster_from_title;
    }

    public void setPoster_from_title(String poster_from_title) {
        this.poster_from_title = poster_from_title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getGrabbing_user_name() {
        return grabbing_user_name;
    }

    public void setGrabbing_user_name(String grabbing_user_name) {
        this.grabbing_user_name = grabbing_user_name;
    }

    public String getPoster_from_uuid() {
        return poster_from_uuid;
    }

    public void setPoster_from_uuid(String poster_from_uuid) {
        this.poster_from_uuid = poster_from_uuid;
    }

    public String getGrabbed_poster_item_uuid() {
        return grabbed_poster_item_uuid;
    }

    public void setGrabbed_poster_item_uuid(String grabbed_poster_item_uuid) {
        this.grabbed_poster_item_uuid = grabbed_poster_item_uuid;
    }

    public String getPoster_from_item_uuid() {
        return poster_from_item_uuid;
    }

    public void setPoster_from_item_uuid(String poster_from_item_uuid) {
        this.poster_from_item_uuid = poster_from_item_uuid;
    }

    public String getUser_from_uuid() {
        return user_from_uuid;
    }

    public void setUser_from_uuid(String user_from_uuid) {
        this.user_from_uuid = user_from_uuid;
    }
}
