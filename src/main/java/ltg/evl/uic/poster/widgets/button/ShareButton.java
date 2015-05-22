package ltg.evl.uic.poster.widgets.button;

import ltg.evl.uic.poster.json.mongo.PosterGrab;
import ltg.evl.uic.poster.widgets.DialogZoneController;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import org.apache.log4j.Logger;
import processing.core.PFont;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 4/17/15.
 */
public class ShareButton extends Zone {

    public static final int SPACING = 4;
    protected int pressedButtonColor;
    protected int unpressedButtonColor;
    protected int outline = ZoneHelper.greyOutline;
    protected PFont font;
    protected String text;
    protected int textColor;
    protected int fontSize;
    protected int currentColor;
    protected org.apache.log4j.Logger logger = Logger.getLogger(ShareButton.class.getName());
    private String posterItemUuid;


    public ShareButton(String uuid, int width, int height) {
        super(uuid, width, height);

    }

    public ShareButton(String uuid, int x, int y, int width, int height) {
        super(uuid, width, height);

    }

    public void initButton() {

        this.unpressedButtonColor = ZoneHelper.redOutline;
        this.pressedButtonColor = ZoneHelper.greyOutline;
        this.currentColor = unpressedButtonColor;
        this.text = "+";
        this.outline = ZoneHelper.greyOutline;
        this.font = ZoneHelper.helveticaNeue18Font;
        this.fontSize = ZoneHelper.CONTROL_BUTTON_FONT_SIZE;
        this.textColor = color(255);
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    @Override
    public void touchDown(Touch touch) {
        logger.info("SHARE PRESSED WITH POSTER_ITEM_UUID:" + posterItemUuid);
        this.currentColor = pressedButtonColor;
    }

    @Override
    public void touchUp(Touch touch) {
        this.currentColor = unpressedButtonColor;
        this.shareAction();
    }


    public void shareAction() {
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.SHARE_CLASSPAGE);
    }

    @Override
    public void draw() {
        smooth(4);
        stroke(outline);
        strokeWeight(1);

        fill(currentColor);
        smooth(4);
        rect(0, 0, getWidth(), getHeight(), 10);


        if (text != null) {
            if (font != null) {
                textFont(font, fontSize);
            }
            textAlign(CENTER, CENTER);
            textSize(fontSize);
            fill(textColor);
            smooth(4);
            text(text, 0, 0, getWidth() - SPACING, getHeight() - SPACING);
        }
    }

    public String getPosterItemUuid() {
        return posterItemUuid;
    }

    public void setPosterItemUuid(String posterItemUuid) {
        this.posterItemUuid = posterItemUuid;
        DialogZoneController.dialog().setSharingObject(new PosterGrab(posterItemUuid));
    }
}
