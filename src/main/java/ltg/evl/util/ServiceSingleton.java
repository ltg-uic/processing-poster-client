package ltg.evl.util;

/**
 * Created by aperritano on 2/20/15.
 */
public class ServiceSingleton {
    private static ServiceSingleton ourInstance = new ServiceSingleton();

    private ServiceSingleton() {

    }

    public static ServiceSingleton getInstance() {
        return ourInstance;
    }
}
