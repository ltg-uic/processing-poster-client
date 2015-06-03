package ltg.evl.uic.poster.widgets;
import java.net.URL;
import ltg.evl.uic.poster.widgets.button.PlayButton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PImage;
import vialab.SMT.SMT;
import vialab.SMT.Touch;
import com.google.common.io.Resources;

import java.io.IOException;

/**
 * Created by krbalmryde on 6/2/15.
 */
public class VideoZone extends PictureZone {
    private String videoURL;
    private boolean isPlaying;

    public VideoZone(PImage image, String url, String uuid, int x, int y, int width, int height) {
        super(image, uuid, x, y, width/2, height/2);
        System.out.println("raw w: " + width + " h: " + height);
        System.out.println("kosher w/h: " + this.getWidth() + " " + this.getHeight());
        setVideoURL(url);
        isPlaying = false;
        super.init();
    }

    @Override
    public void draw() {
        if (isEditing()) {
            fill(255, 255, 255);
            if (isDrawingOutline()) {
                stroke(ZoneHelper.blueOutline);
                strokeWeight(1);
                smooth();
                rect(0, 0, this.getWidth(), this.getHeight());
            } else {
                stroke(ZoneHelper.greyOutline);
                strokeWeight(2);
                smooth();
                rect(0, 0, this.getWidth(), this.getHeight());
            }

            if (this.getZoneImage() != null) {
                image(this.getZoneImage(), 0, 0, this.getWidth(),
                        this.getHeight());
            } else {
                //TODO put text problem with img server.
                fill(255, 255, 255);
                rect(0, 0, this.getWidth(), this.getHeight());
            }
        } else {
            image(this.getZoneImage(), 0, 0, this.getWidth(),
                    this.getHeight());
        }

        if (isHasBeenCited()) {
            stroke(ZoneHelper.orangeColor);
            strokeWeight(3);
            smooth();
            noFill();
            rect(0, 0, this.getWidth(), this.getHeight());
        }
    }

    @Override
    public void touchUp(Touch touch) {
        if (isPlaying) {
            URL rh = Resources.getResource("closeQT.sh");
            System.out.println("Kill the process: " + rh.getPath());
            ProcessBuilder pb = new ProcessBuilder("bash", rh.getPath());
            try {
                pb.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setZoneImage(ZoneHelper.playImage);
        } else {
            this.playVideoAction();
            this.setZoneImage(ZoneHelper.stopImage);
        }
        isPlaying = !isPlaying;
    }

    @Override
    public void touchDown(Touch touch) {
        super.touchDown(touch);
        System.out.println("PictureZone.touchDown name: " + getName());
    }

    public void playVideoAction() {
        System.out.println("Playing a Video!");
        ProcessBuilder pb = new ProcessBuilder("open", "-a", "Quicktime Player", getVideoURL());
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setVideoURL(String url) {
        this.videoURL = url;
    }
    public String getVideoURL() {
        return this.videoURL;
    }
}

