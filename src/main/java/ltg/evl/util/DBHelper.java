package ltg.evl.util;

import de.caluga.morphium.Morphium;
import de.caluga.morphium.MorphiumConfig;
import de.caluga.morphium.query.Query;
import ltg.evl.json.mongo.Poster;
import ltg.evl.json.mongo.User;
import ltg.evl.json.parse.PItem;
import ltg.evl.json.parse.PPoster;
import ltg.evl.json.parse.PUser;
import ltg.evl.uic.poster.PosterMain;
import org.parse4j.ParseException;
import org.parse4j.ParseQuery;
import org.parse4j.callback.FindCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vialab.SMT.Zone;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by aperritano on 2/11/15.
 */
public class DBHelper {

    private static Logger LOGGER = LoggerFactory.getLogger(ParseQuery.class);

    private static Morphium dbClient;
    private ArrayList<User> users;

    private static class StaticHolder {
        static final DBHelper INSTANCE = new DBHelper();
    }
    
    public static DBHelper helper() {
        return StaticHolder.INSTANCE;
    }

    private DBHelper() {
            String json = new javaxt.io.File(ClassLoader.getSystemResource("mongodb.json").getPath()).getText();


        MorphiumConfig cfg = null;
        try {
            cfg = MorphiumConfig.createFromJson(json);
            dbClient = new Morphium(cfg);
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
    
    public List<User> fetchUsers() {
        
            Query<User> query = dbClient.createQueryFor(User.class);
            if( query.asList().isEmpty() ) {
                users = new ArrayList<User>();
                users.add(createUser("DrBanner","poster"));
                return users;
            }
        users = (ArrayList<User>) query.asList();
        return users;
        
    }
    
    public User createUser(String username, String postername) {
        User user = new User();
        user.setName(username);
        user.addPoster(getEmptyPoster(postername));
        dbClient.store(user);
        return user;
    }
    
    public Poster getEmptyPoster(String postername){
        Poster poster = new Poster();
        poster.setName(postername);
        return poster;
    }
    
    public void updateUser(User user) {
//        if( users.contains(user)) {
//            Collections.replaceAll(users, user);
//        }
    }
    public Morphium dbClient() {
        return dbClient;
    }
    
//    public void save(ParseObject parseObject) {
//        parseObject.saveInBackground();
//        try {
//            parseObject.save();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }

    public List<Zone> loadUserState(final PosterMain posterMain) {
        
        final ArrayList<Zone> zones = new ArrayList<>();
        
        ParseQuery<PUser> query = ParseQuery.getQuery(PUser.class);
        query.whereLessThanOrEqualTo("name", "Banner");
        query.findInBackground(new FindCallback<PUser>() {
            @Override
            public void done(List<PUser> results, ParseException parseException) {
                
                LOGGER.debug("results" + results);
                
                for (PUser pUser : results) {

                    PPoster poster = pUser.getPosters().get(0);
                    for (PItem pItem : poster.getPosterItems()) {
                        //SMT.add(new PImageZone(pItem));
                        //zones.add(new PImageZone(pItem));
                    }

                    
                }
            }
        });

        return zones;
    }
    

}
