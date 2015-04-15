package ltg.evl.uic.poster.widgets;

import com.google.common.base.Objects;
import ltg.evl.uic.poster.listeners.EditListener;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

public class EditColorZone extends Zone {

    private final int outline;
    private int color;
    private boolean isEditing;
    private EditListener editListener;

    public EditColorZone(String name, int x, int y, int width) {
        super(name, x, y, width, width);
        this.outline = ZoneHelper.greyOutline;
        this.isEditing(false);
    }

    @Override
    public void touch() {
        rst(false, false, false);
    }

    public void addEditListener(EditListener editListener) {
        this.editListener = editListener;
    }

    @Override
    public void touchDown(Touch touch) {
        this.isEditing = !isEditing;
        toggleColor();
    }

    @Override
    public void touchUp(Touch touch) {
        this.editListener.editModeChanged(isEditing);
    }

    @Override
    public void draw() {
        stroke(outline);
        strokeWeight(1);
        fill(this.color);
        smooth(4);
        ellipse(0, 0, getWidth(), getWidth());
    }

    public void isEditing(boolean isEditing) {
        this.isEditing = isEditing;
        this.toggleColor();
    }

    public boolean getIsEditing() {
        return this.isEditing;
    }

    protected void toggleColor() {
        if (this.isEditing) {
            this.color = ZoneHelper.greenColor;
        } else {
            this.color = ZoneHelper.blueOutline;
        }
    }

    @Override
    public String toString() {

        return Objects.toStringHelper(this)
                      .omitNullValues()
                      .add("x", getX())
                      .add("y", getY())
                      .add("width", getWidth())
                      .add("height", getHeight())
                      .toString();

    }
}
