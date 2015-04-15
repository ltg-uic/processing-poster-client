package ltg.evl.uic.poster.widgets;

import vialab.SMT.Touch;

/**
 * Created by aperritano on 4/4/15.
 */
public class YesButton extends UserButton {


    public YesButton(String uuid, String text, int width, int height, int buttonColor, int fontSize) {
        super(uuid, text, width, height);
        this.currentColor = buttonColor;
        this.fontSize = fontSize;
    }


    @Override
    protected void initButton() {
        this.unpressedButtonColor = this.currentColor;
        this.textColor = color(255);
        this.pressedButtonColor = ZoneHelper.greyOutline;


        this.outline = ZoneHelper.greyOutline;
        this.textColor = color(255);

    }

    @Override
    public void draw() {
        smooth();
        stroke(outline);
        strokeWeight(1);

        fill(currentColor);
        smooth();
        ellipse(getWidth() / 2, getHeight() / 2, getWidth(), getHeight());


        if (text != null) {
            if (font != null) {
                textFont(font, fontSize);
            }
            textAlign(CENTER, CENTER);
            textSize(fontSize);
            fill(textColor);
            text(text, getWidth() / 2 - borderWeight, getHeight() / 2 - borderWeight);
        }
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    @Override
    public void touchUp(Touch touch) {
        this.currentColor = unpressedButtonColor;
    }


}
