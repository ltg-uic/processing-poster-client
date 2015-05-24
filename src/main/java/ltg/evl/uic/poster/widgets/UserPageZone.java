package ltg.evl.uic.poster.widgets;

import com.google.common.collect.ImmutableList;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadUserListener;
import ltg.evl.uic.poster.util.de.looksgood.ani.Ani;
import ltg.evl.uic.poster.widgets.button.UserButton;
import processing.core.PFont;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 3/30/15.
 */
public class UserPageZone extends Zone {

    private final boolean isShare;
    PVector point = new PVector(0, 0);
    int heading_height = 50;
    private int BACKGROUND_COLOR;
    private int TEXT_COLOR = 0;
    private PFont font;
    private int initY;
    private int greyColor = ZoneHelper.greyOutline;
    private LoadUserListener loadUserListerner;
    private String text;

    public UserPageZone(String name, int x, int y, int width, int height, LoadUserListener loadUserListerner,
                        boolean isShare) {
        super(name, x, y, width, height);
        this.initY = SMT.getApplet().getHeight();
        this.loadUserListerner = loadUserListerner;
        this.font = ZoneHelper.helveticaNeue18Font;
        this.isShare = isShare;
        if (isShare) {
            this.text = "Who do you want to share with?";
            this.BACKGROUND_COLOR = ZoneHelper.darkGrey;
            this.TEXT_COLOR = 255;
        } else {
            this.BACKGROUND_COLOR = ZoneHelper.whiteOutline;
            this.TEXT_COLOR = 0;
            this.text = ZoneHelper.WHICH_GROUP_ARE_YOU_IN;
        }
    }
    public void addUsers(ImmutableList<User> users) {

        Zone body = new Zone("bodyu", 0, heading_height, this.getWidth(), getHeight()) {
            @Override
            public void draw() {

                stroke(255);
                strokeWeight(0);
                fill(BACKGROUND_COLOR);
                rect(3, 0, getWidth() - 6, getHeight() - 3, ZoneHelper.ROUND_CORNER);
            }

            @Override
            public void touch() {
                super.touch();
            }
        };

        for (User user : users) {


            user.setColor(ZoneHelper.darkTeal);
            UserButton userButton = new UserButton(user.getUuid(), ZoneHelper.BUTTON_WIDTH, ZoneHelper.BUTTON_HEIGHT);
            userButton.setUser(user);
            userButton.initButton();
            userButton.addLoadUserListener(this.loadUserListerner);
            body.add(userButton);
        }

        SMT.grid(ZoneHelper.GRID_SPACER, 0, body.getWidth(), ZoneHelper.GRID_SPACER, ZoneHelper.GRID_SPACER,
                 body.getChildren());

        this.add(body);
    }

    @Override
    public void draw() {

        stroke(97, 97, 97);
        strokeWeight(3);
        fill(this.BACKGROUND_COLOR);
        rect(0, 0, this.getWidth(), this.getHeight() + heading_height, ZoneHelper.ROUND_CORNER);

        stroke(224, 224, 224);
        strokeWeight(3);
        fill(this.BACKGROUND_COLOR);
        rect(1, 1, this.getWidth() - 2, this.getHeight() + heading_height - 2, ZoneHelper.ROUND_CORNER);

        textFont(font, 20);
        textAlign(CENTER, CENTER);
        textSize(22);
        fill(TEXT_COLOR);
        text(this.text, getWidth() / 2 - 2, heading_height / 2);
    }

    public void startAni(PVector targetPoint, float speed, float delay) {
        Ani diameterAni = new Ani(this, speed, delay, "initY", targetPoint.y, Ani.EXPO_IN_OUT,
                                  "onUpdate:update");
        diameterAni.start();
    }

    public void update() {
        setY(initY);
    }

    @Override
    public void touch() {
        rst(false, false, true);
    }

}
