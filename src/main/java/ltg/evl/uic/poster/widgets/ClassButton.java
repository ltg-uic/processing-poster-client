package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.listeners.LoadClassListener;
import vialab.SMT.Touch;

/**
 * Created by aperritano on 4/4/15.
 */
public class ClassButton extends UserButton {


    private LoadClassListener loadClassListerner;

    public ClassButton(String uuid, String text, int width, int height) {
        super(uuid, text, width, height);
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    public void addLoadClassListener(LoadClassListener loadClassListener) {
        this.loadClassListerner = loadClassListener;
    }

    @Override
    public void touchUp(Touch touch) {
        this.currentColor = unpressedButtonColor;
        this.loadClassListerner.loadClass(getName());
    }


}
