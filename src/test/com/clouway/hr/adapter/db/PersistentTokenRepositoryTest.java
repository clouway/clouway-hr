package com.clouway.hr.adapter.db;

import com.clouway.hr.core.OAuthAuthentication;
import com.clouway.hr.core.TokenRepository;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class PersistentTokenRepositoryTest {

  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  private final ObjectDatastore datastore = new AnnotationObjectDatastore();


  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private final String userId = "my@email.com";
  private final String accessToken = "accessToken";
  private final String refreshToken = "refreshToken";

  private final OAuthAuthentication oAuthAuthentication = context.mock(OAuthAuthentication.class);
  private final TokenRepository tokenRepository = new PersistentTokenRepository(datastore, oAuthAuthentication);

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }


  @Test
  public void store() throws Exception {

    tokenRepository.store(userId, accessToken, refreshToken);

    final TokenEntity tokenEntity = datastore.load(TokenEntity.class, userId);

    assertThat(tokenEntity.getAccessToken(), is(accessToken));
    assertThat(tokenEntity.getRefreshToken(), is(refreshToken));

  }


  @Test
  public void get() throws Exception {

    store(userId, accessToken, refreshToken);

    final Map<String, String> tokens = tokenRepository.get(userId);

    assertThat(tokens.get("accessToken"), is(accessToken));
    assertThat(tokens.get("refreshToken"), is(refreshToken));

  }

  @Test
  public void getUnexciting() throws Exception {

    final Map<String, String> tokens = tokenRepository.get(userId);

    assertThat(tokens, is(nullValue()));
  }

  private void store(String userId, String accessToken, String refreshToken) {
    datastore.store(new TokenEntity(userId, accessToken, refreshToken));
  }

}