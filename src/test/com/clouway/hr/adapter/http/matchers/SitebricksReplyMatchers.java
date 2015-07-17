package com.clouway.hr.adapter.http.matchers;

import com.google.appengine.repackaged.com.google.gson.Gson;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * Created on 15-7-17.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class SitebricksReplyMatchers {
  @Factory
  public static <T> Matcher contains(T expected) {
    return new PrivateReply(expected, "entity");
  }

  @Factory
  public static <T> Matcher hasStatusCode(T expected) {
    return new PrivateReply(expected, "status");
  }

  @Factory
  public static <T> Matcher sayRedirectTo(T expected) {
    return new PrivateReply(expected, "redirectUri");
  }


  private static class PrivateReply<T> extends TypeSafeMatcher<T> {
    private T expected;
    private final String field;
    private String actualValue = null;
    private String expectedValue = null;

    public PrivateReply(T expected, String field) {
      this.expected = expected;
      this.field = field;
    }

    @Override
    public boolean matchesSafely(T actual) {
      Field field;

      try {
        field = actual.getClass().getDeclaredField(this.field);
        field.setAccessible(true);
        T o = (T) field.get(actual);

        Gson gson = new Gson();
        actualValue = gson.toJson(o);
        expectedValue = gson.toJson(expected);

      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

      return actualValue.equals(expectedValue);
    }

    @Override
    public void describeTo(Description description) {
      description.appendText(actualValue);
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
      mismatchDescription.appendText(expectedValue);
    }
  }
}
