package ltg.evl.uic.poster.widgets;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import ltg.evl.uic.poster.json.mongo.Poster;
import ltg.evl.uic.poster.json.mongo.PosterDataModel;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.listeners.LoadClassListener;
import ltg.evl.uic.poster.listeners.LoadPosterListener;
import ltg.evl.uic.poster.listeners.LoadUserListener;
import org.apache.commons.lang.StringUtils;
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
    private static DialogZoneController ourInstance = new DialogZoneController();
    int c_width = 800;
    int c_height = 450;
    int x = (SMT.getApplet().getWidth() / 2) - (c_width / 2);
    int y = (SMT.getApplet().getHeight() / 2) - (c_height / 2);

    private ClassPage classPage = new ClassPage(classpage_id, x, y, c_width, c_height, this);
    private UserPageZone userPage = new UserPageZone(userpage_id, x, y, c_width, c_height, this);
    private PosterPageZone posterPage = new PosterPageZone(posterpage_id, x, y, c_width, c_height, this);
    private EventBus userDialogEventBus = new EventBus();
    private EventBus posterDialogEventBus = new EventBus();

    private DialogZoneController() {
    }

    public static DialogZoneController getInstance() {
        return ourInstance;
    }

    public void registerForLoginEvent(Object obj) {
        PosterDataModel.helper().registerObject(obj);
    }

    public void registerForPosterEvent(Object obj) {
        posterDialogEventBus.post(obj);
    }

    public void translateUserPage(int x, int y) {
        userPage.translate(x, y);
    }

    public void hideUserPage() {
        Zone zone = SMT.get(userpage_id);
        SMT.remove(zone.getChildren());
        SMT.remove(userpage_id);
    }

    public void hidePosterPage() {
        Zone zone = SMT.get(posterpage_id);
        SMT.remove(zone.getChildren());
        SMT.remove(posterpage_id);
    }

    public void hideClassPage() {
        Zone zone = SMT.get(classpage_id);
        SMT.remove(zone.getChildren());
        SMT.remove(classpage_id);
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
                return StringUtils.lowerCase(input.getClassname()).equals("mike");
            }
        };

        Collection<User> resultMike = Collections2.filter(imAllUsers, predicateBen);

        ImmutableMap.Builder<String, Collection<User>> builder = new ImmutableMap.Builder<String, Collection<User>>();

        builder.put("Ben", resultBen);
        builder.put("Mike", resultMike);
        ImmutableMap<String, Collection<User>> classMap = builder.build();


        int size = classMap.keySet().size();

        int num_per_col = 2;
        int reminder = size % num_per_col;

        int rows = ((size - reminder) / 2) + reminder;

        int total_width = (num_per_col * 25) + (num_per_col * 175) + 25;
        int total_height = (rows * 25) + (rows * 175) + 25;

        int x2 = (SMT.getApplet().getWidth() / 2) - (total_width / 2);
        int y2 = (SMT.getApplet().getHeight() / 2) - (total_height / 2);

        classPage.setWidth(total_width);
        classPage.setHeight(total_height);
        classPage.setX(x2);
        classPage.setY(y2);
        classPage.addClasses(classMap);


        SMT.add(classPage);


    }

    public void showUserPage(String classname) {

        ImmutableList<User> imAllUsers = ImmutableList.copyOf(PosterDataModel.helper().allUsers);

        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<String, String>();
        for (User user : imAllUsers) {
            if (user.getClassname().equals(classname))
                builder.put(user.getUuid(), user.getName());
        }

        ImmutableMap<String, String> userMap = builder.build();
        int size = userMap.keySet().size();

        int num_per_col = 4;
        int reminder = size % num_per_col;

        int rows = ((size - reminder) / 4) + reminder;

        int total_width = (num_per_col * 25) + (num_per_col * 175) + 25;
        int total_height = (rows * 25) + (rows * 175) + 25;
        userPage.setWidth(total_width);
        userPage.setHeight(total_height);
        userPage.addUsers(userMap);


        SMT.add(userPage);
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

        int buttonWidth = 350;
        int buttonHeight = 175;

        int num_per_col = 2;
        int reminder = size % num_per_col;

        int rows = ((size - reminder) / num_per_col) + reminder;

        int total_width = (num_per_col * 25) + (num_per_col * buttonWidth) + 25;
        int total_height = (rows * 25) + (rows * buttonHeight) + 25;
        posterPage.setWidth(total_width);
        posterPage.setHeight(total_height);
        posterPage.addPosters(posterMap);


        SMT.add(posterPage);

    }

    @Override
    public void loadUser(String userUuid) {
        PosterDataModel.helper().loadUser(userUuid);

    }

    @Override
    public void loadPoster(String posterUuid) {
        PosterDataModel.helper().loadPoster(posterUuid);
    }


    @Override
    public void loadClass(String classname) {
        PosterDataModel.helper().loadClass(classname);
    }
}
