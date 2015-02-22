package ltg.evl.uic.poster.widgets;

import org.apache.commons.lang3.math.NumberUtils;
import processing.core.PApplet;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 2/19/15.
 */
public class TopBarZone extends Zone {


    public float percentDone = 0;
    public boolean isTimeRunning = false;
    int startTime = 0;
    float w;
    private boolean progressMode = false;
    private int elapsedTime;
    private int totalTime;
    private Thread timerThread;

    public TopBarZone(int x, int y, int width, int height, int backgroundColor) {
        super(x, y, width, height);
        this.backgroundColor = backgroundColor;
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
        if (progressMode) {
            fill(157, 203, 107, 255);
            stroke(80, 174, 85, 255);
            w = PApplet.map(percentDone, 0, 100, 0, this.getWidth());
            rect(0, 0, w, this.getHeight());
        }

    }

    public void startTimer() {
        progressMode = true;
        isTimeRunning = true;
        startTime = 0;
        timerThread = new Thread() {

            @Override
            public void run() {
                tick();
            }
        };
        timerThread.start();
    }

    public void tick() {
        while (isTimeRunning) {
            elapsedTime = applet.millis() - startTime;
            percentDone = (NumberUtils.toFloat(String.valueOf(elapsedTime)) / NumberUtils.toFloat(
                    String.valueOf(totalTime))) * 100f;
            if (percentDone >= 100) {
                stopTimer();
            }

        }
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;

    }

    @Override
    public void touch() {
    }

    public void stopTimer() {
        percentDone = 100;
        startTime = 0;
        progressMode = false;
        isTimeRunning = false;
    }
}
