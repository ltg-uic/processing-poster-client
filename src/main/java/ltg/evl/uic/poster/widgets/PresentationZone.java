package ltg.evl.uic.poster.widgets;

import de.looksgood.ani.Ani;
import processing.core.PFont;
import vialab.SMT.SMT;
import vialab.SMT.Touch;
import vialab.SMT.Zone;


public class PresentationZone extends Zone {

    private final PFont font;
    private int WIDTH = 120;
    private int bgAlpha;
    private boolean shouldDelete;

    public PresentationZone(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        this.bgAlpha = 255;
        this.font = ZoneHelper.helveticaNeue18Font;
    }

    @Override
    public boolean add(Zone zone) {
        return super.add(zone);
    }

    @Override
    public void touch() {
        this.delete();
    }

    @Override
    public void draw() {
        fill(117, 177, 177, bgAlpha);
        rect(0, 0, this.getWidth(), this.getHeight());
    }

    public void showDialog(final String text, int alpha) {
        bgAlpha = alpha;
        int num_per_col = 2;
        int reminder = 2 % num_per_col;

        int rows = ((2 - reminder) / 2) + reminder;

        int total_width = (num_per_col * 25) + (num_per_col * WIDTH) + 25;
        int total_height = (rows * 25) + (rows * WIDTH) + 25;

        int x2 = (this.getWidth() / 2) - (total_width / 2);
        int y2 = (this.getHeight() / 2) - (total_height / 2);

        int c_width = 800;
        int c_height = 450;

        final int heading_height = 50;
        Zone frame = new Zone("frame", 0, heading_height, total_width, total_height) {
            @Override
            public void draw() {
                fill(255);
                stroke(255);
                strokeWeight(1);
                rect(0, 0, this.getWidth() - 3, this.getHeight() - 3);
            }
        };

        Zone heading = new Zone("heading", x2, y2, total_width, heading_height + total_height) {
            @Override
            public void draw() {
                fill(255);
                stroke(ZoneHelper.greyOutline);
                strokeWeight(2);
                rect(0, 0, this.getWidth(), this.getHeight(), ZoneHelper.ROUND_CORNER);

                textFont(font, 20);
                textAlign(CENTER, CENTER);
                textSize(20);
                fill(0);
                text(text, getWidth() / 2 - 2, heading_height / 2);
            }
        };


        YesButton yesButton = new YesButton("Yes", "Yes", WIDTH, WIDTH, ZoneHelper.redOutline, 20) {
            @Override
            public void touchUp(Touch touch) {
                super.touchUp(touch);
                doYesAction();
            }
        };

        yesButton.initButton();

        YesButton noButton = new YesButton("No", "No", WIDTH, WIDTH, ZoneHelper.blueOutline, 20) {
            @Override
            public void touchUp(Touch touch) {
                super.touchUp(touch);
                SMT.remove(PresentationZone.this);
            }
        };
        noButton.initButton();
        frame.add(noButton, yesButton);

        SMT.grid(25, 25, frame.getWidth(), 25, 25, frame.getChildren());


        heading.add(frame);
        this.add(heading);

    }

    public void doYesAction() {
        SMT.remove(this);
    }


    public void fade(float speed, float delay, int alpha, boolean shouldDelete) {
        this.shouldDelete = shouldDelete;
        this.bgAlpha = alpha;
        Ani.to(this, speed, delay, "bgAlpha", bgAlpha, Ani.LINEAR, "onEnd:done");
    }

    public void done() {
        System.out.println("Fade done");
        if (shouldDelete) {
            SMT.remove(this);
        }
    }

    public void delete() {
        for (Zone zone : this.getChildren()) {
            this.remove(zone);
        }
        SMT.remove(this);
    }
}
