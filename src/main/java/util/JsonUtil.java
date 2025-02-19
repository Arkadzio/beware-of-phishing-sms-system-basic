package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import model.SmsMessage;

public class JsonUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static SmsMessage fromJson(String jsonStr) throws IOException {
    return objectMapper.readValue(jsonStr, SmsMessage.class);
  }
}
