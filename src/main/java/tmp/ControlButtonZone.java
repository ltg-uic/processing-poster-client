package tmp;

import ltg.evl.uic.poster.widgets.TextBoxZone;
import processing.core.PFont;
import vialab.SMT.ButtonZone;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 7/31/14.
 */
public class ControlButtonZone extends ButtonZone {

    private final int buttonHeight;
    private final int buttonWidth;
    private final int buttonStartY;
    private final int buttonStartX;
    private final int pressedButtonColor;
    private final int unpressedButtonColor;
    public boolean pressed = false;

    public ControlButtonZone(String name, int buttonStartX, int buttonStartY, int buttonWidth, int buttonHeight, String text, int pressedButtonColor, int unpressedButtonColor, PFont font) {
        super(name, buttonStartX, buttonStartY, buttonWidth, buttonHeight, text,font,0);

        //blic ButtonZone(String name, int x, int y, int width, int height, String text, PFont font,
        //this(name, x, y, width, height, text, 16, font, angle);


        deactivated = false;
        this.buttonStartX = buttonStartX;
        this.buttonStartY = buttonStartY;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        this.pressedButtonColor = pressedButtonColor;
        this.unpressedButtonColor = unpressedButtonColor;

        textColor = color(255);
        pressedTextColor = textColor;
        color = unpressedButtonColor;
        pressedColor = unpressedButtonColor;


    }

//    @Override
//    public void drawImpl() {
//        super
//        drawImpl(color, textColor);
//    }


    @Override
    protected void drawImpl(int buttonColor, int textColor) {
        stroke(255);
        strokeWeight(1);
        fill(buttonColor);
        ellipse(buttonWidth/2,buttonHeight/2, buttonWidth, buttonHeight);


        if (text != null) {
            if (font != null) {
                textFont(font, fontSize);
            }
            textAlign(CENTER, CENTER);
            textSize(fontSize);
            fill(textColor);
            text(text, buttonWidth / 2 - borderWeight, buttonHeight / 2 - borderWeight);
        }
    }

    public void touch() {

    }

    public void pressed() {
        pressed = !pressed;

        Zone[] activeZones = SMT.getZones();

        for (Zone zone : activeZones) {
            if (zone instanceof TextBoxZone) {
                TextBoxZone t = (TextBoxZone) zone;
                t.setVisible(pressed);
            }
        }

        this.color = pressedButtonColor;
        this.pressedColor = pressedButtonColor;
    }

    public void unpressed() {
        this.color = unpressedButtonColor;
        this.pressedColor = unpressedButtonColor;
    }
}
