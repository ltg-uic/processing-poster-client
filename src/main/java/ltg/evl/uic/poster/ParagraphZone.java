package ltg.evl.uic.poster;

import vialab.SMT.Zone;


/**
 * Created by aperritano on 11/2/14.
 */
public class ParagraphZone extends Zone {
    String content;
    float  winWidth;
    public ParagraphZone( String name, int x, int y, int width, int height){
        super(name, x, y, width, height);
        applet.registerMethod("keyEvent", this);
        content = new String( "Hellollll!");



    }

    @Override
    public void draw(){
        pushStyle();
        fill( 10, 10, 10, 80);
        strokeWeight(3);
        stroke(200, 120, 120, 150);
        rect(0, 0, getWidth(), getHeight());
        drawText(content);
        popStyle();
    }

    public void drawText(String text){
        pushStyle();
        fill( 255, 255, 255, 255);
        textAlign(CENTER);
        textMode(SHAPE);


        textSize(Math.round(dimension.height));
        float halfAscent = textAscent() / 2;
        float halfDescent = textDescent() / 2;


        text( text,
                getWidth()/2,
                halfDimension.height + halfAscent - halfDescent);


        System.out.println("FLOAT TEXT FLOAT " + ((int)textWidth(text)) );
        System.out.println("TEXT A " + textAscent() );
        System.out.println("TEXT D " + textDescent() );
        System.out.println("Half W " + halfDimension.width );
        System.out.println("Half H " + halfDimension.height );
        System.out.println("WINDOW H " + getHeight());
        System.out.println("WINDOW W " + getWidth());


        popStyle();
    }

    @Override
    public void touch(){
        rst();
    }

    @Override
    public void keyEvent(processing.event.KeyEvent event) {

        if( event.getAction() == processing.event.KeyEvent.PRESS ) {
            if( event.getKey() == '\b' ) {
                content = content.substring(0,content.length() - 1);
            } else {
                content += String.format( "%s", String.valueOf(event.getKey()));
            }


           setWidth(getWidth()+10);

            System.out.println("KEY " + String.valueOf(event.getKey()));
        }

    }




}
