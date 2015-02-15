package ltg.evl.uic.poster;

public class ControlButtonZoneBuilder {
    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private String buttonText;

    public ControlButtonZoneBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ControlButtonZoneBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public ControlButtonZoneBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public ControlButtonZoneBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public ControlButtonZoneBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public ControlButtonZone createControlButtonZone() {
        return new ControlButtonZone(name, x, y, width, height, buttonText);
    }

    public ControlButtonZoneBuilder setButtonText(String buttonText) {
        this.buttonText = buttonText;
        return this;
    }
}