package ltg.evl.util;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.event.ConfigurationErrorEvent;
import org.apache.commons.configuration.event.ConfigurationErrorListener;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;

import java.net.URL;

public class PosterServices {
    private static PosterServices ourInstance = new PosterServices();
    public CompositeConfiguration config = new CompositeConfiguration();

    private PosterServices() {
        enableConfiguration();
    }

    public static PosterServices getInstance() {
        return ourInstance;
    }

    private void enableConfiguration() {
        try {
            URL resource = getClass().getClassLoader().getResource("system.properties");
            config.addConfiguration(new PropertiesConfiguration(resource));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }


        config.addErrorListener(new ConfigurationErrorListener() {
            @Override
            public void configurationError(ConfigurationErrorEvent event) {
                System.out.println("PROBLEM WITH CONFIG" + event.toString());
            }
        });
        config.addConfigurationListener(new ConfigurationListener() {
            @Override
            public void configurationChanged(ConfigurationEvent event) {
                System.out.println("CHANGE WITH CONFIG" + event.toString());
            }
        });

        try {
            config.addConfiguration(new PropertiesConfiguration(ClassLoader.getSystemResource("system.properties")));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public CompositeConfiguration getConfig() {
        return config;
    }
}
