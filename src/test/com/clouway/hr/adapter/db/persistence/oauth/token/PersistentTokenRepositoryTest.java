package com.clouway.hr.adapter.db.persistence.oauth.token;

import com.clouway.hr.adapter.apis.google.user.oauth.token.TokenRepository;
import com.clouway.hr.adapter.apis.google.user.oauth.token.UserTokens;
import com.clouway.hr.core.cache.Cache;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.base.Optional;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class PersistentTokenRepositoryTest {

  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  private final ObjectDatastore datastore = new AnnotationObjectDatastore();


  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  Cache cache;

  private final String userId = "my@email.com";
  private final String accessToken = "accessToken";
  private final String refreshToken = "refreshToken";

  private TokenRepository tokenRepository;

  @Before
  public void setUp() {
    helper.setUp();
    tokenRepository = new PersistentTokenRepository(datastore, cache);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }


  @Test
  public void storeUserTokens() throws Exception {

    final UserTokens userTokens = new UserTokens(accessToken, refreshToken);

    context.checking(new Expectations() {{
      oneOf(cache).put(userId, userTokens);
    }});

    tokenRepository.store(userId, userTokens);

    final TokenEntity tokenEntity = datastore.load(TokenEntity.class, userId);

    assertThat(tokenEntity.getAccessToken(), is(accessToken));
    assertThat(tokenEntity.getRefreshToken(), is(refreshToken));

  }

  @Test
  public void storeNewAccessToken() throws Exception {

    store(userId, accessToken, refreshToken);

    context.checking(new Expectations() {{
      oneOf(cache).put(with(any(String.class)), with(any(UserTokens.class)));
    }});

    tokenRepository.storeNewAccessToken(userId, "newAccessToken");

    final TokenEntity tokenEntity = datastore.load(TokenEntity.class, userId);

    assertThat(tokenEntity.getAccessToken(), is("newAccessToken"));
    assertThat(tokenEntity.getRefreshToken(), is(refreshToken));

  }

  @Test
  public void get() throws Exception {

    final Optional<UserTokens> optional = Optional.of(new UserTokens(accessToken, refreshToken));
    store(userId, accessToken, refreshToken);

    context.checking(new Expectations() {{
      oneOf(cache).contains(userId);
      will(returnValue(true));
      oneOf(cache).get(userId);
      will(returnValue(optional));
    }});

    final UserTokens tokens = tokenRepository.get(userId);

    assertThat(tokens.getAccessToken(), is(accessToken));
    assertThat(tokens.getRefreshToken(), is(refreshToken));

  }

  @Test
  public void getUnexciting() throws Exception {

    context.checking(new Expectations() {{
      oneOf(cache).contains(userId);
      will(returnValue(false));
    }});

    final UserTokens tokens = tokenRepository.get(userId);

    assertThat(tokens, is(nullValue()));
  }

  @Test
  public void containsCachedUserTokens() throws Exception {

    context.checking(new Expectations() {{
      oneOf(cache).contains(userId);
      will(returnValue(true));
    }});

    final boolean result = tokenRepository.containsTokens(userId);

    assertTrue(result);
  }

  @Test
  public void containsUserTokens() throws Exception {

    store(userId, accessToken, refreshToken);

    context.checking(new Expectations() {{
      oneOf(cache).contains(userId);
      will(returnValue(false));
    }});

    final boolean result = tokenRepository.containsTokens(userId);

    assertTrue(result);
  }

  @Test
  public void doNotContainUserTokens() throws Exception {

    context.checking(new Expectations() {{
      oneOf(cache).contains(userId);
      will(returnValue(false));
    }});

    final boolean result = tokenRepository.containsTokens(userId);

    assertFalse(result);
  }

  private void store(String userId, String accessToken, String refreshToken) {
    datastore.store(new TokenEntity(userId, accessToken, refreshToken));
  }
}