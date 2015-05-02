package ltg.evl.uic.poster;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import ltg.evl.uic.poster.json.mongo.ObjectEvent;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.listeners.LoginCollectionListener;
import ltg.evl.uic.poster.listeners.LoginDialogEvent;
import ltg.evl.uic.poster.util.MQTTPipe;
import ltg.evl.uic.poster.util.RESTHelper;
import ltg.evl.uic.poster.util.collections.PosterItemToPictureZone;
import ltg.evl.uic.poster.util.de.looksgood.ani.Ani;
import ltg.evl.uic.poster.widgets.DialogZoneController;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.PresentationZone;
import ltg.evl.uic.poster.widgets.ZoneHelper;
import ltg.evl.uic.poster.widgets.button.EditModeButton;
import ltg.evl.uic.poster.widgets.button.LogoutButton;
import ltg.evl.uic.poster.widgets.button.RemoveModeButton;
import ltg.evl.uic.poster.widgets.button.SaveButton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;
import vialab.SMT.Zone;

import java.util.Collection;

import static org.apache.commons.lang.WordUtils.capitalize;

public class PosterMain extends PApplet implements LoginCollectionListener {


    protected static org.apache.log4j.Logger logger;
    private boolean lastIsEditToggle = true;

    public static void main(String args[]) {
        logger = Logger.getLogger(PosterMain.class.getName());
        logger.setLevel(Level.ALL);
        MQTTPipe.getInstance();


        String[] appletArgs = new String[]{"ltg.evl.uic.poster.PosterMain"};
        if (args != null) {
            PApplet.main(concat(appletArgs, args));
        } else {
            PApplet.main(appletArgs);
        }
    }

    //region LoginCollectionListener
    @Override
    public void initializationDone() {
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.PRES);
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.CLASS_PAGE);
    }

    @Override
    public void logoutDoneEvent() {
        removeAlZones();
        PosterDataModel.helper().startInitialization();
    }

    @Override
    public void loadUserEvent(LoginDialogEvent loginDialogEvent) {
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.POSTER_PAGE);
    }

    @Override
    public void loadPosterEvent(LoginDialogEvent loginDialogEvent) {
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NO_PRES);
        loadPosterItemsForCurrentUserAndPoster(
                PosterDataModel.helper().getAllPostersItemsForPoster(loginDialogEvent.getUuid()), true);
    }

    @Override
    public void loadClassEvent(LoginDialogEvent loginDialogEvent) {
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.USER_PAGE);
    }

    @Override
    public void updatePosterItem(ObjectEvent objectEvent) {
        PosterItem newPosterItem = (PosterItem) objectEvent.getGenericJson();
        loadPosterItemsForCurrentUserAndPoster(Lists.newArrayList(newPosterItem), false);
    }

    @Override
    public void deletePosterItem(ObjectEvent objectEvent) {
        String posterItemId = objectEvent.getItemId();
        SMT.remove(posterItemId);
    }

    @Override
    public void refreshEventReceived() {
        //check if the dialog is up
        System.out.println("PosterMain.refreshEventReceived isShowing " + DialogZoneController.dialog().isShowing());

        if (DialogZoneController.dialog().isShowing()) {
            DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
            PosterDataModel.helper().startInitialization();
        } else {
            logger.info("DialogZoneController NOT SHOWING ON MESSAGE RECEIVED");
        }
    }

    //endregion LoginCollectionListener

    @Override
    public void setup() {
        size(displayWidth, displayHeight, SMT.RENDERER);
        //hint(ENABLE_RETINA_PIXELS);
        SMT.init(this, TouchSource.MOUSE);

        setupControlButtons();


        PosterDataModel.helper().addLoginCollectionListener(this);

        RESTHelper.getInstance().initAllCollections();

        Ani.init(this);
        Ani.autostart();
    }

    @Override
    public void draw() {
        background(255);
        text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, 10);
    }



    public void loadPosterItemsForCurrentUserAndPoster(Collection<PosterItem> posterItems, boolean shouldRemove) {

        if (Optional.fromNullable(posterItems).isPresent()) {

            if (shouldRemove) {
                removeAlZones();
            }
            FluentIterable<PictureZone> pictureZones = FluentIterable.from(posterItems)
                                                                     .transform(
                                                                             new PosterItemToPictureZone());


            Optional<FluentIterable<PictureZone>> fluentIterableOptional = Optional.fromNullable(pictureZones);

            if (Optional.fromNullable(pictureZones).isPresent()) {

                ImmutableList<PictureZone> list = fluentIterableOptional.get().toList();


                if (!list.isEmpty()) {
                    for (PictureZone pz : list) {
                        if (pz != null) {
                            if (SMT.add(pz)) {
                                // pz.applyScaleRotation();
                                pz.setIsEditing(lastIsEditToggle);
                                pz.startAni(0.5f, 0f);
                            }
                        }
                    }
                }
            }



        }
    }

    private void setupControlButtons() {
        LogoutButton logoutZone = new LogoutButton("logoutZone", "", ZoneHelper.LOGOUT_BUTTON_WIDTH,
                                                   ZoneHelper.LOGOUT_BUTTON_HEIGHT) {
            @Override
            public void logoutAction() {
                final PresentationZone presentationZone = new PresentationZone("shouldLogoutPresentation", 0, 0,
                                                                               SMT.getApplet().getWidth(),
                                                                               SMT.getApplet().getHeight()) {
                    @Override
                    public void doTouchAction() {
                        System.out.println("PosterMain.doTouchAction");
                    }

                    @Override
                    public void delete() {
                        System.out.println("PosterMain.delete");
                    }

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
                return "";
            }

            @Override
            public String getUsername() {
                if (Optional.fromNullable(PosterDataModel.helper().getCurrentUser()).isPresent()) {
                    String uname = PosterDataModel.helper().getCurrentUser().getName();
                    return capitalize(uname);
                }
                return "";
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
                            if (!foundFlag) {
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


        SaveButton saveButton = new SaveButton("Save", ZoneHelper.LOGOUT_BUTTON_WIDTH,
                                               ZoneHelper.LOGOUT_BUTTON_HEIGHT) {

            @Override
            public void saveAction() {
                DialogZoneController.dialog().showOKDialog("Saving...");
                PosterDataModel.helper().savePosterItemsForCurrentUser(false);
            }
        };

        saveButton.initButton();
        saveButton.setVisible(true);

        DialogZoneController.dialog().createControlPage(logoutZone, removeModeButton, editModeButton, saveButton);



    }

    protected void toggleIsEdit(boolean isEditing) {
        lastIsEditToggle = isEditing;
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


    public void hideAlZones() {
        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone) {
                zone.setVisible(false);
            }
        }
    }

    public void removeAlZones() {
        for (Zone zone : SMT.getZones()) {
            if (zone != null) {
                if (zone instanceof PictureZone) {
                    SMT.remove(zone);
                }
            }
        }
    }


}
