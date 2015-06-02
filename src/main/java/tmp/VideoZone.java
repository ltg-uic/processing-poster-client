package tmp;

import ltg.evl.uic.poster.widgets.button.PlayButton;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import ltg.evl.uic.poster.widgets.PictureZone;
import vialab.SMT.SMT;

public class VideoZone extends PictureZone {
    private PlayButton playButton;
    private String videoURL;

    public VideoZone(String url, String uuid, int x, int y, int width, int height) {
        super(null, uuid, x, y, width, height);
        this.videoURL = url;
        init();

    }

    @Override
    public void init() {
        super.init();

        double buttonSize = SMT.getApplet().getHeight() * .667;
        double adjustedButtonSize = (buttonSize / 2.0);
        int x = (int) (this.getWidth() - adjustedButtonSize) - 2;
        int y = (int) (2 - adjustedButtonSize);

        playButton = new PlayButton(ZoneHelper.playImage,  this.videoURL,
                                    PlayButton.PLAY_NAME,  x, y,
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
    @Override
    public void draw() {
        fill(255);
        text(this.getName(), 2, 2);
        rect(0,0, this.getWidth(), this.getHeight());
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