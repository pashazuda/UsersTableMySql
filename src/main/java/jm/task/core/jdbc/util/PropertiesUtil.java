package jm.task.core.jdbc.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil() {}

    private static void loadProperties() {
        // метод позволяет достучаться до нашего файла если он лежит в папке src
        try (var inputStream = PropertiesUtil.class.getClassLoader().
                getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }


}
