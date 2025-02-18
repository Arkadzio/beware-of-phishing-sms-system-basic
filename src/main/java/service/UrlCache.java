package service;

import java.util.HashMap;
import java.util.Map;

public class UrlCache {

  private Map<String, Boolean> cache = new HashMap<>();

  public Boolean get(String url) {
    return cache.get(url);
  }

  public void put(String url, boolean isSafe) {
    cache.put(url, isSafe);
  }
}
