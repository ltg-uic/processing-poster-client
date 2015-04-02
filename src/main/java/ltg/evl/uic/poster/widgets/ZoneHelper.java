package ltg.evl.uic.poster.widgets;

import processing.core.PFont;
import processing.core.PImage;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 3/27/15.
 */
public class ZoneHelper {

    public static PImage deleteImage;
    private static ZoneHelper ourInstance = new ZoneHelper();
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
            .color(212, 225, 87), SMT.getApplet().color(255, 238, 88), SMT.getApplet()
                                                                          .color(255, 202, 40), SMT.getApplet()
                                                                                                   .color(255, 167,
                                                                                                          38), SMT.getApplet()
                                                                                                                  .color(255,
                                                                                                                         112,
                                                                                                                         67), SMT
            .getApplet()
            .color(189, 189, 189), SMT.getApplet().color(120, 144, 156)};

    public PFont controlButtonFont = SMT.getApplet().createFont("HelveticaNeue-Bold", 18);

    private ZoneHelper() {
        deleteImage = SMT.getApplet().loadImage("delete_button_transparent.png");
        blueOutline = SMT.getApplet().color(29, 128, 240);
        redOutline = SMT.getApplet().color(238, 43, 41);
        whiteOutline = SMT.getApplet().color(255, 255, 255);
        greyOutline = SMT.getApplet().color(224, 224, 224);
    }

    public static ZoneHelper getInstance() {
        return ourInstance;
    }

    public DeleteButton getDeleteButton(Zone zone) {

        double buttonSize = SMT.getApplet().getHeight() * .0375;
        double adjustedButtonSize = (buttonSize / 2.0);
        int x = (int) (zone.getWidth() - adjustedButtonSize) - 2;
        int y = (int) (2 - adjustedButtonSize);

        System.out.println("button: " + buttonSize + " adjust " + adjustedButtonSize + "x " + x + "y " + y);

        DeleteButton deleteButton = new DeleteButtonBuilder().setName(DeleteButton.DELETE_NAME)
                                                             .setImage(deleteImage)
                                                             .setX(x)
                                                             .setY(y)
                                                             .setWidth((int) buttonSize)
                                                             .setHeight((int) buttonSize).createDeleteButton();
        deleteButton.addListener((DeleteButtonListener) zone);
        return deleteButton;
    }


}
