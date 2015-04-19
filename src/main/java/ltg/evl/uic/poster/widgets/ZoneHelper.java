package ltg.evl.uic.poster.widgets;

import com.google.common.io.Resources;
import ltg.evl.uic.poster.widgets.buttons.DeleteButton;
import ltg.evl.uic.poster.widgets.buttons.DeleteButtonBuilder;
import ltg.evl.uic.poster.widgets.buttons.DeleteButtonListener;
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

    public static final int ROUND_CORNER = 10;
    public static final String WHICH_CLASS_ARE_YOU_IN = "Select a Class";
    public static final String WHICH_GROUP_ARE_YOU_IN = "Select a Group";
    public static final String SELECT_A_POSTER = "Select a Poster";
    public static final int GRID_SPACER = 15;
    public static final int CLASS_BUTTON_SIZE = 150;
    public static final int BUTTON_WIDTH = 200;
    public static final int BUTTON_HEIGHT = 100;
    public static final int LOGOUT_BUTTON_HEIGHT = 75;
    public static final int LOGOUT_BUTTON_WIDTH = 150;
    public static final int POSTER_BUTTON_WIDTH = 300;
    public static final int POSTER_BUTTON_HEIGHT = 125;
    public static final String LOGOUT = "Logout";
    public static final String REMOVE = "Remove";
    public static final String EDIT = "Editing";
    public static final String PRESENT = "Presenting";
    public static PImage deleteImage;
    public static PFont helveticaNeue18Font;
    public static PFont helveticaNeue48Font;
    public static Font helveticaNeue20JavaFont = new Font("HelveticaNeue", Font.BOLD, 20);
    public static PImage scaleImage;
    public static PFont helveticaNeue48BoldFont;
    public static int[] colors = {SMT.getApplet().color(126, 87, 194), SMT.getApplet()
                                                                          .color(92, 107, 192), SMT.getApplet()
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
            .color(189, 189, 189), SMT.getApplet().color(122, 186, 58)};
    public static int greenColor = SMT.getApplet().color(122, 186, 58);
    public static int blueOutline = SMT.getApplet().color(29, 128, 240);
    public static int redOutline = SMT.getApplet().color(238, 43, 41);
    public static int whiteOutline = SMT.getApplet().color(255, 255, 255);
    public static int greyOutline = SMT.getApplet().color(224, 224, 224);
    public static int darkGreenColor = SMT.getApplet().color(67, 160, 71);
    public static int orangeColor = SMT.getApplet().color(251, 140, 0);
    private static ZoneHelper ourInstance = new ZoneHelper();

    private ZoneHelper() {
        //fuck you processing
        helveticaNeue18Font = SMT.getApplet().loadFont(
                Resources.getResource("HelveticaNeue-18.vlw").getPath().replaceAll("%20", " "));
        helveticaNeue48Font = SMT.getApplet().loadFont(
                Resources.getResource("HelveticaNeue-48.vlw").getPath().replaceAll("%20", " "));
        helveticaNeue48BoldFont = SMT.getApplet().loadFont(
                Resources.getResource("HelveticaNeue-Bold-48.vlw").getPath().replaceAll("%20", " "));
        deleteImage = SMT.getApplet().loadImage("delete_button_transparent.png");
        scaleImage = SMT.getApplet().loadImage("scale_button_transparent.png");

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


    public static Dimension calcGrid(int rows, int cols, int spacer, int zoneWidth, int zoneHeight) {
        int x = spacer;
        int y = spacer;

        //cols
        for (int i = 0; i < cols; i++) {
            x = x + zoneWidth + spacer;
        }

        for (int i = 0; i < rows; i++) {
            y = y + zoneHeight + spacer;
        }

        return new Dimension(x, y);
    }

    public static Dimension grid(int x, int y, int width, int xSpace, int ySpace, int zoneWidth, int zoneHeight,
                                 int numElements) {
        int currentX = x;
        int currentY = y;

        int rowHeight = 0;

        for (int i = 0; i < zoneHeight; i++) {
            if (currentX + zoneWidth > x + width) {
                currentX = x;
                currentY += rowHeight + ySpace;
                rowHeight = 0;
            }

            currentX += zoneWidth + xSpace;
            rowHeight = Math.max(rowHeight, zoneHeight);
        }


        return new Dimension(currentX, currentY);
    }

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        float width_ratio = new Float(boundary.width).floatValue() / new Float(imgSize.width).floatValue();
        float height_ratio = new Float(boundary.height).floatValue() / new Float(imgSize.height).floatValue();
        float new_width;
        float new_height;

        float scale = Math.min(width_ratio, height_ratio);

        new_width = scale * new Float(imgSize.width).floatValue();
        new_height = scale * new Float(imgSize.height).floatValue();

        if ((imgSize.getHeight() >= new_height) || (imgSize.getWidth() >= new_width)) {
            return imgSize;
        }

        return new Dimension((int) new_width, (int) new_height);
    }

    public int randomColor() {
        return colors[random(0, colors.length - 1)];
    }

    public void addDeleteButton(Zone zone) {

        double buttonSize = SMT.getApplet().getHeight() * .03;
        double adjustedButtonSize = (buttonSize / 2.0);
        int x = (int) (zone.getWidth() - adjustedButtonSize) - 2;
        int y = (int) (2 - adjustedButtonSize);

//        System.out.println(
//                "delete button: " + buttonSize + " adjust button size: " + adjustedButtonSize + " x: " + x + " y: " + y);

        DeleteButton deleteButton = new DeleteButtonBuilder().setName(DeleteButton.DELETE_NAME)
                                                             .setImage(deleteImage)
                                                             .setX(x)
                                                             .setY(y)
                                                             .setWidth((int) buttonSize)
                                                             .setHeight((int) buttonSize).createDeleteButton();
        deleteButton.addDeleteListener((DeleteButtonListener) zone);
        deleteButton.setVisible(false);
        zone.add(deleteButton);
    }

}
