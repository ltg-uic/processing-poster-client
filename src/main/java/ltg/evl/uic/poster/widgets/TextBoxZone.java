package ltg.evl.uic.poster.widgets;

import com.google.api.client.util.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.StringList;
import vialab.SMT.SMT;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

import java.util.List;

public class TextBoxZone extends Zone implements DeleteButtonListener {


    int fontSize;
    int lineSpacing = 1;
    private PImage img;
    private PFont font;
    private String content;
    private boolean hasLoadedText = false;
    private DeleteButton deleteButton;
    private boolean isDrawingOutline;
    private int outline = ZoneHelper.blueOutline;
    private StringList lines;
    private int textHeight;


    public TextBoxZone(String uuid, String content, int x, int y, int width, int height) {
        super(uuid, x, y, width, height);
        this.content = content;
        initText();


//
//        img.loadPixels();
//        for (int i = 0; i < img.pixels.length; i++) {
//            img.pixels[i] = color(0, 90, 102, i % img.width * 2);
//        }
//        img.updatePixels();

//        img = SMT.getApplet().createImage(100, 100, RGB);
//        img.loadPixels();
//        int red = color(255, 0, 0);
//        for (int i = 0; x < 100; x++) {
//            for (int j = 0; y < 100; y++) {
//                img.set(i, j, red);
//            }
//        }
//        img.updatePixels();
        // initText(text);

    }

    public TextBoxZone(PosterItem posterItem) {
        super(posterItem.getUuid(), posterItem.getX(), posterItem.getY(),
              posterItem.getWidth(), posterItem.getHeight());

        this.content = posterItem.getContent();
        initText();
    }


    protected void initText() {
        this.font = SMT.getApplet().loadFont(Resources.getResource("Roboto-Light-48.vlw").getPath());
        this.fontSize = 23;
        textFont(font, fontSize);
    }

    @Override
    public boolean add(Zone zone) {

        if (zone instanceof DeleteButton) {
            deleteButton = (DeleteButton) zone;
        }

        return super.add(zone);
    }

    public void addListener(DeleteButtonListener deleteButtonListener) {
    }

    @Override
    public void touch() {
        super.touch();
        SMT.putZoneOnTop(this);
    }

    @Override
    public void touchDown(Touch touch) {
        this.isDrawingOutline = true;
        deleteButton.setDrawingOutline(true);
    }

    @Override
    public void touchUp(Touch touch) {
        this.isDrawingOutline = false;
        deleteButton.setDrawingOutline(false);
    }

    @Override
    public void deleteZone(Zone zone) {
        PosterDataModel.helper().removePosterItem(this.getName());
    }

    public void show(boolean isShowing) {
        if (!isShowing) {
            Zone[] children = getChildren();
            for (Zone zone : children) {
                if (zone.getName().equals(DeleteButton.DELETE_NAME)) {
                    DeleteButton db = (DeleteButton) zone;
                    db.setVisible(false);
                }
            }
        }
    }


    @Override
    public void draw() {
//        smooth(4);
//        stroke(outline);
//        strokeWeight(1);
//
//        fill(200);
//        smooth(4);

        if (isDrawingOutline) {
            stroke(outline);
            strokeWeight(4);
            smooth();

            //rect(0, 0, this.getWidth() + 1, this.getHeight() + 1, 10);
        }
        fill(200);
        rect(0, 0, getWidth(), getHeight(), 10);


        if (getContent() != null) {
            if (font != null) {
                textFont(font, fontSize);
            }
            textAlign(LEFT, CENTER);
            textSize(fontSize);
            fill(0);
            smooth(4);
            text(getContent(), 0, 0, getWidth() - 2, getHeight() - 2);
        }
    }

//    @Override
//    public void draw() {
//
//        textFont(font, fontSize);
//        textSize(fontSize);
//
//
//
//
//        //image(img, 0, 0, getWidth(), getHeight());
//
//        if( hasLoadedText == false ) {
//            lines = wordWrap(getContent(), getWidth());
//            textHeight = calculateTextHeight(lines.get(0), getWidth(), fontSize, lineSpacing);
//            hasLoadedText = true;
//        }
//
//
//
//        if (lines != null) {
//                strokeWeight(1);
//                fill(255);
//                rect(0, 0, getWidth(), textHeight * lines.size());
//                fill(0);
//
//                int ypos = 0;
//                for (String line : lines) {
//                    smooth();
//                    text(line,0, ypos, getWidth(), textHeight);
//                    ypos = ypos + textHeight;
//                }
//        }
//    }


    // this list contains strings that fit within maxWidth
//
    protected List<String> wordWrap2(String sentence, int maxWidth) throws NullPointerException {
        // Make an empty ArrayList
        List<String> a = Lists.newArrayList();
        float accumulatedWidth = 0;    // Accumulate width of chars
        int i = 0;      // Count through chars
        int rememberSpace = 0; // Remember where the last space was

        if (Strings.isNullOrEmpty(sentence) || sentence.trim().isEmpty()) {
            return Lists.newArrayList();
        } else {
            // As long as we are not at the end of the String
            while (i < sentence.length()) {
                // Current char
                String c = sentence.charAt(i) + "";
                System.out.println("NULL CHAR " + c);

                accumulatedWidth += SMT.getApplet().textWidth(c); // accumulate width
                // System.out.println("accumulatedWidth " + accumulatedWidth);

                if (WHITESPACE.equals(c)) {
                    rememberSpace = i; // Are we a blank space?
                }

                if (accumulatedWidth > maxWidth) {  // Have we reached the end of a line?
                    String sub = sentence.substring(0, rememberSpace); // Make a substring
                    // Chop off space at beginning
                    if (sub.length() > 0 && WHITESPACE.equals(sub.charAt(0))) {
                        sub = sub.substring(1, sub.length());
                    }
                    // Add substring to the list
                    a.add(sub);
                    // Reset everything
                    sentence = sentence.substring(rememberSpace, sentence.length());
                    i = 0;
                    accumulatedWidth = 0;
                } else {
                    i++;  // Keep going!
                }
            }


        }

        // Take care of the last remaining line
        if (sentence.length() > 0 && sentence.charAt(0) == ' ') sentence = sentence.substring(1, sentence.length());
        a.add(sentence);

        return a;
    }

    StringList wordWrap(String s, int maxWidth) {
        // Make an empty ArrayList
        StringList a = new StringList();
        float w = 0;    // Accumulate width of chars
        int i = 0;      // Count through chars
        int rememberSpace = 0; // Remember where the last space was
        // As long as we are not at the end of the String
        while (i < s.length()) {
            // Current char
            char c = s.charAt(i);
            w += textWidth(c); // accumulate width
            if (c == ' ') rememberSpace = i; // Are we a blank space?
            if (w > maxWidth) {  // Have we reached the end of a line?
                String sub = s.substring(0, rememberSpace); // Make a substring
                // Chop off space at beginning
                if (sub.length() > 0 && sub.charAt(0) == ' ') sub = sub.substring(1, sub.length());
                // Add substring to the list
                a.append(sub);
                // Reset everything
                s = s.substring(rememberSpace, s.length());
                i = 0;
                w = 0;
            } else {
                i++;  // Keep going!
            }
        }

        // Take care of the last remaining line
        if (s.length() > 0 && s.charAt(0) == ' ') s = s.substring(1, s.length());
        a.append(s);

        return a;
    }

    int calculateTextHeight(String string, int specificWidth, int fontSize, int lineSpacing) {
        String[] wordsArray;
        String tempString = "";
        int numLines = 0;
        float textHeight;

        wordsArray = PApplet.split(string, " ");

        for (String aWordsArray : wordsArray) {
            if (textWidth(tempString + aWordsArray) < specificWidth) {
                tempString += aWordsArray + " ";
            } else {
                tempString = aWordsArray + " ";
                numLines++;
            }
        }

        numLines++; //adds the last line

        textHeight = numLines * (textDescent() + textAscent() + lineSpacing);
        return (PApplet.round(textHeight));
    }

    //    @Override
//    public void draw() {
//        if (isDrawingOutline) {
//            stroke(outline);
//            strokeWeight(4);
//            smooth();
//            rect(0, 0, this.getWidth(), this.getHeight());
//        }
//
//        pushStyle();
//        //set background color
//        //fill(255);
//        //else
//        //     noFill();
//        //set frame color
//        //draw background/frame rectangle
//        fill(200);
//        rect(0, 0, getWidth(), getHeight());
//        //draw text
//        //fill(2);
//        textFont(font, 30);
//        //textSize(27);
//        text(getContent(), 0, 0, getWidth() + 100, getHeight() + 100);
//        popStyle();
//    }
//
//    @Override
//     public void touchDown(Touch touch) {
//        this.isDrawingOutline = true;
//        deleteButton.setDrawingOutline(isDrawingOutline);
//    }
//
//    @Override
//    public void touchUp(Touch touch) {
//        this.isDrawingOutline = false;
//        deleteButton.setDrawingOutline(isDrawingOutline);
//    }

    public void incFont() {
        //fontSize++;
//        setWidth(getWidth()+25);
//
//        List<String> lines = wordWrap(getContent(), getWidth());
//        int t = calculateTextHeight(lines.get(0), getWidth(), fontSize, lineSpacing);
//        System.out.println(
//                "Font is size: " + fontSize + " textHeight: " + t + " actual width: " + getWidth() + " actual height: " + getHeight());
//
//        System.out.println("inc" + getWidth());
    }

    public void decFont() {
        fontSize--;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
