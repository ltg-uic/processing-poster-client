package ltg.evl.uic.poster;

import com.google.common.base.Joiner;
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
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.PresentationZone;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import ltg.evl.uic.poster.widgets.buttons.EditModeButton;
import ltg.evl.uic.poster.widgets.buttons.LogoutButton;
import ltg.evl.uic.poster.widgets.buttons.RemoveModeButton;
import ltg.evl.util.MQTTPipe;
import ltg.evl.util.RESTHelper;
import ltg.evl.util.collections.PosterItemToPictureZone;
import ltg.evl.util.de.looksgood.ani.Ani;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;
import vialab.SMT.Zone;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static org.apache.commons.lang.WordUtils.capitalize;


/**
 * Created by aperritano on 9/24/14.
 */
public class PosterMain extends PApplet {


    protected static org.apache.log4j.Logger logger;
    public static void main(String args[]) {
        logger = Logger.getLogger(PosterMain.class.getName());
        logger.setLevel(Level.ALL);
        MQTTPipe.getInstance();
        PApplet.main(new String[]{"ltg.evl.uic.poster.PosterMain"});
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleLoginDialogEvent(LoginDialogEvent event) {
        try {
            if (Optional.fromNullable(event.getEventType()).isPresent()) {
                switch (event.getEventType()) {
                    case USER:
                        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
                        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.POSTER_PAGE);
                        break;
                    case POST_ITEM:
                        break;
                    case CLASS_NAME:
                        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
                        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.USER_PAGE);
                        break;
                    case POSTER:
                        //logger.log(Level.INFO, "poster event with poster uuid: " + event.getUuid());
                        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
                        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NO_PRES);
                        loadPosterItemsForCurrentUserAndPoster(
                                PosterDataModel.helper().getAllPostersItemsForPoster(event.getUuid()), true);
                        setupControlButtons();
                        break;
                    case LOGOUT:
                        PosterDataModel.helper().savePosterItemsForCurrentUser();
                        removeAlZones();
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
                    loadPosterItemsForCurrentUserAndPoster(Lists.newArrayList(newPosterItem), false);
                    break;
                case POSTER:
                    break;
                case DELETE_POSTER_ITEM:
                    String posterItemId = event.getItemId();
                    SMT.remove(posterItemId);
                    break;
                case INIT_ALL:
                    setupInit();
                    DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
                    DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.PRES);
                    DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.CLASS_PAGE);
                    break;
            }
        }
    }


    void setupInit() {
        removeAlZones();
    }

    @Override
    public void setup() {

        Ani.init(this);
        Ani.autostart();
        int w = displayWidth;
        int h = displayHeight;

        size(w, h, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);


        final DialogZoneController dialogZoneController = DialogZoneController.dialog();

        dialogZoneController.registerForLoginEvent(this);

        logger.debug("POSTER MODELER STARTED");

        PosterDataModel.helper().registerForObjectEvent(this);

        logger.debug("POSTER MODELER INIT ALL COLLECTIONS");
        RESTHelper.getInstance().initAllCollections();


        MQTTPipe.getInstance();
    }


    public void loadPosterItemsForCurrentUserAndPoster(Collection<PosterItem> posterItems, boolean shouldRemove) {

        if (Optional.fromNullable(posterItems).isPresent()) {

            if (shouldRemove) {
                removeAlZones();
            }
            FluentIterable<Callable<PictureZone>> callableFutures = FluentIterable.from(posterItems)
                                                                                  .transform(
                                                                                          new PosterItemToPictureZone());
            ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
            for (Callable<PictureZone> callablePictureZone : callableFutures) {

                ListenableFuture<PictureZone> pictureZoneFuture = service.submit(callablePictureZone);

                Futures.addCallback(pictureZoneFuture, new FutureCallback<PictureZone>() {
                    @Override
                    public void onSuccess(PictureZone result) {
                        if (result != null) {
                            if (SMT.add(result)) {
                                result.startAni(0.5f, 0f);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                        logger.log(Level.ERROR, "PICTURE ZONE FAILED CALLBACK");

                    }
                });

            }
        }
    }

    private void setupControlButtons() {
        LogoutButton logoutZone = new LogoutButton("logoutZone", "", ZoneHelper.LOGOUT_BUTTON_WIDTH,
                                                   ZoneHelper.LOGOUT_BUTTON_HEIGHT) {
            @Override
            public void logoutAction() {
                final PresentationZone presentationZone = new PresentationZone("s", 0, 0,
                                                                               SMT.getApplet().getWidth(),
                                                                               SMT.getApplet().getHeight()) {
                    @Override
                    public void doYesAction() {
                        PosterDataModel.helper().logout();
                        SMT.remove(this);

                    }


                };


                presentationZone.showDialog("Do you want to logout?", 200);
                SMT.add(presentationZone);
            }

            @Override
            public String getNameTags() {
                if (Optional.fromNullable(PosterDataModel.helper().getCurrentUser()).isPresent()) {
                    return capitalize(Joiner.on(",").join(
                            PosterDataModel.helper().getCurrentUser().getNameTags()));
                }
                return "Tony, Jim, Tom";
            }

            @Override
            public String getUsername() {
                if (Optional.fromNullable(PosterDataModel.helper().getCurrentUser()).isPresent()) {
                    String uname = PosterDataModel.helper().getCurrentUser().getName();
                    return capitalize(uname);
                }
                return "Delicious Carrot";
            }
        };
        logoutZone.initButton();
        logoutZone.setVisible(true);


        RemoveModeButton removeModeButton = new RemoveModeButton("Group", ZoneHelper.LOGOUT_BUTTON_WIDTH,
                                                                 ZoneHelper.LOGOUT_BUTTON_HEIGHT) {
            @Override
            public void removeAction() {
                boolean shouldDelete = false;
                boolean foundFlag = false;
                Zone[] zones = SMT.getZones();
                for (Zone zone : zones) {
                    if (zone != null && (zone instanceof PictureZone)) {
                        PictureZone pictureZone = (PictureZone) zone;

                        if (pictureZone.isEditing()) {
                            if (foundFlag == false) {
                                shouldDelete = !pictureZone.isDeleteMode();
                                foundFlag = true;
                            }

                            pictureZone.setIsDeleteMode(shouldDelete);
                        }

                    }
                }

            }

        };
        removeModeButton.initButton();
        removeModeButton.setVisible(true);


        EditModeButton editModeButton = new EditModeButton("Edit", ZoneHelper.LOGOUT_BUTTON_WIDTH,
                                                           ZoneHelper.LOGOUT_BUTTON_HEIGHT) {
            @Override
            public void editAction() {
                toggleIsEdit(true);
            }

            @Override
            public void presentAction() {
                toggleIsEdit(false);
            }
        };
        editModeButton.initButton();
        editModeButton.setVisible(true);
        DialogZoneController.dialog().createControlPage(logoutZone, removeModeButton, editModeButton);



    }

    protected void toggleIsEdit(boolean isEditing) {
        Zone[] activeZones = SMT.getRootZone().getChildren();


        if (activeZones.length >= 1) {
            for (Zone zone : activeZones) {
                if (zone != null & zone instanceof PictureZone) {
                    PictureZone pz = (PictureZone) zone;
                    pz.setIsEditing(isEditing);
                }
            }
        }
    }

    @Override
    public void draw() {
        background(255);
        fill(0);
        text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, 10);
    }

    public void hideAlZones() {
        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone) {
                zone.setVisible(false);
            }
        }
    }

    public void removeAlZones() {
        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone) {
                SMT.remove(zone);
            }
        }
    }


}
