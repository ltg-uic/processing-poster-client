package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.json.mongo.Poster;
import ltg.evl.uic.poster.listeners.LoadPosterListener;
import ltg.evl.util.de.looksgood.ani.Ani;
import org.apache.commons.lang.WordUtils;
import processing.core.PFont;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Collection;


public class PosterPageZone extends Zone {

    private final PFont font;
    public PVector point = new PVector(0, 0);
    int heading_height = 50;
    private LoadPosterListener loadPosterListener;
    private int greyColor = ZoneHelper.greyOutline;

    public PosterPageZone(String name, int x, int y, int width, int height, LoadPosterListener loadPosterListener) {
        super(name, x, y, width, height);
        point.x = x;
        point.y = y;
        this.loadPosterListener = loadPosterListener;
        this.font = ZoneHelper.helveticaNeue18Font;
    }

    public void addPosters(Collection<Poster> posters) {


        Zone body = new Zone("bodyp", 0, heading_height, this.getWidth(), getHeight()) {
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
        for (Poster poster : posters) {

            PosterButton posterButton = new PosterButton(poster.getUuid(), WordUtils.capitalize(poster.getName()),
                                                         ZoneHelper.POSTER_BUTTON_WIDTH,
                                                         ZoneHelper.POSTER_BUTTON_HEIGHT,
                                                         ZoneHelper.blueOutline);
            posterButton.addLoadPosterListener(this.loadPosterListener);
            body.add(posterButton);
        }

        SMT.grid(ZoneHelper.GRID_SPACER, 0, body.getWidth(), ZoneHelper.GRID_SPACER, ZoneHelper.GRID_SPACER,
                 body.getChildren());
        this.add(body);
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
        text(ZoneHelper.SELECT_A_POSTER, getWidth() / 2 - 2, heading_height / 2);
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
