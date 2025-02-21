package util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import service.UrlCache;
import service.UrlChecker;

public class UrlCheckerServer {

  public static void main(String[] args) throws IOException {

    UrlCache urlCache = new UrlCache();
    UrlChecker urlChecker = new UrlChecker(urlCache);

    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.createContext("/check-url", new CheckUrlHandler(urlChecker));
    server.setExecutor(null);
    System.out.println("Server started on port 8080");
    server.start();
  }

  static class CheckUrlHandler implements HttpHandler {

    private final UrlChecker urlChecker;

    public CheckUrlHandler(UrlChecker urlChecker) {
      this.urlChecker = urlChecker;
    }

    public void handle(HttpExchange exchange) throws IOException {
      if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
        exchange.sendResponseHeaders(405, -1);
        return;
      }

      InputStream is = exchange.getRequestBody();
      String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      is.close();

      JSONObject requestJson = new JSONObject(requestBody);
      String url = requestJson.getString("url");

      boolean isPhishing = urlChecker.isPhishing(url);
      String responseText = isPhishing
          ? "!Phishing detected! Message has been blocked"
          : "Message delivered";

      exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
      byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(200, responseBytes.length);
      OutputStream os = exchange.getResponseBody();
      os.write(responseBytes);
      os.close();
    }
  }
}
