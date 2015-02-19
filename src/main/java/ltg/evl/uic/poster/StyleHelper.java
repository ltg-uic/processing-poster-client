package ltg.evl.uic.poster;

import ltg.evl.json.mongo.PosterItem;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.*;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.concurrent.*;

/**
 * Created by aperritano on 2/15/15.
 */
public class StyleHelper {


    private static CompositeConfiguration config;
    private static PApplet graphicsContext;

    private StyleHelper() {
        config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        try {
            config.addConfiguration(new PropertiesConfiguration(ClassLoader.getSystemResource("system.properties")));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static StyleHelper helper() {
        return StaticHolder.INSTANCE;
    }

    public static int createColor(String color) {
        String[] c = config.getStringArray(color);
        return graphicsContext.color(Float.parseFloat(c[0]), Float.parseFloat(c[1]), Float.parseFloat(c[2]),
                                     Float.parseFloat(c[3]));
    }

    public java.awt.Image getImage(final PosterItem posterItem) throws ExecutionException, InterruptedException {


        final Future<Image> task;

        ExecutorService service = Executors.newFixedThreadPool(1);
        task = service.submit(new Callable<Image>() {
            @Override
            public Image call() throws Exception {
                byte[] bytes = Base64.decodeBase64(posterItem.getImageBytes());
                byte[] imgBytes = PApplet.loadBytes(new ByteArrayInputStream(bytes));
                return Toolkit.getDefaultToolkit().createImage(imgBytes);
            }
        });

        return task.get();

    }

    public PApplet getGraphicsContext() {
        return graphicsContext;
    }

    public void setGraphicsContext(PApplet graphicsContext) {
        StyleHelper.graphicsContext = graphicsContext;
    }

    public Configuration getConfiguration() {
        return config;
    }

    public PFont createFont(String fontName, String fontSize) {
        return graphicsContext.createFont(config.getString(fontName), config.getInt(fontSize));
    }

    public PImage createImage(String fileName) {
        return graphicsContext.loadImage(fileName);
    }
    
    private static class StaticHolder {
        static final StyleHelper INSTANCE = new StyleHelper();
    }

}
