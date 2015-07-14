package com.clouway.hr.adapter.http;

import com.google.appengine.repackaged.com.google.gson.Gson;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public class ReplyMatcher {
  @Factory
  public static <T> Matcher contains(T expected) {
    return new PrivateReply(expected);
  }

  private static class PrivateReply<T> extends TypeSafeMatcher<T> {
    private T expected;
    private String actualValue = null;
    private String expectedValue = null;

    public PrivateReply(T expected) {
      this.expected = expected;
    }

    @Override
    public boolean matchesSafely(T actual) {
      Field field;

      try {
        field = actual.getClass().getDeclaredField("entity");
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