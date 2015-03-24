package ltg.evl.util;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import ltg.commons.MessageListener;
import ltg.evl.uic.poster.json.mongo.PosterMessage;

import java.io.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by aperritano on 3/23/15.
 */
public class MQTTPipe implements MessageListener {

    private static MQTTPipe ourInstance = new MQTTPipe();
    private static Logger logger;
    private final String BASE_CHANNEL_OUT;
    private final String BASE_CLIENT_ID;
    private final String BASE_ADDRESS;
    private final String BASE_CHANNEL_IN;
    GsonFactory JSON_FACTORY = new GsonFactory();
    private PosterMQTTClient posterMQTTClient = null;


    private MQTTPipe() {
        enableLogging();

        BASE_ADDRESS = PosterServices.getInstance().getConfig().getString("poster.base.mqtt.host");
        BASE_CLIENT_ID = PosterServices.getInstance().getConfig().getString("poster.base.mqtt.client.id");
        BASE_CHANNEL_IN = PosterServices.getInstance().getConfig().getString("poster.base.mqtt.channel.in");
        BASE_CHANNEL_OUT = PosterServices.getInstance().getConfig().getString("poster.base.mqtt.channel.out");


        posterMQTTClient = new PosterMQTTClient(BASE_ADDRESS, BASE_CLIENT_ID);
        posterMQTTClient.subscribe(BASE_CHANNEL_IN, this);
    }

    public static MQTTPipe getInstance() {
        return ourInstance;
    }

    public static void enableLogging() {
        logger = Logger.getLogger(HttpTransport.class.getName());
        logger.setLevel(Level.INFO);
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

    @Override
    public void processMessage(String rawMessage) {
        logger.log(Level.INFO, "WE HAVE MESSAGE" + rawMessage);


        try {
            JsonObjectParser jsonObjectParser = new JsonObjectParser(JSON_FACTORY);
            InputStream inputStream = new ByteArrayInputStream(rawMessage.getBytes());
            Reader reader = new InputStreamReader(inputStream);
            PosterMessage pm = jsonObjectParser.parseAndClose(reader, PosterMessage.class);

            logger.fine("HEY PM: " + pm);

            RESTHelper.getInstance().mqttMessageForward(pm);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void publishMessage(String message) {

        logger.fine("PUBLISHING " + message + " on: " + BASE_CHANNEL_OUT);

        posterMQTTClient.publish(BASE_CHANNEL_OUT, message);
    }
}
