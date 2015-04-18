package ltg.evl.uic.poster.widgets;

import ltg.evl.util.de.looksgood.ani.Ani;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

/**
 * Created by aperritano on 4/16/15.
 */
public class ControlPage extends Zone {

    private int initX = 0;
    private PVector originPoint = new PVector(0, 0);
    private int greyColor = ZoneHelper.greyOutline;
    private boolean isOut = false;
    private PVector targetPoint;

    public ControlPage(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        this.initX = x;
        originPoint.x = x;
        originPoint.y = y;
    }


    @Override
    public void draw() {
        stroke(97, 97, 97);
        strokeWeight(3);
        fill(255);
        rect(0, 0, this.getWidth(), this.getHeight(), ZoneHelper.ROUND_CORNER);

        stroke(224, 224, 224);
        strokeWeight(3);
        fill(255);
        rect(1, 1, this.getWidth() - 2, this.getHeight() - 2, ZoneHelper.ROUND_CORNER);
    }

    public void startAni(PVector targetPoint, float speed, float delay) {
        isOut = true;
        Ani diameterAni = new Ani(this, speed, delay, "initX", targetPoint.x, Ani.EXPO_IN_OUT,
                                  "onUpdate:update");
        diameterAni.start();
    }


    public void update() {
        setX(initX);
    }


    @Override
    public void touch() {
        SMT.putZoneOnTop(this);
        rst(false, false, true);
    }

    @Override
    public void touchUp(Touch touch) {
        System.out.println("touch = [" + touch + "]");
        if (isOut) {
            this.startAni(originPoint, 1.0f, 0f);
            isOut = false;
        } else {
            this.startAni(targetPoint, 1.0f, 0f);
        }
    }

    public void addButtons(Zone... userButtons) {


        for (Zone userButton : userButtons) {
            this.add(userButton);
        }
        SMT.grid(ZoneHelper.GRID_SPACER, ZoneHelper.GRID_SPACER, this.getWidth(), ZoneHelper.GRID_SPACER,
                 ZoneHelper.GRID_SPACER, this.getChildren());
    }

    public void setTargetPoint(PVector targetPoint) {
        this.targetPoint = targetPoint;
    }
}
