package ltg.evl.uic.poster.widgets;

import com.google.common.collect.ImmutableMap;
import de.looksgood.ani.Ani;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadClassListener;
import processing.core.PFont;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Collection;


public class ClassPage extends Zone {

    private final PFont font;
    private final int initHeight;
    public PVector point = new PVector(0, 0);
    int heading_height = 50;
    //private Ani ani;
    private int greyColor = ZoneHelper.greyOutline;
    private LoadClassListener loadClassListener;

    public ClassPage(String name, int x, int y, int width, int height,
                     LoadClassListener loadClassListener) {
        super(name, x, y, width, height);
        this.loadClassListener = loadClassListener;
        this.initHeight = height;
        point.x = x;
        point.y = y;
        this.font = ZoneHelper.helveticaNeue18Font;
    }

    @Override
    public void draw() {
        setX(point.x);
        setY(point.y);

        stroke(97, 97, 97);
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

    public void addClasses(ImmutableMap<String, Collection<User>> classMap) {
        Zone body = new Zone("bodyc", 0, heading_height, this.getWidth(), getHeight()) {
            @Override
            public void draw() {

                stroke(255);
                strokeWeight(0);
                fill(255);
                rect(3, 0, getWidth() - 6, getHeight() - 3, ZoneHelper.ROUND_CORNER);
            }
        };


        for (String className : classMap.keySet()) {
            Collection<User> u = classMap.get(className);
            ClassButton classButton = new ClassButton(className, className, 175, 175);
            classButton.initButton();
            classButton.addLoadClassListener(this.loadClassListener);
            body.add(classButton);
        }

        SMT.grid(25, 25, body.getWidth(), 25, 25, body.getChildren());

        this.add(body);

    }
}
