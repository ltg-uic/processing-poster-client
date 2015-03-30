package ltg.evl.util;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.*;
import ltg.evl.uic.poster.json.mongo.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
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
    static final GsonFactory JSON_FACTORY = new GsonFactory();
    public static String BASE_URL;
    private static RESTHelper ourInstance = new RESTHelper();
    private static Logger logger;
    public User currentUser = null;
    public Poster currentPoster = null;
    public List<PosterItem> currentPosterItems = null;


    //Singleton constructor
    private RESTHelper() {
        BASE_URL = PosterServices.getInstance().getConfig().getString("poster.base.url");
        enableLogging();
        //initAllCollections();
    }


    public static RESTHelper getInstance() {
        return ourInstance;
    }



    public static void enableLogging() {
        logger = Logger.getLogger(HttpTransport.class.getName());
        logger.setLevel(Level.ALL);
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

    public static int random(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public void initAllCollections() {
        //enableLogging();

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
                logger.log(Level.INFO, "STARTING ALL REST DONE START INIT");
                PosterDataModelHelper.getInstance().initializationDone();
            }

            public void onFailure(Throwable thrown) {
                logger.log(Level.INFO, "REST FAILED!");

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
                    public void initialize(HttpRequest request) throws IOException {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }


                });


        HttpRequest request = null;
        if (url.request_type.equals(PosterUrl.REQUEST_TYPE.ADD)) {
            request = requestFactory.buildPostRequest(url, new JsonHttpContent(new JacksonFactory(), jsonObject));
            Future<HttpResponse> httpUserResponseFuture = request.executeAsync(pool);
            HttpResponse response = httpUserResponseFuture.get();
            parseResponseObject(response, someClass, PosterUrl.REQUEST_TYPE.ADD);
        } else if (url.request_type.equals(PosterUrl.REQUEST_TYPE.DELETE)) {
            request = requestFactory.buildDeleteRequest(url);
            Future<HttpResponse> httpUserResponseFuture = request.executeAsync(pool);
            HttpResponse response = httpUserResponseFuture.get();
            parseResponseObject(response, someClass, PosterUrl.REQUEST_TYPE.DELETE);
        } else if (url.request_type.equals(PosterUrl.REQUEST_TYPE.UPDATE)) {
            request = requestFactory.buildPutRequest(url, new JsonHttpContent(new JacksonFactory(), jsonObject));
            Future<HttpResponse> httpUserResponseFuture = request.executeAsync(pool);
            HttpResponse response = httpUserResponseFuture.get();
            parseResponseObject(response, someClass, PosterUrl.REQUEST_TYPE.UPDATE);
        }


    }

    public void deletePosterItem(
            final String uuid) throws InterruptedException, GeneralSecurityException, ExecutionException, IOException {

        final ImmutableList<Poster> imPosters = ImmutableList.copyOf(PosterDataModelHelper.getInstance().allPosters);

        final ImmutableList<PosterItem> imPosterItems = ImmutableList.copyOf(
                PosterDataModelHelper.getInstance().allPosterItems);


    }

    public void getAllCollections() throws IOException, GeneralSecurityException, ExecutionException, InterruptedException {

        logger.log(Level.INFO, "STARTING ALL COLLECTIONS FETCH");
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

        logger.log(Level.INFO, "STARTING USER COLLECTIONS FETCH");
        PosterUrl url = new PosterUrl(PosterServices.getInstance().getConfig().getString("poster.base.url") + "/user");
        HttpRequest userRequest = requestFactory.buildGetRequest(url);

        Future<HttpResponse> httpUserResponseFuture = userRequest.executeAsync(pool);

        HttpResponse userResponse = httpUserResponseFuture.get();

        parseResponseArray(userResponse, User.class);


        //poster

        logger.log(Level.INFO, "STARTING POSTER COLLECTIONS FETCH");
        url = new PosterUrl(PosterServices.getInstance().getConfig().getString("poster.base.url") + "/poster");

        HttpRequest posterRequest = requestFactory.buildGetRequest(url);

        Future<HttpResponse> httpPosterResponseFuture = posterRequest.executeAsync(pool);

        HttpResponse posterReponse = httpPosterResponseFuture.get();

        parseResponseArray(posterReponse, Poster.class);

        //posterItem

        logger.log(Level.INFO, "STARTING POSTER ITEM COLLECTIONS FETCH");
        url = new PosterUrl(PosterServices.getInstance().getConfig().getString("poster.base.url") + "/poster_item");

        HttpRequest posterItemRequest = requestFactory.buildGetRequest(url);

        Future<HttpResponse> httpPosterItemResponseFuture = posterItemRequest.executeAsync(pool);

        HttpResponse posterItemReponse = httpPosterItemResponseFuture.get();

        parseResponseArray(posterItemReponse, PosterItem.class);

    }

    private void parseResponseArray(HttpResponse response, Class<?> someClass) throws IOException {


        if (someClass.equals(User.class)) {
            logger.log(Level.INFO, "PROCESSING USER RESPONSE");
            User[] users = response.parseAs(User[].class);

            List<User> userList = Lists.newArrayList();
            if (users.length < 0) {

                PosterDataModelHelper.getInstance().updateAllUserCollection(userList);
            } else {
                for (User u : users) {
                    userList.add(u);
                }
                PosterDataModelHelper.getInstance().updateAllUserCollection(userList);
            }


        } else if (someClass.equals(Poster.class)) {
            logger.log(Level.INFO, "PROCESSING POSTER RESPONSE");
            Poster[] posters = response.parseAs(Poster[].class);

            List<Poster> posterList = Lists.newArrayList();
            if (posters.length < 0) {

                PosterDataModelHelper.getInstance().updateAllPosterCollection(posterList);
            } else {

                for (Poster p : posters) {
                    posterList.add(p);
                }
                PosterDataModelHelper.getInstance().updateAllPosterCollection(posterList);
            }

        } else if (someClass.equals(PosterItem.class)) {
            logger.log(Level.INFO, "PROCESSING POSTER_ITEM RESPONSE");
            PosterItem[] posterItems = response.parseAs(PosterItem[].class);

            List<PosterItem> posterItemList = Lists.newArrayList();
            if (posterItems.length < 0) {
                PosterDataModelHelper.getInstance().updateAllPosterItemsCollection(posterItemList);
            } else {

                for (PosterItem pi : posterItems) {
                    posterItemList.add(pi);
                }
                PosterDataModelHelper.getInstance().updateAllPosterItemsCollection(posterItemList);
            }
        }

    }

    public void mqttMessageForward(final PosterMessage posterMessage) {
        if (posterMessage == null) {
            logger.log(Level.SEVERE, "MESSAGE IS NULL");
        } else {
            if (posterMessage.getType().equals(PosterMessage.POSTER)) {
            } else if (posterMessage.getType().equals(PosterMessage.POSTER_ITEM)) {


                ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
                ListenableFuture<Void> doRESTCALL = service.submit(new Callable<Void>() {
                    public Void call() {
                        try {
                            fetchPosterItem(posterMessage);
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
                Futures.addCallback(doRESTCALL, new FutureCallback<Void>() {
                    // we want this handler to run immediately after we push the big red button!
                    public void onSuccess(Void nothing) {
                        logger.log(Level.INFO, "DONE! FETCH POSTER ITEM");
                        //update poster with new _id
                    }

                    public void onFailure(Throwable thrown) {
                        logger.log(Level.INFO, "REST FAILED!");

                        thrown.printStackTrace();
                    }
                });


                //PosterDataModelHelper.getInstance().updatePosterItemCollection((PosterItem) posterMessage);
            } else if (posterMessage.getType().equals(PosterMessage.USER)) {

            }
        }
    }

    public void fetchPosterItem(
            PosterMessage posterMessage) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {

        logger.log(Level.INFO, "POSTERITEM COLLECTION FETCH");
        ExecutorService pool = Executors.newCachedThreadPool();

        ApacheHttpTransport transport = new ApacheHttpTransport.Builder().doNotValidateCertificate().build();


        HttpRequestFactory requestFactory =
                transport.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }


                });

        //poster Item

        PosterUrl url = PosterUrl.fetchPosterItem(posterMessage.getPosterItemId(), PosterUrl.REQUEST_TYPE.GET);

        HttpRequest posterItemRequest = requestFactory.buildGetRequest(url);

        Future<HttpResponse> httpPosterItemResponseFuture = posterItemRequest.executeAsync(pool);

        HttpResponse posterItemReponse = httpPosterItemResponseFuture.get();

        String posterItemUUID = parseResponseObject(posterItemReponse, PosterItem.class, PosterUrl.REQUEST_TYPE.ADD);

        PosterDataModelHelper.getInstance()
                             .addPosterItemUUIDWithPosterId(posterItemUUID, posterMessage.getPosterUuid());

    }

    private String parseResponseObject(HttpResponse response, Class<?> someClass,
                                       PosterUrl.REQUEST_TYPE requestType) throws IOException {

        if (requestType.equals(PosterUrl.REQUEST_TYPE.DELETE) || requestType.equals(PosterUrl.REQUEST_TYPE.UPDATE))
            return null;

        if (someClass.equals(User.class)) {
            User user = response.parseAs(User.class);

            if (user == null) {
            } else {
                PosterDataModelHelper.getInstance().updateUserCollection(user);
                return user.getUuid();
            }
        } else if (someClass.equals(Poster.class)) {
            Poster poster = response.parseAs(Poster.class);

            if (poster == null) {
            } else {
                PosterDataModelHelper.getInstance().updatePosterCollection(poster);
                return poster.getUuid();
            }

        } else if (someClass.equals(PosterItem.class)) {
            PosterItem posterItem = response.parseAs(PosterItem.class);

            if (posterItem == null) {
            } else {
                PosterDataModelHelper.getInstance().updatePosterItemCollection(posterItem);
                return posterItem.getUuid();
            }
        }
        return null;
    }

    public static class PosterUrl extends GenericUrl {

        public REQUEST_TYPE request_type;

        public PosterUrl(String encodedUrl) {
            super(encodedUrl);
        }

        public static PosterUrl addPosterItem() {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "/poster_item");
            posterUrl.request_type = REQUEST_TYPE.ADD;
            return posterUrl;
        }

        public static PosterUrl addPoster() {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "/poster");
            posterUrl.request_type = REQUEST_TYPE.ADD;
            return posterUrl;
        }

        public static PosterUrl addUser() {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "/user");
            posterUrl.request_type = REQUEST_TYPE.ADD;
            return posterUrl;
        }

        public static PosterUrl updateDeletePosterItem(String id, PosterUrl.REQUEST_TYPE request_type) {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "/poster_item/" + id);
            posterUrl.request_type = request_type;
            return posterUrl;
        }

        public static PosterUrl updateDeletePoster(String id, PosterUrl.REQUEST_TYPE request_type) {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "/poster/" + id);
            posterUrl.request_type = request_type;
            return posterUrl;
        }

        public static PosterUrl updateDeleteUser(String id, PosterUrl.REQUEST_TYPE request_type) {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "/user/" + id);
            posterUrl.request_type = request_type;
            return posterUrl;

        }

        public static PosterUrl fetchPosterItem(String id, PosterUrl.REQUEST_TYPE request_type) {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "/poster_item/" + id);
            posterUrl.request_type = request_type;
            return posterUrl;

        }


        public enum REQUEST_TYPE {UPDATE, ADD, DELETE, GET}
    }
}


