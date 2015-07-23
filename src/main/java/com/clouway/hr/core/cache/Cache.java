package com.clouway.hr.core.cache;

import com.google.common.base.Optional;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface Cache {

  boolean contains(String userId);

  void put(String userId, Object value);

  <T> Optional<T> get(String key);

}
