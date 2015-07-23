package com.clouway.hr.adapter.cache.memcache;

import com.clouway.hr.core.cache.Cache;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class CacheModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Cache.class).to(Memcache.class);
  }

  @Provides
  MemcacheService getMemcacheService() {

    return MemcacheServiceFactory.getMemcacheService();
  }

}
