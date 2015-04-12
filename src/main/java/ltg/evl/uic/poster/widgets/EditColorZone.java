package ltg.evl.uic.poster.widgets;

import com.google.common.base.Objects;
import vialab.SMT.Zone;

public class EditColorZone extends Zone {

    private final int outline;
    private int color;

    public EditColorZone(String name, int x, int y, int width) {
        super(name, x, y, width, width);
        this.outline = ZoneHelper.greyOutline;
        this.isEditing(false);
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
        if (isEditing) {
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
