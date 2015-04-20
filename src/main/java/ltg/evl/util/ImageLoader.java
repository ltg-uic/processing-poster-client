package ltg.evl.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import org.apache.commons.codec.binary.Base64;
import processing.core.PApplet;
import processing.core.PImage;
import vialab.SMT.SMT;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

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
//        MediaTracker tracker = new MediaTracker(SMT.getApplet());
//        tracker.addImage(awtImage, 0);
//        try {
//            tracker.waitForAll();
//        } catch (InterruptedException e) {
//            //e.printStackTrace();  // non-fatal, right?
//        }

        PImage image = new PImage(newImage.getBufferedImage());
        image.parent = SMT.getApplet();
        return image;
    }


    public static PImage downloadImage(final String url) throws ExecutionException, InterruptedException {
        final javaxt.io.Image jxtImage = new javaxt.http.Request(
                url).getResponse()
                    .getImage();

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<PImage> explosion = service.submit(new Callable<PImage>() {
            public PImage call() {
                return SMT.getApplet().loadImage(url);
            }
        });


        return explosion.get();
    }
}
