package ltg.evl.util;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.common.util.concurrent.*;
import ltg.evl.uic.poster.json.mongo.Poster;
import ltg.evl.uic.poster.json.mongo.PosterDataModelHelper;
import ltg.evl.uic.poster.json.mongo.PosterItem;
import ltg.evl.uic.poster.json.mongo.User;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by aperritano on 2/11/15.
 */
public class RESTHelper {

    static final HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static RESTHelper ourInstance = new RESTHelper();
    public User currentUser = null;
    public Poster currentPoster = null;
    public List<PosterItem> currentPosterItems = null;


    //Singleton constructor
    private RESTHelper() {
        enableLogging();
    }

    public static RESTHelper getInstance() {
        return ourInstance;
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

    public static void enableLogging() {
        Logger logger = Logger.getLogger(HttpTransport.class.getName());
        logger.setLevel(Level.CONFIG);
        logger.addHandler(new Handler() {

            @Override
            public void close() throws SecurityException {
            }

            @Override
            public void flush() {
            }

            @Override
            public void publish(LogRecord record) {
                // default ConsoleHandler will print >= INFO to System.err
                if (record.getLevel().intValue() < Level.INFO.intValue()) {
                    System.out.println(record.getMessage());
                }
            }
        });
    }

    public void initAllCollections() {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<Void> doRestCallsForAll = service.submit(new Callable<Void>() {
            public Void call() {
                try {
                    getAllCollections();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        Futures.addCallback(doRestCallsForAll, new FutureCallback<Void>() {
            // we want this handler to run immediately after we push the big red button!
            public void onSuccess(Void explosion) {
                System.out.println("REST DONE");
                PosterDataModelHelper.getInstance().initializationDone();
            }

            public void onFailure(Throwable thrown) {
                thrown.printStackTrace();
            }
        });
    }

    public void postCollection(GenericJson jsonObject, PosterUrl url,
                               Class<?> someClass) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {

        ExecutorService pool = Executors.newCachedThreadPool();

        ApacheHttpTransport transport = new ApacheHttpTransport.Builder().doNotValidateCertificate().build();


        HttpRequestFactory requestFactory =
                transport.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }


                });


        HttpRequest request = null;
        if (url.request_type.equals(PosterUrl.REQUEST_TYPE.ADD)) {
            request = requestFactory.buildPostRequest(url, new JsonHttpContent(new JacksonFactory(), jsonObject));
            Future<HttpResponse> httpUserResponseFuture = request.executeAsync(pool);
            HttpResponse response = httpUserResponseFuture.get();
            parseResponseObject(response, someClass);
        } else if (url.request_type.equals(PosterUrl.REQUEST_TYPE.DELETE)) {
            request = requestFactory.buildDeleteRequest(url);
            Future<HttpResponse> httpUserResponseFuture = request.executeAsync(pool);
            HttpResponse response = httpUserResponseFuture.get();
            parseResponseObject(response, someClass);
        } else if (url.request_type.equals(PosterUrl.REQUEST_TYPE.UPDATE)) {
            request = requestFactory.buildPutRequest(url, new JsonHttpContent(new JacksonFactory(), jsonObject));
            Future<HttpResponse> httpUserResponseFuture = request.executeAsync(pool);
            HttpResponse response = httpUserResponseFuture.get();
            parseResponseObject(response, someClass);
        }


    }

    public void getAllCollections() throws IOException, GeneralSecurityException, ExecutionException, InterruptedException {


        ExecutorService pool = Executors.newCachedThreadPool();

        ApacheHttpTransport transport = new ApacheHttpTransport.Builder().doNotValidateCertificate().build();


        HttpRequestFactory requestFactory =
                transport.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }


                });

        //user

        PosterUrl url = new PosterUrl("https://ltg.evl.uic.edu/drowsy/poster/user");
        HttpRequest userRequest = requestFactory.buildGetRequest(url);

        Future<HttpResponse> httpUserResponseFuture = userRequest.executeAsync(pool);

        HttpResponse userResponse = httpUserResponseFuture.get();

        parseResponseArray(userResponse, User.class);


        //poster

        url = new PosterUrl("https://ltg.evl.uic.edu/drowsy/poster/poster");

        HttpRequest posterRequest = requestFactory.buildGetRequest(url);

        Future<HttpResponse> httpPosterResponseFuture = posterRequest.executeAsync(pool);

        HttpResponse posterReponse = httpPosterResponseFuture.get();

        parseResponseArray(posterReponse, Poster.class);

        //posterItem

        url = new PosterUrl("https://ltg.evl.uic.edu/drowsy/poster/poster_item");

        HttpRequest posterItemRequest = requestFactory.buildGetRequest(url);

        Future<HttpResponse> httpPosterItemResponseFuture = posterItemRequest.executeAsync(pool);

        HttpResponse posterItemReponse = httpPosterItemResponseFuture.get();

        parseResponseArray(posterItemReponse, PosterItem.class);

    }

    private void parseResponseObject(HttpResponse response, Class<?> someClass) throws IOException {


        if (someClass.equals(User.class)) {
            User user = response.parseAs(User.class);

            if (user == null) {
            } else {
                PosterDataModelHelper.getInstance().updateUserCollection(user);
            }
        } else if (someClass.equals(Poster.class)) {
            Poster poster = response.parseAs(Poster.class);

            if (poster == null) {
            } else {
                PosterDataModelHelper.getInstance().updatePosterCollection(poster);
            }
        } else if (someClass.equals(PosterItem.class)) {
            PosterItem posterItem = response.parseAs(PosterItem.class);

            if (posterItem == null) {
            } else {
                PosterDataModelHelper.getInstance().updatePosterItemsCollection(posterItem);
            }
        }
    }

    private void parseResponseArray(HttpResponse response, Class<?> someClass) throws IOException {


        if (someClass.equals(User.class)) {
            User[] users = response.parseAs(User[].class);

            if (users.length < 0) {
                List<User> userList = Lists.newArrayList();
                PosterDataModelHelper.getInstance().updateAllUserCollection(userList);
            } else {
                PosterDataModelHelper.getInstance().updateAllUserCollection(Arrays.asList(users));
            }


        } else if (someClass.equals(Poster.class)) {
            Poster[] posters = response.parseAs(Poster[].class);

            if (posters.length < 0) {
                List<Poster> posterList = Lists.newArrayList();
                PosterDataModelHelper.getInstance().updateAllPosterCollection(posterList);
            } else {
                PosterDataModelHelper.getInstance().updateAllPosterCollection(Arrays.asList(posters));
            }

        } else if (someClass.equals(PosterItem.class)) {
            PosterItem[] posterItems = response.parseAs(PosterItem[].class);

            if (posterItems.length < 0) {
                List<PosterItem> posterItemList = Lists.newArrayList();
                PosterDataModelHelper.getInstance().updateAllPosterItemsCollection(posterItemList);
            } else {
                PosterDataModelHelper.getInstance().updateAllPosterItemsCollection(Arrays.asList(posterItems));
            }
        }

    }

    public static class PosterUrl extends GenericUrl {

        private static String BASE_URL = "https://ltg.evl.uic.edu/drowsy/poster/";
        public REQUEST_TYPE request_type;

        public PosterUrl(String encodedUrl) {
            super(encodedUrl);
        }

        public static PosterUrl addPosterItem() {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "poster_item");
            posterUrl.request_type = REQUEST_TYPE.ADD;
            return posterUrl;
        }

        public static PosterUrl addPoster() {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "poster");
            posterUrl.request_type = REQUEST_TYPE.ADD;
            return posterUrl;
        }

        public static PosterUrl addUser() {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "user");
            posterUrl.request_type = REQUEST_TYPE.ADD;
            return posterUrl;
        }

        public static PosterUrl updateDeletePosterItem(String id, PosterUrl.REQUEST_TYPE request_type) {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "poster_item/" + id);
            posterUrl.request_type = request_type;
            return posterUrl;
        }

        public static PosterUrl updateDeletePoster(String id, PosterUrl.REQUEST_TYPE request_type) {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "poster/" + id);
            posterUrl.request_type = request_type;
            return posterUrl;
        }

        public static PosterUrl updateDeleteUser(String id, PosterUrl.REQUEST_TYPE request_type) {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "user/" + id);
            posterUrl.request_type = request_type;
            return posterUrl;

        }


        public enum REQUEST_TYPE {UPDATE, ADD, DELETE}
    }
}


