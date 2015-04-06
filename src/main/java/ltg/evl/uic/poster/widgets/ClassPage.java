package ltg.evl.uic.poster.widgets;

import com.google.common.collect.ImmutableMap;
import de.looksgood.ani.Ani;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadClassListener;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Collection;


public class ClassPage extends Zone {

    private Ani ani;
    private int greyColor = ZoneHelper.getInstance().greyOutline;
    private LoadClassListener loadClassListener;

    public ClassPage(String classpage_id, int x, int y, int c_width, int c_height,
                     LoadClassListener loadClassListener) {
        this.loadClassListener = loadClassListener;
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

    public void addClasses(ImmutableMap<String, Collection<User>> classMap) {
        for (String className : classMap.keySet()) {
            Collection<User> u = classMap.get(className);
            ClassButton classButton = new ClassButton(className, className, 175, 175);
            classButton.addLoadClassListener(this.loadClassListener);
            add(classButton);
        }

        SMT.grid(25, 25, getWidth(), 25, 25, this.getChildren());
    }
}
