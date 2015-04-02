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
    }

    public void addLoadPosterListener(LoadPosterListener loadPosterListener) {
        this.loadPosterListener = loadPosterListener;
    }

    @Override
    public void touchUp(Touch touch) {
        this.currentColor = unpressedButtonColor;
        this.loadPosterListener.loadPoster(getName());
    }
}
