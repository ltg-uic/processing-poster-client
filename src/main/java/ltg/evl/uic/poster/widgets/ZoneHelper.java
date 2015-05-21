package ltg.evl.uic.poster.widgets;

import com.google.api.client.util.Lists;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.util.ModelHelper;
import ltg.evl.uic.poster.widgets.button.DeleteButton;
import ltg.evl.uic.poster.widgets.button.DeleteButtonBuilder;
import ltg.evl.uic.poster.widgets.button.DeleteButtonListener;
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
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
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
    public static final int BUTTON_WIDTH = 300;
    public static final int BUTTON_HEIGHT = 100;
    public static final int LOGOUT_BUTTON_HEIGHT = 100;
    public static final int LOGOUT_BUTTON_WIDTH = 250;
    public static final int POSTER_BUTTON_WIDTH = 300;
    public static final int POSTER_BUTTON_HEIGHT = 125;
    public static final String LOGOUT = "LOGOUT";
    public static final String REMOVE = "REMOVE";
    public static final String EDIT = "EDIT";
    public static final String PRESENT = "PRESENT";
    public static final String SAVE = "SAVE";
    public static final int REFRESH_BUTTON_WIDTH = 100;
    public static final int CONTROL_BUTTON_FONT_SIZE = 34;
    public static PImage deleteImage;
    public static PFont helveticaNeue18Font;
    public static PFont helveticaNeue48Font;
    public static Font helveticaNeue20JavaFont = new Font("HelveticaNeue", Font.BOLD, 20);
    public static PImage scaleImage;
    public static PImage refreshImage;
    public static int[] colors = {SMT.getApplet().color(93, 64, 55), SMT.getApplet()
                                                                        .color(69, 90, 100), SMT.getApplet()
                                                                                                .color(221, 44, 0), SMT
            .getApplet()
            .color(255, 109, 0), SMT.getApplet().color(56, 142, 60), SMT.getApplet()
                                                                        .color(255, 160, 0), SMT.getApplet()
                                                                                                   .color(102, 187,
                                                                                                          106), SMT.getApplet()
                                                                                                                   .color(156,
                                                                                                                          204,
                                                                                                                          101), SMT
            .getApplet()
            .color(212, 167, 38), SMT.getApplet().color(122, 186, 58)};


    public static int darkTeal = SMT.getApplet().color(96, 125, 139);
    public static int greenColor = SMT.getApplet().color(38, 166, 154);
    public static int darkBlueOutline = SMT.getApplet().color(1, 87, 155);
    public static int lightGreyBackground = SMT.getApplet().color(238, 238, 238);
    public static int blueOutline = SMT.getApplet().color(29, 128, 240);
    public static int purpleOutline = SMT.getApplet().color(149, 117, 205);
    public static int redOutline = SMT.getApplet().color(238, 43, 41);
    public static int whiteOutline = SMT.getApplet().color(255, 255, 255);
    public static int greyOutline = SMT.getApplet().color(224, 224, 224);
    public static int darkGreenColor = SMT.getApplet().color(67, 160, 71);
    public static int orangeColor = SMT.getApplet().color(251, 140, 0);
    private static ZoneHelper ourInstance = new ZoneHelper();
    public int maxY;
    public int maxX;


    private ZoneHelper() {
        //fuck you processing
        helveticaNeue18Font = SMT.getApplet().createFont("HelveticaNeue", 18);
        helveticaNeue48Font = SMT.getApplet().createFont("HelveticaNeue", 48);
        deleteImage = SMT.getApplet().loadImage("delete_button_transparent.png");
        //scaleImage = SMT.getApplet().loadImage("scale_button_transparent.png");
        refreshImage = SMT.getApplet().loadImage("refresh_button_transparent.png");
    }

    public static ZoneHelper getInstance() {
        return ourInstance;
    }

    public static BufferedImage renderTextToImage(Font font, Color textColor, String text, int width) {
        Hashtable<TextAttribute, java.io.Serializable> map = new Hashtable<>();

        map.put(TextAttribute.FAMILY, font.getName());
        map.put(TextAttribute.SIZE, new Float(30.0));

        int offsetSize = text.length()/ 2;
        System.out.println("renderTextToImage: " + text);
        for (int i = 0; i < offsetSize; i++) {
            text = " " + text;
            System.out.println("renderTextToImage: " + i + " " + text);
        }
        System.out.println("renderTextToImage: " + text);


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



    public static ArrayList<Object> tokenize(String text, String language, String country){
        ArrayList<Object> sentences = Lists.newArrayList();
        Locale currentLocale = new Locale(language, country);
        BreakIterator sentenceIterator = BreakIterator.getSentenceInstance(currentLocale);
        sentenceIterator.setText(text);
        int boundary = sentenceIterator.first();
        int lastBoundary = 0;
        while (boundary != BreakIterator.DONE) {
            boundary = sentenceIterator.next();
            if(boundary != BreakIterator.DONE){
                sentences.add(text.substring(lastBoundary, boundary));
            }
            lastBoundary = boundary;
        }
        return sentences;
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

        return rand.nextInt((max - min) + 1) + min;
    }


    public static Dimension calcGrid(double size, double cols, int spacer, int zoneWidth, int zoneHeight) {

        double reminder;
        double rows;
        if (size <= cols) {
            cols = size;
            reminder = 1.0;
        } else {
            reminder = Math.ceil(size / cols);
        }

        rows = reminder;

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

    //here
    public static Dimension resizeImage(PImage image, int maxWidth, int maxHeight) {

        Dimension largestDimension = new Dimension(maxWidth, maxHeight);

        // Original size
        float imageWidth = new Float(image.width).floatValue();
        float imageHeight = new Float(image.height).floatValue();

        float aspectRatio = imageWidth / imageHeight;

        float w_aspect = imageWidth / (float) maxWidth;
        float h_aspect = imageHeight / (float) maxHeight;

        float scale = Math.min(1, Math.min(w_aspect, h_aspect));

        System.out.println("\nIn ZoneHelper");
        System.out.println("=============================");
        System.out.println("imageWidth:" + imageWidth );
        System.out.println("imageHeight:" + imageHeight );
        System.out.println("aspectRatio:" + aspectRatio );
        System.out.println("w_aspect:" + w_aspect );
        System.out.println("h_aspect:" + h_aspect);
        System.out.println("maxWidth:" + maxWidth );
        System.out.println("maxHeight:" + maxHeight );

        if (imageWidth > maxWidth || imageHeight > maxHeight) {
            if ((float) largestDimension.width / largestDimension.height > aspectRatio) {
                largestDimension.width = (int) Math.ceil(largestDimension.height * aspectRatio);
            } else {
                largestDimension.height = (int) Math.ceil(largestDimension.width / aspectRatio);
            }

            imageWidth = largestDimension.width;
            imageHeight = largestDimension.height;
        }

        System.out.println("largestDimension.width:" + largestDimension.width );
        System.out.println("largestDimension.heightt:" + largestDimension.height );

        return new Dimension(largestDimension.width, largestDimension.height);
    }

    public static Dimension resizeImageKRA(PImage image, int maxWidth, int maxHeight) {
        Dimension largestDimension = new Dimension(maxWidth, maxHeight);

        // Original size
        float imageWidth = new Float(image.width).floatValue();
        float imageHeight = new Float(image.height).floatValue();

        float aspectRatio = imageWidth / imageHeight;
        float w_aspect = imageWidth / (float) maxWidth;
        float h_aspect = imageHeight / (float) maxHeight;

        System.out.println("\nIn ZoneHelper KRA");
        System.out.println("=============================");
        System.out.println("imageWidth:" + imageWidth );
        System.out.println("imageHeight:" + imageHeight );
        System.out.println("aspectRatio:" + aspectRatio );
        System.out.println("maxWidth:" + maxWidth );
        System.out.println("maxHeight:" + maxHeight );

//        while ( largestDimension.width > maxWidth || largestDimension.height > maxHeight) {
//        }
        if (imageWidth > maxWidth) {
            System.out.println("Its too wide!");
            float imageHeightAdjusted = maxWidth * imageHeight / imageWidth;
            largestDimension = resizeImageKRAHelper(largestDimension, maxWidth, imageHeightAdjusted);
        } else if (imageHeight > maxHeight) {
            System.out.println("Its too tall!");
            float imageWidthAdjusted = maxHeight * imageWidth / imageHeight;
            largestDimension = resizeImageKRAHelper(largestDimension, imageWidthAdjusted, maxHeight);
        } else {
            System.out.println("All riiiight");
            largestDimension = resizeImageKRAHelper(largestDimension, imageWidth, imageHeight);
        }

        System.out.println("------------------------------\nChecking...");
        System.out.println("largestDimension.width:" + largestDimension.width);
        System.out.println("largestDimension.height:" + largestDimension.height);
        System.out.println("maxWidth:" + maxWidth );
        System.out.println("maxHeight:" + maxHeight );

        if (largestDimension.width > maxWidth && largestDimension.height > maxHeight ) {
            System.out.println("Both too large...do something about it!");
        } else if ( largestDimension.width > maxWidth ) {
            System.out.println("Its too wide!" + ((largestDimension.height / largestDimension.width*1.0) * maxWidth));
            largestDimension.height = (int)((largestDimension.height / largestDimension.width*1.0) * maxWidth);
            largestDimension.width = maxWidth;

        } else if ( largestDimension.height > maxHeight ) {
            System.out.println("Its too tall!" + ((largestDimension.width / largestDimension.height*1.0) * maxHeight));
            largestDimension.width = (int)((largestDimension.width / largestDimension.height*1.0) * maxHeight);
            largestDimension.height = maxHeight;
        }

        System.out.println("largestDimension.width:" + largestDimension.width);
        System.out.println("largestDimension.height:" + largestDimension.height);
        return new Dimension(largestDimension.width, largestDimension.height);
    }

    /**
     * Simple helper function to encapsulate repetitive code
     * */
    private static Dimension resizeImageKRAHelper(Dimension largestDimension, float imageWidth, float imageHeight) {
        if ( imageWidth > imageHeight ) {
            largestDimension.height = (int) (largestDimension.width * (imageHeight / imageWidth));
        }
        else if ( imageHeight > imageWidth ) {
            largestDimension.width = (int) (largestDimension.height * (imageWidth / imageHeight));
        }
        return new Dimension(largestDimension.width, largestDimension.height);
    }


    public static Dimension getScaledDimension(PImage imgSize, Dimension boundary) {

//        Compute two ratios (with floating point result):
//
//        input width divided by maximum allowed width
//        input height divided by maximum allowed height
//                Then,
//
//        if both ratios < 1.0, don't resize.
//        if one of the ratio > 1.0, scale down by that factor.
//        if both ratios > 1.0, scale down by the bigger of the two factors.
        float width_ratio = new Float(boundary.width).floatValue() / new Float(imgSize.width).floatValue();
        float height_ratio = new Float(boundary.height).floatValue() / new Float(imgSize.height).floatValue();
        float new_width;
        float new_height;


        float scale = Math.min(1, Math.min(width_ratio, height_ratio));


        new_width = Math.round(scale * new Float(imgSize.width).floatValue());
        new_height = scale * new Float(imgSize.height).floatValue();

//        if ((imgSize.getHeight() >= new_height) || (imgSize.getWidth() >= new_width)) {
//            return imgSize;
//        }

        return new Dimension((int) new_width, (int) new_height);
    }

    public static Image resizeImage(Image image, int width, int height, boolean max) {
        if (width < 0 && height > 0) {
            return resizeImageBy(image, height, false);
        } else if (width > 0 && height < 0) {
            return resizeImageBy(image, width, true);
        } else if (width < 0 && height < 0) {
            System.out.println("Setting the image size to (width, height) of: ("
                                       + width + ", " + height + ") effectively means \"do nothing\"... Returning original image");
            return image;
            //alternatively you can use System.err.println("");
            //or you could just ignore this case
        }
        int currentHeight = image.getHeight(null);
        int currentWidth = image.getWidth(null);
        int expectedWidth = (height * currentWidth) / currentHeight;
        //Size will be set to the height
        //unless the expectedWidth is greater than the width and the constraint is maximum
        //or the expectedWidth is less than the width and the constraint is minimum
        int size = height;
        if (max && expectedWidth > width) {
            size = width;
        } else if (!max && expectedWidth < width) {
            size = width;
        }
        return resizeImageBy(image, size, (size == width));
    }

    /**
     * Resizes the given image using Image.SCALE_SMOOTH.
     *
     * @param image    the image to be resized
     * @param size     the size to resize the width/height by (see setWidth)
     * @param setWidth whether the size applies to the height or to the width
     * @return the resized image
     */
    public static Image resizeImageBy(Image image, int size, boolean setWidth) {
        if (setWidth) {
            return image.getScaledInstance(size, -1, Image.SCALE_SMOOTH);
        } else {
            return image.getScaledInstance(-1, size, Image.SCALE_SMOOTH);
        }
    }

    public static void computeCoefficient(PosterItem posterItem) {


        double xn = Math.abs((posterItem.getX() * 1.0) / ZoneHelper.getInstance().maxX);
        posterItem.setXn(xn);
        double yn = Math.abs((posterItem.getY() * 1.0) / ZoneHelper.getInstance().maxY);
        posterItem.setYn(yn);
        double wn = Math.abs((posterItem.getWidth() * 1.0) / ZoneHelper.getInstance().maxX);
        posterItem.setWn(wn);
        double hn = Math.abs((posterItem.getHeight() * 1.0) / ZoneHelper.getInstance().maxY);
        posterItem.setHn(hn);

    }

    public static PosterItem pictureZoneToPosterItem(PictureZone pictureZone) {

        Optional<PosterItem> posterItemOptional = Optional.fromNullable(
                PosterDataModel.helper().findPosterItemWithPosterItemUuid(pictureZone.getName()));

        PosterItem modPosteritem = null;

        if (posterItemOptional.isPresent()) {
            modPosteritem = posterItemOptional.get();


            Dimension screenSize = pictureZone.getScreenSize();
            System.out.println("screenSize " + screenSize.toString());

            Dimension size = pictureZone.getSize();
            System.out.println("Size " + size.toString());


            modPosteritem.setLastEdited(ModelHelper.getTimestampMilli());
            modPosteritem.setXn((pictureZone.getX() * 1.0) / SMT.getApplet().displayWidth);
            modPosteritem.setYn((pictureZone.getY() * 1.0) / SMT.getApplet().displayHeight);
            modPosteritem.setWn((pictureZone.getWidth() * 1.0) / SMT.getApplet().displayWidth);
            modPosteritem.setHn((pictureZone.getHeight() * 1.0) / SMT.getApplet().displayHeight);

            return modPosteritem;
        }
        return null;
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

        DeleteButton deleteButton = new DeleteButtonBuilder().setUUID(DeleteButton.DELETE_NAME)
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
