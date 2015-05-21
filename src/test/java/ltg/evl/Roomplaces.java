package ltg.evl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ltg.commons.MessageListener;
import ltg.commons.SimpleMQTTClient;

import java.util.Arrays;
import java.util.List;

public class Roomplaces {

    // Members
    private SimpleMQTTClient mqtt;
    private List<String> hotspots;
    private String app_id;
    private String run_id;


    // Constructor
    Roomplaces(SimpleMQTTClient mqtt, List<String> hotspots, String app_id, String run_id) {
        this.mqtt = mqtt;
        this.hotspots = hotspots;
        this.app_id = app_id;
        this.run_id = run_id;
    }

    // Utility static methods
    private static JsonObject getBeacon(String message, int n) {
        return new JsonParser().parse(message)
                               .getAsJsonObject()
                               .getAsJsonObject("payload")
                               .getAsJsonArray("resources")
                               .get(n)
                               .getAsJsonObject();
    }

    private static String getBeaconId(JsonObject json) {
        return json.getAsJsonPrimitive("rid").getAsString();
    }

    private static String getResource(JsonObject json) {
        return json.getAsJsonObject("proximity").getAsJsonPrimitive("rid").getAsString();
    }

    // Example usage
    public static void main(String[] args) {
        // Create a new client and connect to a broker
        SimpleMQTTClient sc = new SimpleMQTTClient("ltg.evl.uic.edu");

        // Initialize RoomPlaces
        List<String> hotspots = Arrays.asList("ipad1", "ipad2");
        String app_id = "roomplaces";
        String run_id = "tony";
        Roomplaces rp = new Roomplaces(sc, hotspots, app_id, run_id);

        rp.resourceEntered(new ResourceCallback() {
            @Override
            public void processEnterExitEvent(String dynamicResource, String staticResource) {
                System.out.println(dynamicResource + " exited " + staticResource);
            }
        });

        rp.resourceExited(new ResourceCallback() {
            @Override
            public void processEnterExitEvent(String dynamicResource, String staticResource) {
                System.out.println(dynamicResource + " entered " + staticResource);
            }
        });


        // Just for fun, let's wait indefinitely
        // Don't do this Tony, delete this line, your main thread is the interface thread
        while (!Thread.currentThread().isInterrupted()) {
        }
    }

    // Public methods
    public void resourceEntered(final ResourceCallback cb) {
        for (String h : hotspots) {
            this.mqtt.subscribe(
                    "/nutella/apps/" + app_id + "/runs/" + run_id + "/location/resource/static/" + h + "/enter",
                    new MessageListener() {
                        @Override
                        public void processMessage(String message) {
                            JsonObject json = getBeacon(message, 0);
                            cb.processEnterExitEvent(getBeaconId(json), getResource(json));
                        }
                    });
        }
    }

    public void resourceExited(final ResourceCallback cb) {
        for (String h : hotspots) {
            this.mqtt.subscribe(
                    "/nutella/apps/" + app_id + "/runs/" + run_id + "location/resource/static/" + h + "/exit",
                    new MessageListener() {
                        @Override
                        public void processMessage(String message) {
                            JsonObject json = getBeacon(message, 0);
                            cb.processEnterExitEvent(getBeaconId(json), getResource(json));
                        }
                    });
        }
    }

    //------------------------------------------------


    // Convenience callback interface
    public interface ResourceCallback {
        void processEnterExitEvent(String dr, String sr);
    }


}
