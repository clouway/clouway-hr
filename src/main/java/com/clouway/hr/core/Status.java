package com.clouway.hr.core;

/**
 * @author Tihomir Kehayov <kehayov89@gmail.com>
 */
public interface Status {
  int accept();

  int reject();

  int pending();
}
