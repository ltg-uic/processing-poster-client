package ltg.evl.uic.poster;

import processing.core.PFont;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 2/15/15.
 */
public class ControlButtonZone extends Zone {

    private PFont buttonFont;
    private int buttonBackground;
    private int buttonHighlight;
    private int buttonOutline;
    private int buttonColor;
    private String buttonText;

    public ControlButtonZone(String name, int x, int y, int width, int height, String buttonText) {
        super(name, x, y, width, height);

        this.buttonBackground = StyleHelper.createColor("button.color.background");
        this.buttonOutline = StyleHelper.createColor("button.color.outline");
        this.buttonHighlight = StyleHelper.createColor("button.color.highlight");
        this.buttonColor = buttonBackground;

        this.buttonFont = StyleHelper.helper().createFont("button.font.name", "button.font.size");
        this.buttonText = buttonText;
    }

    @Override
    public void draw() {
        fill(buttonColor);
        //fill( 220, 140, 160, 140);
        stroke(buttonOutline);
        strokeWeight(3);

        rect(0, 0, this.getWidth(), this.getHeight());

        textAlign(CENTER, CENTER);
        textFont(buttonFont);
        //textMode( TEXT);
        fill(0, 0, 0);
        text(buttonText, this.getWidth() / 2, this.getHeight() / 2);
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    @Override
    public void touchDown(Touch touch) {
        buttonColor = buttonHighlight;
        System.out.println("touch down");
    }

    @Override
    public void touchUp(Touch touch) {
        if (this.getNumTouches() == 0) {
            buttonColor = buttonBackground;
            System.out.println("touch up");
        }
    }
}
