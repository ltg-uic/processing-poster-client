package ltg.evl.uic.poster;

import com.google.common.util.concurrent.*;
import ltg.evl.json.mongo.User;
import ltg.evl.util.DBHelper;
import ltg.evl.util.DownloadHelper;
import processing.core.PApplet;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;


/**
 * Created by aperritano on 9/24/14.
 */
public class PosterMain extends PApplet implements DownloadHelper.NewFileAddedEventListener {


    private static DownloadHelper downloadHelper;

    private static String backpackPath;
    private static List<User> users;

    public static void main(String args[]) {

        ListeningExecutorService pool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        final ListenableFuture<List<User>> future = pool.submit(new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                return DBHelper.helper().fetchUsers();
            }
        });


        Futures.addCallback(future, new FutureCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                users = result;
                StyleHelper.helper();
                PApplet.main(new String[]{"ltg.evl.uic.poster.PosterMain"});
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("FAILED");
            }
        });

        //get the back path

        backpackPath = StyleHelper.helper().getConfiguration().getString("backpack.path");
        downloadHelper = new DownloadHelper(backpackPath);


    }

    private void fetchUsers() {
        DBHelper.helper().fetchUsers();
    }

    @Override
    public void setup() {
        System.out.println("Setup started");
        List<User> fetchedUsers = DBHelper.helper().fetchUsers();

        StyleHelper.helper().setGraphicsContext(this);
        setupStyles();


        size(displayWidth, displayHeight, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);

        SMT.add(new ControlButtonZoneBuilder().setButtonText("SAVE")
                                              .setName("save_button")
                                              .setHeight(200)
                                              .setWidth(200)
                                              .setX(50)
                                              .setY(50)
                                              .createControlButtonZone());
        //


    }

    private void setupStyles() {
        downloadHelper.addFileWatchEventListener(this);
        //load fonts
    }

    private void load() {

    }

    @Override
    public void draw() {

        background(StyleHelper.helper().createColor("color.mainBackground"));

        fill(0, 0, 0, 255);
        text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, height / 2);


    }

    @Override
    public void newFileAdded(DownloadHelper.FileWatchEvent evt) {
        PictureZone imageZone = new PictureZoneBuilder().setImage(loadImage(evt.getFilePath())).createPictureZone();
        SMT.add(imageZone);
    }
}
