package com.clouway.hr.adapter.http.oauth2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 15-6-19.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class Util {

  public static URL getAbsoluteUrl(HttpServletRequest request, String relativePath) throws MalformedURLException {
    URL requestUrl = new URL(request.getRequestURL().toString());
    return new URL(requestUrl, relativePath);
  }

  public static String sendHttpPost(String url, Collection<NameValuePair> parameters) throws IOException {
    HttpPost post = new HttpPost(url);
    post.setEntity(new UrlEncodedFormEntity(parameters));

    return sendHttpRequest(post);
  }

  public static String sendHttpRequest(HttpUriRequest request) throws IOException {
    HttpResponse response = new DefaultHttpClient().execute(request);

    if (response.getStatusLine().getStatusCode() != 200) {
      response.getEntity().writeTo(System.err);
      return null;
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    response.getEntity().writeTo(outputStream);
    return new String(outputStream.toByteArray());
  }

  public static Map<String, String> parseJson(String json) {

    Map<String, String> result = new HashMap<>();
    if (StringUtils.isBlank(json)) {
      return result;
    }
    JsonObject jsonObject = (JsonObject) new JsonParser().parse(json);
    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()){
      result.put(entry.getKey(),entry.getValue().getAsString());
    }
      return result;
  }

}
