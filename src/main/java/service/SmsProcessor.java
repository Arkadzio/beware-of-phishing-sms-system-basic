package service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.SmsMessage;

public class SmsProcessor {
  private final SubscriptionService subscriptionService;
  private final UrlChecker UrlChecker;

  public SmsProcessor(SubscriptionService subscriptionService, UrlChecker UrlChecker) {
    this.subscriptionService = subscriptionService;
    this.UrlChecker = UrlChecker;
  }

  public void processSms(SmsMessage sms) {
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
        if (UrlChecker.isPhishing(url)) {
          phishingFound = true;
          System.out.println("Message blocked for the recipient " + sms.getRecipient() +
              " - link to phishing website found: " + url);
          break;
        }
      }
      if (!phishingFound) {
        System.out.println("Message delivered to " + sms.getRecipient());
      }
    } else {
      System.out.println("!Phishing filter not active! Message delivered WITHOUT CHECKING to: " + sms.getRecipient());
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
