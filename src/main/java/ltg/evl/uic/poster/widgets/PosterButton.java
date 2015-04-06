package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.listeners.LoadPosterListener;
import vialab.SMT.Touch;

/**
 * Created by aperritano on 4/1/15.
 */
public class PosterButton extends UserButton {

    private LoadPosterListener loadPosterListener;

    public PosterButton(String uuid, String text, int width, int height) {
        super(uuid, text, width, height);
        initText();
    }

    public PosterButton(String uuid, String text, int x, int y, int width, int height) {
        super(uuid, text, width, height);
        setX(x);
        setY(y);
        initText();
    }

    protected void initText() {
//        this.font = SMT.getApplet().loadFont(Resources.getResource("Roboto-Light-48.vlw").getPath());
//        this.fontSize = 20;
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


//    @Override
//    public void draw() {
//        smooth(4);
//        stroke(outline);
//        strokeWeight(1);
//
//        fill(currentColor);
//        smooth(4);
//        ellipse(getWidth() / 2, getHeight() / 2, getWidth(), getHeight());
//
//
//        if (text != null) {
//            if (font != null) {
//                textFont(font, fontSize);
//            }
//            textAlign(CENTER, CENTER);
//            textSize(fontSize);
//            fill(textColor);
//            text(text, getWidth() / 2 - borderWeight, getHeight() / 2 - borderWeight);
//        }
//    }


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
            text(text, 0, 0, getWidth() - 2, getHeight() - 2);
        }
    }

}
