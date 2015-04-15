package tmp;

import com.google.common.base.Objects;
import ltg.evl.uic.poster.widgets.UserButton;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import vialab.SMT.Touch;

/**
 * Created by aperritano on 4/9/15.
 */
public class LogoutButton extends UserButton {

    private int borderColor;
    private float brightness;
    private int buttonColor;

    public LogoutButton(String uuid, String text, int width, int height, int buttonColor) {
        super(uuid, text, width, height);
        setUnpressedButtonColor(buttonColor);
        this.brightness = saturation(buttonColor);
    }

    public LogoutButton(String uuid, String text, int x, int y, int width, int height, int buttonColor) {
        super(uuid, text, width, height);
        setUnpressedButtonColor(buttonColor);
        setX(x);
        setY(y);
        this.buttonColor = buttonColor;
        this.brightness = saturation(buttonColor);
    }


    @Override
    public void initButton() {
        super.initButton();
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
        this.logoutAction();
    }

    public void logoutAction() {

    }

    @Override
    public void draw() {
        stroke(outline);
        strokeWeight(1);
        fill(currentColor);
        smooth(4);
        rect(borderWeight, borderWeight, getWidth() - 2 * borderWeight, getHeight() - 2 * borderWeight, 5);

        if (text != null) {
            if (font != null) {
                textFont(font, fontSize);
            }
            textAlign(CENTER, CENTER);
            textSize(fontSize);
            fill(ZoneHelper.whiteOutline);
            smooth(4);
            text(text, width / 2 - borderWeight, height / 2 - borderWeight);
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
}
