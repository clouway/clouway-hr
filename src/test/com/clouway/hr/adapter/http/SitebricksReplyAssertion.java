package com.clouway.hr.adapter.http;

import com.google.sitebricks.headless.Reply;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Created on 15-6-30.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class SitebricksReplyAssertion {

  public static <T> void assertIsRepliedWith(Reply<T> reply, T expected) {
    assertFieldValue("entity", reply, expected);
  }

  public static <T> void assertThatReplyStatusIs(Reply<T> reply, int expected) {
    assertFieldValue("status", reply, expected);
  }

  public static <T> void assertRedirectUriIs(Reply<T> reply, String expected){
    assertFieldValue("redirectUri",reply,expected);
  }


  private static <T> void assertFieldValue(String fieldName, Reply reply, T expected) {
    Field field = null;
    try {
      field = reply.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      T actual = (T) field.get(reply);
      assert actual != null;

      assertThat(actual, is(equalTo(expected)));
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

}












