package ltg.evl.uic.poster.widgets.button;

import ltg.evl.uic.poster.listeners.LoadPosterListener;
import vialab.SMT.Touch;

/**
 * Created by aperritano on 4/1/15.
 */
public class PosterButton extends UserButton {

    public static final int SPACING = 4;
    private LoadPosterListener loadPosterListener;

    public PosterButton(String uuid, String text, int width, int height, int color) {
        super(uuid, text, width, height);
        this.setUnpressedButtonColor(color);
        initText();
    }

    public PosterButton(String uuid, String text, int x, int y, int width, int height, int color) {
        super(uuid, text, width, height);
        this.setUnpressedButtonColor(color);
        setX(x);
        setY(y);
        initText();
    }

    protected void initText() {
        this.textColor = color(255);
//        this.font = SMT.getApplet().loadFont(Resources.getResource("Roboto-Light-48.vlw").getPath());
        this.fontSize = 16;
//        textFont(font, fontSize);
    }


    public void addLoadPosterListener(LoadPosterListener loadPosterListener) {
        this.loadPosterListener = loadPosterListener;
    }

    @Override
    public void touchUp(Touch touch) {
        this.currentColor = unpressedButtonColor;
        this.loadPosterListener.loadPoster(getName());
    }

    public void setUnpressedButtonColor(int color) {
        this.unpressedButtonColor = color;
    }

    public void setPressedButtonColor(int color) {
        this.pressedButtonColor = color;
    }

    @Override
    public void draw() {
        smooth(4);
        stroke(outline);
        strokeWeight(1);

        fill(this.unpressedButtonColor);
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
