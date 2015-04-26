package ltg.evl.util;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.util.concurrent.*;
import ltg.evl.uic.poster.json.mongo.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
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


    //region setup
    private RESTHelper() {
        BASE_URL = PosterServices.getInstance().getConfig().getString("poster.base.url");
        enableLogging();
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

    //endregion setup


    public void initAllCollections() {
        PosterDataModel.helper().resetData();

        try {
            List<ListenableFuture<HttpResponse>> allRequests = getAllCollectionRequests();

            ListenableFuture<List<HttpResponse>> successfulRequests = Futures.successfulAsList(allRequests);

            Futures.addCallback(successfulRequests, new FutureCallback<List<HttpResponse>>() {
                // we want this handler to run immediately after we push the big red button!

                @Override
                public void onSuccess(List<HttpResponse> result) {
                    logger.log(Level.INFO, "STARTING ALL REST DONE START INIT");
                    PosterDataModel.helper().initializationDone();
                }

                @Override
                public void onFailure(Throwable thrown) {
                    logger.log(Level.INFO, "REST FAILED!");
                    thrown.printStackTrace();
                }
            });
            //
        } catch (IOException | GeneralSecurityException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public ListenableFuture<List<HttpResponse>> updatePosterItems(
            FluentIterable<PosterItem> posterItems) throws InterruptedException, GeneralSecurityException, ExecutionException, IOException {
        List<ListenableFuture<HttpResponse>> listOfFutures = Lists.newArrayList();

        if (!posterItems.isEmpty()) {
            for (PosterItem pi : posterItems) {
                Optional<PosterItem> posterItemOptional = Optional.fromNullable(pi);

                if (posterItemOptional.isPresent()) {

                    PosterItem posterItem = new PosterItem();
                    posterItem.setHeight(pi.getHeight());
                    posterItem.setWidth(pi.getWidth());
                    posterItem.setScale(pi.getScale());
                    posterItem.setRotation(pi.getRotation());
                    posterItem.setY(pi.getY());
                    posterItem.setX(pi.getX());

                    listOfFutures.add(postCollection(posterItem, RESTHelper.PosterUrl.patchPosterItem(
                                                             pi.get_id().get("$oid").toString(),
                                                             PosterUrl.REQUEST_TYPE.PATCH),
                                                     PosterItem.class, false));
                } else {
                    logger.log(Level.SEVERE, "UPDATE POSTER ITEM NULL");
                }
            }
        }


        ListenableFuture<List<HttpResponse>> successfullQueries = Futures.successfulAsList(listOfFutures);


        return successfullQueries;
    }

    //region http

    public ListenableFuture<HttpResponse> postCollection(GenericJson jsonObject, PosterUrl url,
                                                         final Class<?> someClass, boolean doCallable) {

        ListeningExecutorService service = MoreExecutors.listeningDecorator(
                Executors.newCachedThreadPool());


        ApacheHttpTransport transport = null;
        try {
            transport = new ApacheHttpTransport.Builder().doNotValidateCertificate().build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }


        HttpRequestFactory requestFactory =
                transport.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }


                });


        HttpRequest request;
        ListenableFuture<HttpResponse> listenableFuture = null;
        Future<HttpResponse> jdkFuture;
        switch (url.request_type) {
            case UPDATE:
                try {
                    request = requestFactory.buildPutRequest(url, new JsonHttpContent(new JacksonFactory(),
                                                                                      jsonObject));
                    jdkFuture = request.executeAsync(service);
                    listenableFuture = JdkFutureAdapters.listenInPoolThread(
                            jdkFuture);

                    if (doCallable) {
                        Futures.addCallback(listenableFuture, new FutureCallback<HttpResponse>() {

                            @Override
                            public void onSuccess(HttpResponse updateFutureResponse) {
                                try {
                                    logger.log(Level.INFO, "POSTED UPDATE: " + someClass.getName());
                                    parseResponseObject(updateFutureResponse, someClass, PosterUrl.REQUEST_TYPE.UPDATE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                handleHttpFailure(throwable);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;
            case ADD:

                try {
                    request = requestFactory.buildPostRequest(url,
                                                              new JsonHttpContent(new JacksonFactory(), jsonObject));
                    jdkFuture = request.executeAsync(service);
                    listenableFuture = JdkFutureAdapters.listenInPoolThread(
                            jdkFuture);

                    if (doCallable) {
                        Futures.addCallback(listenableFuture, new FutureCallback<HttpResponse>() {
                            @Override
                            public void onSuccess(HttpResponse addFutureResponse) {
                                try {
                                    logger.log(Level.INFO, "POSTED ADD: " + someClass.getName());
                                    parseResponseObject(addFutureResponse, someClass, PosterUrl.REQUEST_TYPE.ADD);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Throwable thrown) {
                                handleHttpFailure(thrown);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;
            case PATCH:

                try {
                    request = requestFactory.buildPatchRequest(url,
                                                               new JsonHttpContent(new JacksonFactory(), jsonObject));
                    jdkFuture = request.executeAsync(service);
                    listenableFuture = JdkFutureAdapters.listenInPoolThread(
                            jdkFuture);

                    if (doCallable) {
                        Futures.addCallback(listenableFuture, new FutureCallback<HttpResponse>() {
                            @Override
                            public void onSuccess(HttpResponse addFutureResponse) {
                                try {
                                    logger.log(Level.INFO, "PATCHED patch: " + someClass.getName());
                                    parseResponseObject(addFutureResponse, someClass, PosterUrl.REQUEST_TYPE.PATCH);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Throwable thrown) {
                                handleHttpFailure(thrown);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;
            case DELETE:
                try {
                    request = requestFactory.buildDeleteRequest(url);
                    jdkFuture = request.executeAsync(service);
                    listenableFuture = JdkFutureAdapters.listenInPoolThread(
                            jdkFuture);

                    if (doCallable) {
                        Futures.addCallback(listenableFuture, new FutureCallback<HttpResponse>() {
                            @Override
                            public void onSuccess(HttpResponse deleteFutureResponse) {
                                logger.log(Level.INFO, "POSTED DELETE: " + someClass.getName());
//                                try {
//
//                                    parseResponseObject(deleteFutureResponse, someClass, PosterUrl.REQUEST_TYPE.DELETE);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                            }

                            @Override
                            public void onFailure(Throwable thrown) {
                                handleHttpFailure(thrown);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;
            default:
                break;
        }

        return listenableFuture;
    }




    public List<ListenableFuture<HttpResponse>> getAllCollectionRequests() throws IOException, GeneralSecurityException, ExecutionException, InterruptedException {

        logger.log(Level.INFO, "STARTING ALL COLLECTIONS FETCH");

        List<ListenableFuture<HttpResponse>> futures = Lists.newArrayList();


        ListeningExecutorService service = MoreExecutors.listeningDecorator(
                Executors.newCachedThreadPool());

        ApacheHttpTransport transport = new ApacheHttpTransport.Builder().doNotValidateCertificate().build();

        HttpRequestFactory requestFactory =
                transport.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }


                });

        //user
        PosterUrl url = new PosterUrl(PosterServices.getInstance().getConfig().getString("poster.base.url") + "/user");
        HttpRequest userRequest = requestFactory.buildGetRequest(url);
        Future<HttpResponse> httpUserResponseFuture = userRequest.executeAsync(service);
        ListenableFuture<HttpResponse> userFuture = JdkFutureAdapters.listenInPoolThread(
                httpUserResponseFuture);

        Futures.addCallback(userFuture, new FutureCallback<HttpResponse>() {
            @Override
            public void onSuccess(HttpResponse userResponse) {
                try {
                    logger.log(Level.INFO, "RECIEVED USER COLLECTIONS RESPONSE");
                    parseAllResponse(userResponse, User.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable thrown) {
                logger.log(Level.SEVERE, "USER FETCH FAILED");
                handleHttpFailure(thrown);

            }
        });

        futures.add(userFuture);

        // HttpResponse userResponse = httpUserResponseFuture.get();
        // poster
        url = new PosterUrl(PosterServices.getInstance().getConfig().getString("poster.base.url") + "/poster");
        HttpRequest posterRequest = requestFactory.buildGetRequest(url);
        Future<HttpResponse> httpPosterResponseFuture = posterRequest.executeAsync(service);

        ListenableFuture<HttpResponse> posterFuture = JdkFutureAdapters.listenInPoolThread(
                httpPosterResponseFuture);

        Futures.addCallback(posterFuture, new FutureCallback<HttpResponse>() {
            @Override
            public void onSuccess(HttpResponse posterReponse) {
                try {
                    logger.log(Level.INFO, "RECEIVED POSTER COLLECTIONS RESPONSE");
                    parseAllResponse(posterReponse, Poster.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable thrown) {
                logger.log(Level.SEVERE, "POSTER FETCH FAILED");
                handleHttpFailure(thrown);
            }
        });

        futures.add(posterFuture);

        //posterItem


        url = new PosterUrl(PosterServices.getInstance().getConfig().getString("poster.base.url") + "/poster_item");

        HttpRequest posterItemRequest = requestFactory.buildGetRequest(url);

        Future<HttpResponse> httpPosterItemResponseFuture = posterItemRequest.executeAsync(service);

        ListenableFuture<HttpResponse> posterItemFuture = JdkFutureAdapters.listenInPoolThread(
                httpPosterItemResponseFuture);

        Futures.addCallback(posterItemFuture, new FutureCallback<HttpResponse>() {
            @Override
            public void onSuccess(HttpResponse posterItemFuture) {
                try {
                    logger.log(Level.INFO, "RECEIVED POSTER ITEM COLLECTIONS RESPONSE");
                    parseAllResponse(posterItemFuture, PosterItem.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable thrown) {
                logger.log(Level.SEVERE, "POSTERITEM FETCH FAILED");

                handleHttpFailure(thrown);
            }
        });

        futures.add(posterItemFuture);

        return futures;
    }

    private void handleHttpFailure(Throwable thrown) {
        thrown.printStackTrace();
    }

    private void parseAllResponse(HttpResponse response, Class<?> someClass) throws IOException {
        if (someClass.equals(User.class)) {
            logger.log(Level.INFO, "PROCESSING USER RESPONSE");
            User[] users = response.parseAs(User[].class);

            List<User> userList = Lists.newArrayList();
            if (users.length < 0) {

                PosterDataModel.helper().updateAllUserCollection(userList);
            } else {
                Collections.addAll(userList, users);
                PosterDataModel.helper().updateAllUserCollection(userList);
            }
        } else if (someClass.equals(Poster.class)) {
            logger.log(Level.INFO, "PROCESSING POSTER RESPONSE");
            Poster[] posters = response.parseAs(Poster[].class);

            List<Poster> posterList = Lists.newArrayList();
            if (posters.length < 0) {

                PosterDataModel.helper().updateAllPosterCollection(posterList);
            } else {

                Collections.addAll(posterList, posters);
                PosterDataModel.helper().updateAllPosterCollection(posterList);
            }

        } else if (someClass.equals(PosterItem.class)) {
            logger.log(Level.INFO, "PROCESSING POSTER_ITEM RESPONSE");
            PosterItem[] posterItems = response.parseAs(PosterItem[].class);

            List<PosterItem> posterItemList = Lists.newArrayList();
            if (posterItems.length < 0) {
                PosterDataModel.helper().updateAllPosterItemsCollection(posterItemList);
            } else {

                Collections.addAll(posterItemList, posterItems);
                PosterDataModel.helper().updateAllPosterItemsCollection(posterItemList);
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

        PosterDataModel.helper()
                       .addPosterItemUUIDWithPosterId(posterItemUUID, posterMessage.getPosterUuid());

    }

    private String parseResponseObject(HttpResponse response, Class<?> someClass,
                                       PosterUrl.REQUEST_TYPE requestType) throws IOException {
        return parseResponseObject(response, someClass);
    }

    protected String parseResponseObject(HttpResponse response, Class<?> someClass) throws IOException {

        if (someClass.equals(User.class)) {
            User user = response.parseAs(User.class);

            if (user != null) {
                PosterDataModel.helper().updateUserCollection(user);
                return user.getUuid();
            }
        } else if (someClass.equals(Poster.class)) {
            Poster poster = response.parseAs(Poster.class);

            if (poster != null) {
                PosterDataModel.helper().updatePosterCollection(poster);
                return poster.getUuid();
            }

        } else if (someClass.equals(PosterItem.class)) {
            PosterItem posterItem = response.parseAs(PosterItem.class);

            if (posterItem != null) {
                PosterDataModel.helper().updatePosterItemCollection(posterItem);
                return posterItem.getUuid();
            }
        }
        return "";
    }

    //region mqtt
    public void mqttMessageForward(final PosterMessage message) {

        Optional<PosterMessage> posterMessageOptional = Optional.fromNullable(message);

        if (posterMessageOptional.isPresent()) {

            final PosterMessage posterMessage = posterMessageOptional.get();


            Optional<User> userOptional = Optional.fromNullable(PosterDataModel.helper().getCurrentUser());

            if (userOptional.isPresent()) {
                if (userOptional.get().getUuid().equals(posterMessage.getUserUuid())) {

                    logger.log(Level.INFO,
                               "We CARE about his user uuid: " + posterMessage.getUserUuid() + " bacause user uuid is there: " + userOptional
                                       .get()
                                       .getUuid());


                    if (posterMessage.getType().equals(PosterMessage.POSTER_ITEM)) {

                        ListeningExecutorService service = MoreExecutors.listeningDecorator(
                                Executors.newCachedThreadPool());
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
                            @Override
                            public void onSuccess(Void nothing) {
                                logger.log(Level.INFO, "DONE! FETCH POSTER ITEM");
                                //update poster with new _id
                            }

                            @Override
                            public void onFailure(Throwable thrown) {
                                logger.log(Level.INFO, "REST FAILED!");

                                thrown.printStackTrace();
                            }
                        });
                    }
                } else {
                    logger.log(Level.INFO,
                               "We don't care about his user uuid: " + posterMessage.getUserUuid() + " bacause user uuid is there: " + userOptional
                                       .get()
                                       .getUuid());
                }
            } else {
                logger.log(Level.SEVERE, "MESSAGE IS NULL");
            }
        }
    }

    //endregion http

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


        public static PosterUrl patchPosterItem(String id, PosterUrl.REQUEST_TYPE request_type) {
            PosterUrl posterUrl = new PosterUrl(
                    BASE_URL + "/poster_item/" + id);
            posterUrl.request_type = request_type;
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


        public enum REQUEST_TYPE {UPDATE, ADD, DELETE, GET, PATCH}
    }

    //endregion mqtt

}


