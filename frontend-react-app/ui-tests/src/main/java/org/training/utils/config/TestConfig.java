package org.training.utils.config;

import org.training.exception.PropertyLoadingException;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class TestConfig {

  private static Properties properties;
  public static String baseUrl = getProperty("baseUrl");
  public static String formPage = getProperty("formPage");
  public static String apiBaseUrl = getProperty("apiBaseUrl");
  public static String customersApi = getProperty("customersApi");

  private static String getProperty(String key) {
    Properties properties = getProperties();
    Optional<String> result = Optional.ofNullable(properties.getProperty(key));
    return result.orElseThrow(
        () -> new PropertyLoadingException(String.format("Failed loading the property: %s", key))
    );
  }

  private static Properties getProperties() {
    if (properties == null) {
      properties = new Properties();
      try (InputStream stream = TestConfig.class.getResourceAsStream("/config.properties")) {
        if (stream != null) {
          properties.load(stream);
        }
      } catch (Exception e) {
        throw new PropertyLoadingException("Could not load properties file: ", e);
      }
    }
    return properties;
  }
}
