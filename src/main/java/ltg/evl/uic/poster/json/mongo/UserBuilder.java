package ltg.evl.uic.poster.json.mongo;

import java.util.List;

public class UserBuilder {

    private String name;
    private String uuid;
    private int color;
    private List<String> nameTags;
    private String classname;
    private List<String> posters;


    public UserBuilder setPosters(List<String> posters) {
        this.posters = posters;
        return this;
    }

    public UserBuilder setClassname(String classname) {
        this.classname = classname;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public UserBuilder setNameTags(List<String> nameTags) {
        this.nameTags = nameTags;
        return this;
    }

    public UserBuilder setColor(int color) {
        this.color = color;
        return this;
    }

    public User createUser() {
        return new User(name, uuid, classname, nameTags, color, posters);
    }


}