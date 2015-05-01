package ltg.evl.uic.poster.widgets.button;

import processing.core.PFont;
import vialab.SMT.ButtonZone;

/**
 * Created by aperritano on 3/23/15.
 */
public class RemoveButtonZone extends ButtonZone {


    public RemoveButtonZone(String buttonId, String text, PFont controlButtonFont) {
        super(buttonId, text, controlButtonFont);
    }

    public RemoveButtonZone(String buttonId, String text, PFont controlButtonFont, int x, int y, int width,
                            int height) {

        super(buttonId, x, y, width, height, text, 16, controlButtonFont, 0);

    }

    @Override
    protected void drawImpl(int buttonColor, int textColor) {
        stroke(166, 9, 22, 255);
        strokeWeight(borderWeight);
        fill(238, 43, 41, 255);
        rect(borderWeight, borderWeight, getWidth() - 2 * borderWeight, getHeight() - 2 * borderWeight);

        if (text != null) {
            if (font != null) {
                textFont(font, fontSize);
            }
            textAlign(CENTER, CENTER);
            textSize(fontSize);
            fill(255);
            text(text, getWidth() / 2 - borderWeight, getHeight() / 2 - borderWeight);
        }
    }


}
