package com.clouway.hr.adapter.cache.memcache;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.base.Optional;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class MemcacheTest {

  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  MemcacheService gaeMemcache;

  private Memcache memcache;

  @Before
  public void setUp() {
    helper.setUp();

    memcache = new Memcache(gaeMemcache);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void put() throws Exception {

    context.checking(new Expectations() {{
      oneOf(gaeMemcache).put("key_tokens","value");
    }});

    memcache.put("key","value");

  }

  @Test
  public void getCached() throws Exception {

    context.checking(new Expectations() {{
      oneOf(gaeMemcache).contains("key_tokens");
      will(returnValue(true));
      oneOf(gaeMemcache).get("key_tokens");
      will(returnValue("value"));
    }});

    final Optional<String> result = memcache.get("key");

    assertThat(result.get(), is("value"));
  }

  @Test
  public void getUnCached() throws Exception {

    context.checking(new Expectations() {{
      oneOf(gaeMemcache).contains("key_tokens");
      will(returnValue(false));
    }});

    final Optional<String> result = memcache.get("key");

    assertFalse(result.isPresent());
  }

  @Test
  public void contains() throws Exception {

    context.checking(new Expectations() {{
      oneOf(gaeMemcache).contains("key");
    }});

    memcache.contains("key");

  }
}