package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.listeners.LoadClassListener;
import vialab.SMT.Touch;

/**
 * Created by aperritano on 4/4/15.
 */
public class ClassButton extends UserButton {


    private LoadClassListener loadClassListerner;

    public ClassButton(String uuid, String text, int width, int height) {
        super(uuid, text, width, height);
    }


    @Override
    protected void initButton() {
        this.currentColor = ZoneHelper.blueOutline;
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

    public void addLoadClassListener(LoadClassListener loadClassListener) {
        this.loadClassListerner = loadClassListener;
    }

    @Override
    public void touchUp(Touch touch) {
        this.currentColor = unpressedButtonColor;
        this.loadClassListerner.loadClass(getName());
    }


}
