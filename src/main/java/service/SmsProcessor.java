package service;

public class SmsProcessor {
  private SubscriptionService subscriptionService;
  private URLChecker urlChecker;

  public SMSProcessor(SubscriptionService subscriptionService, URLChecker urlChecker) {
    this.subscriptionService = subscriptionService;
    this.urlChecker = urlChecker;
  }

  public void processSms(SMSMessage sms) {
    String message = sms.getMessage().trim();

    if (message.equalsIgnoreCase("START")) {
      subscriptionService.subscribe(sms.getSender());
      return;
    } else if (message.equalsIgnoreCase("STOP")) {
      subscriptionService.unsubscribe(sms.getSender());
      return;
    }

    if (subscriptionService.isSubscribed(sms.getRecipient())) {
      List<String> urls = extractUrls(message);
      boolean phishingFound = false;
      for (String url : urls) {
        if (urlChecker.isPhishing(url)) {
          phishingFound = true;
          System.out.println("Wiadomość zablokowana dla odbiorcy " + sms.getRecipient() +
              " - wykryto phishingowy URL: " + url);
          break;
        }
      }
      if (!phishingFound) {
        System.out.println("Wiadomość dostarczona do " + sms.getRecipient());
      }
    } else {
      System.out.println("Wiadomość dostarczona (filtr nieaktywny) do " + sms.getRecipient());
    }
  }

  private List<String> extractUrls(String text) {
    List<String> urls = new ArrayList<>();
    Pattern urlPattern = Pattern.compile("(https?://\\S+)");
    Matcher matcher = urlPattern.matcher(text);
    while (matcher.find()) {
      urls.add(matcher.group(1));
    }
    return urls;
  }
}
