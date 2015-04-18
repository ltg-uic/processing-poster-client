package ltg.evl.uic.poster.widgets.buttons;

import com.google.common.base.Objects;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import processing.core.PFont;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 4/17/15.
 */
public class EditModeButton extends Zone {

    public static final int SPACING = 4;
    protected int pressedButtonColor;
    protected int unpressedButtonColor;
    protected int outline = ZoneHelper.greyOutline;
    protected PFont font;
    protected String text;
    protected int textColor;
    protected int fontSize;
    protected int currentColor;
    private boolean isEditing = true;

    public EditModeButton(String uuid, int width, int height) {
        super(uuid, width, height);

    }

    public EditModeButton(String uuid, int x, int y, int width, int height) {
        super(uuid, width, height);

    }

    public void initButton() {

        this.toggleButton();

        this.currentColor = unpressedButtonColor;

        this.pressedButtonColor = ZoneHelper.greyOutline;
        this.outline = ZoneHelper.greyOutline;
        this.font = ZoneHelper.helveticaNeue18Font;
        this.fontSize = 18;
        this.textColor = color(255);
    }

    public void toggleButton() {
        if (isEditing) {
            this.text = ZoneHelper.EDIT;
            this.unpressedButtonColor = ZoneHelper.greenColor;
        } else {
            this.text = ZoneHelper.PRESENT;
            this.unpressedButtonColor = ZoneHelper.orangeColor;
        }
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    @Override
    public void touchDown(Touch touch) {
        this.currentColor = pressedButtonColor;
        this.isEditing = !this.isEditing;
        this.toggleButton();
    }

    @Override
    public void touchUp(Touch touch) {
        this.currentColor = unpressedButtonColor;
        if (isEditing) {
            editAction();
        } else {
            presentAction();
        }
    }

    public void editAction() {

    }

    public void presentAction() {

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

        return Objects.toStringHelper(this)
                      .omitNullValues()
                      .add("x", getX())
                      .add("y", getY())
                      .add("width", getWidth())
                      .add("height", getHeight())
                      .toString();

    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }
}
