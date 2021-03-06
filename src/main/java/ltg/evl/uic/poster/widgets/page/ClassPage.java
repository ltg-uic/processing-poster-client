package ltg.evl.uic.poster.widgets.page;

import com.google.common.collect.ImmutableMap;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadClassListener;
import ltg.evl.uic.poster.util.de.looksgood.ani.Ani;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import ltg.evl.uic.poster.widgets.button.ClassButton;
import processing.core.PFont;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Collection;


public class ClassPage extends Zone {

    private final PFont font;
    int heading_height = 50;
    private int BACKGROUND_COLOR = 255;
    private int TEXT_COLOR = 0;
    //private Ani ani;
    private LoadClassListener loadClassListener;
    private int initY;

    public ClassPage(String name, int x, int y, int width, int height,
                     LoadClassListener loadClassListener, boolean isShare) {
        super(name, x, y, width, height);
        this.loadClassListener = loadClassListener;
        this.initY = SMT.getApplet().getHeight();
        this.font = ZoneHelper.helveticaNeue18Font;

        if (isShare) {
            this.BACKGROUND_COLOR = ZoneHelper.darkGrey;
            this.TEXT_COLOR = 255;
        } else {
            this.BACKGROUND_COLOR = ZoneHelper.whiteOutline;
            this.TEXT_COLOR = 0;
        }
    }

    @Override
    public void draw() {


        stroke(ZoneHelper.greyOutline);
        strokeWeight(3);
        fill(BACKGROUND_COLOR);
        rect(0, 0, this.getWidth(), this.getHeight() + heading_height, ZoneHelper.ROUND_CORNER);

        stroke(224, 224, 224);
        strokeWeight(3);
        fill(BACKGROUND_COLOR);
        rect(1, 1, this.getWidth() - 2, this.getHeight() + heading_height - 2, ZoneHelper.ROUND_CORNER);

        textFont(font, 20);
        textAlign(CENTER, CENTER);
        textSize(22);
        fill(TEXT_COLOR);
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
                stroke(BACKGROUND_COLOR);
                strokeWeight(0);
                fill(BACKGROUND_COLOR);
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
