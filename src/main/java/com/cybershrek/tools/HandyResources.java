package com.cybershrek.tools;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class HandyResources {

    public static Charset CHARSET = StandardCharsets.UTF_8;

    private static final ClassLoader loader = HandyResources.class.getClassLoader();

    public static String loadText(String resourceName) {
        try (InputStream stream = loader.getResourceAsStream(resourceName)
        ){
            return stream == null ? "" : new String(stream.readAllBytes(), CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties loadProperties(String resourceName) {
        try (InputStream stream = loader.getResourceAsStream(resourceName)
        ){
            Properties properties = new Properties();
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HandyResources() {}
}