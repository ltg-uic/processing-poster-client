package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.listeners.LoadClassListener;
import processing.core.PFont;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 4/4/15.
 */
public class ClassButton extends Zone {

    protected int pressedButtonColor;
    protected int unpressedButtonColor;
    protected int outline = ZoneHelper.greyOutline;
    protected PFont font;
    protected String text;
    protected int textColor;
    protected int fontSize;
    protected int currentColor;
    protected int borderWeight = 5;

    private LoadClassListener loadClassListerner;

    public ClassButton(String uuid, String text, int width, int height) {
        super(uuid, 0, 0, width, height);
        this.text = text;
    }


    public void initButton() {
        this.unpressedButtonColor = ZoneHelper.greenColor;
        this.pressedButtonColor = ZoneHelper.greyOutline;
        this.currentColor = unpressedButtonColor;
        this.outline = ZoneHelper.greyOutline;
        this.textColor = color(255);
        this.font = ZoneHelper.helveticaNeue18Font;
        this.fontSize = 18;
    }


    @Override
    public void draw() {
        smooth(4);
        stroke(outline);
        strokeWeight(1);

        fill(currentColor);
        smooth(4);
        ellipse(getWidth() / 2, getHeight() / 2, getWidth(), getHeight());


        if (text != null) {
            if (font != null) {
                textFont(font, fontSize);
            }
            textAlign(CENTER, CENTER);
            textSize(fontSize);
            fill(textColor);
            smooth(4);
            text(text, getWidth() / 2, getHeight() / 2);
        }
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    @Override
    public void touchDown(Touch touch) {
        this.currentColor = pressedButtonColor;
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
