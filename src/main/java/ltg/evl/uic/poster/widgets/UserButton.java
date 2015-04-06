package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.listeners.LoadUserListener;
import processing.core.PFont;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class UserButton extends Zone {

    public boolean pressed = false;
    protected int pressedButtonColor;
    protected int unpressedButtonColor;
    protected int outline = ZoneHelper.getInstance().greyOutline;
    protected PFont font;
    protected String text;
    protected int textColor;
    protected int fontSize;
    protected int currentColor;
    protected int borderWeight = 1;
    private int savedWidth;
    private int savedHeight;
    private LoadUserListener loadUserListerner;

    public UserButton(String name, int buttonStartX, int buttonStartY, int buttonWidth, int buttonHeight, String text,
                      int pressedButtonColor, int unpressedButtonColor, PFont font) {
        super(name, buttonStartX, buttonStartY, buttonWidth, buttonHeight);

        this.font = font;
        this.text = text;
        this.fontSize = font.getSize();
        this.pressedButtonColor = pressedButtonColor;
        this.unpressedButtonColor = unpressedButtonColor;
        this.currentColor = unpressedButtonColor;


        this.textColor = color(255);
    }

    public UserButton(String uuid, String text, int width, int height) {
        super(uuid, width, height);
        this.savedWidth = width;
        this.savedHeight = height;
        this.font = ZoneHelper.helveticaNeue18Font;
        this.text = text;
        this.fontSize = font.getSize();
        //this.fontSize = 18;

        int num = ZoneHelper.getInstance().colors.length - 1;
        int n = ZoneHelper.random(0, num);


        this.unpressedButtonColor = ZoneHelper.getInstance().colors[n];
        this.pressedButtonColor = ZoneHelper.getInstance().greyOutline;
        this.currentColor = unpressedButtonColor;

        this.textColor = color(255);


    }


    @Override
    public void touch() {
        rst(false, false, false);
    }

    public void addLoadUserListener(LoadUserListener loadUserListerner) {
        this.loadUserListerner = loadUserListerner;
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
            text(text, getWidth() / 2 - borderWeight, getHeight() / 2 - borderWeight);
        }
    }


    @Override
    public void touchDown(Touch touch) {
        this.currentColor = pressedButtonColor;
    }

    @Override
    public void touchUp(Touch touch) {
        this.currentColor = unpressedButtonColor;
        this.loadUserListerner.loadUser(getName());
    }
}
