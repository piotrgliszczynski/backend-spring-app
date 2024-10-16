package org.training.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {

  private JsonUtils() {
  }

  public static <T> T deserializeJson(String filePath, Class<T> T) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    try (InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(filePath)) {
      return objectMapper.readValue(is, T);
    }
  }
}
