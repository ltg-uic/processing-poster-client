package ltg.evl.uic.poster;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.*;
import ltg.evl.uic.poster.json.mongo.ObjectEvent;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.listeners.LoginDialogEvent;
import ltg.evl.uic.poster.widgets.DialogZoneController;
import ltg.evl.uic.poster.widgets.EditColorZone;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import ltg.evl.util.RESTHelper;
import ltg.evl.util.collections.PosterItemToPictureZone;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import tmp.LogoutButton;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;
import vialab.SMT.Zone;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;


/**
 * Created by aperritano on 9/24/14.
 */
public class PosterMain extends PApplet {


    protected static org.apache.log4j.Logger logger;
    private EditColorZone colorZone;
    private LogoutButton logoutZone;


    public static void main(String args[]) {
        logger = Logger.getLogger(PosterMain.class.getName());
        logger.setLevel(Level.ALL);
        //  MQTTPipe.getInstance();

        PApplet.main(new String[]{"ltg.evl.uic.poster.PosterMain"});
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleLoginDialogEvent(LoginDialogEvent event) {

        try {
            if (Optional.fromNullable(event.getEventType()).isPresent()) {
                switch (event.getEventType()) {
                    case USER:
                        //logger.log(Level.INFO, "user event with user uuid: " + event.getUuid());


                        DialogZoneController.getInstance().hideUserPage();
                        DialogZoneController.getInstance().showPosterPage();
                        break;
                    case POST_ITEM:
                        break;
                    case CLASS_NAME:
                        //logger.log(Level.INFO, "class name: " + event.getUuid());
                        DialogZoneController.getInstance().hideClassPage();
                        DialogZoneController.getInstance().showUserPage(event.getUuid());
                        break;
                    case POSTER:
                        //logger.log(Level.INFO, "poster event with poster uuid: " + event.getUuid());
                        DialogZoneController.getInstance().hidePosterPage();
                        logoutZone.setUser(PosterDataModel.helper().getCurrentUser());
                        logoutZone.initButton();
                        colorZone.setVisible(true);
                        logoutZone.setVisible(true);
                        loadPosterItems(PosterDataModel.helper().getAllPostersItemsForPoster(event.getUuid()), true);
                        break;
                    case LOGOUT:
                        //when we logout we save all the all the posteritems, remove them from the canvas and then show dialog
                        colorZone.isEditing(false);
                        logoutZone.setVisible(false);
                        PosterDataModel.helper().savePosterItemsForCurrentUser();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleObjectEvent(ObjectEvent event) {

        if (Optional.fromNullable(event.getEventType()).isPresent()) {
            switch (event.getEventType()) {
                case USER:
                    break;
                case POST_ITEM:
                    PosterItem newPosterItem = (PosterItem) event.getGenericJson();
                    loadPosterItems(Lists.newArrayList(newPosterItem), false);
                    break;
                case POSTER:
                    break;
                case DELETE_POSTER_ITEM:
                    String posterItemId = event.getItemId();
                    SMT.remove(posterItemId);
                    break;
                case INIT_ALL:
                    removeAlZones();
                    DialogZoneController.getInstance().showClassPage();
                    break;
            }
        }
    }

    @Override
    public void setup() {

        int w = displayWidth - 100;
        int h = displayHeight - 100;

        int bar_h = (int) (h * .07);

        size(w, h, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);

        setupControlButtons();

        final DialogZoneController dialogZoneController = DialogZoneController.getInstance();

        dialogZoneController.registerForLoginEvent(this);

        logger.debug("POSTER MODELER STARTED");

        PosterDataModel.helper().registerForObjectEvent(this);

        logger.debug("POSTER MODELER INIT ALL COLLECTIONS");
        RESTHelper.getInstance().initAllCollections();
    }


    public void loadPosterItems(Collection<PosterItem> posterItems, boolean shouldRemove) {

        if (Optional.fromNullable(posterItems).isPresent()) {

            if (shouldRemove) {
                removeAlZones();
            }
            FluentIterable<Callable<PictureZone>> callableFutures = FluentIterable.from(posterItems)
                                                                                  .transform(
                                                                                          new PosterItemToPictureZone());
            ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
            for (Callable<PictureZone> callablePictureZone : callableFutures) {

                ListenableFuture<PictureZone> pictureZoneFuture = service.submit(callablePictureZone);
                Futures.addCallback(pictureZoneFuture, new FutureCallback<PictureZone>() {
                    @Override
                    public void onSuccess(PictureZone result) {
                        boolean hasAdded = SMT.add(result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        logger.log(Level.ERROR, "PICTUREZONES FAILED CALLBACK");
                    }
                });

            }
        }
    }

    ////removeSMT.remove(zone.getName());


    private void setupControlButtons() {
        colorZone = new EditColorZone("lol", 40, 40, 50);
        colorZone.isEditing(false);
        colorZone.setVisible(true);
        logoutZone = new LogoutButton("b", "Group 1", colorZone.getWidth() + (colorZone.getX() / 2) + 5,
                                      (colorZone.getY() / 2) - 5, 200, 50, ZoneHelper.redOutline);
        logoutZone.setVisible(false);
        SMT.add(colorZone, logoutZone);


    }

    @Override
    public void draw() {
        background(255);
        fill(0);
        text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, 10);
    }

    public void removeAlZones() {
        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone) {
                SMT.remove(zone);
            }
        }
    }




}
