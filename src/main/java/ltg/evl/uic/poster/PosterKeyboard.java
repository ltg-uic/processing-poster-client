package ltg.evl.uic.poster;

import vialab.SMT.SwipeKeyboard;

/**
 * Created by aperritano on 9/24/14.
 */
public class PosterKeyboard extends SwipeKeyboard {


    private final int bgolor;

    public PosterKeyboard(int color) {
        this.bgolor = color;
    }

    @Override
    public void drawImpl() {
//        if( super.getBackgroundEnabled()){
//            fill( bgolor);
//            noStroke();
//            rect(
//                    position.x, position.y,
//                    dimension.width, dimension.height,
//                    cornerRounding_topLeft, cornerRounding_topRight,
//                    cornerRounding_bottomRight, cornerRounding_bottomLeft);
//        }
    }
}
