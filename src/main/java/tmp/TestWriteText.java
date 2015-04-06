package tmp;


import com.google.api.client.util.Lists;
import javaxt.io.Image;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by aperritano on 4/4/15.
 */
public class TestWriteText {

    private static final
    Hashtable<TextAttribute, Object> map =
            new Hashtable<TextAttribute, Object>();
    // The LineBreakMeasurer used to line-break the paragraph.
    private static LineBreakMeasurer lineMeasurer;
    // index of the first character in the paragraph.
    private static int paragraphStart;
    // index of the first character after the end of the paragraph.
    private static int paragraphEnd;
    private static AttributedString vanGogh = new AttributedString(
            "Many people believe that Vincent van Gogh painted his best works " +
                    "during the two-year period he spent in Provence. Here is where he " +
                    "painted The Starry Night--which some consider to be his greatest " +
                    "work of all. However, as his artistic brilliance reached new " +
                    "heights in Provence, his physical and mental health plummeted. ",
            map);

    static {
        map.put(TextAttribute.FAMILY, "HelveticaNeue");
        map.put(TextAttribute.SIZE, new Float(48.0));
    }

    public static void main(String[] args) {
        String text =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                        "Maecenas porttitor risus vitae urna hendrerit ac condimentum " +
                        "odio tincidunt. Donec porttitor felis quis nulla aliquet " +
                        "lobortis. Suspendisse mattis sapien ut metus congue tincidunt. " +
                        "Quisque gravida, augue sed congue tempor, tortor augue rhoncus " +
                        "leo, eget luctus nisl risus id erat. Nunc tempor pretium gravida.";

        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        Image jxt = null;

        Font helveticaNeue = new Font("HelveticaNeue", Font.PLAIN, 16);


        // Set break width to width of Component.
        float breakWidth = 250;
        float drawPosY = 0;


        FontMetrics metrics = g2d.getFontMetrics(helveticaNeue);
        int ascent = metrics.getAscent();
        int decent = metrics.getDescent();
        int hgt = metrics.getHeight();
        int height = hgt;
        ArrayList<String> strings = wrap(text, 100);
        int adv = metrics.stringWidth(strings.get(0));
        jxt = new Image(adv, (strings.size() * hgt));


        for (String line : strings) {
            System.out.println("height: " + height);
            System.out.println("lines:  " + line);

            jxt.addText(line, 0, height + ascent + decent, helveticaNeue, 0, 0, 0);


            height = height + hgt;


        }

        Image jxt2 = new Image(renderTextToImage(helveticaNeue, new Color(0, 0, 0), text, adv));

        jxt2.saveAs("/Users/aperritano/dev/research/poster/processing-poster-client/src/main/java/tmp/NEW_TEXT.png");
    }

    public static BufferedImage renderTextToImage(Font font, Color textColor, String text, int width) {
        Hashtable map = new Hashtable();
        map.put(TextAttribute.FONT, font);
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
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawPosY = 0;
        lineMeasurer.setPosition(paragraphStart);
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
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                                                 .getDefaultScreenDevice().getDefaultConfiguration();
        return configuration.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

    public static ArrayList<String> wrap(String str, int wrapLength) {

        ArrayList<String> lines = Lists.newArrayList();
        int offset = 0;
        StringBuilder resultBuilder = new StringBuilder();

        while ((str.length() - offset) > wrapLength) {
            if (str.charAt(offset) == ' ') {
                offset++;
                continue;
            }

            int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);
            // if the next string with length maxLength doesn't contain ' '
            if (spaceToWrapAt < offset) {
                spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
                // if no more ' '
                if (spaceToWrapAt < 0) {
                    break;
                }
            }

            lines.add(str.substring(offset, spaceToWrapAt));
            lines.add("\n");
            offset = spaceToWrapAt + 1;
        }

        lines.add(str.substring(offset));
        return lines;
    }
}
