package ltg.evl.uic.poster.util;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

/**
 * Created by aperritano on 2/15/15.
 */
public class StyleHelper {


    private PApplet graphicsContext;

    private StyleHelper() {

    }

    public static StyleHelper helper() {
        return StaticHolder.INSTANCE;
    }

    public int createColor(String color) {
        String[] c = PosterServices.getInstance().getConfig().getStringArray(color);
        return graphicsContext.color(Float.parseFloat(c[0]), Float.parseFloat(c[1]), Float.parseFloat(c[2]),
                                     Float.parseFloat(c[3]));
    }


    public PApplet getGraphicsContext() {
        return graphicsContext;
    }

    public void setGraphicsContext(PApplet graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public PFont createFont(String fontName, String fontSize) {
        return graphicsContext.createFont(PosterServices.getInstance().getConfig().getString(fontName),
                                          PosterServices.getInstance().getConfig().getInt(fontSize));
    }

    public PImage createImage(String fileName) {
        return graphicsContext.loadImage(fileName);
    }

    private static class StaticHolder {
        static final StyleHelper INSTANCE = new StyleHelper();
    }

}
