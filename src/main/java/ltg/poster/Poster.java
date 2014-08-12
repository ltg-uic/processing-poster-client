package ltg.poster;

import com.phloc.commons.charset.CCharset;
import com.phloc.css.ECSSVersion;
import com.phloc.css.decl.CascadingStyleSheet;
import com.phloc.css.reader.CSSReader;
import ltg.commons.MessageListener;
import ltg.commons.SimpleMQTTClient;
import processing.core.PApplet;
import processing.core.PFont;
import vialab.SMT.*;

import java.io.File;


public class Poster extends PApplet {

    public static CascadingStyleSheet aCSS;
    private static SimpleMQTTClient sc;
    int buttonWidth = 100;
    int buttonHeight = buttonWidth;

    int buttonStartX = 20;
    int buttonStartY = buttonStartX;

    int whiteColor = color(255);
    int greenButtonColor = color(35, 147, 70);
    int yellowButtonColor = color(244, 208, 63);
    private ControlButtonZone editButton;
    private ControlButtonZone presentButton;
    private NewTextButton newTextButton ;

    public static void main(String args[]) {


        sc = new SimpleMQTTClient("ltg.evl.uic.edu", "test-bot");
        sc.subscribe("quakes", new MessageListener() {
            @Override
            public void processMessage(String message) {


                SMT.add(new TextBoxZone("quake", Util.randInt(0,500), Util.randInt(0,500) ,300,35, message));
                System.out.println("Received " + message );
            }
        });

        PApplet.main(new String[]{"ltg.poster.Poster"});
    }



    public void setup() {

        size(1024, 576, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);
        SMT.debug = true;
        SMT.setTouchDraw( TouchDraw.SMOOTH);
        PFont controlButtonFont = createFont("HelveticaNeue-Bold", 23);


        //create mode buttons
        presentButton = new ControlButtonZone("PresentButton", buttonStartX, buttonStartY, buttonWidth, buttonHeight, "PRESENT", greenButtonColor, yellowButtonColor, controlButtonFont);
        editButton = new ControlButtonZone("EditButton", buttonStartX, presentButton.getY() + presentButton.getHeight() + presentButton.getY(), buttonWidth, buttonHeight, "EDIT", greenButtonColor, yellowButtonColor, controlButtonFont);
        newTextButton = new NewTextButton("TextButton", loadImage("document6.png"), buttonStartX, editButton.getY() + editButton.getHeight() + editButton.getY() + 5,buttonWidth-5, buttonHeight-5 );

        SMT.add(presentButton);
        SMT.add(editButton);
        SMT.add(newTextButton);

    }

    public void draw() {
        background(255);
        fill(0);
        text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, 10);
    }

    public void drawTexting(TextZone zone) {
        fill(100, 150, 60, 200);

    }
    public void pressPresentButton(ControlButtonZone zone) {
        zone.pressed();
        editButton.unpressed();
        println("Button Pressed");
    }

    public void pressEditButton(ControlButtonZone zone) {
        zone.pressed();
        presentButton.unpressed();
        println("Button Pressed");
    }

    public void pressTextButton(NewTextButton zone) {

        int x = new Float(random(10, 500)).intValue();
        int y = new Float(random(10, 500)).intValue();

        TextBoxZone tbx = new TextBoxZone("Texting", x, y, 300, 35, "");
        SMT.add(tbx,tbx.getKeyboard() );

    }

}
