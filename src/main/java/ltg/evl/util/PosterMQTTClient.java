package ltg.evl.util;

import ltg.commons.SimpleMQTTClient;

/**
 * Created by aperritano on 3/23/15.
 */
public class PosterMQTTClient extends SimpleMQTTClient {

    public PosterMQTTClient(String host, String clientId) {
        super(host, clientId);

//        this.connection.listener(new Listener() {
//            public void onConnected() {
//                publish( PosterServices.getInstance().getConfig().getString("poster.base.mqtt.channel"), "WE ARE CONNECTED BITCHES");
//            }
//            public void onDisconnected() {
//                publish( PosterServices.getInstance().getConfig().getString("poster.base.mqtt.channel"), "WE ARE NOT CONNECTED BITCHES");
//            }
//
//            @Override
//            public void onPublish(UTF8Buffer utf8Buffer, Buffer buffer, Runnable runnable) {
//
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//                publish( PosterServices.getInstance().getConfig().getString("poster.base.mqtt.channel"), "FAIL BITCHES");
//            }
//        });
    }


}
