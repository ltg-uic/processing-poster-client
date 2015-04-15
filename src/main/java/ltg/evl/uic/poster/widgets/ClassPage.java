package ltg.evl.uic.poster.widgets;

import com.google.common.collect.ImmutableMap;
import de.looksgood.ani.Ani;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadClassListener;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Collection;


public class ClassPage extends Zone {

    public PVector point = new PVector(0, 0);
    //private Ani ani;
    private int greyColor = ZoneHelper.greyOutline;
    private LoadClassListener loadClassListener;

    public ClassPage(String name, int x, int y, int width, int height,
                     LoadClassListener loadClassListener) {
        super(name, x, y, width, height);
        this.loadClassListener = loadClassListener;
        point.x = x;
        point.y = y;
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

    public void addClasses(ImmutableMap<String, Collection<User>> classMap) {
        for (String className : classMap.keySet()) {
            Collection<User> u = classMap.get(className);
            ClassButton classButton = new ClassButton(className, className, 175, 175);
            classButton.initButton();
            classButton.addLoadClassListener(this.loadClassListener);
            add(classButton);
        }

        SMT.grid(25, 25, getWidth(), 25, 25, this.getChildren());
    }
}
