package ltg.evl.uic.poster.widgets;

import com.google.common.collect.ImmutableMap;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadClassListener;
import ltg.evl.uic.poster.widgets.buttons.ClassButton;
import ltg.evl.util.de.looksgood.ani.Ani;
import processing.core.PFont;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Collection;


public class ClassPage extends Zone {

    private final PFont font;
    int heading_height = 50;
    //private Ani ani;
    private LoadClassListener loadClassListener;
    private int initY;

    public ClassPage(String name, int x, int y, int width, int height,
                     LoadClassListener loadClassListener) {
        super(name, x, y, width, height);
        this.loadClassListener = loadClassListener;
        this.initY = SMT.getApplet().getHeight();
        this.font = ZoneHelper.helveticaNeue18Font;
    }

    @Override
    public void draw() {


        stroke(ZoneHelper.greyOutline);
        strokeWeight(3);
        fill(255);
        rect(0, 0, this.getWidth(), this.getHeight() + heading_height, ZoneHelper.ROUND_CORNER);

        stroke(224, 224, 224);
        strokeWeight(3);
        fill(255);
        rect(1, 1, this.getWidth() - 2, this.getHeight() + heading_height - 2, ZoneHelper.ROUND_CORNER);

        textFont(font, 20);
        textAlign(CENTER, CENTER);
        textSize(22);
        fill(0);
        text(ZoneHelper.WHICH_CLASS_ARE_YOU_IN, getWidth() / 2 - 2, heading_height / 2);
    }


    public void startAni(PVector targetPoint, float speed, float delay) {
        Ani diameterAni = new Ani(this, speed, delay, "initY", targetPoint.y, Ani.EXPO_IN_OUT,
                                  "onUpdate:update");
        diameterAni.start();
    }

    public void update() {
        setY(initY);
    }

    public void done() {
        System.out.println("Done with ClassPage Animation");
    }

    @Override
    public void touch() {
        rst(false, false, true);
    }

    public void addClasses(ImmutableMap<String, Collection<User>> classMap) {
        Zone body = new Zone("bodyc", 0, heading_height, this.getWidth(), getHeight()) {
            @Override
            public void draw() {
                stroke(255);
                strokeWeight(0);
                fill(255);
                rect(3, 0, getWidth() - 6, getHeight() - 3, ZoneHelper.ROUND_CORNER);
            }

            @Override
            public void touch() {
                super.touch();
            }
        };


        for (String className : classMap.keySet()) {
            ClassButton classButton = new ClassButton(className, className, ZoneHelper.CLASS_BUTTON_SIZE,
                                                      ZoneHelper.CLASS_BUTTON_SIZE);
            classButton.initButton();
            classButton.addLoadClassListener(this.loadClassListener);
            body.add(classButton);
        }

        SMT.grid(ZoneHelper.GRID_SPACER, ZoneHelper.GRID_SPACER, body.getWidth(), ZoneHelper.GRID_SPACER,
                 ZoneHelper.GRID_SPACER, body.getChildren());

        this.add(body);

    }
}
