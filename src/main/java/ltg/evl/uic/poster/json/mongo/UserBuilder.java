package ltg.evl.uic.poster.json.mongo;

public class UserBuilder {

    private String name;

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public User createUser() {
        return new User(name);
    }
}