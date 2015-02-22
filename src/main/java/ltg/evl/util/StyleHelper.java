package ltg.evl.util;

import org.apache.commons.configuration.*;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

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
