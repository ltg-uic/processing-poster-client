package ltg.evl.uic.poster.widgets;

import de.looksgood.ani.Ani;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadUserListener;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Map;

/**
 * Created by aperritano on 3/30/15.
 */
public class UserPageZone extends Zone {

    PVector point = new PVector(0, 0);
    private int greyColor = ZoneHelper.greyOutline;
    private LoadUserListener loadUserListerner;

    public UserPageZone(String name, int x, int y, int width, int height, LoadUserListener loadUserListerner) {
        super(name, x, y, width, height);
        point.x = x;
        point.y = y;
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


    @Override
    public void draw() {
        setX(point.x);
        setY(point.y);
        //background(255);
        fill(255);
        stroke(greyColor);
        strokeWeight(2);
        rect(0, 0, this.getWidth(), this.getHeight(), ZoneHelper.ROUND_CORNER);
    }

    public void startAni(PVector target, float speed, float delay) {
        Ani.to(point, speed, delay, "x", target.x, Ani.EXPO_OUT, "done");
        Ani.to(point, speed, delay, "y", target.y, Ani.EXPO_OUT, "done");
    }

    public void done() {
        System.out.println("done");
    }

    @Override
    public void touch() {
        rst(false, false, true);
    }

}
