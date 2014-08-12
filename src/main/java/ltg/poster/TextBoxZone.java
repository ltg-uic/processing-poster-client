package ltg.poster;

import vialab.SMT.SwipeKeyboard;
import vialab.SMT.Zone;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by aperritano on 8/11/14.
 */
public class TextBoxZone extends Zone implements KeyListener {

    private String textstring;
    private SwipeKeyboard keyboard;

    public TextBoxZone(String id, int x, int y, int width, int height, String textstring) {
        super(id, x, y, width, height);
        this.setTextstring(textstring);
        keyboard = new SwipeKeyboard();
        keyboard.setLocation( x, y+200);
        keyboard.addKeyListener(this);
        keyboard.setVisible(true);
        keyboard.setPickable(true);

    }

    public void draw() {
       fill(79, 129, 189);
       stroke(255);
       rect( 0, 0, dimension.width, dimension.height);
       //draw text
       fill( 255);
       textAlign( RIGHT, CENTER);
       textSize( 15);
       text(getTextstring(), dimension.width/2,dimension.height/2);
   }

    public void touchDown() {
        keyboard.setVisible(true);
        keyboard.setPickable(true);
    }

    public void keyPressed(KeyEvent e) {
        System.out.println( "KEY PRESSED: " + e.getKeyChar());

        char key = e.getKeyChar();
        switch( key ){
            case '\b': //backspace
                if( ! getTextstring().isEmpty())
                    setTextstring(getTextstring().substring(
                            0, getTextstring().length() - 1));
                break;
            case '\n': //enter
                if( getTextstring().isEmpty()) break;
                keyboard.setVisible( false);
                keyboard.setPickable( false);
                setTextstring(":: Access Granted ::");
                break;
            case (char) 65535: //unknown keys
                break;
            default: //any other keys
                setTextstring(textstring += key);
        }
    }

    public String getTextstring() {
        return textstring;
    }

    public void setTextstring(String textstring) {
        this.textstring = textstring;
    }

    public SwipeKeyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(SwipeKeyboard keyboard) {
        this.keyboard = keyboard;
    }
}
