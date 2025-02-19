import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import model.SmsMessage;
import service.SmsProcessor;
import service.SubscriptionService;
import service.UrlCache;
import service.UrlChecker;
import util.JsonUtil;

public class App {

  public static void main(String[] args) {

    SubscriptionService subscriptionService = new SubscriptionService();
    UrlCache urlCache = new UrlCache();
    UrlChecker urlChecker = new UrlChecker(urlCache);
    SmsProcessor smsProcessor = new SmsProcessor(subscriptionService, urlChecker);

    List<String> jsonSmsList = Arrays.asList(

        "{\"sender\": \"48700112233\", \"recipient\": \"48700112233\", \"message\": \"START\"}",
        "{\"sender\": \"234100200300\", \"recipient\": \"48700112233\", \"message\": \"Hello. Please confrim your data on: https://www.safe-bank.com/info\"}",
        "{\"sender\": \"234100200300\", \"recipient\": \"48700112233\", \"message\": \"Attention! Important account update: https://phishing-bank.fake/login\"}",
        "{\"sender\": \"48700112233\", \"recipient\": \"48700112233\", \"message\": \"STOP\"}",
        "{\"sender\": \"234100200300\", \"recipient\": \"48700112233\", \"message\": \"Infos from your bank: https://www.anybank.com/alert\"}"
    );

    for (String jsonSms : jsonSmsList) {
      try {
        SmsMessage sms = JsonUtil.fromJson(jsonSms);
        smsProcessor.processSms(sms);
      } catch (IOException e) {
        System.err.println("Error during JSON deserialization: " + e.getMessage());
      }
    }
  }
}
