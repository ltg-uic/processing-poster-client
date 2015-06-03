package ltg.evl.uic.poster.widgets;

import ltg.evl.uic.poster.widgets.button.PlayButton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PImage;
import vialab.SMT.SMT;

/**
 * Created by krbalmryde on 6/2/15.
 */
public class VideoZone extends PictureZone {
    private PlayButton playButton;
    private String videoURL;
    private final Logger logger = Logger.getLogger(this.getClass());

    public VideoZone(PImage image, String url, String uuid, int x, int y, int width, int height) {
        super(image, uuid, x, y, width, height);
        setVideoURL(url);
        init();
    }

//    0, 0, getWidth() - SPACING, getHeight() - SPACING

    @Override
    public void init() {
        super.init();
        System.out.println("Initializing videos..." + this.getWidth() + " " + this.getHeight());
        double buttonSize = SMT.getApplet().getHeight() * .5;
        double adjustedButtonSize = (buttonSize / 2.0);
//        int x = (int) (this.getWidth() - adjustedButtonSize) - 2;
//        int y = (int) (2 - adjustedButtonSize);

        playButton = new PlayButton(ZoneHelper.playImage,  getVideoURL(),
                                    PlayButton.PLAY_NAME,  0, 0,
                                    (int) buttonSize, (int) adjustedButtonSize);
        //create stop button
        add(playButton);
    }

    //    public void setup() {
    //
    //        //URL url = this.getClass().getResource("transit.mov");
    //        File f = new File("/Users/aperritano/Desktop/transit.mov");
    //        String newPath = f.getPath().replaceAll("%20", " ");
    //        movie = new Movie(this.applet, newPath);
    //    }
    //
//    @Override
//    public void draw() {
////        System.out.println("Drawing the video and shit");
//        fill(255, 255, 255);
//        if (this.getName() != null)
//            text(this.getName(), 2, 2);
//        else
//            text("No Name", 2, 2);
//        stroke(ZoneHelper.orangeColor);
//        strokeWeight(3);
//        smooth();
//        rect(0, 0, this.getWidth(), this.getHeight());
////        image(this.getZoneImage(), 0, 0, this.getWidth(), this.getHeight());
//    }

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

//    @Override
//    public void touch() {
//        super.touch();
//        logger.log(Level.INFO, "TOUCHED PZ: " + this.getName());
//        // printThisShit();
//        if (this.isEditing()) {
//            SMT.putZoneOnTop(this);
//            rst(false, true, true);
//        } else if (this.isDeleteMode()) {
//            rst(false, false, false);
//        }
//    }

    public void setVideoURL(String url) {
        this.videoURL = url;
    }
    public String getVideoURL() {
        return this.videoURL;
    }

    //    @Override
    //    public void pickDraw() {
    //        rect(175, 0, 25, 175);
    //    }
    //
    //    protected void pressImpl(Touch touch) {
    //
    //        rect(0, 0, 170, 175);
    //        PApplet.println("PRESSED");
    //    }
    //
    //
    //    public Movie getMovie() {
    //        return movie;
    //    }
    //
    //    public void movieEvent(Movie m) {
    //        m.read();
    //    }
}
