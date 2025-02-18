package service;

import java.util.HashSet;
import java.util.Set;

public class SubscriptionService {

  private Set<String> subscribedNumbers = new HashSet<>();

  public void subscribe(String phoneNumber) {
    subscribedNumbers.add(phoneNumber);
    System.out.println("Subskrypcja włączona dla: " + phoneNumber);
  }

  public void unsubscribe(String phoneNumber) {
    subscribedNumbers.remove(phoneNumber);
    System.out.println("Subskrypcja wyłączona dla: " + phoneNumber);
  }

  public boolean isSubscribed(String phoneNumber) {
    return subscribedNumbers.contains(phoneNumber);
  }
}
