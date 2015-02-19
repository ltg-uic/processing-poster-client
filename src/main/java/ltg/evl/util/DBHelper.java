package ltg.evl.util;

import de.caluga.morphium.Morphium;
import de.caluga.morphium.MorphiumConfig;
import de.caluga.morphium.async.AsyncOperationCallback;
import de.caluga.morphium.async.AsyncOperationType;
import de.caluga.morphium.query.Query;
import ltg.evl.uic.poster.PictureZone;
import ltg.evl.uic.poster.json.mongo.Poster;
import ltg.evl.uic.poster.json.mongo.PosterBuilder;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.json.mongo.User;
import org.apache.log4j.Logger;
import vialab.SMT.Zone;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.io.Resources.getResource;

/**
 * Created by aperritano on 2/11/15.
 */
public class DBHelper {

    private static Morphium dbClient;
    protected Logger logger = Logger.getLogger(getClass().getName());
    private List<User> users = new ArrayList<>();
    private ArrayList<PosterItem> posterItems;
    private ArrayList<Zone> zones = new ArrayList<>();
    private List<PictureZone> pictureZones = new ArrayList<>();
    private List<DBListener> dbListeners = new ArrayList<>();

    private DBHelper() {


        String json = new javaxt.io.File(ClassLoader.getSystemResource("mongodb.json").getPath()).getText();
        MorphiumConfig cfg = null;
        try {
            cfg = MorphiumConfig.createFromJson(json);
            cfg.setLoggingConfigFile(getResource("logging.properties").getFile());
            dbClient = new Morphium(cfg);

            String loggingConfigFile = cfg.getLoggingConfigFile();


        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public static DBHelper helper() {
        return StaticHolder.INSTANCE;
    }

    public List<PictureZone> getPictureZones() {
        return pictureZones;
    }

    public PosterItem getPosterItem(String uuid) {
        for (PosterItem posterItem : users.get(0).getPosters().get(0).getPosterItems()) {
            if (posterItem.getId().toString().equals(uuid)) {
                return posterItem;
            }
        }

        return null;
    }

    public void replacePosterItem(PosterItem posterItem) {
        Poster poster = users.get(0).getPosters().get(0);

        poster.getPosterItems();
    }

    public void addDBListener(DBListener dbListener) {
        dbListeners.add(dbListener);
    }

    public List<User> fetchUsers() {

        Query<User> query = dbClient.createQueryFor(User.class);
        if (query.asList().isEmpty()) {
            users = new ArrayList<User>();
            users.add(createUser("DrBanner", "poster"));
            return users;
        }
        query.asList(new AsyncOperationCallback<User>() {
            @Override
            public void onOperationSucceeded(AsyncOperationType type, Query<User> q, long duration, List<User> result,
                                             User entity, Object... param) {


                users = result;
//                User user = result.get(0);
//
//
//                for (DBListener dbListener : dbListeners) {
//                    dbListener.posterItemsUpdated(user.getPosters().get(0).getPosterItems());
//                }

                //createPictureZone(user.getPosters().get(0).getPosterItems());


            }

            @Override
            public void onOperationError(AsyncOperationType type, Query<User> q, long duration, String error,
                                         Throwable t,
                                         User entity, Object... param) {
                t.printStackTrace();

            }
        });


        return users;

    }

    public User fetchUser(String stringName) {
        for (User user : users) {
            if (user.getName().equals(stringName)) {
                return user;
            }
        }

        return null;
    }

    public User createUser(String username, String postername) {
        User user = new User();
        user.setName(username);
        user.addPoster(getEmptyPoster(postername));
        dbClient.store(user);
        return user;
    }

    public Poster getEmptyPoster(String postername) {
        Poster poster = new PosterBuilder().createPoster();
        poster.setName(postername);
        return poster;
    }

    public Morphium dbClient() {
        return dbClient;
    }

    private static class StaticHolder {
        static final DBHelper INSTANCE = new DBHelper();
    }

}


