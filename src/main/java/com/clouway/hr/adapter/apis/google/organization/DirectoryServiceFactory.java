package com.clouway.hr.adapter.apis.google.organization;

import com.google.api.services.admin.directory.Directory;

/**
 * @author Panayot Kulchev <panayotkulchev@gmail.com>
 */

public interface DirectoryServiceFactory {

  Directory create(String email);

}