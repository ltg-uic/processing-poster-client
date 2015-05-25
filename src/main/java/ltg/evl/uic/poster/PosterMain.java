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
import ltg.evl.uic.poster.widgets.button.SaveButton;
import ltg.evl.uic.poster.widgets.button.ShareButton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;
import vialab.SMT.Zone;

import java.io.IOException;
import java.util.Collection;

import static org.apache.commons.lang.WordUtils.capitalize;

public class PosterMain extends PApplet implements LoginCollectionListener {


    protected static org.apache.log4j.Logger logger = Logger.getLogger(PosterMain.class.getName());
    private boolean lastIsEditToggle = false;

    public static void main(String args[]) {
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
        logger.info("UserLoaded: " + loginDialogEvent.getUuid());

        if (loginDialogEvent.getEventType().equals(LoginDialogEvent.EVENT_TYPES.USER)) {
            DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
            DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.POSTER_PAGE);
        } else {
            PosterItem posterItem = PosterDataModel.helper()
                                                   .findPosterItemWithPosterItemUuid(DialogZoneController.dialog()
                                                                                                         .getSharingObject()
                                                                                                         .getGrabbed_poster_item_uuid());


            DialogZoneController.dialog().getSharingObject().update(posterItem);
            DialogZoneController.dialog().getSharingObject().update(PosterDataModel.helper().getCurrentPoster());
            DialogZoneController.dialog().getSharingObject().update(PosterDataModel.helper().getCurrentUser(), true);
            DialogZoneController.dialog().getSharingObject().update(loginDialogEvent.getUser(), false);
            DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.SHARE_NONE);

            try {
                System.out.println(DialogZoneController.dialog().getSharingObject().toPrettyString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            new Thread() {
                @Override
                public void run() {
                    PosterDataModel.helper()
                                   .sendMQTTPosterGrabMessage(DialogZoneController.dialog().getSharingObject());
                }
            }.start();
        }

    }

    @Override
    public void loadPosterEvent(final LoginDialogEvent loginDialogEvent) {
        logger.info("PosterLoaded: " + loginDialogEvent.getUuid());
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
        DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NO_PRES);

        new Thread() {
            @Override
            public void run() {
                loadPosterItemsForCurrentUserAndPoster(
                        PosterDataModel.helper().getAllPostersItemsForPoster(loginDialogEvent.getUuid()), true);
            }
        }.start();

    }


    @Override
    public void loadClassEvent(LoginDialogEvent loginDialogEvent) {
        logger.info("ClassLoaded: " + loginDialogEvent.getUuid());

        if (loginDialogEvent.getEventType().equals(LoginDialogEvent.EVENT_TYPES.CLASS_NAME)) {
            DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
            DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.USER_PAGE);
        } else {
            DialogZoneController.dialog().getSharingObject().setClass_name(loginDialogEvent.getUuid());
            DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.SHARE_NO_CLASS);
            DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.SHARE_USER_PAGE);
        }

    }

    @Override
    public void updatePosterItem(ObjectEvent objectEvent) {

        PosterItem newPosterItem = (PosterItem) objectEvent.getGenericJson();
        logger.info("PosterItemLoaded: " + newPosterItem.getUuid());
        loadPosterItemsForCurrentUserAndPoster(Lists.newArrayList(newPosterItem), false);
    }

    @Override
    public void deletePosterItem(ObjectEvent objectEvent) {
        String posterItemId = objectEvent.getItemId();
        logger.info("PosterIdDeleted: " + posterItemId);
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
    public boolean sketchFullScreen() {
        return true;
    }

    @Override
    public void setup() {

        Ani.init(this);
        Ani.autostart();

        size(displayWidth, displayHeight, SMT.RENDERER);
        //hint(ENABLE_RETINA_PIXELS);
        SMT.init(this, TouchSource.AUTOMATIC);


        SMT.setTouchColor(33, 150, 243, 220);
        SMT.drawDebugTouchPoints();
        //SMT.setTouchDraw(TouchDraw.DEBUG);

        setupControlButtons();


        PosterDataModel.helper().addLoginCollectionListener(this);

        RESTHelper.getInstance().initAllCollections();


    }

    @Override
    public void draw() {
        background(ZoneHelper.lightGreyBackground);
        fill(0);
        text(round(frameRate) + "v1.4 fps, # of zones: " + SMT.getZones().length, width / 2, 10);
    }


    public void removeDupZone(String zoneName) {
        Zone[] zones = SMT.getZones();
        for (Zone zone : zones) {
            if (zone.getName().equals(zoneName)) {
                SMT.remove(zone);
                logger.info("REMOVED DUP ZONE: " + zoneName);
            }
        }
    }

    public void loadPosterItemsForCurrentUserAndPoster(Collection<PosterItem> posterItems, boolean shouldRemove) {



        if( !posterItems.isEmpty() ) {
            ZoneHelper.getInstance().maxX = 0;
            ZoneHelper.getInstance().maxY = 0;

            boolean needsFix = false;
            for(PosterItem posterItem: posterItems) {
                if (posterItem.getXn() < 0 ) {
                    needsFix = true;
                    break;
                }
            }
            if ( needsFix) {
                for (PosterItem p : posterItems) {
                    if ((p.getX() + p.getWidth()) > ZoneHelper.getInstance().maxX) {
                        ZoneHelper.getInstance().maxX = p.getX() + p.getWidth();
                    }
                    if ((p.getY() + p.getHeight()) > ZoneHelper.getInstance().maxY) {
                        ZoneHelper.getInstance().maxY = p.getY() + p.getHeight();
                    }

                }

                ZoneHelper.getInstance().maxX = 2560;
                ZoneHelper.getInstance().maxY = 1600;
            }

        }

//        ZoneHelper.getInstance().maxX = 2560;
//        ZoneHelper.getInstance().maxY = 1600;

        ZoneHelper.getInstance().maxX = 2560;
        ZoneHelper.getInstance().maxY = 1600;

        if (Optional.fromNullable(posterItems).isPresent()) {

            if (shouldRemove) {
                removeAlZones();
            }


            //first check to see if it is on the board

            for (PosterItem addPosterItem : posterItems) {
                this.removeDupZone(addPosterItem.getUuid());
            }

            FluentIterable<PictureZone> pictureZones = FluentIterable.from(posterItems)
                                                                     .transform(
                                                                             new PosterItemToPictureZone(
                                                                                     lastIsEditToggle));


            Optional<FluentIterable<PictureZone>> fluentIterableOptional = Optional.fromNullable(pictureZones);

            if (Optional.fromNullable(pictureZones).isPresent()) {

                ImmutableList<PictureZone> list = fluentIterableOptional.get().toList();


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


        ShareButton shareButton = new ShareButton("Group", ZoneHelper.LOGOUT_BUTTON_WIDTH,
                                                                 ZoneHelper.LOGOUT_BUTTON_HEIGHT) {
            @Override
            public void shareAction() {
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
        shareButton.initButton();
        // shareButton.setVisible(true);


        EditModeButton editModeButton = new EditModeButton("Edit", ZoneHelper.LOGOUT_BUTTON_WIDTH,
                                                           ZoneHelper.LOGOUT_BUTTON_HEIGHT) {
            @Override
            public void editAction() {
                toggleIsEdit(!lastIsEditToggle);
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

                new Thread() {

                    @Override
                    public void run() {
                        PosterDataModel.helper().savePosterItemsForCurrentUser(false);
                    }
                }.start();


            }
        };

        saveButton.initButton();
        saveButton.setVisible(true);

        DialogZoneController.dialog().createControlPage(logoutZone, editModeButton, saveButton);



    }

    protected void toggleIsEdit(boolean isEditing) {
        lastIsEditToggle = isEditing;
        Zone[] activeZones = SMT.getRootZone().getChildren();


        if (activeZones.length >= 1) {
            for (Zone zone : activeZones) {
                if (zone != null & zone instanceof PictureZone) {
                    PictureZone pz = (PictureZone) zone;
                    pz.setIsEditing(isEditing);
                    pz.setIsDeleteMode(isEditing);
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
