package ltg.evl.uic.poster.json.mongo;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class PosterMessage extends GenericJson {

    public static String ADD = "ADD";
    public static String UPDATE = "UPDATE";
    public static String DELETE = "DELETE";
    public static String POSTER = "POSTER";
    public static String POSTER_ITEM = "POSTER_ITEM";
    public static String USER = "USER";
    @Key
    private String action;
    @Key
    private String content;
    @Key
    private String type;
    @Key
    private String userUuid;
    @Key
    private String posterUuid;
    @Key
    private String posterItemUuid;

    @Key
    private String posterItemId;

    @Key
    private String contentType;

    private GenericJson contentObject;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public GenericJson getContentObject() {
        return this.contentObject;
    }

    public void setContentObject(GenericJson contentObject) {
        this.contentObject = contentObject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosterItemUuid() {
        return posterItemUuid;
    }

    public void setPosterItemUuid(String posterItemUuid) {
        this.posterItemUuid = posterItemUuid;
    }

    public String getPosterUuid() {
        return posterUuid;
    }

    public void setPosterUuid(String posterUuid) {
        this.posterUuid = posterUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPosterItemId() {
        return posterItemId;
    }

    public void setPosterItemId(String posterItemId) {
        this.posterItemId = posterItemId;
    }
}
