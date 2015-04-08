package ltg.evl;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import ltg.evl.uic.poster.json.mongo.*;
import ltg.evl.util.RESTHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.randname.RandomNameGenerator;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by aperritano on 2/11/15.
 */
public class MongoDBTest {
    public static final String A41B9E1B8325873000044 = "550a41b9e1b8325873000044";

//    DBHelper helper = DBHelper.helper();

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
                                                   .setUuid(UUID.randomUUID().toString())
                                                   .setWidth(w)
                                                   .setHeight(h)
                                                   .setX(x)
                                                   .setY(y)
                                                   .setColor("#34567")
                                                   .setRotation(0)
                                                   .setType("jpg").setImageBytes(
                            Base64.encodeBase64String(image.getByteArray()))
                                                   .createPosterItem();


            pis.add(pi);
        }

        return pis;
    }


    @Ignore
    public void multipleRequests() throws IOException, InterruptedException, GeneralSecurityException, ExecutionException {

        // testUser();
        //RESTHelper.helper().testPullUser();
        //RESTHelper.helper().getAllCollections();


        // createPosterItem();
//
//

        //updatePosterItem();


//        final String pid1 = createRestPoster("Interstellar");
//        final String pid2 = createRestPoster("Black Holes");
//
//        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
//
//
//        ListenableFuture<Boolean> explosion = service.submit(new Callable<Boolean>() {
//            public Boolean call() {
//
//                Boolean val = true;
//                return val;
//            }
//        });
//        Futures.addCallback(explosion, new FutureCallback<Boolean>() {
//
//
//            // we want this handler to run immediately after we push the big red button!
//            public void onSuccess(Boolean explosion) {
//
//                try {
//                    createUser("GALAXIES", Lists.asList("Dr.Banner", new String[]{"P.Parker", "MJ"}), pid1);
//                    createUser("SUPERNOVAS", Lists.asList("Octavian", new String[]{"Caliguia", "Otho"}), pid2);
//                } catch (GeneralSecurityException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            public void onFailure(Throwable thrown) {
//                // battleArchNemesis(); // escaped the explosion!
//            }
//        });


        // createUser("PULSARS", Lists.asList("Peyton Key", new String[]{"M. Preston", "Sam Snyder"}), UUID.randomUUID().toString());
        // updatePoster();
        // deleteUser();

        // updateUser();

        //deletePoster();

        //deletePosterItem();

    }


    //-- create BIG TEST DATA
    @Test
    public void createBIGTest() throws InterruptedException, GeneralSecurityException, ExecutionException, IOException {

        String[] names = {"Brock", "Gale", "Gustavo", "Hank", "Hector", "Holly", "Jane", "Jesse", "Lydia", "Marie", "Mike", "Pete", "Saul", "Skyler", "Todd", "Walter"};
        String[] classes = {"Ben", "Mike"};

        ArrayList<String> imageItems = Lists.newArrayList();

//        imageItems.add(createPosterItemImage(random(1, 4)));
//        imageItems.add(createPosterItemImage(random(1, 4)));
//        imageItems.add(createPosterItemImage(random(1, 4)));


        imageItems.add(createPosterItemImage(0));
        imageItems.add(createPosterItemImage(1));
        imageItems.add(createPosterItemImage(2));

        int NUM_GROUPS = 1;
        int NUM_POSTERS_PER_GROUP = 1;

        for (int y = 0; y < 2; y++) {
            String CLASS_NAME = classes[y];
            //first posteritems

            RandomNameGenerator randGroupName = new RandomNameGenerator(random(0, 100));

            for (int i = 0; i < NUM_GROUPS; i++) {

                System.out.println("START GROUP " + i);
                //create 6 posters per group

                ArrayList<String> posterUuids = Lists.newArrayList();

                for (int j = 0; j < NUM_POSTERS_PER_GROUP; j++) {

                    ArrayList<String> posterItems = Lists.newArrayList();
                    posterItems.addAll(imageItems);
                    posterItems.add(createPosterItemText(1, SentenceGenerator.getInstance().makeText(random(1, 4))));
                    posterItems.add(createPosterItemText(2, SentenceGenerator.getInstance().makeText(random(1, 4))));
                    posterItems.add(createPosterItemText(3, SentenceGenerator.getInstance().makeText(random(1, 4))));

                    posterUuids.add(createPoster(SentenceGenerator.getInstance().makeHeadline(), posterItems));
                }


                ArrayList<String> studentNames = Lists.newArrayList(names[random(0, names.length - 1)],
                                                                    names[random(0, names.length - 1)],
                                                                    names[random(0, names.length - 1)]);

                String group_name = randGroupName.next();
                createUser(CharMatcher.anyOf("_").replaceFrom(group_name, ' '), studentNames, CLASS_NAME, posterUuids);

                System.out.println("DONE WITH GROUP " + i);
            }
        }


        System.out.println("DONE WITH TEST MOFO!!!");
    }


    private String createUser(String userName, List<String> nameTags, String className,
                              List<String> uuidPosterId) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {

        String id = UUID.randomUUID().toString();

        User user = new UserBuilder().setName(userName).setUuid(id).createUser();
        user.setClassname(className);
        user.setNameTags(nameTags);
        user.setUuid(id);
        user.addAllPosters(uuidPosterId);

        RESTHelper.getInstance()
                  .postCollection(user, RESTHelper.PosterUrl.addUser(), User.class);
        return id;
    }

    private String createPoster(
            String posterName,
            List<String> posterItemUuids) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {

        String id = UUID.randomUUID().toString();

        Poster poster = new PosterBuilder().setHeight(2876)
                                           .setWidth(1584)
                                           .setName(posterName)
                                           .setUuid(id)
                                           .createPoster();

        poster.addAllPosterItems(posterItemUuids);

        RESTHelper.getInstance()
                  .postCollection(poster, RESTHelper.PosterUrl.addPoster(), Poster.class);

        return id;
    }

    private String createPosterItemImage(
            int i) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        //javaxt.io.Image image = new javaxt.io.Image(Resources.getResource(i + ".jpg").getPath());

        String[] links = {"http://i.kinja-img.com/gawker-media/image/upload/s--CEy---op--/c_fit,fl_progressive,q_80,w_636/18pagzhyr0jixjpg.jpg", "http://4.bp.blogspot.com/-JYPN0tF1Ly0/T7zvkxdvVKI/AAAAAAAAEyc/bU8_C-JiDD8/s640/Face,+Faces,+anomoly,+anomolies,+Mars,+Rover,+Spirit,+photo,+alien,+aliens,+base,+building,+structure,+UFO,+UFOs,+sighting,+sightings,+news,+May,+2012,+paranormalScreen+Shot+2012-05-23+at+1.43.15+PM.png", "http://mars.nasa.gov/mer/gallery/press/opportunity/20130122a/PIA16703_Sol3137B_Matijevic_Pan_L257atc.jpg"};

        String uuid = UUID.randomUUID().toString();

        PosterItem pi = new PosterItemBuilder().setName("posteritem-img-" + i)
                                               .setUuid(uuid)
                                               .setWidth(400)
                                               .setHeight(500)
                                               .setX(random(0, 500))
                                               .setY(random(0, 500))
                                               .setColor("#34567")
                                               .setRotation(0)
                                               .setContent(links[i])
                .setType("img")
                        //.setImageBytes(Base64.encodeBase64String(image.getByteArray()))
                                               .createPosterItem();
        RESTHelper.getInstance()
                  .postCollection(pi, RESTHelper.PosterUrl.addPosterItem(), PosterItem.class);
        return uuid;
    }

    private String createPosterItemText(int i,
                                        String text) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {


        String uuid = UUID.randomUUID().toString();

        PosterItem pi = new PosterItemBuilder().setName("posteritem-txt-" + i)
                                               .setUuid(uuid)
                                               .setWidth(random(0, 450))
                                               .setHeight(random(0, 450))
                                               .setX(random(0, 500))
                                               .setY(random(0, 500))
                                               .setColor("#34567")
                                               .setRotation(0)
                                               .setType("txt")
                                               .setContent(text)
                                               .createPosterItem();
        RESTHelper.getInstance()
                  .postCollection(pi, RESTHelper.PosterUrl.addPosterItem(), PosterItem.class);

        return uuid;
    }

    private void createPosterItem(
            PosterItem pi) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        RESTHelper.getInstance()
                  .postCollection(pi, RESTHelper.PosterUrl.addPosterItem(), PosterItem.class);
    }


    private String createRestPoster(
            String posterName) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {

        String id = UUID.randomUUID().toString();

        Poster poster = new PosterBuilder().setHeight(2876)
                                           .setWidth(1584)
                                           .setName(posterName)
                                           .setUuid(id)
                                           .createPoster();

        for (PosterItem pi : getPosterItems()) {

            this.createPosterItem(pi);

            poster.addPosterItems(pi.getUuid());
        }

        RESTHelper.getInstance()
                  .postCollection(poster, RESTHelper.PosterUrl.addPoster(), Poster.class);

        return id;
    }

    private void deletePoster() throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        RESTHelper.getInstance()
                  .postCollection(null, RESTHelper.PosterUrl.updateDeletePoster(A41B9E1B8325873000044,
                                                                                RESTHelper.PosterUrl.REQUEST_TYPE.DELETE),
                                  Poster.class);
    }

    private void deleteUser() throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        RESTHelper.getInstance()
                  .postCollection(null, RESTHelper.PosterUrl.updateDeleteUser(A41B9E1B8325873000044,
                                                                              RESTHelper.PosterUrl.REQUEST_TYPE.DELETE),
                                  User.class);
    }

    @Ignore
    public void pullUser() {


    }

    private void updatePoster() throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        Poster poster = new PosterBuilder().setHeight(2876).setWidth(1584).setName("ASSHOLE").setUuid(
                "660a41b9e1b8325873000044").createPoster();
        poster.addPosterItems(UUID.randomUUID().toString());
        poster.addPosterItems(UUID.randomUUID().toString());


        RESTHelper.getInstance()
                  .postCollection(poster, RESTHelper.PosterUrl.updateDeletePoster("660a41b9e1b8325873000044",
                                                                                  RESTHelper.PosterUrl.REQUEST_TYPE.UPDATE),
                                  Poster.class);
    }

    private void updatePosterItem() throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        javaxt.io.Image image = new javaxt.io.Image(Resources.getResource("1.jpg").getPath());


        PosterItem pi = new PosterItemBuilder().setName("posteritem-NOT")
                                               .setUuid(A41B9E1B8325873000044)
                                               .setWidth(400)
                                               .setHeight(500)
                                               .setX(150)
                                               .setY(600)
                                               .setColor("#345dfdfd67")
                                               .setRotation(0)
                                               .setType("jpg").setImageBytes(
                        Base64.encodeBase64String(image.getByteArray()))
                                               .createPosterItem();

        RESTHelper.getInstance()
                  .postCollection(pi, RESTHelper.PosterUrl.updateDeletePosterItem("550a49dee1b8325873000049",
                                                                                  RESTHelper.PosterUrl.REQUEST_TYPE.UPDATE),
                                  PosterItem.class);
    }

    private void updateUser() throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        User user = new UserBuilder().setName("HEY YOU").createUser();
        user.addPoster(A41B9E1B8325873000044);

        RESTHelper.getInstance()
                  .postCollection(user, RESTHelper.PosterUrl.updateDeleteUser("550a4834e1b8325873000047",
                                                                              RESTHelper.PosterUrl.REQUEST_TYPE.UPDATE),
                                  User.class);
    }

    private void deletePosterItem() throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        RESTHelper.getInstance()
                  .postCollection(null, RESTHelper.PosterUrl.updateDeletePosterItem("550a469ae1b8325873000046",
                                                                                    RESTHelper.PosterUrl.REQUEST_TYPE.DELETE),
                                  PosterItem.class);
    }

    @Ignore
    public void testObjectId() throws InterruptedException, GeneralSecurityException, ExecutionException, IOException {
        // ObjectId o = new ObjectId("5516f60ce1b8325873000178");

        //System.out.println("print " + o.toStringBabble() + " " + o.toStringMongod());

        RESTHelper.getInstance().getAllCollections();

        List<Poster> allPosters = PosterDataModel.helper().allPosters;

        for (Poster p : allPosters) {
            System.out.println("Poster " + p.toPrettyString() + "\n\nOBJECT _id" + p.get_id().get("$oid"));
        }

        allPosters.get(0).getPosterItems().remove(0);

        RESTHelper.getInstance()
                  .postCollection(allPosters.get(0), RESTHelper.PosterUrl.updateDeletePoster(
                          allPosters.get(0).get_id().get("$oid").toString(),
                          RESTHelper.PosterUrl.REQUEST_TYPE.UPDATE), Poster.class);

    }

    public Poster getPoster(String posterName) throws IOException {
        Poster poster = new PosterBuilder().setHeight(2876)
                                           .setWidth(1584)
                                           .setName(posterName)
                                           .setUuid(UUID.randomUUID().toString())
                                           .createPoster();

        for (PosterItem posterItem : getPosterItems()) {
            poster.addPosterItems(posterItem.getUuid());
        }

        return poster;
    }




}
