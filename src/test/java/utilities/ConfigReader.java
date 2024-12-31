package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties = new Properties();

    // Static block to load the properties file
    static {
        try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load the config.properties file.");
        }
    }

    // Method to retrieve property value by key
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    // Method to set property value
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }


}
