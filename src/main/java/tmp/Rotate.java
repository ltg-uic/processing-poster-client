package tmp;

import processing.core.PApplet;

public class Rotate extends PApplet {

    /**
     * Rotate.
     * <p/>
     * Rotating a square around the Z axis. To get the results
     * you expect, send the rotate function angle parameters that are
     * values between 0 and PI*2 (TWO_PI which is roughly 6.28). If you prefer to
     * think about angles as degrees (0-360), you can use the radians()
     * method to convert your values. For example: scale(radians(90))
     * is identical to the statement scale(PI/2).
     */

    float angle;
    float jitter;

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"--full-screen", "--bgcolor=#666666", "--hide-stop", "Rotate"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }

    public void setup() {
        size(displayWidth, displayHeight);
        noStroke();
        fill(255);
        rectMode(CENTER);
    }

    public void draw() {
        background(51);

        // during even-numbered seconds (0, 2, 4, 6...)
        if (second() % 2 == 0) {
            jitter = random(-0.1f, 0.1f);
        }
        angle = angle + jitter;
        float c = cos(angle);
        translate(width / 2, height / 2);
        rotate(c);
        rect(0, 0, 180, 180);
    }
}
