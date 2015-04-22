package tmp;

import ltg.commons.SimpleMQTTClient;
import ltg.evl.uic.poster.json.mongo.Poster;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.widgets.PictureZone;
import ltg.evl.uic.poster.widgets.buttons.UserButton;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import processing.core.PFont;
import vialab.SMT.SMT;
import vialab.SMT.TouchDraw;
import vialab.SMT.TouchSource;
import vialab.SMT.Zone;

import java.util.Collection;


@SuppressWarnings("SuspiciousNameCombination")
public class PosterTest extends PApplet {


    private static SimpleMQTTClient sc;
    private static Logger logger;
    int buttonWidth = 100;
    int buttonHeight = buttonWidth;
    int buttonStartX = 20;
    int buttonStartY = buttonStartX;
    int whiteColor = color(255);
    int greenButtonColor = color(35, 147, 70);
    int yellowButtonColor = color(244, 208, 63);
    private NewTextButton newTextButton;

    public static void main(String args[]) {

        logger = Logger.getLogger(PosterTest.class.getName());
        logger.setLevel(org.apache.log4j.Level.ALL);

//        sc = new SimpleMQTTClient("ltg.evl.uic.edu", "test-bot");
//        sc.subscribe("quakes", new MessageListener() {
//            @Override
//            public void processMessage(String message) {
//
//
//                SMT.add(new TextBoxZone("quake", ColorHelper.randInt(0,500), ColorHelper.randInt(0,500) ,300,35, message));
//                System.out.println("Received " + message );
//            }
//        });

        PApplet.main(new String[]{"tmp.PosterTest"});
    }


    public void setup() {

        int screen_width = 1200;
        int screen_height = 800;
        size(screen_width, screen_height, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);
        SMT.debug = true;
        SMT.setTouchDraw(TouchDraw.NONE);
        PFont controlButtonFont = createFont("HelveticaNeue-Bold", 15);


        //create mode buttons
        UserButton presentButton = new UserButton("PresentButton", buttonStartX, buttonStartY, buttonWidth,
                                                  buttonHeight,
                                                  "PRESENT", greenButtonColor, yellowButtonColor, controlButtonFont);
//        UserButton editButton = new UserButton("EditButton", buttonStartX,
//                                               presentButton.getY() + presentButton.getHeight() + presentButton.getY(),
//                                               buttonWidth, buttonHeight, "EDIT", greenButtonColor, yellowButtonColor,
//                                               controlButtonFont);
        // newTextButton = new NewTextButton("TextButton", loadImage("document6.png"), buttonStartX, editButton.getY() + editButton.getHeight() + editButton.getY() + 5,buttonWidth/2, buttonHeight/2 );
//
        // SMT.add(presentButton);
//        int c_width = 650;
//         int c_height = 450;
//        int x = (screen_width/2) - (c_width/2);
//        int y = (screen_height/2) - (c_height/2);
//
//        ObjectSubscriber objectSubscriber = new ObjectSubscriber() {
//            @Override
//            public void handleUpdatedObject(ObjectEvent objectEvent) {
//                super.handleUpdatedObject(objectEvent);
//                if (objectEvent.getEventType().equals(ObjectEvent.OBJ_TYPES.POST_ITEM)) {
//                    PosterItem newPosterItem = (PosterItem) objectEvent.getGenericJson();
//                    loadPosterItems(Lists.newArrayList(newPosterItem), false);
//                } else if (objectEvent.getEventType().equals(ObjectEvent.OBJ_TYPES.INIT_ALL)) {
//
//                    DialogZoneController.dialog().showClassPage();
//                    //DialogZoneController.helper().translateUserPage(300, 300);
//                    //loadPosterForUser(allUsers.get(0));                   }
//
//                } else if (objectEvent.getEventType().equals(ObjectEvent.OBJ_TYPES.DELETE_POSTER_ITEM)) {
//
//                    String posterItemId = objectEvent.getItemId();
////                    for(PosterItem pi : PosterDataModel.helper().allPosterItems) {
////                        if(pi.getUuid().equals(posterItemId)) {
////                            PosterDataModel.helper().allPosterItems.remove(pi);
////                        }
////                    }
//
//
//                    SMT.remove(posterItemId);
//
//                }
//
//            }
        //   };

//        UserLoadEvent ule = new UserLoadEvent() {
//            @Override
//            public void handleUserLoadEvent(String userUuid) {
//                super.handleUserLoadEvent(userUuid);
//                ImmutableList<User> imAllUsers = ImmutableList.copyOf(PosterDataModel.helper().allUsers);
//
//                if (!imAllUsers.isEmpty()) {
//                    for (User user : imAllUsers) {
//                        if (user.getUuid().equals(userUuid)) {
//                            DialogZoneController.dialog().hideUserPage();
//                            loadPosterForUser(user);
//                        }
//                    }
//                }
//
//            }
//        };


        //DialogZoneController.dialog().registerForLoginEvent(ule);

//        logger.debug("POSTER MODELER STARTED");
//        // PosterDataModel.helper().addUserSubscriber(userSubscriber);
//        //PosterDataModel.helper().registerForObjectEvent(objectSubscriber);
//
//        logger.debug("POSTER MODELER INIT ALL COLLECTIONS");
//        RESTHelper.getInstance().initAllCollections();
    }

    private void loadPosterForUser(User user) {

        Collection<Poster> allPostersForUser = PosterDataModel.helper().getAllPostersForUser(user);

        if (!allPostersForUser.isEmpty()) {
            Poster poster = allPostersForUser.iterator().next();

            Collection<PosterItem> allPostersItemsForPoster = PosterDataModel.helper()
                                                                             .getAllPostersItemsForPoster(poster);
            if (!allPostersItemsForPoster.isEmpty()) {
                loadPosterItems(allPostersItemsForPoster, true);
            }
        }

    }

    public void loadPosterItems(Collection<PosterItem> posterItems, boolean shouldRemove) {
//
//        if (shouldRemove)
//            removeAlZones();
//
//        List<PosterItem> textItems = Lists.newArrayList();
//        List<PosterItem> pictureItems = Lists.newArrayList();
//
//        for (PosterItem posterItem : posterItems) {
//            if (posterItem.getType().equals(PosterItem.TEXT)) {
//                textItems.add(posterItem);
//            } else {
//                pictureItems.add(posterItem);
//            }
//        }
//
//        FluentIterable<Callable<PictureZone>> pictureZones = FluentIterable.from(pictureItems)
//                                                                 .transform(new PosterItemToPictureZone());
//
//        FluentIterable<TextBoxZone> textZones = FluentIterable.from(textItems)
//                                                              .transform(new PosterItemToTextZone());
//
//
//        for (PictureZone pictureZone : pictureZones) {
//            if (pictureZone != null) {
//
//                boolean hasAdded = SMT.add(pictureZone);
//                if (hasAdded) {
//                    // topBarZone.stopTimer();
//                    pictureZone.startAnimation(true);
//                }
//            }
//        }
//
//        for (TextBoxZone textZone : textZones) {
//            if (textZone != null) {
//                boolean hasAdded = SMT.add(textZone);
////                if (hasAdded) {
////                    // topBarZone.stopTimer();
////                    pictureZone.startAnimation(true);
////                }
//            }
//        }

    }

    public void removeAlZones() {
        for (Zone zone : SMT.getZones()) {
            if (zone instanceof PictureZone) {
                SMT.remove(zone);
            }
        }
    }

    public void draw() {
        background(255);
        fill(0);
        text(round(frameRate) + "fps, # of zones: " + SMT.getZones().length, width / 2, 10);
    }


    public void pressTextButton(NewTextButton zone) {

//        int x = new Float(random(10, 500)).intValue();
//        int y = new Float(random(10, 500)).intValue();


    }

}
