package com.clouway.hr.adapter.cache.memcache;

import com.clouway.hr.core.cache.Cache;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.common.base.Optional;
import com.google.inject.Inject;


/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public class Memcache implements Cache {

  private final MemcacheService memcacheService;

  @Inject
  public Memcache(MemcacheService memcacheService) {
    this.memcacheService = memcacheService;
  }

  @Override
  public boolean contains(String userId) {
    return memcacheService.contains(userId);
  }

  @Override
  public void put(String userId, Object value) {
    String key = userId + "_tokens";
    memcacheService.put(key, value);

  }

  @Override
  public <T> Optional<T> get(String userId) {

    String key = userId + "_tokens";

    if (memcacheService.contains(key)) {

      return Optional.of((T) memcacheService.get(key));
    }
    return Optional.absent();
  }


}
