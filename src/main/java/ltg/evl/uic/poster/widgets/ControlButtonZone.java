package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.listeners.LoadUserListener;
import ltg.evl.uic.poster.listeners.SaveUserListerner;
import processing.core.PFont;
import vialab.SMT.SMT;
import vialab.SMT.Touch;
import vialab.SMT.Zone;
import vialab.SMT.event.TouchListener;

import java.util.ArrayList;
import java.util.List;

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
    private List<TouchListener> touchListeners = new ArrayList<>();
    private List<LoadUserListener> loadUserListerners = new ArrayList<>();
    private List<SaveUserListerner> saveUserListerners = new ArrayList<>();

    public ControlButtonZone(String name, int x, int y, int width, int height, String buttonText) {
        super(name, x, y, width, height);

        this.buttonBackground = SMT.getApplet().color(29, 128, 240);
        this.buttonOutline = SMT.getApplet().color(29, 128, 240);
        this.buttonHighlight = SMT.getApplet().color(29, 128, 240);
        this.buttonColor = buttonBackground;

        this.buttonFont = SMT.getApplet().createFont("HelveticaNeue-Bold", 12);
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

            if (getButtonText().equals("SAVE")) {
                for (SaveUserListerner saveUserListerner : saveUserListerners) {
                    saveUserListerner.saveUser("DrBanner");
                }

            } else {
                for (LoadUserListener loadUserListerner : loadUserListerners) {
                    loadUserListerner.loadUser("DrBanner", this.buttonColor);
                }
            }
        }
//        for (TouchListener listener : touchListeners) {
//            listener.handleTouchUp(new TouchEvent(this, TouchEvent.TouchType.UP, touch));
//        }


    }


    public void addTouchListener(TouchListener touchListener) {
        touchListeners.add(touchListener);
    }

    public void addLoadUserListener(LoadUserListener loadUserListerner) {
        loadUserListerners.add(loadUserListerner);
    }

    public void addSaveListener(SaveUserListerner saveUserListerner) {
        saveUserListerners.add(saveUserListerner);
        
    }
}
