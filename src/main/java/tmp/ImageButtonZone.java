package tmp;

import processing.core.PImage;
import vialab.SMT.ButtonZone;
import vialab.SMT.Touch;

/**
 * Created by aperritano on 4/26/15.
 */
public class ImageButtonZone extends ButtonZone {

    private PImage zoneImage;

    public ImageButtonZone(String name, int x, int y, int width, int height, PImage zoneImage, int buttonColor) {
        super(name, x, y, width, height, "", 18, null, 0);
        this.zoneImage = zoneImage;
        this.color = buttonColor;
        this.borderColor = 255;

    }

    protected void drawImpl(int buttonColor, int textColor) {
        stroke(borderColor);
        strokeWeight(borderWeight);
        fill(buttonColor);
        //rect(0, 0, getWidth(), getHeight());
        ellipse(getWidth() / 2, getHeight() / 2, getWidth(), getHeight());
        image(zoneImage, (getWidth() / 2) - (zoneImage.width / 2), (getHeight() / 2) - (zoneImage.height / 2),
              zoneImage.width, zoneImage.height);
    }

    @Override
    public void touchUp(Touch touch) {
        super.touchUp(touch);
        System.out.println("HELO");
    }
}
