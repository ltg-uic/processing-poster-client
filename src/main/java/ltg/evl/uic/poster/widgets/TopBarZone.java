package ltg.evl.uic.poster.widgets;

//import org.apache.commons.lang3.math.NumberUtils;

import processing.core.PApplet;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 2/19/15.
 */
public class TopBarZone extends Zone {


    public float percentDone = 0;

    float w;
    private boolean progressMode = false;

    private int totalNumberOfTicks;
    private int tickCounter;

    public TopBarZone(int x, int y, int width, int height, int backgroundColor) {
        super(x, y, width, height);
        this.backgroundColor = backgroundColor;
    }

    public void setTotalNumberOfTicks(int totalNumberOfTicks) {
        this.totalNumberOfTicks = totalNumberOfTicks;
    }

    public void incrementTickCounter() {
        tickCounter++;
        percentDone = (new Float(tickCounter).floatValue() / new Float(totalNumberOfTicks).floatValue()) * 100;
    }

    public void resetTickCounter() {
        tickCounter = 0;
        percentDone = 0;
    }

    @Override
    public void draw() {
        noStroke();
        fill(0, 0, 0, 30);
        rect(0, 0, getWidth(), getHeight() + 3);

        fill(backgroundColor);
        stroke(66, 182, 245, 255);
        rect(0, 0, this.getWidth(), this.getHeight());


        //filter(BLUR, 4);
        if (percentDone >= 100) {
            fill(157, 203, 107, 255);
            stroke(80, 174, 85, 255);
            w = PApplet.map(percentDone, 0, 100, 0, this.getWidth());
            rect(0, 0, w, this.getHeight());
        } else {
            resetTickCounter();
        }

    }

    @Override
    public void touch() {
    }
}
