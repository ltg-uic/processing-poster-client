package ltg.evl.uic.poster.widgets.button;

import com.google.common.base.MoreObjects;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import processing.core.PFont;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class SaveButton extends Zone {

    public static final int SPACING = 4;
    protected int pressedButtonColor;
    protected int unpressedButtonColor;
    protected PFont font;
    protected String text;
    protected int textColor;
    protected int fontSize;
    protected int currentColor;
    private int outline;

    public SaveButton(String uuid, int width, int height) {
        super(uuid, width, height);

    }

    public SaveButton(String uuid, int x, int y, int width, int height) {
        super(uuid, width, height);

    }

    public void initButton() {

        this.unpressedButtonColor = ZoneHelper.blueOutline;
        this.currentColor = unpressedButtonColor;
        this.text = ZoneHelper.SAVE;
        this.textColor = ZoneHelper.whiteOutline;
        this.pressedButtonColor = ZoneHelper.greyOutline;
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
        this.currentColor = unpressedButtonColor;
        saveAction();
    }

    public void saveAction() {

    }

    @Override
    public void draw() {
        smooth(4);
        stroke(outline);
        strokeWeight(1);

        fill(this.currentColor);
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .omitNullValues()
                          .add("x", getX())
                          .add("y", getY())
                          .add("width", getWidth())
                          .add("height", getHeight())
                          .toString();
    }

}
