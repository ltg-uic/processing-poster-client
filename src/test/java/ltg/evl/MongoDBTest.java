package ltg.evl;

import com.google.common.io.Resources;
import de.caluga.morphium.async.AsyncOperationCallback;
import de.caluga.morphium.async.AsyncOperationType;
import de.caluga.morphium.query.Query;
import javaxt.io.Image;
import ltg.evl.json.mongo.*;
import ltg.evl.util.DBHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by aperritano on 2/11/15.
 */
public class MongoDBTest {

    DBHelper helper = DBHelper.helper();

    Logger logger = Logger.getLogger(getClass().getName());

    @BeforeClass
    public static void setUpClass() {
    }

    public static int random(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    @Ignore
    public void createDefaultUser() throws IOException {
        User user = new User();
        user.setName("DrBanner");
        user.addPoster(getPoster());
        helper.dbClient().store(user);
    }

    @Test
    public void getPosterItemPicture() {

        Query<PosterItem> posterItemQuery = helper.dbClient()
                                                  .createQueryFor(PosterItem.class)
                                                  .f("_id")
                                                  .eq("54e4a82f77c8eb677e3736d0");
        posterItemQuery.asList(new AsyncOperationCallback<PosterItem>() {
            @Override
            public void onOperationSucceeded(AsyncOperationType type, Query<PosterItem> q, long duration,
                                             List<PosterItem> result, PosterItem entity, Object... param) {


                PosterItem posterItem = result.get(0);
                byte[] bytes = Base64.decodeBase64(posterItem.getImageBytes());

                new Image(bytes).saveAs("/Users/aperritano/dev/research/poster-client/images/save.jpg");


            }

            @Override
            public void onOperationError(AsyncOperationType type, Query<PosterItem> q, long duration, String error,
                                         Throwable t,
                                         PosterItem entity, Object... param) {

            }
        });

//        javaxt.io.Image image = new javaxt.io.Image(Resources.getResource("0.jpg").getPath());
//
//        PosterItem pi = new PosterItemBuilder().setName("pi").setWidth(500).setHeight(800).setX(50).setY(20).setImageId("id").createPosterItem();
//
//
//
//        pi.setImageBytes(Base64.encodeBase64String(image.getByteArray()));
//        helper.dbClient().store(pi, new AsyncOperationCallback<PosterItem>() {
//            @Override
//            public void onOperationSucceeded(AsyncOperationType type, Query<PosterItem> q, long duration,
//                                             List<PosterItem> result, PosterItem entity, Object... param) {
//                logger.debug("we re done!!!");
//            }
//
//            @Override
//            public void onOperationError(AsyncOperationType type, Query<PosterItem> q, long duration, String error,
//                                         Throwable t,
//                                         PosterItem entity, Object... param) {
//
//            }
//        });
    }
    
    @Ignore
    public void insertPosterItemPicture() {

        javaxt.io.Image image = new javaxt.io.Image(Resources.getResource("0.jpg").getPath());

        PosterItem pi = new PosterItemBuilder().setName("pi")
                                               .setWidth(500)
                                               .setHeight(800)
                                               .setX(50)
                                               .setY(20)
                                               .setImageId("jpg")
                                               .createPosterItem();


        pi.setImageBytes(Base64.encodeBase64String(image.getByteArray()));
        helper.dbClient().store(pi, new AsyncOperationCallback<PosterItem>() {
            @Override
            public void onOperationSucceeded(AsyncOperationType type, Query<PosterItem> q, long duration,
                                             List<PosterItem> result, PosterItem entity, Object... param) {
                logger.debug("we re done!!!");
            }

            @Override
            public void onOperationError(AsyncOperationType type, Query<PosterItem> q, long duration, String error,
                                         Throwable t,
                                         PosterItem entity, Object... param) {

            }
        });
    }

    @Ignore
    public void getUser() {
        User user = helper.fetchUser("DrBanner");
        System.out.println(user);
    }

    @Ignore
    public void getUsers() {
        Query<User> u = helper.dbClient().createQueryFor(User.class);
        List<User> users = u.asList();

        if (users.isEmpty()) {
            helper.createUser("DrBanner", "poster");
        }

        for (User user : users) {
            System.out.println("USER " + user);
        }


    }


    public List<PosterItem> getPosterItems() throws IOException {

        List<PosterItem> pis = new ArrayList<>();

        int displayWidth = 2800;
        int displayHeight = 1800;

        for (int i = 0; i < 5; i++) {

            int x = random(0, displayWidth - 400);
            int y = random(0, displayHeight - 400);
            int w = random(100, 200);
            int h = random(100, 200);


            javaxt.io.Image image = new javaxt.io.Image(Resources.getResource(i + ".jpg").getPath());

            PosterItem pi = new PosterItemBuilder().setName("posteritem-" + i)
                                                   .setWidth(w)
                                                   .setHeight(h)
                                                   .setX(x)
                                                   .setY(y)
                                                   .setImageId("jpg").setImageBytes(
                            Base64.encodeBase64String(image.getByteArray()))
                                                   .createPosterItem();


            pis.add(pi);
        }

        return pis;
    }

    public Poster getPoster() throws IOException {
        Poster poster = new PosterBuilder().setHeight(2876).setWidth(1584).setName("My First Poster").createPoster();

        for (PosterItem posterItem : getPosterItems()) {
            poster.addPosterItems(posterItem);
        }

        return poster;
    }

    @After
    public void tearDown() throws Exception {
        helper.dbClient().close();
    }
}
