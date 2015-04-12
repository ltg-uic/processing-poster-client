package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadUserListener;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Map;

/**
 * Created by aperritano on 3/30/15.
 */
public class UserPageZone extends Zone {

    private int greyColor = ZoneHelper.greyOutline;
    private LoadUserListener loadUserListerner;

    public UserPageZone(String name, int x, int y, int width, int height, LoadUserListener loadUserListerner) {
        super(name, x, y, width, height);
        this.loadUserListerner = loadUserListerner;
    }

    public void addUsers(Map<String, User> uuidIdToUser) {

        int i = 0;
        for (String uuid : uuidIdToUser.keySet()) {
            User user = uuidIdToUser.get(uuid);
            user.setColor(ZoneHelper.colors[i]);
            UserButton userButton = new UserButton(uuid, 175, 175);
            userButton.setUser(user);
            userButton.initButton();
            userButton.addLoadUserListener(this.loadUserListerner);
            add(userButton);
        }

        SMT.grid(25, 25, getWidth(), 25, 25, this.getChildren());

    }

    public void translate(int x, int y) {

        // define a Ani with callbacks, specify the method name after the keywords: onStart, onEnd, onDelayEnd and onUpdate
//        widthAni = new Ani(this, 1.5f, "width", animationWidth, Ani.EXPO_IN_OUT, "onStart:itsStarted");
//        heightAni = new Ani(this, 1.5f, "height", animationHeight, Ani.EXPO_IN_OUT, "onStart:itsStarted");
//        widthAni.start();
//        heightAni.start();
//
//        //Ani x1 = Ani.to(this, 1.0f, "x", 500f);
//        Ani y1 = Ani.to(this, 1.0f, "y", 200f);
//
//        //x1.start();
//        y1.start();

    }

    @Override
    public void draw() {
        //background(255);
        fill(255);
        stroke(greyColor);
        strokeWeight(2);
        rect(0, 0, this.getWidth(), this.getHeight());
    }

    @Override
    public void touch() {
        rst(false, false, true);
    }

}
