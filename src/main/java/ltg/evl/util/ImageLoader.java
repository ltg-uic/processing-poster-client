package ltg.evl.util;

import org.apache.commons.codec.binary.Base64;
import processing.core.PApplet;
import processing.core.PImage;
import vialab.SMT.SMT;

import java.awt.*;
import java.io.ByteArrayInputStream;

/**
 * Created by aperritano on 2/20/15.
 */
public class ImageLoader {

    public static PImage toPImage(String imageString) {
        byte[] bytes = Base64.decodeBase64(imageString);
        byte[] imgBytes = PApplet.loadBytes(new ByteArrayInputStream(bytes));
        Image awtImage = Toolkit.getDefaultToolkit().createImage(imgBytes);

        PApplet p = new PApplet();

        MediaTracker tracker = new MediaTracker(SMT.getApplet());
        tracker.addImage(awtImage, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            //e.printStackTrace();  // non-fatal, right?
        }

        PImage image = new PImage(awtImage);
        image.parent = SMT.getApplet();
        return image;
    }

}
