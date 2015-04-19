package ltg.evl.util;

import ltg.evl.uic.poster.widgets.ZoneHelper;
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
        byte[] bytes = Base64.decodeBase64(imageString.getBytes());
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

    public static PImage textToImage(String text) {
        javaxt.io.Image jxt2 = new javaxt.io.Image(ZoneHelper.renderTextToImage(
                ZoneHelper.helveticaNeue20JavaFont, new Color(0, 0, 0), text,
                600));

        PImage pImage = new PImage(jxt2.getBufferedImage());
        return pImage;
    }
    public static PImage toPImage(javaxt.io.Image newImage) {


        Image awtImage = Toolkit.getDefaultToolkit().createImage(newImage.getByteArray());

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


    public static PImage downloadImage(String url) {
        javaxt.io.Image jxtImage = new javaxt.http.Request(
                url).getResponse()
                    .getImage();

        return toPImage(jxtImage);
    }
}
