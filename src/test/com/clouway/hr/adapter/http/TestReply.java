package com.clouway.hr.adapter.http;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.sitebricks.headless.Reply;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class TestReply {
  public static <T> void sameReply(Reply<T> actualReply, T expectingDto) {
    Field field;
    try {
      field = actualReply.getClass().getDeclaredField("entity");
      field.setAccessible(true);
      T o = (T) field.get(actualReply);

      Gson gson = new Gson();
      String s = gson.toJson(o);
      String q = gson.toJson(expectingDto);

      assertThat(s, is(q));
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

}
