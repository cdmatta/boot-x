package com.charandeepmatta.sam.monitoring;

import static java.lang.System.currentTimeMillis;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class HttpService extends MonitorableService {

  private URL url;

  @Override
  public boolean checkStatus(final int timeoutSeconds) {
    HttpURLConnection connection = null;
    long startMillis = currentTimeMillis();
    InputStream inputStream = null;
    try {
      connection = openConnection(timeoutSeconds);
      int responseCode = connection.getResponseCode();
      if (responseCode != HTTP_OK) {
        log.error("Result up=false url={} responseCode={}", url, responseCode);
        return false;
      }
      inputStream = connection.getInputStream();
      long duration = currentTimeMillis() - startMillis;
      log.info("Result up=true url={} responseCode={} took={} ms ", url, responseCode, duration);
    } catch (Exception e) {
      log.error("Result up=false. Exception when checking url=" + url, e);
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (Exception ignored) {
        }
      }
      if (connection != null) {
        connection.disconnect();
      }
    }
    return false;
  }

  protected HttpURLConnection openConnection(final int timeoutSeconds) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    int timeoutMillis = timeoutSeconds * 1000;
    connection.setConnectTimeout(timeoutMillis);
    connection.setReadTimeout(timeoutMillis);
    connection.connect();
    return connection;
  }
}
