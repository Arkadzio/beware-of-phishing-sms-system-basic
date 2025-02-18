package service;

public class UrlChecker {
  private static final String API_URL = "https://web-risk-api-host.com/v1:evaluateUri";
  private static final String API_KEY = "test-1234567890";
  private URLCache urlCache;

  public URLChecker(URLCache urlCache) {
    this.urlCache = urlCache;
  }

  public boolean isPhishing(String urlToCheck) {
    Boolean cachedResult = urlCache.get(urlToCheck);
    if (cachedResult != null) {
      return !cachedResult;
    }


    JSONObject jsonRequest = new JSONObject();
    jsonRequest.put("uri", urlToCheck);

    try {
      URL apiUrl = new URL(API_URL);
      HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
      connection.setDoOutput(true);

      OutputStream os = connection.getOutputStream();
      os.write(jsonRequest.toString().getBytes("UTF-8"));
      os.close();

      int responseCode = connection.getResponseCode();
      if (responseCode == 200) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseStr = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          responseStr.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(responseStr.toString());
        boolean isSafe = jsonResponse.getBoolean("isSafe");
        urlCache.put(urlToCheck, isSafe);
        return !isSafe;
      } else {
        System.out.println("API call failed with response code: " + responseCode);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    urlCache.put(urlToCheck, true);
    return false;
  }
}
