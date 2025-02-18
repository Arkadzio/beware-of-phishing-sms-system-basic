public class App {

  public static void main(String[] args) {

    SubscriptionService subscriptionService = new SubscriptionService();
    URLCache urlCache = new URLCache();
    URLChecker urlChecker = new URLChecker(urlCache);
    SMSProcessor smsProcessor = new SMSProcessor(subscriptionService, urlChecker);

    List<String> jsonSmsList = Arrays.asList(

        "{\"sender\": \"48700112233\", \"recipient\": \"48700112233\", \"message\": \"START\"}",
        "{\"sender\": \"234100200300\", \"recipient\": \"48700112233\", \"message\": \"Dzień dobry. Potwierdź dane na: https://www.safe-bank.com/info\"}",
        "{\"sender\": \"234100200300\", \"recipient\": \"48700112233\", \"message\": \"Ważna aktualizacja konta: https://phishing-bank.fake/login\"}",
        "{\"sender\": \"48700112233\", \"recipient\": \"48700112233\", \"message\": \"STOP\"}",
        "{\"sender\": \"234100200300\", \"recipient\": \"48700112233\", \"message\": \"Informacja bankowa: https://www.anybank.com/alert\"}"
    );

    for (String jsonSms : jsonSmsList) {
      try {
        SMSMessage sms = JsonUtil.fromJson(jsonSms);
        smsProcessor.processSms(sms);
      } catch (IOException e) {
        System.err.println("Błąd podczas deserializacji JSON: " + e.getMessage());
      }
    }
  }
}
