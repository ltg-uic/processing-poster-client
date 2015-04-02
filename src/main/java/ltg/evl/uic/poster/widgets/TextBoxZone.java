package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import processing.core.PFont;
import vialab.SMT.SMT;
import vialab.SMT.TextBox;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

import java.awt.event.KeyListener;

public class TextBoxZone extends TextBox implements KeyListener, DeleteButtonListener {


    private String text;
    private boolean isShowing;
    private DeleteButton deleteButton;
    private PFont font = applet.createFont("HelveticaNeue-Bold", 23);
    private boolean isDrawingOutline;
    private int outline = ZoneHelper.getInstance().blueOutline;
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
    public void touch() {
        super.touch();
        SMT.putZoneOnTop(this);
    }

    @Override
    public void deleteZone(Zone zone) {
        PosterDataModel.helper().removePosterItem(this.getName());
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
        if (isDrawingOutline) {
            stroke(outline);
            strokeWeight(4);
            smooth();
            rect(0, 0, this.getWidth(), this.getHeight());
        }

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

    @Override
    public void touchDown(Touch touch) {
        this.isDrawingOutline = true;
        deleteButton.setDrawingOutline(isDrawingOutline);
    }

    @Override
    public void touchUp(Touch touch) {
        this.isDrawingOutline = false;
        deleteButton.setDrawingOutline(isDrawingOutline);
    }


    public String getText() {
        return text;
    }

    public void setText(String textstring) {
        this.text = textstring;
    }

}
