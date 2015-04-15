package ltg.evl.uic.poster.widgets;

import de.looksgood.ani.Ani;
import ltg.evl.uic.poster.listeners.LoadPosterListener;
import org.apache.commons.lang.WordUtils;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Map;


public class PosterPageZone extends Zone {

    public PVector point = new PVector(0, 0);
    private LoadPosterListener loadPosterListener;
    private int greyColor = ZoneHelper.greyOutline;

    public PosterPageZone(String name, int x, int y, int width, int height, LoadPosterListener loadPosterListener) {
        super(name, x, y, width, height);
        point.x = x;
        point.y = y;
        this.loadPosterListener = loadPosterListener;
    }

    public void addPosters(Map<String, String> uuidIdToPosterName, int buttonWidth, int buttonHeight) {

        for (String uuid : uuidIdToPosterName.keySet()) {
            String posterTitle = uuidIdToPosterName.get(uuid);
            PosterButton posterButton = new PosterButton(uuid, WordUtils.capitalize(posterTitle), buttonWidth,
                                                         buttonHeight,
                                                         ZoneHelper.blueOutline);
            posterButton.addLoadPosterListener(this.loadPosterListener);
            add(posterButton);
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
