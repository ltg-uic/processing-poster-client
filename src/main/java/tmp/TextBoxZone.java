package tmp;

import processing.core.PFont;
import vialab.SMT.TextBox;
import vialab.SMT.Zone;

import java.awt.event.KeyListener;

/**
 * Created by aperritano on 8/11/14.
 */
public class TextBoxZone extends TextBox implements KeyListener {

    private PFont font = null;
    private String textstring;

    public TextBoxZone(String id, int x, int y, int width, int height) {
        super(id, x, y, width, height);
        this.setTextstring(textstring);
    }

    public TextBoxZone(String id, String content) {
        super(content, id);
    }

    public TextBoxZone(String content, String name, int x, int y, int width, int height) {
        super(content, name, x, y, width, height);
        font = applet.createFont("HelveticaNeue-Bold", 23);

    }

    public void drawHandle(Zone zone) {
        fill(0);
    }

    @Override
    public void draw() {
        pushStyle();
        //set background color
        //fill(255);
        //else
        //     noFill();
        //set frame color
        //draw background/frame rectangle
        //fill(0);
        //rect( 0, 0, width, height);
        //draw text
        //fill(2);
        textFont(font, 30);
        //textSize(27);
        text(getContent(), 0, 0, width + 100, height + 100);
        popStyle();
   }

    public String getTextstring() {
        return textstring;
    }

    public void setTextstring(String textstring) {
        this.textstring = textstring;
    }

}
