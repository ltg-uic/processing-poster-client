package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.json.mongo.PosterDataModelHelper;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import processing.core.PFont;
import vialab.SMT.TextBox;
import vialab.SMT.Zone;

import java.awt.event.KeyListener;

public class TextBoxZone extends TextBox implements KeyListener, DeleteButtonListener {


    private String text;
    private boolean isShowing;
    private DeleteButton deleteButton;
    private PFont font = applet.createFont("HelveticaNeue-Bold", 23);
    private DeleteButtonListener deleteButtonListener;

    public TextBoxZone(String uuid, String text, int x, int y, int width, int height) {
        super(uuid, x, y, width, height);
        this.setText(text);
    }

    public TextBoxZone(PosterItem posterItem) {
        super(posterItem.getContent(), posterItem.getUuid(), posterItem.getX(), posterItem.getY(),
              posterItem.getWidth(), posterItem.getHeight());
    }

    @Override
    public boolean add(Zone zone) {

        if (zone instanceof DeleteButton) {
            deleteButton = (DeleteButton) zone;
        }

        return super.add(zone);
    }

    public void addListener(DeleteButtonListener deleteButtonListener) {
        this.deleteButtonListener = deleteButtonListener;
    }

    @Override
    public void deleteZone(Zone zone) {
        PosterDataModelHelper.getInstance().removePosterItem(this.getName());
    }

    public void show(boolean isShowing) {
        this.isShowing = isShowing;
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
        pushStyle();
        //set background color
        //fill(255);
        //else
        //     noFill();
        //set frame color
        //draw background/frame rectangle
        fill(200);
        rect(0, 0, width, height);
        //draw text
        //fill(2);
        textFont(font, 30);
        //textSize(27);
        text(getContent(), 0, 0, width + 100, height + 100);
        popStyle();
    }

    public String getText() {
        return text;
    }

    public void setText(String textstring) {
        this.text = textstring;
    }

}
