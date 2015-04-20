package ltg.evl.util;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import ltg.commons.MessageListener;
import ltg.commons.SimpleMQTTClient;
import ltg.evl.uic.poster.json.mongo.PosterMessage;

import java.io.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Publishes and Subscribes to MQTT Messages
 */
public class MQTTPipe implements MessageListener {

    private static MQTTPipe ourInstance = new MQTTPipe();
    private static Logger logger;
    private GsonFactory JSON_FACTORY = new GsonFactory();
    private String BASE_CHANNEL_OUT;
    private String BASE_CLIENT_ID;
    private String BASE_ADDRESS;
    private String BASE_CHANNEL_IN;
    private SimpleMQTTClient posterMQTTClient = null;


    private MQTTPipe() {


                enableLogging();
                BASE_ADDRESS = PosterServices.getInstance().getConfig().getString("poster.base.mqtt.host");
                BASE_CLIENT_ID = PosterServices.getInstance().getConfig().getString("poster.base.mqtt.client._id");
                BASE_CHANNEL_IN = PosterServices.getInstance().getConfig().getString("poster.base.mqtt.channel.in");
                BASE_CHANNEL_OUT = PosterServices.getInstance().getConfig().getString("poster.base.mqtt.channel.out");


                logger.log(Level.INFO, "BASE_ADDRESS: " + BASE_ADDRESS);
                logger.log(Level.INFO, "BASE_CLIENT_ID: " + BASE_CLIENT_ID);
                logger.log(Level.INFO, "BASE_CHANNEL_OUT: " + BASE_CHANNEL_OUT);
                logger.log(Level.INFO, "BASE_CHANNEL_IN: " + BASE_CHANNEL_IN);

                posterMQTTClient = new SimpleMQTTClient(BASE_ADDRESS, BASE_CLIENT_ID);
                posterMQTTClient.subscribe(BASE_CHANNEL_IN, MQTTPipe.this);



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
        logger.log(Level.INFO, "WE HAVE A MESSAGE" + rawMessage);


        try {
            JsonObjectParser jsonObjectParser = new JsonObjectParser(JSON_FACTORY);
            InputStream inputStream = new ByteArrayInputStream(rawMessage.getBytes());
            Reader reader = new InputStreamReader(inputStream);
            PosterMessage pm = jsonObjectParser.parseAndClose(reader, PosterMessage.class);

            logger.fine("Processing Message: " + pm);

            RESTHelper.getInstance().mqttMessageForward(pm);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void publishMessage(String message) {

        logger.log(Level.INFO, "PUBLISHING " + message + " on: " + BASE_CHANNEL_OUT);

        posterMQTTClient.publish(BASE_CHANNEL_OUT, message);
    }
}
