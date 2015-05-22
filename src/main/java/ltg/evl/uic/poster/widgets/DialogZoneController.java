package ltg.evl.uic.poster.widgets;

import com.google.api.client.util.Lists;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import ltg.evl.uic.poster.json.mongo.Poster;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.PosterGrab;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadClassListener;
import ltg.evl.uic.poster.listeners.LoadPosterListener;
import ltg.evl.uic.poster.listeners.LoadUserListener;
import ltg.evl.uic.poster.util.de.looksgood.ani.Ani;
import ltg.evl.uic.poster.widgets.page.ClassPage;
import ltg.evl.uic.poster.widgets.page.ControlPage;
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

    public static String share_userpage_id = "ShareUserPage";
    public static String share_classpage_id = "ShareClassPage";

    private static DialogZoneController ourInstance = new DialogZoneController();
    public String controlpage_id = "ControlPage";
    int c_width = 800;
    int c_height = 450;
    int x = (SMT.getApplet().getWidth() / 2) - (c_width / 2);
    int y = (SMT.getApplet().getHeight() / 2) - (c_height / 2);
    private Ani ani;
    private float aniSpeed = 1.0f;
    private PresentationZone presentationZone;
    private boolean isShowing;
    private PresentationZone okDialog;
    private PosterGrab sharingObject;


    private DialogZoneController() {
    }

    //protected int which_showing

    public static DialogZoneController dialog() {
        return ourInstance;
    }

    public PosterGrab getSharingObject() {
        return sharingObject;
    }

    public void setSharingObject(PosterGrab sharingObject) {
        this.sharingObject = sharingObject;
    }

    public void showPage(PAGE_TYPES page) {
        switch (page) {
            case CLASS_PAGE:
                this.showClassPage(classpage_id);
                setIsShowing(true);
                break;
            case POSTER_PAGE:
                this.showPosterPage(posterpage_id);
                setIsShowing(true);
                break;
            case USER_PAGE:
                this.showUserPage(userpage_id);
                setIsShowing(true);
                break;
            case NO_PRES:
                this.hidePresentationPage();
                setIsShowing(false);
                break;
            case PRES:
                showPresentationPage();
                setIsShowing(true);
                break;
            case NONE:
                this.hidePage(classpage_id);
                this.hidePage(userpage_id);
                this.hidePage(posterpage_id);
                setIsShowing(false);
                break;
            case SHARE_CLASSPAGE:
                this.showClassPage(share_classpage_id);
                setIsShowing(true);
                break;
            case SHARE_USER_PAGE:
                this.showUserPage(share_userpage_id);
                setIsShowing(true);
                break;
            case SHARE_NONE:
                this.hidePage(share_classpage_id);
                this.hidePage(share_userpage_id);
                setIsShowing(false);
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

    public void hidePage(String page_id) {
        this.removeZone(page_id);
    }

    public void showClassPage(String page_id) {

        ImmutableMap<String, Collection<User>> classMap;

        if (page_id.equals(classpage_id)) {
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


            classMap = builder.build();
        } else {


            Iterable<String> splitTest = Splitter.on(',')
                                                 .trimResults()
                                                 .omitEmptyStrings()
                                                 .split("alexander," +
                                                                "aman," +
                                                                "anika," +
                                                                "ayleen," +
                                                                "blaede," +
                                                                "claire," +
                                                                "eli," +
                                                                "erin," +
                                                                "hamish," +
                                                                "harper," +
                                                                "imogen," +
                                                                "jayden," +
                                                                "jessica," +
                                                                "julia," +
                                                                "lindsay," +
                                                                "marten," +
                                                                "morgan," +
                                                                "norah," +
                                                                "rahim," +
                                                                "ruby," +
                                                                "sam," +
                                                                "teadora," +
                                                                "thomas");

            Iterable<String> splitBen = Splitter.on(',')
                                                .trimResults()
                                                .omitEmptyStrings().split("abby," +
                                                                                  "adam," +
                                                                                  "amelia," +
                                                                                  "anna," +
                                                                                  "anthony," +
                                                                                  "carson," +
                                                                                  "chloe," +
                                                                                  "cole," +
                                                                                  "daeja," +
                                                                                  "elliot," +
                                                                                  "eva," +
                                                                                  "frank," +
                                                                                  "kismet," +
                                                                                  "liam," +
                                                                                  "marshall," +
                                                                                  "michael," +
                                                                                  "mira," +
                                                                                  "mylo," +
                                                                                  "niku," +
                                                                                  "phoebe," +
                                                                                  "teacher," +
                                                                                  "trevor," +
                                                                                  "vita," +
                                                                                  "will");

            Iterable<String> splitMike = Splitter.on(',')
                                                 .trimResults()
                                                 .omitEmptyStrings().split("alexander," +
                                                                                   "aman," +
                                                                                   "anika," +
                                                                                   "ayleen," +
                                                                                   "blaede," +
                                                                                   "claire," +
                                                                                   "eli," +
                                                                                   "erin," +
                                                                                   "hamish," +
                                                                                   "harper," +
                                                                                   "imogen," +
                                                                                   "jayden," +
                                                                                   "jessica," +
                                                                                   "julia," +
                                                                                   "lindsay," +
                                                                                   "marten," +
                                                                                   "morgan," +
                                                                                   "norah," +
                                                                                   "rahim," +
                                                                                   "ruby," +
                                                                                   "sam," +
                                                                                   "teacher," +
                                                                                   "teadora," +
                                                                                   "thomas");

            Collection<User> resultBen = Lists.newArrayList();
            for (String s : splitBen) {
                resultBen.add(new User(s, "Ben"));
            }

            Collection<User> resultMike = Lists.newArrayList();
            for (String s : splitMike) {
                resultMike.add(new User(s, "Michael"));
            }

            Collection<User> resultTest = Lists.newArrayList();
            for (String s : splitTest) {
                resultMike.add(new User(s, "Test"));
            }


            ImmutableMap.Builder<String, Collection<User>> builder = new ImmutableMap.Builder<>();

            if (!resultBen.isEmpty())
                builder.put("Ben", resultBen);

            if (!resultMike.isEmpty())
                builder.put("Michael", resultMike);

            if (!resultTest.isEmpty())
                builder.put("Test", resultTest);


            classMap = builder.build();

        }

        if (!classMap.isEmpty()) {

            int size = classMap.keySet().size();


            //int rows = ((size - reminder) / 2) + reminder;
            int rows = 1;
            Dimension dimension = ZoneHelper.calcGrid(size, size, ZoneHelper.GRID_SPACER, ZoneHelper.CLASS_BUTTON_SIZE,
                                                      ZoneHelper.CLASS_BUTTON_SIZE);

            int total_width = (int) dimension.getWidth();
            int total_height = (int) dimension.getHeight();


            int x2 = (SMT.getApplet().getWidth() / 2) - (total_width / 2);
            int y2 = (SMT.getApplet().getHeight() / 2) - (total_height / 2);

            ClassPage classPage;

            boolean isShare = false;

            LoadClassListener lcl = null;
            String title = null;
            if (page_id.equals(share_classpage_id)) {
                isShare = true;
                lcl = new LoadClassListener() {
                    @Override
                    public void loadClass(String classname) {
                        PosterDataModel.helper().fetchAllUsersForCurrentClass(classname, true);

                    }
                };
            } else {
                lcl = this;
            }
            classPage = new ClassPage(page_id, x2, SMT.getApplet().getHeight(), total_width,
                                      total_height, lcl, isShare);


            classPage.addClasses(classMap);


            if (SMT.add(classPage)) {
                classPage.startAni(new PVector(x2, y2), aniSpeed, 0f);
            }

        }
    }

    public void showUserPage(String page_id) {

        Optional<String> classNamOp = Optional.fromNullable(PosterDataModel.helper().getCurrentClassName());
        Optional<Collection<User>> classUsersOp = Optional.fromNullable(
                PosterDataModel.helper().getCurrentClassUsers());
        if (classNamOp.isPresent() && classUsersOp.isPresent()) {


            ImmutableList<User> imAllClassUsers = ImmutableList.copyOf(classUsersOp.get());


            if (!imAllClassUsers.isEmpty()) {
                int size = imAllClassUsers.size();

                double cols = 4.0;


                Dimension dimension = ZoneHelper.calcGrid(size, cols, ZoneHelper.GRID_SPACER, ZoneHelper.BUTTON_WIDTH,
                                                          ZoneHelper.BUTTON_HEIGHT);

                int total_width = (int) dimension.getWidth();
                int total_height = (int) dimension.getHeight();


                int x2 = (SMT.getApplet().getWidth() / 2) - (total_width / 2);
                int y2 = (SMT.getApplet().getHeight() / 2) - (total_height / 2);
                boolean isShare = false;
                LoadUserListener lul;
                if (page_id.equals(share_userpage_id)) {


                    isShare = true;
                    lul = new LoadUserListener() {
                        @Override
                        public void loadUser(String userUuid, int buttonColor) {

                        }

                        @Override
                        public void loadUser(User user) {
                            PosterDataModel.helper().loadUser(user, true);
                        }
                    };

                } else {
                    isShare = false;
                    lul = this;
                }


                UserPageZone userPage = new UserPageZone(page_id, x2, SMT.getApplet().getHeight(),
                                                         (int) dimension.getWidth(),
                                                         (int) dimension.getHeight(), lul, isShare);

                userPage.addUsers(imAllClassUsers);
                //userPage.setY(y2);
                if (SMT.add(userPage)) {
                    userPage.startAni(new PVector(x2, y2), aniSpeed, 0f);
                }
            }
        }
    }

    public void showPosterPage(String posterpage_id) {

        Optional<Collection<Poster>> collectionOptional = Optional.fromNullable(
                PosterDataModel.helper().getCurrentUserPosters());

        if (collectionOptional.isPresent()) {
            Collection<Poster> posters = collectionOptional.get();

            if (!posters.isEmpty()) {

                double size = posters.size();
                double cols = 4.0;


                Dimension dimension = ZoneHelper.calcGrid(size, cols, ZoneHelper.GRID_SPACER,
                                                          ZoneHelper.POSTER_BUTTON_WIDTH,
                                                          ZoneHelper.POSTER_BUTTON_HEIGHT);

                int total_width = (int) dimension.getWidth();
                int total_height = (int) dimension.getHeight();


                int x2 = (SMT.getApplet().getWidth() / 2) - (total_width / 2);
                int y2 = (SMT.getApplet().getHeight() / 2) - (total_height / 2);
                PosterPageZone posterPage = new PosterPageZone(DialogZoneController.posterpage_id, x2,
                                                               SMT.getApplet().getHeight(),
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

    public void showOKDialog(String text) {
        if (okDialog != null) {
            okDialog.setOkDialogText(text);
        } else {
            okDialog = new PresentationZone("okDialog", 0, 0,
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
                    hideOKDialog();
                }

                @Override
                public void doCancelAction() {
                    super.doCancelAction();
                    hideOKDialog();
                }
            };
            okDialog.showOKDialog(text, 200);
            SMT.add(okDialog);
        }


    }

    public void hideOKDialog() {
        if (okDialog != null) {
            SMT.remove(okDialog);
            okDialog = null;
        }
    }

    @Override
    public void loadUser(String userUuid, int buttonColor) {
        PosterDataModel.helper().loadUser(userUuid, buttonColor);
    }

    @Override
    public void loadUser(User user) {
        PosterDataModel.helper().loadUser(user, false);
    }

    @Override
    public void loadPoster(String posterUuid) {
        PosterDataModel.helper().loadPoster(posterUuid);
    }

    @Override
    public void loadClass(String classname) {
        PosterDataModel.helper().fetchAllUsersForCurrentClass(classname, false);
    }

    public Ani getAni() {
        return ani;
    }

    public void setAni(Ani ani) {
        this.ani = ani;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setIsShowing(boolean isShowing) {
        this.isShowing = isShowing;
    }

    public enum PAGE_TYPES {CLASS_PAGE, POSTER_PAGE, USER_PAGE, NONE, NO_PRES, PRES, CONTROL, SHARE_CLASSPAGE, SHARE_USER_PAGE, SHARE_NONE}



}
