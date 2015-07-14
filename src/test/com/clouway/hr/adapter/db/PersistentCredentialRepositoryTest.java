package com.clouway.hr.adapter.db;

import com.clouway.hr.adapter.http.oauth2.OAuth2Provider;
import com.clouway.hr.core.CredentialRepository;
import com.clouway.hr.core.OAuthAuthentication;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created on 15-7-9.
 *
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class PersistentCredentialRepositoryTest {

  private final LocalServiceTestHelper helper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  private final ObjectDatastore datastore = new AnnotationObjectDatastore();


  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private final String userId = "my@email.com";
  private final String accessToken = "accessToken";
  private final String refreshToken = "refreshToken";

  private final OAuthAuthentication oAuthAuthentication = context.mock(OAuthAuthentication.class);
  private final CredentialRepository credentialRepository = new PersistentCredentialRepository(datastore, oAuthAuthentication);

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

    final GoogleCredential googleCredential = newCredential(accessToken, refreshToken);

    credentialRepository.store(userId, googleCredential);

    final CredentialEntity credentialEntity = datastore.load(CredentialEntity.class, userId);

    assertThat(credentialEntity.getAccessToken(), is(accessToken));
    assertThat(credentialEntity.getRefreshToken(), is(refreshToken));

  }


  @Test
  public void get() throws Exception {

    final GoogleCredential googleCredential = newCredential(accessToken, refreshToken);
    store(userId, googleCredential);

    context.checking(new Expectations() {{
      oneOf(oAuthAuthentication).getGoogleCredential(accessToken, refreshToken);
      will(returnValue(newCredential(accessToken, refreshToken)));
    }});

    final GoogleCredential retrievedCredential = credentialRepository.get(userId);

    assertThat(retrievedCredential.getAccessToken(), is(googleCredential.getAccessToken()));
    assertThat(retrievedCredential.getRefreshToken(), is(googleCredential.getRefreshToken()));

  }

  @Test
  public void getUnexciting() throws Exception {

    final GoogleCredential retrievedCredential = credentialRepository.get(userId);

    assertThat(retrievedCredential, is(nullValue()));
  }


  private GoogleCredential newCredential(String accessToken, String refreshToken) {

    return new GoogleCredential.Builder()

            .setJsonFactory(new JacksonFactory())
            .setTransport(new NetHttpTransport())
            .setClientSecrets(OAuth2Provider.GOOGLE_CLIENT_SECRET, OAuth2Provider.GOOGLE_CLIENT_ID).build()
            .setAccessToken(accessToken)
            .setRefreshToken(refreshToken);

  }

  private void store(String userId, GoogleCredential googleCredential) {
    datastore.store(new CredentialEntity(userId, googleCredential.getAccessToken(), googleCredential.getRefreshToken()));
  }

}