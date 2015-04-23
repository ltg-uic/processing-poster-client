package ltg.evl.uic.poster.widgets;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import ltg.evl.uic.poster.json.mongo.Poster;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadClassListener;
import ltg.evl.uic.poster.listeners.LoadPosterListener;
import ltg.evl.uic.poster.listeners.LoadUserListener;
import ltg.evl.util.de.looksgood.ani.Ani;
import org.apache.commons.lang.StringUtils;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.awt.*;
import java.util.Collection;

public class DialogZoneController implements LoadUserListener, LoadPosterListener, LoadClassListener {

    public static String userpage_id = "UserPage";
    public static String posterpage_id = "PosterPage";
    public static String classpage_id = "ClassPage";
    public static String presentation_id = "PresentationPage";
    private static DialogZoneController ourInstance = new DialogZoneController();
    public String controlpage_id = "ControlPage";
    int c_width = 800;
    int c_height = 450;
    int x = (SMT.getApplet().getWidth() / 2) - (c_width / 2);
    int y = (SMT.getApplet().getHeight() / 2) - (c_height / 2);
    private Ani ani;
    private float aniSpeed = 1.0f;
    private PresentationZone presentationZone;


    private DialogZoneController() {
    }

    //protected int which_showing

    public static DialogZoneController dialog() {
        return ourInstance;
    }

    public void showPage(PAGE_TYPES page) {
        switch (page) {
            case CLASS_PAGE:
                this.showClassPage();
                break;
            case POSTER_PAGE:
                this.showPosterPage();
                break;
            case USER_PAGE:
                this.showUserPage();
                break;
            case NO_PRES:
                this.hidePresentationPage();
                break;
            case PRES:
                showPresentationPage();
                break;
            case NONE:
                this.hideClassPage();
                this.hideUserPage();
                this.hidePosterPage();
                break;
        }
    }

    public void removeZone(String id) {
        for (Zone zone : SMT.getZones()) {
            if (zone.getName().equals(id)) {
                zone.setVisible(false);
                SMT.remove(id);
            }
        }

    }

    public void showPresentationPage() {

        Optional<PresentationZone> presentationZoneOptional = Optional.fromNullable(presentationZone);
        if (presentationZoneOptional.isPresent()) {
            SMT.remove(presentationZone);
        }


        presentationZone = new PresentationZone("presentation_id") {
            @Override
            public void doTouchAction() {
                DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.NONE);
                DialogZoneController.dialog().showPage(DialogZoneController.PAGE_TYPES.CLASS_PAGE);
            }
        };
        presentationZone.setBgAlpha(0);
        SMT.add(presentationZone);
        presentationZone.fade(1.0f, 0f, 255, false);

    }


    public void hidePresentationPage() {
        Optional<PresentationZone> presentationZoneOptional = Optional.fromNullable(presentationZone);
        if (presentationZoneOptional.isPresent()) {
            PresentationZone presentationZone = presentationZoneOptional.get();
            presentationZone.fade(1.0f, 0f, 0, true);
        }
    }

    public void hideUserPage() {
        this.removeZone(userpage_id);
    }

    public void hidePosterPage() {
        this.removeZone(posterpage_id);
    }

    public void hideClassPage() {
        this.removeZone(classpage_id);
    }

    public void showClassPage() {

        ImmutableList<User> imAllUsers = ImmutableList.copyOf(PosterDataModel.helper().allUsers);

        Predicate<User> predicateBen = new Predicate<User>() {
            @Override
            public boolean apply(User input) {
                return StringUtils.lowerCase(input.getClassname()).equals("ben");
            }
        };

        Collection<User> resultBen = Collections2.filter(imAllUsers, predicateBen);


        Predicate<User> predicateMike = new Predicate<User>() {
            @Override
            public boolean apply(User input) {
                return StringUtils.lowerCase(input.getClassname()).equals("michael");
            }
        };

        Collection<User> resultMike = Collections2.filter(imAllUsers, predicateMike);

        Predicate<User> predicateTest = new Predicate<User>() {
            @Override
            public boolean apply(User input) {
                return StringUtils.lowerCase(input.getClassname()).equals("test");
            }
        };

        Collection<User> resultTest = Collections2.filter(imAllUsers, predicateTest);

        ImmutableMap.Builder<String, Collection<User>> builder = new ImmutableMap.Builder<>();

        if (!resultBen.isEmpty())
            builder.put("Ben", resultBen);

        if (!resultMike.isEmpty())
            builder.put("Michael", resultMike);

        if (!resultTest.isEmpty())
            builder.put("Test", resultTest);


        ImmutableMap<String, Collection<User>> classMap = builder.build();

        if (!classMap.isEmpty()) {

            int size = classMap.keySet().size();





            //int rows = ((size - reminder) / 2) + reminder;
            int rows = 1;
            Dimension dimension = ZoneHelper.calcGrid(rows, size, ZoneHelper.GRID_SPACER, ZoneHelper.CLASS_BUTTON_SIZE,
                                                      ZoneHelper.CLASS_BUTTON_SIZE);

            int total_width = (int) dimension.getWidth();
            int total_height = (int) dimension.getHeight();


            int x2 = (SMT.getApplet().getWidth() / 2) - (total_width / 2);
            int y2 = (SMT.getApplet().getHeight() / 2) - (total_height / 2);

            ClassPage classPage = new ClassPage(classpage_id, x2, SMT.getApplet().getHeight(), total_width,
                                                total_height, this);
            classPage.addClasses(classMap);


            if (SMT.add(classPage)) {
                classPage.startAni(new PVector(x2, y2), aniSpeed, 0f);
            }

        }
    }

    public void showUserPage() {

        Optional<String> classNamOp = Optional.fromNullable(PosterDataModel.helper().getCurrentClassName());
        Optional<Collection<User>> classUsersOp = Optional.fromNullable(
                PosterDataModel.helper().getCurrentClassUsers());
        if (classNamOp.isPresent() && classUsersOp.isPresent()) {


            ImmutableList<User> imAllClassUsers = ImmutableList.copyOf(classUsersOp.get());


            if (!imAllClassUsers.isEmpty()) {
                int size = imAllClassUsers.size();

                int cols = 10;

                int reminder;
                int rows;
                if (size < cols) {
                    cols = size;
                    reminder = 1;
                } else {
                    reminder = size % cols;
                }

                rows = reminder;


                Dimension dimension = ZoneHelper.calcGrid(rows, cols, ZoneHelper.GRID_SPACER, ZoneHelper.BUTTON_WIDTH,
                                                          ZoneHelper.BUTTON_HEIGHT);

                int total_width = (int) dimension.getWidth();
                int total_height = (int) dimension.getHeight();


                int x2 = (SMT.getApplet().getWidth() / 2) - (total_width / 2);
                int y2 = (SMT.getApplet().getHeight() / 2) - (total_height / 2);
                UserPageZone userPage = new UserPageZone(userpage_id, x2, SMT.getApplet().getHeight(),
                                                         (int) dimension.getWidth(),
                                                         (int) dimension.getHeight(), this);

                userPage.addUsers(imAllClassUsers);
                //userPage.setY(y2);
                if (SMT.add(userPage)) {
                    userPage.startAni(new PVector(x2, y2), aniSpeed, 0f);
                }
            }
        }
    }

    public void showPosterPage() {

        Optional<Collection<Poster>> collectionOptional = Optional.fromNullable(
                PosterDataModel.helper().getCurrentUserPosters());

        if (collectionOptional.isPresent()) {
            Collection<Poster> posters = collectionOptional.get();

            if (!posters.isEmpty()) {

                int size = posters.size();
                int cols = 10;

                int reminder;
                int rows;
                if (size < cols) {
                    cols = size;
                    reminder = 1;
                } else {
                    reminder = size % cols;
                }

                rows = reminder;

                Dimension dimension = ZoneHelper.calcGrid(rows, cols, ZoneHelper.GRID_SPACER,
                                                          ZoneHelper.POSTER_BUTTON_WIDTH,
                                                          ZoneHelper.POSTER_BUTTON_HEIGHT);

                int total_width = (int) dimension.getWidth();
                int total_height = (int) dimension.getHeight();


                int x2 = (SMT.getApplet().getWidth() / 2) - (total_width / 2);
                int y2 = (SMT.getApplet().getHeight() / 2) - (total_height / 2);
                PosterPageZone posterPage = new PosterPageZone(posterpage_id, x2, SMT.getApplet().getHeight(),
                                                               total_width,
                                                               total_height, this);
                posterPage.addPosters(posters);

                posterPage.setY(y2);

                if (SMT.add(posterPage)) {
                    posterPage.startAni(new PVector(x2, y2), aniSpeed, 0f);
                }
            }
        }
    }

    public void createControlPage(Zone... userButtons) {
        int cols = 1;
        int rows = 3;

        Dimension dimension = ZoneHelper.calcGrid(rows, cols, ZoneHelper.GRID_SPACER, ZoneHelper.LOGOUT_BUTTON_WIDTH,
                                                  ZoneHelper.LOGOUT_BUTTON_HEIGHT);

        int total_width = (int) dimension.getWidth();
        int total_height = (int) dimension.getHeight();


        int x2 = 25;
        int y2 = 25;

        ControlPage controlPage = new ControlPage(controlpage_id, -total_width + 15, y2, total_width,
                                                  total_height);


        controlPage.addButtons(userButtons);
        controlPage.setTargetPoint(new PVector(x2, y2));
        controlPage.setVisible(false);
        if (SMT.add(controlPage)) {
            controlPage.setVisible(true);
            SMT.putZoneOnTop(controlPage);
            //controlPage.startAni(new PVector(x2, y2), aniSpeed, 0f);
        }
    }

    @Override
    public void loadUser(String userUuid, int buttonColor) {
        PosterDataModel.helper().loadUser(userUuid, buttonColor);
    }

    @Override
    public void loadUser(User user) {
        PosterDataModel.helper().loadUser(user);
    }

    @Override
    public void loadPoster(String posterUuid) {
        PosterDataModel.helper().loadPoster(posterUuid);
    }

    @Override
    public void loadClass(String classname) {
        PosterDataModel.helper().fetchAllUsersForCurrentClass(classname);
    }

    public Ani getAni() {
        return ani;
    }

    public void setAni(Ani ani) {
        this.ani = ani;
    }

    public enum PAGE_TYPES {CLASS_PAGE, POSTER_PAGE, USER_PAGE, NONE, NO_PRES, PRES, CONTROL}
}
