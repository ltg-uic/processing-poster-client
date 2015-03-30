package ltg.evl.uic.poster.widgets;

import processing.core.PImage;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 3/27/15.
 */
public class ZoneHelper {

    public static PImage deleteImage;
    private static ZoneHelper ourInstance = new ZoneHelper();
    public int blueOutline;
    public int redOutline;

    private ZoneHelper() {
        deleteImage = SMT.getApplet().loadImage("delete_button.png");
        blueOutline = SMT.getApplet().color(29, 128, 240);
        redOutline = SMT.getApplet().color(197, 0, 6);
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
