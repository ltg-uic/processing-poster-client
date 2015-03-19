package ltg.evl;

import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.common.io.Resources;
import ltg.evl.uic.poster.json.mongo.*;
import ltg.evl.util.RESTHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Created by aperritano on 2/11/15.
 */
public class MongoDBTest {
    public static final String A41B9E1B8325873000044 = "550a41b9e1b8325873000044";

//    DBHelper getInstance = DBHelper.getInstance();

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

//    @Ignore
//    public void getUser() {
//        User user = getInstance.fetchUser("DrBanner");
//        System.out.println(user);
//    }

    public static void testUser() throws IOException, InterruptedException {


        List<HttpRequest> requests = Lists.newArrayList();
        List<MongoCallback> callbacks = Lists.newArrayList();

        User user = new UserBuilder().setName("DrBanner").createUser();

        Poster poster = new PosterBuilder().setHeight(2876).setWidth(1584).setName("My First Poster").setId(
                UUID.randomUUID().toString()).createPoster();

        user.addPoster(poster.getId());

        final HttpPost userRequest = new HttpPost(
                "https://ltg.evl.uic.edu/drowsy/poster/user");

        userRequest.addHeader("content-type", "application/json");
        userRequest.setEntity(new StringEntity(user.toJSON()));

        requests.add(userRequest);

        callbacks.add(new MongoCallback(userRequest));


        List<PosterItem> pis = new ArrayList<>();

        int displayWidth = 2800;
        int displayHeight = 1800;

        for (int i = 0; i < 5; i++) {

            int x = random(0, 500);
            int y = random(0, 500);
            int w = random(100, 200);
            int h = random(100, 200);


            javaxt.io.Image image = new javaxt.io.Image(Resources.getResource(i + ".jpg").getPath());

            PosterItem pi = new PosterItemBuilder().setName("posteritem-" + i)
                                                   .setId(UUID.randomUUID().toString())
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

            HttpPost piRequest = new HttpPost(
                    "https://ltg.evl.uic.edu/drowsy/poster/poster_item");

            piRequest.addHeader("content-type", "application/json");
            piRequest.setEntity(new StringEntity(pi.toJSON()));
            requests.add(piRequest);

            callbacks.add(new MongoCallback(piRequest));


        }


        for (PosterItem posterItem : pis) {
            poster.addPosterItems(posterItem.getId());
        }

        HttpPost posterRequest = new HttpPost(
                "https://ltg.evl.uic.edu/drowsy/poster/poster");

        posterRequest.addHeader("content-type", "application/json");
        posterRequest.setEntity(new StringEntity(poster.toJSON()));
        requests.add(posterRequest);

        callbacks.add(new MongoCallback(posterRequest));

        makeHttpRequests(requests, callbacks);
    }

    public static void makeHttpRequests(List<HttpRequest> requests,
                                        List<MongoCallback> callbacks) throws InterruptedException, IOException {

        CloseableHttpAsyncClient httpclient = null;
        try {
            httpclient = HttpAsyncClients.custom().
                    setHostnameVerifier(new AllowAllHostnameVerifier()).
                                                 setSSLContext(
                                                         new SSLContextBuilder().loadTrustMaterial(null,
                                                                                                   new TrustStrategy() {
                                                                                                       @Override
                                                                                                       public boolean isTrusted(
                                                                                                               java.security.cert.X509Certificate[] x509Certificates,
                                                                                                               String s) throws java.security.cert.CertificateException {
                                                                                                           return true;
                                                                                                       }


                                                                                                   })
                                                                                .build()).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        httpclient.start();

        try {
            httpclient.start();
            final CountDownLatch latch = new CountDownLatch(requests.size());

            for (int i = 0; i < requests.size(); i++) {
                MongoCallback c = callbacks.get(i);
                c.latch = latch;

                httpclient.execute((HttpUriRequest) requests.get(i), callbacks.get(i));

            }
            latch.await();

            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

    @Ignore
    public void getPosterItemPicture() {

//        Query<PosterItem> posterItemQuery = getInstance.dbClient()
//                                                  .createQueryFor(PosterItem.class)
//                                                  .f("_id")
//                                                  .eq("54e4a82f77c8eb677e3736d0");
//        posterItemQuery.asList(new AsyncOperationCallback<PosterItem>() {
//            @Override
//            public void onOperationSucceeded(AsyncOperationType type, Query<PosterItem> q, long duration,
//                                             List<PosterItem> result, PosterItem entity, Object... param) {
//
//
//                PosterItem posterItem = result.get(0);
//                byte[] bytes = Base64.decodeBase64(posterItem.getImageBytes());
//
//                new Image(bytes).saveAs("/Users/aperritano/dev/research/poster-client/images/save.jpg");
//
//
//            }
//
//            @Override
//            public void onOperationError(AsyncOperationType type, Query<PosterItem> q, long duration, String error,
//                                         Throwable t,
//                                         PosterItem entity, Object... param) {
//
//            }
//        });

//        javaxt.io.Image image = new javaxt.io.Image(Resources.getResource("0.jpg").getPath());
//
//        PosterItem pi = new PosterItemBuilder().setName("pi").setWidth(500).setHeight(800).setX(50).setY(20).setType("id").createPosterItem();
//
//
//
//        pi.setImageBytes(Base64.encodeBase64String(image.getByteArray()));
//        getInstance.dbClient().store(pi, new AsyncOperationCallback<PosterItem>() {
//            @Override
//            public void onOperationSucceeded(AsyncOperationType getType, Query<PosterItem> q, long duration,
//                                             List<PosterItem> result, PosterItem entity, Object... param) {
//                logger.debug("we re done!!!");
//            }
//
//            @Override
//            public void onOperationError(AsyncOperationType getType, Query<PosterItem> q, long duration, String error,
//                                         Throwable t,
//                                         PosterItem entity, Object... param) {
//
//            }
//        });
    }

    @Ignore
    public void insertPosterItemPicture() {

//        javaxt.io.Image image = new javaxt.io.Image(Resources.getResource("0.jpg").getPath());
//
//        PosterItem pi = new PosterItemBuilder().setName("pi")
//                                               .setWidth(500)
//                                               .setHeight(800)
//                                               .setX(50)
//                                               .setY(20)
//                                               .setType("jpg")
//                                               .createPosterItem();
//
//
//        pi.setImageBytes(Base64.encodeBase64String(image.getByteArray()));
//        getInstance.dbClient().store(pi, new AsyncOperationCallback<PosterItem>() {
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
                                                   .setId(UUID.randomUUID().toString())
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

    public Poster getPoster() throws IOException {
        Poster poster = new PosterBuilder().setHeight(2876)
                                           .setWidth(1584)
                                           .setName("My First Poster")
                                           .setId(UUID.randomUUID().toString())
                                           .createPoster();

        for (PosterItem posterItem : getPosterItems()) {
            poster.addPosterItems(posterItem.getId());
        }

        return poster;
    }

    public User createDefaultUser() throws IOException {
        User user = new UserBuilder().setName("DrBanner").createUser();
        user.addPoster(getPoster().getId());
        //System.out.print("JSON: " + user.toJSON());
        return user;
    }

    public void postUserSingle() {

    }

    @Ignore
    public void postUser() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        User user = createDefaultUser();
        CloseableHttpClient httpClient = HttpClients.custom().
                setHostnameVerifier(new AllowAllHostnameVerifier()).
                                                            setSslcontext(
                                                                    new SSLContextBuilder().loadTrustMaterial(null,
                                                                                                              new TrustStrategy() {
                                                                                                                  @Override
                                                                                                                  public boolean isTrusted(
                                                                                                                          java.security.cert.X509Certificate[] x509Certificates,
                                                                                                                          String s) throws java.security.cert.CertificateException {
                                                                                                                      return true;
                                                                                                                  }


                                                                                                              })
                                                                                           .build()).build();
        HttpPost request = new HttpPost(
                "https://ltg.evl.uic.edu/drowsy/poster/user");

        StringEntity params = new StringEntity(user.toJSON());
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        HttpResponse response = httpClient.execute(request);

        if (response.getStatusLine().getStatusCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                                               + response.getStatusLine().getStatusCode());
        }

//        HttpGet getRequest = new HttpGet(
//                "https://ltg.evl.uic.edu/drowsy/poster/user");
//
//        response = httpClient.execute(request);
//
//        if (response.getStatusLine().getStatusCode() != 201) {
//            throw new RuntimeException("Failed : HTTP error code : "
//                                               + response.getStatusLine().getStatusCode());
//        }
//
//
//
        final InputStreamReader inr = new InputStreamReader(response.getEntity().getContent());
        String text = CharStreams.toString(inr);
        // System.out.println("String from InputStream in Java: " + text);


        User newUser = User.toObj(text);

//        BufferedReader br = new BufferedReader(
//                new InputStreamReader((response.getEntity().getContent())));
//
//        String output;
//        System.out.println("Output from Server .... \n");
//        while ((output = br.readLine()) != null) {
//            System.out.println(output);
//        }
//
//        httpClient.getConnectionManager().shutdown();


    }

    @Test
    public void multipleRequests() throws IOException, InterruptedException, GeneralSecurityException, ExecutionException {

        // testUser();
        //RESTHelper.getInstance().testPullUser();
        //RESTHelper.getInstance().getAllCollections();


        // createPosterItem();
//
//

        //updatePosterItem();

        //createUser();
        // createRestPoster();

        updatePoster();
        // deleteUser();

        // updateUser();

        //deletePoster();

        //deletePosterItem();

    }

    private void updatePoster() throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        Poster poster = new PosterBuilder().setHeight(2876).setWidth(1584).setName("ASSHOLE").setId(
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
                                               .setId(A41B9E1B8325873000044)
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

    private void createPosterItem() throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        javaxt.io.Image image = new javaxt.io.Image(Resources.getResource("1.jpg").getPath());


        PosterItem pi = new PosterItemBuilder().setName("posteritem-test")
                                               .setId(A41B9E1B8325873000044)
                                               .setWidth(400)
                                               .setHeight(500)
                                               .setX(150)
                                               .setY(600)
                                               .setColor("#34567")
                                               .setRotation(0)
                                               .setType("jpg").setImageBytes(
                        Base64.encodeBase64String(image.getByteArray()))
                                               .createPosterItem();
        RESTHelper.getInstance()
                  .postCollection(pi, RESTHelper.PosterUrl.addPosterItem(), PosterItem.class);
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

    private void createUser() throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        User user = new UserBuilder().setName("FUCK TYOU").createUser();
        user.addPoster(A41B9E1B8325873000044);

        RESTHelper.getInstance()
                  .postCollection(user, RESTHelper.PosterUrl.addUser(), User.class);
    }

    private void createRestPoster() throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        Poster poster = new PosterBuilder().setHeight(2876).setWidth(1584).setName("My FUCK you Poster").setId(
                "660a41b9e1b8325873000044").createPoster();
        poster.addPosterItems(UUID.randomUUID().toString());
        poster.addPosterItems(UUID.randomUUID().toString());

        RESTHelper.getInstance()
                  .postCollection(poster, RESTHelper.PosterUrl.addPoster(), Poster.class);
    }

    @Ignore
    public void pullUser() {


    }

    @Ignore
    public void getUsers() {

//        List<PosterItem> pis = DBHelper.getInstance().getPosterItemsForUser("DrBanner");
//
//        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
//
//
//        FluentIterable<PictureZone> pictureZones = FluentIterable.from(pis).transform(new PosterItemToPictureZone());
//
//        for (PictureZone pictureZone : pictureZones) {
//            System.out.println(pictureZone.toString());
//        }

    }

    static class MongoCallback implements org.apache.http.concurrent.FutureCallback<HttpResponse> {

        public CountDownLatch latch;
        private HttpRequest request;

        public MongoCallback(HttpRequest request) {
            this.request = request;
        }

        public MongoCallback() {

        }

        @Override
        public void completed(HttpResponse response) {
            latch.countDown();
            System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
        }

        @Override
        public void failed(Exception ex) {
            latch.countDown();
            System.out.println(request.getRequestLine() + "->" + ex);
        }

        @Override
        public void cancelled() {
            System.out.println(request.getRequestLine() + " cancelled");
        }
    }
//    @After
//    public void tearDown() throws Exception {
//        getInstance.dbClient().close();
//    }
}
