package tmp;

//import processing.core.PApplet;
//import processing.video.Movie;
//import vialab.SMT.Touch;
import vialab.SMT.Zone;


public class VideoZone extends Zone {
    //private Movie movie;

    public VideoZone(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        // setup();
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
//        background(200);
//        image(movie, 0, 0, 175, 175);
//    }
//
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