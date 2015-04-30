package ltg.evl.uic.poster.widgets;

import processing.core.PFont;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 4/17/15.
 */
public class RemoveModeButton extends Zone {

    public static final int SPACING = 4;
    protected int pressedButtonColor;
    protected int unpressedButtonColor;
    protected int outline = ZoneHelper.greyOutline;
    protected PFont font;
    protected String text;
    protected int textColor;
    protected int fontSize;
    protected int currentColor;

    public RemoveModeButton(String uuid, int width, int height) {
        super(uuid, width, height);

    }

    public RemoveModeButton(String uuid, int x, int y, int width, int height) {
        super(uuid, width, height);

    }

    public void initButton() {

        this.unpressedButtonColor = ZoneHelper.redOutline;
        this.pressedButtonColor = ZoneHelper.greyOutline;
        this.currentColor = unpressedButtonColor;
        this.text = ZoneHelper.REMOVE;
        this.outline = ZoneHelper.greyOutline;
        this.font = ZoneHelper.helveticaNeue18Font;
        this.fontSize = 18;
        this.textColor = color(255);
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    @Override
    public void touchDown(Touch touch) {
        this.currentColor = pressedButtonColor;
    }

    @Override
    public void touchUp(Touch touch) {
        System.out.println("RemoveModeButton.touchUp");
        this.currentColor = unpressedButtonColor;
        this.removeAction();
    }

    public void removeAction() {

    }

    @Override
    public void draw() {
        smooth(4);
        stroke(outline);
        strokeWeight(1);

        fill(currentColor);
        smooth(4);
        rect(0, 0, getWidth(), getHeight(), 10);


        if (text != null) {
            if (font != null) {
                textFont(font, fontSize);
            }
            textAlign(CENTER, CENTER);
            textSize(fontSize);
            fill(textColor);
            smooth(4);
            text(text, 0, 0, getWidth() - SPACING, getHeight() - SPACING);
        }
    }

}
