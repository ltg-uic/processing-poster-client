package ltg.evl.uic.poster.widgets.buttons;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadUserListener;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import org.apache.commons.lang.WordUtils;
import processing.core.PFont;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class UserButton extends Zone {

    protected int pressedButtonColor;
    protected int unpressedButtonColor;
    protected int outline = ZoneHelper.greyOutline;
    protected PFont font;
    protected String text;
    protected int textColor;
    protected int fontSize;
    protected int currentColor;
    private LoadUserListener loadUserListerner;
    private User user;
    private String nameTags;

    public UserButton(String name, int buttonStartX, int buttonStartY, int buttonWidth, int buttonHeight, String text,
                      int pressedButtonColor, int unpressedButtonColor, PFont font) {
        super(name, buttonStartX, buttonStartY, buttonWidth, buttonHeight);

        this.font = font;
        this.text = text;
        this.fontSize = font.getSize();
        //this.initButton();
    }

    public UserButton(String uuid, int width, int height) {
        super(uuid, width, height);

        this.font = ZoneHelper.helveticaNeue18Font;
        //this.text = user.getName();

        this.fontSize = 18;
        //this.initButton();
    }

    public UserButton(String uuid, String text, int width, int height) {
        super(uuid, width, height);
        this.text = text;
        this.font = ZoneHelper.helveticaNeue18Font;
        //this.text = user.getName();

        this.fontSize = 18;
    }

    public void initButton() {
        if (Optional.fromNullable(user).isPresent()) {
            this.unpressedButtonColor = Optional.of(user.getColor()).or(ZoneHelper.whiteOutline);
            this.text = WordUtils.capitalize(user.getName());
            if (Optional.fromNullable(user.getNameTags()).isPresent()) {
                this.nameTags = WordUtils.capitalize(Joiner.on(",").join(user.getNameTags()));
            } else {
                this.nameTags = "";
            }
        } else {
            this.unpressedButtonColor = ZoneHelper.whiteOutline;
        }
        this.pressedButtonColor = ZoneHelper.greyOutline;
        this.currentColor = unpressedButtonColor;
        this.outline = ZoneHelper.greyOutline;
        this.textColor = color(255);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUnpressedButtonColor(int color) {
        this.unpressedButtonColor = color;
    }

    public void setPressedButtonColor(int color) {
        this.pressedButtonColor = color;
        this.currentColor = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    public void addLoadUserListener(LoadUserListener loadUserListerner) {
        this.loadUserListerner = loadUserListerner;
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
            int y1 = (int) (getHeight() / 2 - (hght / 2));
            text(text, x1, y1);
            if (this.nameTags != null) {
                textAlign(CENTER, CENTER);
                textSize(fontSize - 4);
                fill(textColor);
                smooth(4);
                float h = textAscent();
                text(nameTags, x1, y1 + (h * 2));
            }

        }
    }

    @Override
    public void touchDown(Touch touch) {
        this.currentColor = pressedButtonColor;
    }

    @Override
    public void touchUp(Touch touch) {
        this.currentColor = unpressedButtonColor;
        this.loadUserListerner.loadUser(this.user);
    }


}
