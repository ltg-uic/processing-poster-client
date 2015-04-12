package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.listeners.LoadPosterListener;
import org.apache.commons.lang.WordUtils;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Map;


public class PosterPageZone extends Zone {

    private LoadPosterListener loadPosterListener;
    private int greyColor = ZoneHelper.greyOutline;

    public PosterPageZone(String name, int x, int y, int width, int height, LoadPosterListener loadPosterListener) {
        super(name, x, y, width, height);
        this.loadPosterListener = loadPosterListener;
    }

    public void addPosters(Map<String, String> uuidIdToPosterName) {

        for (String uuid : uuidIdToPosterName.keySet()) {
            String posterTitle = uuidIdToPosterName.get(uuid);
            PosterButton posterButton = new PosterButton(uuid, WordUtils.capitalize(posterTitle), 350, 175,
                                                         ZoneHelper.blueOutline);
            posterButton.addLoadPosterListener(this.loadPosterListener);
            add(posterButton);
        }

        SMT.grid(25, 25, getWidth(), 25, 25, this.getChildren());

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


}
