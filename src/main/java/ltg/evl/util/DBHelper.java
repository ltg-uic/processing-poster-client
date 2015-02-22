package ltg.evl.util;

import de.caluga.morphium.Morphium;
import de.caluga.morphium.MorphiumConfig;
import de.caluga.morphium.query.Query;
import ltg.evl.uic.poster.json.mongo.Poster;
import ltg.evl.uic.poster.json.mongo.PosterBuilder;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.json.mongo.User;
import ltg.evl.uic.poster.widgets.PictureZone;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import processing.core.PApplet;
import vialab.SMT.Zone;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.google.common.io.Resources.getResource;

/**
 * Created by aperritano on 2/11/15.
 */
public class DBHelper {

    private static Morphium dbClient;
    protected Logger logger = Logger.getLogger(getClass().getName());
    private ExecutorService threadPool = null;
    private List<User> users = new ArrayList<>();
    private ArrayList<PosterItem> posterItems;
    private ArrayList<Zone> zones = new ArrayList<>();
    private List<PictureZone> pictureZones = new ArrayList<>();
    private List<DBListener> dbListeners = new ArrayList<>();
    private User currentUser;

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

    public ExecutorService getThreadPool() {
        if (threadPool == null)
            return threadPool = Executors.newFixedThreadPool(3);
        else
            return threadPool;
    }

    public void shutdownThreads() {
        threadPool.shutdownNow();
        threadPool = null;
    }

    public User getUser(String username) {
        Query<User> u = DBHelper.helper().dbClient().createQueryFor(User.class).f("name").eq(username);
        List<User> users = u.asList();
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public List<PosterItem> getPosterItemsForUser(String username) {
        currentUser = getUser(username);
        return currentUser.getPosters().get(0).getPosterItems();
    }

    public Future<Image> getImage(final PosterItem posterItem) throws ExecutionException, InterruptedException {
        return getThreadPool().submit(new Callable<Image>() {
            @Override
            public Image call() throws Exception {
                byte[] bytes = Base64.decodeBase64(posterItem.getImageBytes());
                byte[] imgBytes = PApplet.loadBytes(new ByteArrayInputStream(bytes));
                return Toolkit.getDefaultToolkit().createImage(imgBytes);
            }
        });

    }

    public Future<List<User>> fetchUsers() throws ExecutionException, InterruptedException {

        FutureTask<List<User>> futureTask = new FutureTask<List<User>>(new FetchUser());
        return getThreadPool().submit(new FetchUser());
    }

    public User fetchUser(String stringName) {

        if (users.isEmpty()) {
            try {
                fetchUsers();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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

    class FetchUser implements Callable<List<User>> {


        @Override
        public List<User> call() throws Exception {
            Query<User> query = dbClient.createQueryFor(User.class);
            if (query.asList().isEmpty()) {
                users = new ArrayList<User>();
                users.add(createUser("DrBanner", "poster"));
                return users;
            }
            List<User> users = query.asList();
            return users;
        }
    }
}


