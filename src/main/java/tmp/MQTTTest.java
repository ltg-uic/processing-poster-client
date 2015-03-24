package tmp;

import ltg.evl.util.MQTTPipe;

/**
 * Created by aperritano on 3/23/15.
 */
public class MQTTTest {

    public static void main(String args[]) {
        MQTTPipe.getInstance();

        MQTTPipe.getInstance().publishMessage("HELLO ROBOT");
        System.out.println("HELLO");

        while (true) {
        }
    }
}
