package ltg.evl.uic.poster.widgets.button;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import org.apache.commons.lang.WordUtils;
import processing.core.PFont;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 4/9/15.
 */
public class LogoutButton extends Zone {

    public boolean pressed = false;
    protected int pressedButtonColor;
    protected int unpressedButtonColor;
    protected int outline = ZoneHelper.greyOutline;
    protected PFont font;
    protected String text;
    protected int textColor;
    protected int fontSize;
    protected int currentColor;
    int padding = 10;
    private User user;

    public LogoutButton(String uuid, String text, int width, int height) {
        super(uuid, width, height);
        this.text = text;

    }

    public LogoutButton(String uuid, String text, int x, int y, int width, int height) {
        super(uuid, width, height);
        setX(x);
        setY(y);
        this.text = text;


    }

    public void initButton() {
        if (Optional.fromNullable(user).isPresent()) {
            this.unpressedButtonColor = Optional.of(user.getColor()).or(ZoneHelper.blueOutline);
            this.text = WordUtils.capitalize(user.getName());
            if (Optional.fromNullable(user.getNameTags()).isPresent()) {
                this.setName(WordUtils.capitalize(Joiner.on(",").join(user.getNameTags())));
            } else {
                this.setName("");
            }
        } else {

            this.unpressedButtonColor = ZoneHelper.blueOutline;
            this.text = "HOME";
        }

        this.pressedButtonColor = ZoneHelper.greyOutline;
        this.currentColor = unpressedButtonColor;
        this.outline = ZoneHelper.greyOutline;
        this.font = ZoneHelper.helveticaNeue18Font;
        this.fontSize = 18;
        this.textColor = color(255);
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    @Override
    public void touchDown(Touch touch) {
        this.currentColor = pressedButtonColor;
    }

    @Override
    public void touchUp(Touch touch) {
        this.currentColor = unpressedButtonColor;
        this.logoutAction();
    }

    public void logoutAction() {

    }

    @Override
    public void draw() {
        stroke(outline);
        strokeWeight(1);
        fill(currentColor);
        smooth(4);
        rect(0, 0, getWidth(), getHeight(), ZoneHelper.ROUND_CORNER);


        if (text != null) {
            if (font != null) {
                textFont(font, fontSize);
            }
            textAlign(CENTER, CENTER);
            textSize(fontSize);
            fill(textColor);
            smooth(4);
            float hght = textAscent();
            int x1 = getWidth() / 2;
            //int y1 = (int) (getHeight() / 2 - (hght / 2));
            int y1 = (int) hght;
            text("HOME", x1, y1 + (padding / 2));


            //UserName
            textAlign(CENTER, CENTER);
            textSize(fontSize - 4);
            fill(textColor);
            smooth(4);
            float h = textAscent();
            int y2 = (int) (y1 + h + padding + (padding / 2));
            text(getUsername(), x1, y2);


            //nametags
            textAlign(CENTER, CENTER);
            textSize(fontSize - 4);
            fill(textColor);
            smooth(4);
            h = textAscent();
            int y3 = (int) (y2 + (h + padding));
            text(getNameTags(), x1, y3);
        }
    }

    public String getNameTags() {
        return "";
    }

    public String getUsername() {
        return "";
    }
}
