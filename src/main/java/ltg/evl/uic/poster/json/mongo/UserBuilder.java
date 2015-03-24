package ltg.evl.uic.poster.json.mongo;

import java.util.List;

public class UserBuilder {

    private String name;
    private String uuid;
    private List<String> nameTags;

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
    public User createUser() {
        return new User(name, uuid, nameTags);
    }
}