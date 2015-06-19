package com.clouway.hr.core;


/**
 * @author Dimitar Dimitrov (dimitar.dimitrov045@gmail.com)
 */
public interface VacationRepository {
  void updateStatus(Long id, String status);

  void add(Long id, String status);

  String getStatus(Long id);
}
