package ltg.evl.uic.poster.widgets;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.looksgood.ani.Ani;
import ltg.evl.uic.poster.json.mongo.Poster;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadClassListener;
import ltg.evl.uic.poster.listeners.LoadPosterListener;
import ltg.evl.uic.poster.listeners.LoadUserListener;
import org.apache.commons.lang.StringUtils;
import processing.core.PVector;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

import java.util.Collection;

/**
 * Created by aperritano on 3/30/15.
 */
public class DialogZoneController implements LoadUserListener, LoadPosterListener, LoadClassListener {

    public static String userpage_id = "UserPage";
    public static String posterpage_id = "PosterPage";
    public static String classpage_id = "ClassPage";
    public static String presentation_id = "BackgroundPage";
    private static DialogZoneController ourInstance = new DialogZoneController();
    int c_width = 800;
    int c_height = 450;
    int x = (SMT.getApplet().getWidth() / 2) - (c_width / 2);
    int y = (SMT.getApplet().getHeight() / 2) - (c_height / 2);
    private ClassPage classPage;
    private UserPageZone userPage;
    private PosterPageZone posterPage;
    private Ani ani;
    private float aniSpeed = 1.0f;
    private PresentationZone presentationZone = new PresentationZone(presentation_id, 0, 0, SMT.getApplet().getWidth(),
                                                                     SMT.getApplet().getHeight());

    private DialogZoneController() {
    }

    public static DialogZoneController getInstance() {
        return ourInstance;
    }

    public void registerForLoginEvent(Object obj) {
        PosterDataModel.helper().registerObject(obj);
    }

    public void hideUserPage() {
//        Zone zone = SMT.get(userpage_id);
//        SMT.remove(zone.getChildren());
        SMT.remove(userpage_id);
    }

    public void hidePosterPage() {
//        Zone zone = SMT.get(posterpage_id);
//        SMT.remove(zone.getChildren());
        SMT.remove(posterpage_id);
    }

    public void hideBackgroundPage() {
//        Zone zone = SMT.get(presentation_id);
//        SMT.remove(zone.getChildren());
        SMT.remove(presentation_id);
    }

    public void hideClassPage() {
        Zone zone = SMT.get(classpage_id);
        SMT.remove(zone.getChildren());
        SMT.remove(classpage_id);
    }

    public void showClassPage() {

        Zone foundZone = SMT.get(presentation_id);
        if (foundZone == null) {
            if (SMT.add(presentationZone)) {
                presentationZone.fade(2.0f, 0f, 255, false);
            }
        }

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
                return StringUtils.lowerCase(input.getClassname()).equals("mike");
            }
        };

        Collection<User> resultMike = Collections2.filter(imAllUsers, predicateBen);


        ImmutableMap.Builder<String, Collection<User>> builder = new ImmutableMap.Builder<String, Collection<User>>();

        if (!resultBen.isEmpty())
            builder.put("Ben", resultBen);

        if (!resultMike.isEmpty())
            builder.put("Mike", resultMike);


        ImmutableMap<String, Collection<User>> classMap = builder.build();

        if (!classMap.isEmpty()) {

            int size = classMap.keySet().size();

            int num_per_col = 2;
            int reminder = size % num_per_col;

            int rows = ((size - reminder) / 2) + reminder;

            int total_width = (num_per_col * 25) + (num_per_col * 175) + 25;
            int total_height = (rows * 25) + (rows * 175) + 25;

            int x2 = (SMT.getApplet().getWidth() / 2) - (total_width / 2);
            int y2 = (SMT.getApplet().getHeight() / 2) - (total_height / 2);

            classPage = new ClassPage(classpage_id, x2, SMT.getApplet().getHeight(), c_width, c_height, this);
            classPage.setWidth(total_width);
            classPage.setHeight(total_height);
            classPage.addClasses(classMap);


            if (SMT.add(classPage)) {
                classPage.startAni(new PVector(x2, y2), aniSpeed, 0f);
            }

        }
    }

    public void showUserPage(String classname) {

        userPage = new UserPageZone(userpage_id, x, SMT.getApplet().getHeight(), c_width, c_height, this);

        ImmutableList<User> imAllUsers = ImmutableList.copyOf(PosterDataModel.helper().allUsers);

        ImmutableMap.Builder<String, User> builder = new ImmutableMap.Builder<String, User>();
        for (User user : imAllUsers) {
            if (user.getClassname().equals(classname))
                builder.put(user.getUuid(), user);
        }

        ImmutableMap<String, User> userMap = builder.build();
        int size = userMap.keySet().size();

        int num_per_col = 4;
        int reminder = size % num_per_col;

        int rows = reminder;

        int total_width = (num_per_col * 25) + (num_per_col * 175) + 25;
        int total_height = (rows * 25) + (rows * 175) + 25;
        userPage.setWidth(total_width);
        userPage.setHeight(total_height);
        userPage.addUsers(userMap);
        if (SMT.add(userPage)) {
            userPage.startAni(new PVector(x, y), aniSpeed, 0f);
        }
    }

    public void showPosterPage() {
        Collection<Poster> allPostersForUser = PosterDataModel.helper().getAllPostersForCurrentUser();

        if (!allPostersForUser.isEmpty()) {
            createPosterLayout(allPostersForUser);
        } else {
            //flag error, NOT FOUND
        }
    }

    private void createPosterLayout(Collection<Poster> allPostersForUser) {


        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<String, String>();
        for (Poster poster : allPostersForUser) {
            builder.put(poster.getUuid(), poster.getName());
        }

        ImmutableMap<String, String> posterMap = builder.build();
        int size = posterMap.keySet().size();

        int buttonWidth = 275;
        int buttonHeight = 175;

        int num_per_col = 2;
        int reminder = size % num_per_col;

        int rows = ((size - reminder) / num_per_col) + reminder;

        int total_width = (num_per_col * 25) + (num_per_col * buttonWidth) + 25;
        int total_height = (rows * 25) + (rows * buttonHeight) + 25;

        int x2 = (SMT.getApplet().getWidth() / 2) - (total_width / 2);
        int y2 = (SMT.getApplet().getHeight() / 2) - (total_height / 2);
        posterPage = new PosterPageZone(posterpage_id, x2, SMT.getApplet().getHeight(), c_width, c_height, this);

        posterPage.setWidth(total_width);
        posterPage.setHeight(total_height);
        posterPage.addPosters(posterMap, buttonWidth, buttonHeight);


        if (SMT.add(posterPage)) {
            posterPage.startAni(new PVector(x2, y2), aniSpeed, 0f);
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
        presentationZone.fade(1.0f, 0f, 0, true);
    }


    @Override
    public void loadClass(String classname) {
        PosterDataModel.helper().loadClass(classname);
    }

    public Ani getAni() {
        return ani;
    }

    public void setAni(Ani ani) {
        this.ani = ani;
    }
}
