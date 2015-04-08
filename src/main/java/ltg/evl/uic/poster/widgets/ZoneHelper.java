package ltg.evl.uic.poster.widgets;

import com.google.common.io.Resources;
import processing.core.PFont;
import processing.core.PImage;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by aperritano on 3/27/15.
 */
public class ZoneHelper {

    public static PImage deleteImage;
    public static PFont helveticaNeue18Font;
    public static Font helveticaNeue18JavaFont = new Font("HelveticaNeue", Font.BOLD, 20);
    private static ZoneHelper ourInstance = new ZoneHelper();
    private final PImage scaleImage;
    public int greyOutline;
    public int blueOutline;
    public int redOutline;
    public int whiteOutline;
    public int[] colors = {SMT.getApplet().color(126, 87, 194), SMT.getApplet().color(92, 107, 192), SMT.getApplet()
                                                                                                        .color(66, 165,
                                                                                                               245), SMT
            .getApplet()
            .color(41, 182, 246), SMT.getApplet().color(38, 198, 218), SMT.getApplet()
                                                                          .color(38, 166, 154), SMT.getApplet()
                                                                                                   .color(102, 187,
                                                                                                          106), SMT.getApplet()
                                                                                                                   .color(156,
                                                                                                                          204,
                                                                                                                          101), SMT
            .getApplet()
            .color(212, 167, 38), SMT
            .getApplet()
            .color(189, 189, 189), SMT.getApplet().color(120, 144, 156)};

    private ZoneHelper() {
        //fuck you processing
        helveticaNeue18Font = SMT.getApplet().loadFont(
                Resources.getResource("HelveticaNeue-18.vlw").getPath().replaceAll("%20", " "));
        deleteImage = SMT.getApplet().loadImage("delete_button_transparent.png");
        scaleImage = SMT.getApplet().loadImage("scale_button_transparent.png");
        blueOutline = SMT.getApplet().color(29, 128, 240);
        redOutline = SMT.getApplet().color(238, 43, 41);
        whiteOutline = SMT.getApplet().color(255, 255, 255);
        greyOutline = SMT.getApplet().color(224, 224, 224);
    }

    public static ZoneHelper getInstance() {
        return ourInstance;
    }

    public static BufferedImage renderTextToImage(Font font, Color textColor, String text, int width) {
        Hashtable map = new Hashtable();

        map.put(TextAttribute.FAMILY, font.getName());
        map.put(TextAttribute.SIZE, new Float(30.0));

        //map.put(TextAttribute.FONT, font);
        AttributedString attributedString = new AttributedString(text, map);
        AttributedCharacterIterator paragraph = attributedString.getIterator();

        FontRenderContext frc = new FontRenderContext(null, false, false);
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        float drawPosY = 0;

        //First time around, just determine the height
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(width);

            // Move it down
            drawPosY += layout.getAscent() + layout.getDescent() + layout.getLeading();
        }

        BufferedImage image = createCompatibleImage(width, (int) drawPosY);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
//        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                   RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
               RenderingHints.VALUE_RENDER_QUALITY);

        graphics.setRenderingHints(rh);

        drawPosY = 0;
        lineMeasurer.setPosition(paragraphStart);
        //graphics.setBackground(Color.tr);
        graphics.setColor(textColor);
        while (lineMeasurer.getPosition() < paragraphEnd) {
            TextLayout layout = lineMeasurer.nextLayout(width);

            // Move y-coordinate by the ascent of the layout.
            drawPosY += layout.getAscent();

         /* Compute pen x position.  If the paragraph is
            right-to-left, we want to align the TextLayouts
            to the right edge of the panel.
          */
            float drawPosX;
            if (layout.isLeftToRight()) {
                drawPosX = 0;
            } else {
                drawPosX = width - layout.getAdvance();
            }

            // Draw the TextLayout at (drawPosX, drawPosY).
            layout.draw(graphics, drawPosX, drawPosY);

            // Move y-coordinate in preparation for next layout.
            drawPosY += layout.getDescent() + layout.getLeading();
        }

        graphics.dispose();
        return image;
    }

    /**
     * Creates an image compatible with the current display
     *
     * @return A BufferedImage with the appropriate color model
     */
    public static BufferedImage createCompatibleImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public static int random(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public DeleteButton getDeleteButton(Zone zone) {

        double buttonSize = SMT.getApplet().getHeight() * .03;
        double adjustedButtonSize = (buttonSize / 2.0);
        int x = (int) (zone.getWidth() - adjustedButtonSize) - 2;
        int y = (int) (2 - adjustedButtonSize);

        System.out.println(
                "delete button: " + buttonSize + " adjust button size: " + adjustedButtonSize + " x: " + x + " y: " + y);

        DeleteButton deleteButton = new DeleteButtonBuilder().setName(DeleteButton.DELETE_NAME)
                                                             .setImage(deleteImage)
                                                             .setX(x)
                                                             .setY(y)
                                                             .setWidth((int) buttonSize)
                                                             .setHeight((int) buttonSize).createDeleteButton();
        deleteButton.addDeleteListener((DeleteButtonListener) zone);
        return deleteButton;
    }

    public ScaleButton getScaleButton(Zone zone) {

        double buttonSize = SMT.getApplet().getHeight() * .03;
        double adjustedButtonSize = (buttonSize / 2.0);
        int x = (int) (zone.getWidth() - adjustedButtonSize) - 2;
        int y = (int) (zone.getHeight() - adjustedButtonSize) - 2;

        System.out.println(
                "SCALE BUTTON: " + buttonSize + " ADJUST Size" + adjustedButtonSize + " x: " + x + " y: " + y);

        ScaleButton scaleButton = new ScaleButtonBuilder().setUUID(ScaleButton.SCALE_NAME)
                                                          .setImage(scaleImage)
                                                          .setX(x)
                                                          .setY(y)
                                                          .setWidth((int) buttonSize)
                                                          .setHeight((int) buttonSize).createScaleButton();
        scaleButton.addDeleteListener((DeleteButtonListener) zone);
        return scaleButton;
    }



}