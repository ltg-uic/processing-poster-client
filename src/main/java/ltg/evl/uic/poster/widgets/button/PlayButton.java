package ltg.evl.uic.poster.widgets.button;

import ltg.evl.uic.poster.widgets.ZoneHelper;
import processing.core.PImage;
import vialab.SMT.ImageZone;
import vialab.SMT.Touch;
import vialab.SMT.Zone;

import java.io.IOException;

/**
 * Created by krbalmryde on 6/1/15.
 */
public class PlayButton extends ImageZone {
    public static String PLAY_NAME = "playButton";
    public static final int SPACING = 4;
    protected int pressedButtonColor = ZoneHelper.orangeColor;
    protected int unpressedButtonColor = ZoneHelper.darkTeal;
    protected int outline = ZoneHelper.greyOutline;
    protected String text;
    protected int textColor;
    protected int fontSize;
    protected int currentColor;
    private Process process;
    protected String videoURL;
    private boolean isPlaying;

    public PlayButton(PImage image, String url, String uuid, int x, int y, int width, int height) {
        super(uuid, image, x, y, width, height);
        this.videoURL = url;
        isPlaying = false;
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
        if (isPlaying) {
            System.out.println("Kill the process");
            process.destroy();
            this.setZoneImage(ZoneHelper.playImage);
        } else {
            this.playVideoAction();
            this.setZoneImage(ZoneHelper.stopImage);
        }
        isPlaying = !isPlaying;
        this.currentColor = unpressedButtonColor;
    }

    public void playVideoAction() {
        System.out.println("Playing a Video!");
        ProcessBuilder pb = new ProcessBuilder("open", "-a", "Quicktime Player", this.videoURL);
        try {
            process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//
//    @Override
//    public void draw() {
//        smooth(4);
//        stroke(outline);
//        strokeWeight(1);
//
//        fill(currentColor);
//        smooth(4);
//        ellipse(getWidth() / 2, getHeight() / 2, getWidth(), getHeight());
//
//
//        if (text != null) {
//            if (font != null) {
//                textFont(font, fontSize);
//            }
//            textAlign(CENTER, CENTER);
//            textSize(fontSize);
//            fill(textColor);
//            smooth(4);
//            text(text, getWidth() / 2, getHeight() / 2);
//        }
//    }

}
